package org.fbb.balkna.swing;

import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.javax.JavaxWawPlayerProvider;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.merged.uncompressed.MainTimer;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.BasicTime;
import org.fbb.balkna.model.primitives.Cycle;
import org.fbb.balkna.model.primitives.Exercise;
import org.fbb.balkna.model.primitives.Training;
import org.fbb.balkna.model.primitives.history.StatisticHelper;
import org.fbb.balkna.swing.locales.SwingTranslator;
import org.fbb.balkna.tui.TuiMain;

/**
 *
 * @author jvanek
 */
public class FlashBoulderBalkna extends javax.swing.JFrame {

    private class ShowSettings implements Runnable {

        public ShowSettings() {
        }

        @Override
        public void run() {
            TrainingWithCycle tc = getSelectedTraining();
            Training t = null;
            Cycle c = null;
            if (tc != null) {
                t = tc.t;
                c = tc.c;
            }
            SettingsDialogue d = new SettingsDialogue(t, c);
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(f1);
            d.setLocationRelativeTo(null);
            d.setLocation(d.getLocation().x, ScreenFinder.getCurrentScreenSizeWithoutBounds().height - d.getHeight());
            d.setVisible(true);
            d.dispose();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(f1);
        }
    }

    public static final File exportDir = new File(System.getProperty("user.home"));
    public static final File configDir = new File(System.getProperty("user.home") + "/.config/FlashBalkna");

    final ImagePreviewComponent ip = new ImagePreviewComponent();
    static FlashBoulderBalkna hack;
    private final KeyEventDispatcher f1;

    private void selectTraining() {
        if (trainingsList.getSelectedValue() == null) {
            deselect();
        } else {
            Training t = (Training) trainingsList.getSelectedValue();
            select(t);
        }
        repaint();

    }

    private void select(Training t) {
        List<BufferedImage> l = ImgUtils.getTrainingImages(t, ip.getWidth(), ip.getHeight());
        ip.setSrcs(l);
        textPreview.setText(t.getStory());
        textPreview.setCaretPosition(0);
        startTrainingButton.setEnabled(true);
    }

    private void deselect() {
        ip.setSrc(ImgUtils.getDefaultImage());
        textPreview.setText(Model.getModel().getDefaultStory() + getSwingGuiStory());
        startTrainingButton.setEnabled(false);
        trainingForward.setEnabled(false);
        trainingBack.setEnabled(false);
        setLocales();
    }

    private void selectExercise() {
        if (exercisesList.getSelectedValue() == null) {
            deselect();
        } else {
            Exercise e = (Exercise) exercisesList.getSelectedValue();
            Training t = new Training(e);
            select(t);
        }
        repaint();

    }

    private void selectCycle() {
        if (cyclesList.getSelectedValue() == null) {
            deselect();
        } else {
            Cycle c = (Cycle) cyclesList.getSelectedValue();
            currentTraining.setText(SwingTranslator.R("trainingCurrent", c.getTrainingPointer() + " - " + c.getTraining().getName()));
            trainingForward.setEnabled(true);
            trainingBack.setEnabled(true);
            textPreview.setText(c.getStory());
            textPreview.setCaretPosition(0);
            startTrainingButton.setEnabled(true);

            if (c.getTrainingPointer() == 1) {
                startTrainingButton.setText(SwingTranslator.R("StartTraining1"));
            } else if (c.getTrainingPointer() == c.getTrainingOverrides().size()) {
                startTrainingButton.setText(SwingTranslator.R("StartTraining3"));
            } else {
                startTrainingButton.setText(SwingTranslator.R("StartTraining2"));
            }

        }
        repaint();

    }

    public FlashBoulderBalkna() {
        Model.createrModel(configDir, new JavaxWawPlayerProvider());
        hack = this;
        initComponents();
        setLocales();
        reloadTrainings();

        f1 = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1 && e.getID() == KeyEvent.KEY_PRESSED) {
                    SwingUtilities.invokeLater(new ShowSettings());
                    return true;
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(f1);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnelWithList = new javax.swing.JPanel();
        buttonsPanel = new javax.swing.JPanel();
        startTrainingButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        exercisesList = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        trainingsList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        cyclesList = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        currentTraining = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        trainingBack = new javax.swing.JButton();
        trainingForward = new javax.swing.JButton();
        panelWithInfo = new javax.swing.JPanel();
        imgPreview = new javax.swing.JPanel();
        textPreviewPanel = new javax.swing.JPanel();
        textPreviewContainer = new javax.swing.JScrollPane();
        textPreview = new javax.swing.JTextArea();
        settingsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        pnelWithList.setLayout(new java.awt.BorderLayout());

        buttonsPanel.setLayout(new java.awt.BorderLayout());

        startTrainingButton.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        startTrainingButton.setText("Start training");
        startTrainingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTrainingButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(startTrainingButton, java.awt.BorderLayout.PAGE_END);

        pnelWithList.add(buttonsPanel, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        exercisesList.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        exercisesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(exercisesList);

        jTabbedPane1.addTab("tab1", jScrollPane3);

        trainingsList.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        trainingsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(trainingsList);

        jTabbedPane1.addTab("tab1", jScrollPane1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        cyclesList.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        cyclesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(cyclesList);

        jPanel2.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        currentTraining.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentTraining.setText("Current training:");
        currentTraining.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel3.add(currentTraining);

        jPanel4.setLayout(new java.awt.GridLayout(1, 2));

        trainingBack.setText("Training back");
        trainingBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainingBackActionPerformed(evt);
            }
        });
        jPanel4.add(trainingBack);

