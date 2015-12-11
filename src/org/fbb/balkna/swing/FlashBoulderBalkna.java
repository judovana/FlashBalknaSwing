package org.fbb.balkna.swing;

import org.fbb.balkna.model.utils.JavaPluginProvider;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
import org.fbb.balkna.model.primitives.Training;
import org.fbb.balkna.model.utils.JavaPluginProvider;
import org.fbb.balkna.swing.locales.SwingTranslator;
import org.fbb.balkna.tui.TuiMain;

/**
 *
 * @author jvanek
 */
public class FlashBoulderBalkna extends javax.swing.JFrame {

    public static final File exportDir = new File(System.getProperty("user.home"));
    public static final File configDir = new File(System.getProperty("user.home") + "/.config/FlashBalkna");

    final ImagePreviewComponent ip = new ImagePreviewComponent();
    static FlashBoulderBalkna hack;
    private final KeyEventDispatcher f1;

    private void selectTraining() {
        if (jList1.getSelectedValue() == null) {
            ip.setSrc(ImgUtils.getDefaultImage());
            jTextArea1.setText(Model.getModel().getDefaultStory() + getSwingGuiStory());
            startTrainingButton.setEnabled(false);
        } else {
            Training t = (Training) jList1.getSelectedValue();
            List<BufferedImage> l = ImgUtils.getTrainingImages(t, ip.getWidth(), ip.getHeight());
            ip.setSrcs(l);
            jTextArea1.setText(t.getStory());
            jTextArea1.setCaretPosition(0);
            startTrainingButton.setEnabled(true);
        }
        repaint();

    }

    public FlashBoulderBalkna() {
        Model.createrModel(configDir, new JavaxWawPlayerProvider(), new JavaPluginProvider());
        hack = this;
        initComponents();
        SwingTranslator.load(Model.getModel().getLanguage());
        setLocales();
        reloadTrainings();

        f1 = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1 && e.getID() == KeyEvent.KEY_PRESSED) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            SettingsDialogue d = new SettingsDialogue((Training) jList1.getSelectedValue());
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(f1);
                            d.setVisible(true);
                            d.dispose();
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(f1);
                        }
                    });

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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        panelWithInfo = new javax.swing.JPanel();
        imgPreview = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

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

        jList1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        pnelWithList.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(pnelWithList);

        panelWithInfo.setLayout(new java.awt.GridLayout(2, 1));

        imgPreview.setLayout(new java.awt.BorderLayout());
        panelWithInfo.add(imgPreview);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        panelWithInfo.add(jScrollPane2);

        jPanel1.add(panelWithInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
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

    private void startTrainingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTrainingButtonActionPerformed

        if (jList1.getSelectedValue() != null) {
            Training t = (Training) jList1.getSelectedValue();
            List<BasicTime> l = t.getMergedExercises(Model.getModel().getTimeShift()).decompress();
            l.add(0, Model.getModel().getWarmUp());
            TraningWindow traningWindow = new TraningWindow(this, true, new MainTimer(l), t);
            traningWindow.setVisible(true);
        }

    }//GEN-LAST:event_startTrainingButtonActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        selectTraining();
    }//GEN-LAST:event_formComponentResized

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(f1);
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {
        final boolean[] tui = new boolean[]{args.length > 0};
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
                            new FlashBoulderBalkna().setVisible(true);
                        } catch (Exception ex) {
                            System.err.println(SwingTranslator.R("NoGui"));
                            tui[0] = true;
                        }
                    }
                });
            }
        } catch (InterruptedException ex) {
            System.err.println(SwingTranslator.R("NoGui"));
            tui[0] = true;
        } catch (InvocationTargetException ex) {
            System.err.println(SwingTranslator.R("NoGui"));
            tui[0] = true;
        }
        if (tui[0]){
            TuiMain.main(args);
            
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel imgPreview;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel panelWithInfo;
    private javax.swing.JPanel pnelWithList;
    private javax.swing.JButton startTrainingButton;
    // End of variables declaration//GEN-END:variables

    private String getSwingGuiStory() {
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
        jList1.setModel(mainList);
        imgPreview.add(ip);
        jList1.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectTraining();
            }

        });
        selectTraining();
    }

    final void setLocales() {
        setTitle(Model.getModel().getTitle());
        startTrainingButton.setText(SwingTranslator.R("StartTraining"));
    }

}
