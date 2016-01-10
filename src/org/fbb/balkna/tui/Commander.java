package org.fbb.balkna.tui;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.Trainable;
import org.fbb.balkna.model.merged.uncompressed.MainTimer;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.BasicTime;
import org.fbb.balkna.model.primitives.Cycle;
import org.fbb.balkna.model.primitives.Exercise;
import org.fbb.balkna.model.primitives.Training;
import static org.fbb.balkna.swing.FlashBoulderBalkna.configDir;
import org.fbb.balkna.swing.locales.SwingTranslator;

/**
 *
 * @author jvanek
 */
public class Commander {

    private static List<String> history = new ArrayList<String>();

    private static enum Current {

        EXRCS, TRAINS, CYCLS
    }

    private static Current current = Current.TRAINS;

    private static String s = "";

    static boolean processCommands(BufferedReader br) throws NumberFormatException, IOException {
        List<Trainable> l = new ArrayList<Trainable>(0);
        if (current == Current.TRAINS) {
            l.addAll(Model.getModel().getTraingNames());
        }
        if (current == Current.EXRCS) {
            List<Exercise> q = Model.getModel().getExercises();
            for (Exercise ex : q) {
                l.add(new Training(ex));
            }
        }
        if (current == Current.CYCLS) {
            l.addAll(Model.getModel().getCycles());
        }
        if (!isHistory(s)) {
            printInfo(l);
        }
        s = br.readLine().trim();
        boolean r = processCommand(s, l);
        hhhh(s);
        return r;
    }

    private static void printInfo(List<Trainable> l) {
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands20"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands10"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands9"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands8", configDir));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands7"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands6", configDir));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands5"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands102"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands101"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands4"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands3"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands100"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands2"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands12"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands11"));
        System.out.println(" * " + SwingTranslator.R("TuiMainCommands1"));
        for (int i = 0; i < l.size(); i++) {
            if (current == Current.CYCLS) {
                Trainable c = l.get(i);
                String q = SwingTranslator.R("trainingCurrent", c.getCycle().getTrainingPointer() + " - " + c.getTraining().getName());
                System.out.println(i + 1 + ") " + c.getName() + "; " + q);
            } else {
                System.out.println(i + 1 + ") " + l.get(i).getName());
            }

        }
    }

    private static boolean processCommand(String s, List<Trainable> l) throws IOException, NumberFormatException {
        if (s.equalsIgnoreCase("exit") || s.equalsIgnoreCase("quit")) {
            return true;
        }

        SimpleCommand.proceed(s, l);

        if (isHistory(s)) {
            if (s.equals("hh")) {
                System.out.println(history.get(0));
            } else if (s.contains(" ")) {
                System.out.println(history.get(getIdFromCommand(s) + 1));
            } else {
                for (int i = history.size() - 1; i >= 0; i--) {
                    String ss = history.get(i);
                    System.out.println(i + ") " + ss);
                }
            }
        }

        if (s.equals("trainings")) {
            current = Current.TRAINS;
        }

        if (s.equals("cycles")) {
            current = Current.CYCLS;
        }

        if (s.equals("exercises")) {
            current = Current.EXRCS;
        }

        if (s.toLowerCase().startsWith("train ")) {
            int i = getIdFromCommand(s);
            String[] isShift = s.split("\\s+");
            if (isShift.length > 3) {
                Model.getModel().getTimeShift().setTraining(new Double(isShift[2].trim()));
                Model.getModel().getTimeShift().setPause(new Double(isShift[3].trim()));
                Model.getModel().getTimeShift().setRest(new Double(isShift[4].trim()));
                Model.getModel().getTimeShift().setIterations(new Double(isShift[5].trim()));
            }
            Training t = l.get(i).getTraining();
            Cycle c = null;
            String message = "tui";
            if (current == Current.CYCLS) {
                c = l.get(i).getCycle();
                message +=" in "+c.getTrainingPointer()+" - "+c.getName();
                c.startCyclesTraining();
            }
            t.started(message);
            List<BasicTime> br = t.getMergedExercises(Model.getModel().getTimeShift()).decompress();
            br.add(0, Model.getModel().getWarmUp());
            new TuiTraining(t, c, new MainTimer(br)).start();
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

    private static boolean isHistory(String s) {
        return s.toLowerCase().startsWith("history") || s.toLowerCase().equals("h") || s.toLowerCase().equals("hh") || s.toLowerCase().startsWith("h ");
    }

    private static void hhhh(String s) {
        if (s == null || s.isEmpty() || isHistory(s)) {
            return;
        }
        if (history.isEmpty()) {
            history.add(s);
        } else {
            if (history.get(0).equals(s)) {

            } else {
                history.add(0, s);
            }
        }
    }

}
