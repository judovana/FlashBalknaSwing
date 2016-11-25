package org.fbb.balkna.tui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.javax.JavaxWawPlayerProvider;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.settings.Settings;
import org.fbb.balkna.model.primitives.Training;
import static org.fbb.balkna.swing.FlashBoulderBalkna.configDir;
import org.fbb.balkna.swing.locales.SwingTranslator;
import org.judovana.linux.ConsoleImageViewer;
import static org.judovana.linux.ConsoleImageViewer.haveParam;

/**
 *
 * @author jvanek
 */
public class TuiMain {

    static boolean globalSounds = true;
    static boolean globalImages = true;

    public static void main(String[] args) throws IOException {
        System.out.println(SwingTranslator.R("TuiWelcome"));
        if (haveParam("help", args) || haveParam("h", args)) {
            System.out.println(SwingTranslator.R("TuiHelp1"));
            System.out.println(SwingTranslator.R("TuiHelp2"));
            ConsoleImageViewer.printHelp();
            System.exit(0);
        }
        if (haveParam("disableImages", args)) {
            globalImages = false;
        }
        if (haveParam("disableSounds", args)) {
            globalSounds = false;
        }
        if (args.length == 0) {
            args = new String[]{"-best"};
        }
        ConsoleImageViewer.setParams(args);
        Model.createrModel(configDir, new JavaxWawPlayerProvider());
        if (globalImages) {
            ConsoleImageViewer.doJob(ImgUtils.getDefaultImage());
        }
        System.out.println(Model.getModel().getDefaultStory());
        String[] s = Settings.getSettings().listItems();
        for (String s1 : s) {
            System.out.println(s1);
        }
        List<Training> l = Model.getModel().getTraingNames();
        for (int i = 0; i < l.size(); i++) {
            System.out.println(i + 1 + ") " + l.get(i).getName());

        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (Commander.processCommands(br)) {
                break;
            }
        }
    }

}
