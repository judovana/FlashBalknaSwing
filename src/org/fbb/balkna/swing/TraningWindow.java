package org.fbb.balkna.swing;

import java.awt.Component;
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
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;
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

    TraningWindow(JFrame parent, boolean modal, MainTimer mainTimer, final Training src) {
        super(parent, src.getName(), modal);
        TraningWindow.hack = this;
        this.src = src;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                model.stop();

            }

        });
        this.model = mainTimer;
        initComponents();

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
                label.setText(b.getHtmlPreview1());
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
                if (model.isEnded()) {
                    pauseRestInfoLabel.setText(time.getEndMssage());
                    ip.resetSrcs();
                    nowNextLAbel.setText("");

                } else {
                    time.play();
                    pauseRestInfoLabel.setText(time.getInformaiveTitle());
                    if (time instanceof PausaTime) {
                        ((BgLabel) timer).cross(true);
                        nowNextLAbel.setText(model.next());
                        BasicTime ntime = model.getNext();
                        Exercise t = ntime.getOriginator().getOriginal();
                        List<BufferedImage> l = ImgUtils.getExerciseImages(t, ip.getWidth(), ip.getHeight());
                        ip.setSrcs(l);
                        jTextArea1.setText(t.getDescription());
                        nowNextLAbel.setText(model.next() + " " + t.getName());
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
                        ((BgLabel) timer).cross(false);
                        nowNextLAbel.setText(model.now());
                        Exercise t = time.getOriginator().getOriginal();
                        List<BufferedImage> l = ImgUtils.getExerciseImages(t, ip.getWidth(), ip.getHeight());
                        ip.setSrcs(l);
                        jTextArea1.setText(t.getDescription());
                        nowNextLAbel.setText(model.now() + " " + t.getName());
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
                c.soundLogicRuntime();
                final String s = TimeUtils.secondsToHours(c.getCurrentValue() + model.getFutureTime()) + "/" + TimeUtils.secondsToHours(model.getTotalTime());
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
        ip.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    ((BgLabel) timer).setSrcs(ip.getSrcs());
                    ((BgLabel) timer).setSelcted(ip.getSelcted());
                }
            }

        });
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
        // TODO add your handling code here:
        jPanel2.setVisible(false);
    }//GEN-LAST:event_jList1MouseClicked

    private void timerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timerMouseClicked
        // TODO add your handling code here:
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
        boolean was = Model.getModel().isLaud();
        Model.getModel().setLaud(false);
        model.jumpBack();
        runAllListeners();
        Model.getModel().setLaud(was);
    }//GEN-LAST:event_backButtonActionPerformed

    private void jTextArea1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextArea1MouseClicked
        // TODO add your handling code here:
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

}
