package org.fbb.balkna.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.settings.Settings;
import org.fbb.balkna.model.merged.uncompressed.MainTimer;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.BasicTime;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.BigRestTime;
import org.fbb.balkna.model.merged.uncompressed.timeUnits.PausaTime;
import org.fbb.balkna.model.primitives.Cycle;
import org.fbb.balkna.model.primitives.Exercise;
import org.fbb.balkna.model.primitives.Training;
import org.fbb.balkna.model.utils.TimeUtils;
import org.fbb.balkna.swing.locales.SwingTranslator;

/**
 *
 * @author jvanek
 */
public class TraningWindow extends javax.swing.JDialog {

    static TraningWindow hack;

    private final MainTimer model;
    private final NoTitleImagePreviewComponent ip;
    Runnable exercseShiftedLIstener;
    Runnable oneTenthOfSecondListener;
    Runnable secondListener;
    private final Training src;
    private final Cycle src2;
    private int skipps;

    TraningWindow(JFrame parent, boolean modal, MainTimer mainTimer, final Training src, Cycle src2) {
        super(parent, generateTitle(src, src2), modal);
        skipps = 0;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                if (jList1.getSelectedIndex() < jList1.getModel().getSize() - 1) {

                    int a = JOptionPane.showConfirmDialog(TraningWindow.this, SwingTranslator.R("AndroidBackTraining"));
                    if (a == JOptionPane.YES_OPTION) {
                        try {
                            src.canceled(" missing " + getRemainingTime() + " on " + jList1.getSelectedIndex() + " - " + model.getCurrent().getOriginator().getOriginal().getName() + src2ToString());
                        } catch (Exception ex) {
                            src.canceled(ex.toString());
                        }
                        try {
                            model.getCurrent().getOriginator().getOriginal().canceled(" at " + TimeUtils.secondsToHours(model.getCurrent().getCurrentValue()) + " during " + generateTitle());
                        } catch (Exception ex) {
                            model.getCurrent().getOriginator().getOriginal().canceled(ex.toString());
                        }
                        TraningWindow.this.dispose();
                    }
                } else {
                    TraningWindow.this.dispose();
                }

            }
        });
        TraningWindow.hack = this;
        this.src = src;
        this.src2 = src2;
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                model.stop();

            }

        });
        this.model = mainTimer;
        initComponents();
        setTimerFont();

        jList1.setModel(new ListModel<BasicTime>() {

            @Override
            public int getSize() {
                return TraningWindow.this.model.getSrc().size();
            }

            @Override
            public BasicTime getElementAt(int index) {
                return TraningWindow.this.model.getSrc().get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        }
        );
        jList1.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                BasicTime b = (BasicTime) value;
                label.setText(b.getHtmlPreview1(true));
                return label;
            }
        });
        ip = new NoTitleImagePreviewComponent();
        imageHolder.add(ip);
        exercseShiftedLIstener = new Runnable() {

            @Override
            public void run() {
                jList1.setSelectedIndex(model.getIndex());
                jList1.ensureIndexIsVisible(model.getIndex());
                BasicTime time = model.getCurrent();
                Exercise currentExercise = time.getOriginator().getOriginal();
                if (model.isEnded()) {
                    try {
                        currentExercise.finished("during " + generateTitle());
                    } catch (Exception ex) {
                        currentExercise.finished(ex.toString());
                    }
                    if (skipps == 0) {
                        src.finished(null);
                    } else {
                        src.finishedWithSkips(" skipps " + skipps + " during " + generateTitle());
                    }
                    pauseRestInfoLabel.setText(time.getEndMssage());
                    ip.resetSrcs();
                    nowNextLAbel.setText("");
                    if (((BgLabel) timer).getImgs() != null) {
                        ((BgLabel) timer).setSrcs(ip.getSrcs());
                        ((BgLabel) timer).setSelcted(ip.getSelcted());
                    }
                    BasicTime.payEnd();
                } else {
                    time.play();
                    pauseRestInfoLabel.setText(time.getInformaiveTitle());
                    if (time instanceof PausaTime) {
                        ((BgLabel) timer).cross(true);
                        currentExercise.finished("for " + generateTitle());
                        nowNextLAbel.setText(model.next());
                        BasicTime ntime = model.getNext();
                        Exercise nextTime = ntime.getOriginator().getOriginal();
                        List<BufferedImage> l = ImgUtils.getExerciseImages(nextTime, ip.getWidth(), ip.getHeight());
                        ip.setSrcs(l);
                        jTextArea1.setText(nextTime.getDescription());
                        nowNextLAbel.setText(model.next() + " " + nextTime.getName());
                        if (((BgLabel) timer).getImgs() != null) {
                            ((BgLabel) timer).setSrcs(ip.getSrcs());
                            ((BgLabel) timer).setSelcted(ip.getSelcted());
                        }
                        if (time instanceof BigRestTime) {
                            if (Model.getModel().isPauseOnChange() || Model.getModel().isPauseOnExercise()) {
                                startButtonActionPerformed(null);
                            }
                        } else {
                            if (Model.getModel().isPauseOnExercise()) {
                                startButtonActionPerformed(null);
                            }
                        }

                    } else {
                        currentExercise.started("for " + generateTitle());
                        ((BgLabel) timer).cross(false);
                        nowNextLAbel.setText(model.now());
                        List<BufferedImage> l = ImgUtils.getExerciseImages(currentExercise, ip.getWidth(), ip.getHeight());
                        ip.setSrcs(l);
                        jTextArea1.setText(currentExercise.getDescription());
                        nowNextLAbel.setText(model.now() + " " + currentExercise.getName());
                        if (((BgLabel) timer).getImgs() != null) {
                            ((BgLabel) timer).setSrcs(ip.getSrcs());
                            ((BgLabel) timer).setSelcted(ip.getSelcted());
                        }
                    }
                }
                timer.setText(TimeUtils.secondsToMinutes(time.getCurrentValue()));

            }
        };
        model.setExerciseShifted(exercseShiftedLIstener);

        oneTenthOfSecondListener = new Runnable() {

            @Override
            public void run() {
                final String s = TimeUtils.secondsToMinutes(model.getCurrent().getCurrentValue()) + ":" + model.getTenthOfSecond();
                //System.out.println(s);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        timer.setText(s);
                    }
                });

            }
        };
        model.setOneTenthOfSecondListener(oneTenthOfSecondListener);

        secondListener = new Runnable() {

            @Override
            public void run() {
                BasicTime c = model.getCurrent();
                c.soundLogicRuntime(model);
                final String s = getRemainingTime(c);
                //System.out.println(s);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        jLabel3.setText(s);
                    }
                });

            }

        };
        model.setSecondListener(secondListener);
        runAllListeners();
        pack();
        ip.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (!Settings.getSettings().isAllowScreenChange()) {
                    return;
                }
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    ((BgLabel) timer).setSrcs(ip.getSrcs());
                    ((BgLabel) timer).setSelcted(ip.getSelcted());
                }
            }

        });

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        long w = gd.getDisplayMode().getWidth();
        long h = gd.getDisplayMode().getHeight();

        if ((w * h < 800l * 600l && !Settings.getSettings().isInvertScreenCompress())
                || (w * h >= 800l * 600l && Settings.getSettings().isInvertScreenCompress())) {
            jPanel2.setVisible(false);
            imgAndDescription.setVisible(false);
            mainPanel.remove(imgAndDescription);
            mainPanel.setLayout(new GridLayout());
            if (((BgLabel) timer).getImgs() != null) {
                ((BgLabel) timer).setSrcs(ip.getSrcs());
                ((BgLabel) timer).setSelcted(ip.getSelcted());
            }
        }
        setLocales();

    }

    private void runAllListeners() {
        boolean was = Model.getModel().isLaud();
        Model.getModel().setLaud(false);
        exercseShiftedLIstener.run();
        //oneTenthOfSecondListener.run();
        secondListener.run();
        Model.getModel().setLaud(was);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        restAndListPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        timerPanel = new javax.swing.JPanel();
        pauseRestInfoLabel = new javax.swing.JLabel();
        timer = new BgLabel();
        jPanel1 = new javax.swing.JPanel();
        nowNextLAbel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        imgAndDescription = new javax.swing.JPanel();
        imageHolder = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<BasicTime>()
        ;
        controlsPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        skipButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        restAndListPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setLayout(new java.awt.GridLayout(2, 1));

        infoPanel.setLayout(new java.awt.GridLayout(1, 0));

        timerPanel.setLayout(new java.awt.BorderLayout());

        pauseRestInfoLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        pauseRestInfoLabel.setText("jLabel3");
        timerPanel.add(pauseRestInfoLabel, java.awt.BorderLayout.PAGE_START);

        timer.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        timer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timer.setText("jLabel1");
        timer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                timerMouseClicked(evt);
            }
        });
        timerPanel.add(timer, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        nowNextLAbel.setText("Next:");
        jPanel1.add(nowNextLAbel);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("totalTime");
        jPanel1.add(jLabel3);

        timerPanel.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        infoPanel.add(timerPanel);

        mainPanel.add(infoPanel);

        imgAndDescription.setLayout(new java.awt.GridLayout(1, 2));

        imageHolder.setLayout(new java.awt.BorderLayout());
        imgAndDescription.add(imageHolder);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextArea1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        imgAndDescription.add(jPanel3);

        mainPanel.add(imgAndDescription);

        restAndListPanel.add(mainPanel, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setEnabled(false);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jPanel2.add(jScrollPane2);

        restAndListPanel.add(jPanel2, java.awt.BorderLayout.LINE_END);

        getContentPane().add(restAndListPanel, java.awt.BorderLayout.CENTER);

        controlsPanel.setLayout(new java.awt.GridLayout(0, 3));

        backButton.setText("Jump back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        controlsPanel.add(backButton);

        startButton.setText("Start!");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        controlsPanel.add(startButton);

        skipButton.setText("Skip forward");
        skipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipButtonActionPerformed(evt);
            }
        });
        controlsPanel.add(skipButton);

        getContentPane().add(controlsPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (!Settings.getSettings().isAllowScreenChange()) {
            return;
        }
        jPanel2.setVisible(false);
    }//GEN-LAST:event_jList1MouseClicked

    private void timerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timerMouseClicked
        if (!Settings.getSettings().isAllowScreenChange()) {
            return;
        }
        imgAndDescription.setVisible(true);
        mainPanel.add(imgAndDescription);
        if (evt.getClickCount() > 1) {
            ((BgLabel) timer).setSrcs(null);
            ((BgLabel) timer).setSelcted(0);
        }
        mainPanel.setLayout(new GridLayout(2, 1));
        jPanel2.setVisible(false);
        jPanel2.setVisible(true);
    }//GEN-LAST:event_timerMouseClicked

    private void skipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipButtonActionPerformed
        if (!Model.getModel().isAllowSkipping()) {
            return;
        }
        if (!(model.getCurrent() instanceof PausaTime)) {
            skipps++;
        }
        cancelEx();
        boolean was = Model.getModel().isLaud();
        Model.getModel().setLaud(false);
        model.skipForward();
        runAllListeners();
        Model.getModel().setLaud(was);
    }//GEN-LAST:event_skipButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        // TODO add your handling code here:
        if (model.isStopped()) {
            startButton.setText(Model.getModel().getPauseTitle());
            model.go();
        } else {
            startButton.setText(Model.getModel().getContinueTitle());
            model.stop();
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        if (!Model.getModel().isAllowSkipping()) {
            return;
        }
        if (!(model.getCurrent() instanceof PausaTime)) {
            skipps--;
        }
        cancelEx();
        boolean was = Model.getModel().isLaud();
        Model.getModel().setLaud(false);
        model.jumpBack();
        runAllListeners();
        Model.getModel().setLaud(was);
    }//GEN-LAST:event_backButtonActionPerformed

    private void jTextArea1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextArea1MouseClicked
        if (!Settings.getSettings().isAllowScreenChange()) {
            return;
        }
        imgAndDescription.setVisible(false);
        mainPanel.remove(imgAndDescription);
        mainPanel.setLayout(new GridLayout());

    }//GEN-LAST:event_jTextArea1MouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        hack = null;

    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {

        FlashBoulderBalkna.main(args);

//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TraningWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TraningWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TraningWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TraningWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//
//                TraningWindow dialog = new TraningWindow(new javax.swing.JFrame(), true, new MainTimer(new ArrayList<>()));
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JPanel imageHolder;
    private javax.swing.JPanel imgAndDescription;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<BasicTime> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nowNextLAbel;
    private javax.swing.JLabel pauseRestInfoLabel;
    private javax.swing.JPanel restAndListPanel;
    private javax.swing.JButton skipButton;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel timer;
    private javax.swing.JPanel timerPanel;
    // End of variables declaration//GEN-END:variables

    void setLocales() {
        skipButton.setText(SwingTranslator.R("skipForward"));
        backButton.setText(SwingTranslator.R("jumpBack"));
        this.setTitle(src.getName());
        // not localised because of logic
        //startButton.setText(SwingTranslator.R("Start"));
        validate();
        repaint();;
    }

    private void setTimerFont(int nvalue) {
        timer.setFont(timer.getFont().deriveFont(Font.BOLD, nvalue));
        if (Settings.getSettings().getMainTimerColor() != null) {
            timer.setForeground(new Color(Settings.getSettings().getMainTimerColor()));
        } else {
            timer.setForeground(timer.getParent().getForeground());
        }
    }

    void setTimerFont() {
        int nvalue = Settings.getSettings().getMainTimerSize();
        if (nvalue == 0) {
            setTimerFont(36);
        } else {
            setTimerFont(nvalue);
        }

        int grav = SwingConstants.CENTER;
        String v = Settings.getSettings().getMainTimerPositionV();
        if (Settings.VPOS_T.equals(v)) {
            grav = SwingConstants.TOP;
        }
        if (Settings.VPOS_B.equals(v)) {
            grav = SwingConstants.BOTTOM;
        }
        timer.setVerticalTextPosition(grav);
        timer.setVerticalAlignment(grav);

        int ali = SwingConstants.CENTER;
        String h = Settings.getSettings().getMainTimerPositionH();
        if (Settings.HPOS_L.equals(h)) {
            ali = SwingConstants.LEFT;
        }
        if (Settings.HPOS_R.equals(h)) {
            ali = SwingConstants.RIGHT;
        }

        timer.setHorizontalAlignment(ali);
        timer.setHorizontalTextPosition(ali);

    }

    private static String src2ToString(Cycle src2) {
        return (src2 == null ? "" : " - " + src2.getName());
    }

    private String src2ToString() {
        return src2ToString(src2);
    }

    private String generateTitle() {
        return generateTitle(src, src2);
    }

    private static String generateTitle(Training src, Cycle src2) {
        return src.getName() + src2ToString(src2);
    }

    private void cancelEx() {
        try {
            model.getCurrent().getOriginator().getOriginal().canceled(" missing " + getRemainingTime() + " during " + generateTitle() + ", skipps: " + skipps);
        } catch (Exception ex) {
            model.getCurrent().getOriginator().getOriginal().canceled(ex.toString());
        }
    }

    private String getRemainingTime(BasicTime c) {
        return TimeUtils.getRemainingTime(c, model);
    }

    private String getRemainingTime() {
        return getRemainingTime(model.getCurrent());
    }

}
