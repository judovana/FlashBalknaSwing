/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fbb.balkna.tui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.fbb.balkna.Packages;
import org.fbb.balkna.awt.utils.ImagesSaverImpl;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.SoundProvider;
import org.fbb.balkna.model.Trainable;
import org.fbb.balkna.model.Translator;
import org.fbb.balkna.model.primitives.history.Record;
import org.fbb.balkna.model.primitives.history.RecordWithOrigin;
import org.fbb.balkna.model.settings.Settings;
import org.fbb.balkna.swing.FlashBoulderBalkna;
import org.fbb.balkna.swing.locales.SwingTranslator;
import static org.fbb.balkna.tui.Commander.getIdFromCommand;
import org.judovana.linux.ConsoleImageViewer;

/**
 *
 * @author jvanek
 */
public class SimpleCommand {

    static void proceed(String s, List<Trainable> l) throws IOException {

        if (s.toLowerCase().startsWith("statistics")) {
            List<RecordWithOrigin> sl;
            String[] isOpt = s.split("\\s+");
            if (isOpt.length > 3) {
                boolean b1 = (Boolean.valueOf(isOpt[1].trim()));
                boolean b2 = (Boolean.valueOf(isOpt[2].trim()));
                boolean b3 = (Boolean.valueOf(isOpt[3].trim()));
                boolean b4 = (Boolean.valueOf(isOpt[4].trim()));

                RecordWithOrigin.SHOW_CLASS = !((b1 && !b2 && !b3)
                        || (!b1 && b2 && !b3)
                        || (!b1 && !b2 && b3));
                Record.SHOW_MESSAGE = b4;
                sl = Model.getModel().gatherStatistics(b1, b2, b3);
            } else {
                sl = Model.getModel().gatherStatistics();

            }
            Collections.reverse(sl);
            for (RecordWithOrigin r : sl) {
                System.out.println(r);
            }
        }

        if (s.toLowerCase().startsWith("info ")) {
            int i = getIdFromCommand(s);
            if (TuiMain.globalImages) {
                List<BufferedImage> imgs = ImgUtils.getTrainingImages(l.get(i), ConsoleImageViewer.getW(), ConsoleImageViewer.getH());
                int index = 0;
                if (imgs.size() > 1) {
                    index = 1;
                }
                ConsoleImageViewer.doJob(imgs.get(index));
            }
            System.out.println(l.get(i).getStory());
        }

        if (s.toLowerCase().startsWith("images ")) {
            if (!TuiMain.globalImages) {
                System.out.println(SwingTranslator.R("TuiImagesDissabled"));
            }
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
            if (TuiMain.globalSounds) {
                SoundProvider.getInstance().load(Settings.getSettings().getForcedSoundFont());
            }
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
        if (s.toLowerCase().startsWith("sound")) {
            System.out.println(SwingTranslator.R("TuiSoundInfo1"));
            String[] ls = Packages.SOUND_PACKS();
            for (String l1 : ls) {
                System.out.println("  " + l1);
            }
            if (!TuiMain.globalSounds) {
                System.out.println(SwingTranslator.R("TuiSoundDissabled"));
            }
            String[] isPack = s.split("\\s+");
            if (isPack.length == 1) {
                System.out.println(SwingTranslator.R("TuiSoundInfo2", SoundProvider.getInstance().getUsedSoundPack()));
                SoundProvider.getInstance().test();
            } else {
                System.out.println(SwingTranslator.R("TuiSoundInfo2", isPack[1]));
                SoundProvider.getInstance().test(isPack[1]);

            }

        }
        if (s.toLowerCase().startsWith("export ")) {
            int i = getIdFromCommand(s);
            if (!TuiMain.globalImages) {
                System.out.println(SwingTranslator.R("TuiImagesDissabled"));
            }
            File f = l.get(i).export(FlashBoulderBalkna.exportDir, new ImagesSaverImpl());
            System.out.println("Exported: " + f.getAbsolutePath());
            System.out.println("");

        }
    }

}
