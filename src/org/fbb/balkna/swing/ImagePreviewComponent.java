package org.fbb.balkna.swing;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import org.fbb.balkna.awt.utils.ImgUtils;


/**
 *
 * @author jvanek
 */
class ImagePreviewComponent extends JComponent {

    protected List<BufferedImage> srcs;
    protected int selcted;

    public ImagePreviewComponent(List<BufferedImage> srcs) {
        this.setSrcs(srcs);
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                ImagePreviewComponent q = (ImagePreviewComponent) e.getSource();
                if (e.getX() < q.getWidth() / 3) {
                    q.shiftLeft();
                } else {
                    q.shiftRight();
                }
            }

        });
    }

    public ImagePreviewComponent(BufferedImage src) {
        this(Arrays.asList(new BufferedImage[]{src}));
    }

    public ImagePreviewComponent() {
        this(ImgUtils.getDefaultImage());

    }

    private final int b1 = 2;
    private final int b2 = 4;
    @Override

    public void paint(Graphics g) {
        if (getWidth() > 2 * b2 && getHeight() > 2 * b2) {
            BufferedImage img = null;
            if (srcs != null) {
                img = srcs.get(selcted);
            }
            if (img != null) {
                g.drawImage(ImgUtils.squeeze(img, getWidth() - 2 * b2, getHeight() - 2 * b2), b2, b2, getWidth() - 2 * b2, getHeight() - 2 * b2, null);
            }
        }
        g.drawRect(b1, b1, getWidth() - 2 * b1, getHeight() - 2 * b1);
    }

    public void setSrcs(List<BufferedImage> nws) {
        this.srcs = nws;
        adjsutSelectedTo1();
    }

    public void setSrc(BufferedImage src) {
        this.srcs = new ArrayList<BufferedImage>(1);
        this.srcs.add(src);
        adjsutSelectedTo1();
    }

    public void resetSrcs() {
        this.srcs = new ArrayList<BufferedImage>(1);
        this.srcs.add(ImgUtils.getDefaultImage());
        adjsutSelectedTo1();
    }

    void shiftLeft() {
        if (selcted >= 1) {
            selcted--;
            repaint();
        }
    }

    void shiftRight() {
        if (selcted < srcs.size() - 1) {
            selcted++;
            repaint();
        }
    }

    protected void adjsutSelectedTo1() {
        if (srcs.size() > 1) {
            selcted = 1;
            repaint();
        } else {
            selcted = 0;
            repaint();
        }
    }

    public List<BufferedImage> getSrcs() {
        return srcs;
    }

    public BufferedImage getCurrent() {
        return srcs.get(getSelcted());
    }

    public int getSelcted() {
        return selcted;
    }

}
