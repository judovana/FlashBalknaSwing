/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fbb.balkna.tui;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.merged.uncompressed.MainTimer;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.BasicTime;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.BigRestTime;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.PausaTime;
import org.fbb.balkna.model.primitives.Exercise;
import org.fbb.balkna.model.primitives.Training;
import org.fbb.balkna.model.utils.TimeUtils;
import org.fbb.balkna.swing.locales.SwingTranslator;
import org.judovana.linux.ConsoleImageViewer;

/**
 *
 * @author jvanek
 */
class TuiTraining {

    private final MainTimer model;
    Runnable exercseShiftedLIstener;
    Runnable secondListener;
    BufferedReader br;

    public TuiTraining(Training t, MainTimer timer) {
        model = timer;
        this.br = new BufferedReader(new InputStreamReader(System.in));
        exercseShiftedLIstener = new Runnable() {

            @Override
            public void run() {
                BasicTime time = model.getCurrent();
                if (model.isEnded()) {
                    ConsoleImageViewer.doJob(ImgUtils.getDefaultImage());
                    System.out.println(time.getEndMssage());

                } else {
                    time.play();
                    if (time instanceof PausaTime) {
                        BasicTime ntime = model.getNext();
                        Exercise t = ntime.getOriginator().getOriginal();
                        List<BufferedImage> l = ImgUtils.getExerciseImages(t, ConsoleImageViewer.getW(), ConsoleImageViewer.getH());
                        ConsoleImageViewer.doJob(l.get(0));
                        bigInfo1(time);
                        System.out.println(model.next());
                        System.out.println(t.getDescription());
                        System.out.println(model.next() + " " + t.getName());
                        if (time instanceof BigRestTime) {
                            if (Model.getModel().isPauseOnChange() || Model.getModel().isPauseOnExercise()) {
                                waitForEnter(l);
                            }
                        } else {
                            if (Model.getModel().isPauseOnExercise()) {
                                waitForEnter(l);
                            }
                        }

                    } else {
                        Exercise t = time.getOriginator().getOriginal();
                        List<BufferedImage> l = ImgUtils.getExerciseImages(t, ConsoleImageViewer.getW(), ConsoleImageViewer.getH());
                        ConsoleImageViewer.doJob(l.get(0));
                        bigInfo2();
                        System.out.println(t.getDescription());
                        System.out.println(model.now() + " " + t.getName());
                    }
                }
                System.out.println(TimeUtils.secondsToMinutes(time.getCurrentValue()));

            }

            private void bigInfo1(BasicTime time) {
                BasicTime c = model.getCurrent();
                final String s = TimeUtils.secondsToHours(c.getCurrentValue() + model.getFutureTime()) + "/" + TimeUtils.secondsToHours(model.getTotalTime());
                System.out.println(time.getInformaiveTitle() + " " + s);
            }

            private void bigInfo2() {
                BasicTime c = model.getCurrent();
                final String s = TimeUtils.secondsToHours(c.getCurrentValue() + model.getFutureTime()) + "/" + TimeUtils.secondsToHours(model.getTotalTime());
                System.out.println(model.now() + " " + s);
            }

        };
        model.setExerciseShifted(exercseShiftedLIstener);

        secondListener = new Runnable() {

            @Override
            public void run() {
                BasicTime c = model.getCurrent();
                c.soundLogicRuntime();
                final String s = TimeUtils.secondsToMinutes(model.getCurrent().getCurrentValue()) + ":" + model.getTenthOfSecond();
                System.out.println(s);

            }

        };
        model.setSecondListener(secondListener);
        runAllListeners();
    }

    private void runAllListeners() {
        boolean was = Model.getModel().isLaud();
        Model.getModel().setLaud(false);
        exercseShiftedLIstener.run();
        //oneTenthOfSecondListener.run();
        secondListener.run();
        Model.getModel().setLaud(was);

    }

    void start() {
        model.go();

    }

    private void waitForEnter(List<BufferedImage> l) {
        model.stop();
        System.out.println(SwingTranslator.R("TuiPause"));
        int i = 0;
        while (true) {
            try {
                String s = br.readLine().trim();
                s = s.toLowerCase();
                if (s.startsWith("p")) {
                    i--;
                    if (i < 0) {
                        i = 0;
                    }
                }
                if (s.startsWith("n")) {
                    i++;
                    if (i >= l.size()) {
                        i = l.size() - 1;
                    }
                }
                if (s.startsWith("s")) {
                    skip();
                    skip();
                    break;
                }
                if (s.startsWith("b")) {
                    back();
                    back();
                    back();
                    break;
                }
                if (s.startsWith("n") || s.startsWith("p")) {
                    ConsoleImageViewer.doJob(l.get(i));
                }
                if (s.isEmpty()) {
                    break;
                }
            } catch (Exception ex) {
                System.out.println(ex);
                break;
            }

        }
        model.go();
    }

    private void skip() {
        if (!Model.getModel().isAllowSkipping()) {
            return;
        }
        boolean was = Model.getModel().isLaud();
        Model.getModel().setLaud(false);
        model.skipForward();
        runAllListeners();
        Model.getModel().setLaud(was);
    }

    private void back() {
        if (!Model.getModel().isAllowSkipping()) {
            return;
        }
        boolean was = Model.getModel().isLaud();
        Model.getModel().setLaud(false);
        model.jumpBack();
        runAllListeners();
        Model.getModel().setLaud(was);
    }

}
