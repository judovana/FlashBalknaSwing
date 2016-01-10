package org.fbb.balkna.awt.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.fbb.balkna.Packages;
import org.fbb.balkna.model.Exportable;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.primitives.Exercise;
import org.fbb.balkna.model.primitives.Training;
import static org.fbb.balkna.model.utils.IoUtils.getFile;

/**
 *
 * @author jvanek
 */
public class ImgUtils {

    private static Map<URL, BufferedImage> cache = new HashMap<URL, BufferedImage>();

    public static BufferedImage getImage(String subPackage, String fileName) {
        try {
            return processCache(getFile(subPackage, fileName));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<BufferedImage> getImages(String subPackage, List<String> l) {
        String[] ll = new String[l.size()];
        for (int i = 0; i < l.size(); i++) {
            String get = l.get(i);
            ll[i] = get;

        }
        return getImages(subPackage, ll);
    }

    public static List<BufferedImage> getImages(String subPackage, String... names) {
        List<BufferedImage> r = new ArrayList<BufferedImage>(names.length + 1); //we can expect also the "title one" to eb added later
        for (String l1 : names) {
            try {
                BufferedImage img = processCache(getFile(subPackage, l1));
                if (img != null) {
                    r.add(img);
                }
            } catch (Exception ex) {
                System.out.println("Eror loading: " + l1);
                ex.printStackTrace();
            }
        }
        return r;
    }

    public static BufferedImage createAllImage(List<BufferedImage> imgs, int targetW, int targetH) {
        if (imgs == null || imgs.isEmpty()) {
            return null;
        }
        if (imgs.size() == 1) {
            return imgs.get(0);
        }
        int W = 100;
        int H = 100;
        int width = (int) Math.round(Math.sqrt(imgs.size()));
        int height = width;
        if (imgs.size() == 2) {
            if (targetW > targetH) {
                width = 2;
                height = 1;
            } else {
                width = 1;
                height = 2;
            }
        }
        if (imgs.size() == 3) {
            width = 2;
            height = 2;
        }
        if (imgs.size() == 5 || imgs.size() == 6) {
            if (targetW > targetH) {
                width = 3;
                height = 2;
            } else {
                width = 2;
                height = 3;
            }
        }
        if (imgs.size() > 6 && imgs.size() < 10) {
            width = 3;
            height = 3;
        }
        if (imgs.size() >= 10 && imgs.size() < 16) {
            if (targetW > targetH) {
                width = 4;
                height = 3;
            } else {
                width = 3;
                height = 4;
            }
        }
        int iw = (width) * W;
        int ih = (height) * H;
        int x = 0;
        int y = 0;
        BufferedImage r = new BufferedImage(iw, ih, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < imgs.size(); i++) {
            BufferedImage img = imgs.get(i);
            r.createGraphics().drawImage(squeeze(img, W, H), x, y, W, H, null);
            if ((i + 1) % (width) == 0) {
                x = 0;
                y = y + W;

            } else {
                x = x + H;
            }
        }
        return r;

    }

    private static BufferedImage processCache(URL file) throws IOException {
        if (cache.get(file) != null) {
            return cache.get(file);
        } else {
            BufferedImage img = ImageIO.read(file);
            cache.put(file, img);
            return img;
        }
    }

    public static Image squeeze(BufferedImage img, int W, int H) {
        if (Model.getModel().isRatioForced()) {
            BufferedImage buf = new BufferedImage(W, H, BufferedImage.TYPE_4BYTE_ABGR);
            int w = W;
            int h = H;
            double wRatio = (double) W / (double) img.getWidth();
            double hRatio = (double) H / (double) img.getHeight();
            if (wRatio < hRatio) {
                w = (int) (wRatio * (double) img.getWidth());
                h = (int) (wRatio * (double) img.getHeight());
            } else {
                w = (int) (hRatio * (double) img.getWidth());
                h = (int) (hRatio * (double) img.getHeight());
            }
            buf.createGraphics().drawImage(img, Math.abs((W - w) / 2), Math.abs((H - h) / 2), w, h, null);
            return buf;
        } else {
            return img;
        }
    }

    public static List<BufferedImage> getTrainingImages(Exportable t, int targetW, int targetH) {
        List<BufferedImage> r = getImages(Packages.IMAGES_TRA, t.getImages());
        List<BufferedImage> r2 = getImages(Packages.IMAGES_EXE, t.getExerciseImages());
        if (r == null) {
            r = new ArrayList<BufferedImage>();
        }
        r.add(0, getDefaultImage());
        if (r2.size() > 1) {
            r.add(createAllImage(r2, targetW, targetH));
        }
        r.addAll(r2);

        return r;
    }

    public static List<BufferedImage> getExerciseImages(Exercise e, int targetW, int targetH) {
        List<BufferedImage> r = getImages(Packages.IMAGES_EXE, e.getImages());
        if (r == null) {
            r = new ArrayList<BufferedImage>();
        }
        if (r.isEmpty()) {
            r.add(getDefaultImage());
        } else {
            if (r.size() > 1) {
                r.add(0, createAllImage(r, targetW, targetH));
            }
        }

        return r;
    }

    public static void writeExercisesImagesToDir(File imgDir, List<String> names) throws IOException {
     List<BufferedImage> allti = getImages(Packages.IMAGES_EXE, names);   
        writeImagesToDir(imgDir, names, allti);
    }
    
    public static void writeTrainingsImagesToDir(File imgDir, List<String> names) throws IOException {
     List<BufferedImage> allti = getImages(Packages.IMAGES_TRA, names);   
        writeImagesToDir(imgDir, names, allti);
    }
    
    private static void writeImagesToDir(File imgDir, List<String> names, List<BufferedImage> allti) throws IOException {
        for (int i = 0; i < allti.size(); i++) {
            BufferedImage it = allti.get(i);
            ImageIO.write(it, "jpg", new File(imgDir, names.get(i)));
        }
        BufferedImage orig = getDefaultImage();
        BufferedImage im = new BufferedImage(orig.getWidth(), orig.getHeight(), BufferedImage.TYPE_INT_RGB);
        im.createGraphics().drawImage(orig, null, null);
        ImageIO.write(im, "jpg", new File(imgDir, Model.getDefaultImageName()));
    }
    
    
    public static BufferedImage getDefaultImage() {
        return ImgUtils.getImage(Packages.IMAGES_APP, Model.getDefaultImage());
    }

}
