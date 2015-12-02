package org.fbb.balkna.tui;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.fbb.balkna.awt.utils.ImagesSaverImpl;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.Settings;
import org.fbb.balkna.model.SoundProvider;
import org.fbb.balkna.model.Translator;
import org.fbb.balkna.model.merged.uncompressed.MainTimer;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.BasicTime;
import org.fbb.balkna.model.primitives.Training;
import org.fbb.balkna.swing.FlashBoulderBalkna;
import static org.fbb.balkna.swing.FlashBoulderBalkna.configDir;
import org.fbb.balkna.swing.locales.SwingTranslator;
import org.judovana.linux.ConsoleImageViewer;

/**
 *
 * @author jvanek
 */
public class Commander {

    static boolean processCommands(BufferedReader br) throws NumberFormatException, IOException {
        List<Training> l = Model.getModel().getTraingNames();
        printInfo(l);
        String s = br.readLine().trim();
        return processCommand(s, l);
    }

    private static void printInfo(List<Training> l) {
        System.out.println(SwingTranslator.R("TuiMainCommands10"));
        System.out.println(SwingTranslator.R("TuiMainCommands9"));
        System.out.println(SwingTranslator.R("TuiMainCommands8", configDir));
        System.out.println(SwingTranslator.R("TuiMainCommands7"));
        System.out.println(SwingTranslator.R("TuiMainCommands6", configDir));
        System.out.println(SwingTranslator.R("TuiMainCommands5"));
        System.out.println(SwingTranslator.R("TuiMainCommands4"));
        System.out.println(SwingTranslator.R("TuiMainCommands3"));
        System.out.println(SwingTranslator.R("TuiMainCommands2"));
        System.out.println(SwingTranslator.R("TuiMainCommands12"));
        System.out.println(SwingTranslator.R("TuiMainCommands11"));
        System.out.println(SwingTranslator.R("TuiMainCommands1"));
        for (int i = 0; i < l.size(); i++) {
            System.out.println(i + 1 + ") " + l.get(i).getName());

        }
    }

    private static boolean processCommand(String s, List<Training> l) throws IOException, NumberFormatException {
        if (s.equalsIgnoreCase("exit") || s.equalsIgnoreCase("quit")) {
            return true;
        }
        if (s.toLowerCase().startsWith("info ")) {
            int i = getIdFromCommand(s);
            List<BufferedImage> imgs = ImgUtils.getTrainingImages(l.get(i), ConsoleImageViewer.getW(), ConsoleImageViewer.getH());
            int index = 0;
            if (imgs.size() > 1) {
                index = 1;
            }
            ConsoleImageViewer.doJob(imgs.get(index));
            System.out.println(l.get(i).getStory());
        }
        if (s.toLowerCase().startsWith("images ")) {
            int i = getIdFromCommand(s);
            List<BufferedImage> imgs = ImgUtils.getTrainingImages(l.get(i), ConsoleImageViewer.getW(), ConsoleImageViewer.getH());
            int index = 0;
            if (imgs.size() > 1) {
                index = 1;
            }
            if (imgs.size() > 2) {
                index = 2;
            }
            for (int x = index; x < imgs.size(); x++) {
                ConsoleImageViewer.doJob(imgs.get(x));
            }

        }
        if (s.toLowerCase().startsWith("get ")) {
            try {
                Model.getModel().reload(false, new URL(s.split("\\s+")[1]));
                System.out.println("OK");
                System.out.println("");
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        }
        if (s.equalsIgnoreCase("properties")) {
            String[] ss = Settings.getSettings().listItems();
            System.out.println("********************");
            for (String s1 : ss) {
                System.out.println(s1);
            }
            System.out.println("********************");
        }
        if (s.toLowerCase().startsWith("set ")) {
            String s1 = s.split("\\s+")[1];
            String[] s2 = s1.split("=");
            String key = s2[0];
            String value = "";
            if (s2.length > 1) {
                value = s2[1].trim();
            }
            if (value.equals("null")) {
                value = null;
            }
            Settings.getSettings().readItem(key, value);
            SoundProvider.getInstance().load(Settings.getSettings().getForcedSoundFont());
            Translator.load(Settings.getSettings().getForcedLanguage());
            SwingTranslator.load(Settings.getSettings().getForcedLanguage());
            System.out.println("OK");
            System.out.println("");

        }

        if (s.equalsIgnoreCase("save")) {
            Model.getModel().save();
            System.out.println("OK");
            System.out.println("");

        }
        if (s.toLowerCase().startsWith("export ")) {
            int i = getIdFromCommand(s);
            File f = l.get(i).export(FlashBoulderBalkna.exportDir, new ImagesSaverImpl());
            System.out.println("Exported: " + f.getAbsolutePath());
            System.out.println("");

        }

        if (s.toLowerCase().startsWith("train ")) {
            int i = getIdFromCommand(s);
            String[] isShift = s.split("\\s+");
            if (isShift.length>3){
                Model.getModel().getTimeShift().setTraining(new Double(isShift[2].trim()));
                Model.getModel().getTimeShift().setPause(new Double(isShift[3].trim()));
                Model.getModel().getTimeShift().setRest(new Double(isShift[4].trim()));
                Model.getModel().getTimeShift().setIterations(new Double(isShift[5].trim()));
            }
            Training t = l.get(i);
            List<BasicTime> br = t.getMergedExercises(Model.getModel().getTimeShift()).decompress();
            br.add(0, Model.getModel().getWarmUp());
            new TuiTraining(t, new MainTimer(br)).start();
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
        return false;
    }

    static int getIdFromCommand(String s) throws NumberFormatException {
        int i = new Integer(s.split("\\s+")[1]);
        return i - 1;
    }
}