        trainingForward.setText("Skip forward");
        trainingForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainingForwardActionPerformed(evt);
            }
        });
        jPanel4.add(trainingForward);

        jPanel3.add(jPanel4);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("tab4", jPanel2);

        pnelWithList.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(pnelWithList);

        panelWithInfo.setLayout(new java.awt.GridLayout(2, 1));

        imgPreview.setLayout(new java.awt.BorderLayout());
        panelWithInfo.add(imgPreview);

        textPreviewPanel.setLayout(new java.awt.BorderLayout());

        textPreview.setEditable(false);
        textPreview.setColumns(20);
        textPreview.setLineWrap(true);
        textPreview.setRows(5);
        textPreviewContainer.setViewportView(textPreview);

        textPreviewPanel.add(textPreviewContainer, java.awt.BorderLayout.CENTER);

        settingsButton.setText("III (F1)");
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });
        textPreviewPanel.add(settingsButton, java.awt.BorderLayout.PAGE_END);

        panelWithInfo.add(textPreviewPanel);

        jPanel1.add(panelWithInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mdfc(Cycle c, int i) {
        if (i != c.getTrainingPointer()) {
            c.modified(" pointer changed  from " + i + " to " + c.getTrainingPointer() + ". So training " + c.getTraining(i).getName() + " to " + c.getTraining().getName());
        }
    }

    private static class TrainingWithCycle {

        final Cycle c;
        final Training t;

        public TrainingWithCycle(Cycle c, Training t) {
            this.c = c;
            this.t = t;
        }

        public TrainingWithCycle(Training t) {
            this.c = null;
            this.t = t;
        }

    }

    TrainingWithCycle getSelectedTraining() {
        if (jTabbedPane1.getSelectedIndex() == 1) {
            if (trainingsList.getSelectedValue() != null) {
                Training t = (Training) trainingsList.getSelectedValue();
                return new TrainingWithCycle(t);
            }
        }
        if (jTabbedPane1.getSelectedIndex() == 0) {
            if (exercisesList.getSelectedValue() != null) {
                Exercise ex = (Exercise) exercisesList.getSelectedValue();
                Training t = new Training(ex);
                return new TrainingWithCycle(t);
            }
        }
        if (jTabbedPane1.getSelectedIndex() == 2) {
            if (cyclesList.getSelectedValue() != null) {
                Cycle ex = (Cycle) cyclesList.getSelectedValue();
                Training t = ex.getTraining();
                return new TrainingWithCycle(ex, t);
            }
        }
        return null;
    }

    private void startTrainingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTrainingButtonActionPerformed
        TrainingWithCycle t = getSelectedTraining();
        if (t != null) {
            if (t.c != null) {
                t.c.startCyclesTraining();
            }
            List<BasicTime> l = t.t.getMergedExercises(Model.getModel().getTimeShift()).decompress();
            l.add(0, Model.getModel().getWarmUp());
            TraningWindow traningWindow = new TraningWindow(this, true, new MainTimer(l), t.t, t.c);
            t.t.getStatsHelper().started(StatisticHelper.generateMessage(t.c, t.t, (Exercise) null));
            deselect();
            setIdealWindowSize(traningWindow);
            traningWindow.setLocationRelativeTo(null);
            traningWindow.setVisible(true);
        }
    }//GEN-LAST:event_startTrainingButtonActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        jTabbedPane1StateChanged(null);
    }//GEN-LAST:event_formComponentResized

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(f1);
    }//GEN-LAST:event_formWindowClosing

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        deselect();
        if (jTabbedPane1.getSelectedIndex() == 2) {
            panelWithInfo.removeAll();
            panelWithInfo.setLayout(new GridLayout(1, 1));
            panelWithInfo.add(textPreviewPanel);
            panelWithInfo.validate();
            if (cyclesList.getModel().getSize() <= 0) {
                JOptionPane.showMessageDialog(this, SwingTranslator.R("NoCyclesFound"));
            }
        } else {
            panelWithInfo.removeAll();
            panelWithInfo.setLayout(new GridLayout(2, 1));
            panelWithInfo.add(imgPreview);
            panelWithInfo.add(textPreviewPanel);
            panelWithInfo.validate();
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void trainingBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingBackActionPerformed
        // TODO add your handling code here:
        TrainingWithCycle tc = getSelectedTraining();
        if (tc != null && tc.c != null) {
            int i = tc.c.getTrainingPointer();
            tc.c.decTrainingPointer();
            mdfc(tc.c, i);
            selectCycle();
        }
    }//GEN-LAST:event_trainingBackActionPerformed

    private void trainingForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingForwardActionPerformed
        TrainingWithCycle tc = getSelectedTraining();
        if (tc != null && tc.c != null) {
            int i = tc.c.getTrainingPointer();
            tc.c.incTrainingPointer();
            mdfc(tc.c, i);
            selectCycle();
        }
    }//GEN-LAST:event_trainingForwardActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        SwingUtilities.invokeLater(new ShowSettings());
    }//GEN-LAST:event_settingsButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {
        final boolean[] iconify = new boolean[]{false};
        int argsToTui = 0;
        for (String arg : args) {
            if (arg.toLowerCase().contains("iconifyWindow".toLowerCase())) {
                iconify[0] = true;
                argsToTui = 1;
            }
        }
        final boolean[] tui = new boolean[]{args.length > argsToTui};
        try {
            if (!tui[0]) {
                try {
                    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //</editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FlashBoulderBalkna fbb = new FlashBoulderBalkna();
                            setIdealWindowSize(fbb);
                            fbb.setLocationRelativeTo(null);
                            fbb.setVisible(true);
                            if (iconify[0]) {
                                fbb.setState(ICONIFIED);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.err.println(SwingTranslator.R("NoGui"));
                            tui[0] = true;
                        }
                    }
                });
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.err.println(SwingTranslator.R("NoGui"));
            tui[0] = true;
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            System.err.println(SwingTranslator.R("NoGui"));
            tui[0] = true;
        }
        if (tui[0]) {
            TuiMain.main(args);

        }
    }

    private static void setIdealWindowSize(Window b) {
        Rectangle size = ScreenFinder.getCurrentScreenSizeWithoutBounds();
        if (size.width > size.height) {
            b.setSize(size.width / 2, (size.height * 9) / 10);
        } else {
            b.setSize(((size.width * 9) / 10), size.height / 2);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JLabel currentTraining;
    private javax.swing.JList cyclesList;
    private javax.swing.JList exercisesList;
    private javax.swing.JPanel imgPreview;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelWithInfo;
    private javax.swing.JPanel pnelWithList;
    private javax.swing.JButton settingsButton;
    private javax.swing.JButton startTrainingButton;
    private javax.swing.JTextArea textPreview;
    private javax.swing.JScrollPane textPreviewContainer;
    private javax.swing.JPanel textPreviewPanel;
    private javax.swing.JButton trainingBack;
    private javax.swing.JButton trainingForward;
    private javax.swing.JList trainingsList;
    // End of variables declaration//GEN-END:variables

    public static String getSwingGuiStory() {
        return "\n" + SwingTranslator.R("SwingInfoLine1")
                + "\n" + SwingTranslator.R("SwingInfoLine2")
                + "\n" + SwingTranslator.R("SwingInfoLine3")
                + "\n" + SwingTranslator.R("SwingInfoLine4")
                + "\n" + SwingTranslator.R("SwingInfoLine5")
                + "\n" + SwingTranslator.R("SwingInfoLine6");
    }

    final void reloadTrainings() {
        final List<Training> tranings = Model.getModel().getTraingNames();
        ListModel<Training> mainList = new ListModel<Training>() {

            @Override
            public int getSize() {
                return tranings.size();
            }

            @Override
            public Training getElementAt(int index) {
                return tranings.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        };
        trainingsList.setModel(mainList);

        final List<Exercise> exercises = Model.getModel().getExercises();
        ListModel<Exercise> exList = new ListModel<Exercise>() {

            @Override
            public int getSize() {
                return exercises.size();
            }

            @Override
            public Exercise getElementAt(int index) {
                return exercises.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        };
        exercisesList.setModel(exList);

        final List<Cycle> cycles = Model.getModel().getCycles();
        ListModel<Cycle> cyclesModel = new ListModel<Cycle>() {

            @Override
            public int getSize() {
                return cycles.size();
            }

            @Override
            public Cycle getElementAt(int index) {
                return cycles.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        };
        cyclesList.setModel(cyclesModel);

        imgPreview.add(ip);
        trainingsList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectTraining();
            }

        });

        exercisesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectExercise();
            }

        });

        cyclesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectCycle();
            }

        });
        jTabbedPane1.setSelectedIndex(1);
        selectTraining();
    }

    final void setLocales() {
        setTitle(Model.getModel().getTitle());
        startTrainingButton.setText(SwingTranslator.R("StartTraining"));

        trainingBack.setText(SwingTranslator.R("trainingBack"));
        trainingForward.setText(SwingTranslator.R("trainingFwd"));
        currentTraining.setText(SwingTranslator.R("trainingCurrent", "?"));

        try {
            jTabbedPane1.setTitleAt(0, (SwingTranslator.R("mainTabExercise")));
            jTabbedPane1.setTitleAt(1, (SwingTranslator.R("mainTabTrainings")));
            jTabbedPane1.setTitleAt(2, (SwingTranslator.R("mainTabCycles")));
        } catch (Exception ex) {
            // set locales is called from jTabbedPane1  stateChanged
        }
    }

}
