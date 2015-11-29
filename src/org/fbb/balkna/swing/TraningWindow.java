package org.fbb.balkna.swing;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import org.fbb.balkna.Packages;
import org.fbb.balkna.awt.utils.ImagesSaverImpl;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.SoundProvider;
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

    private final MainTimer model;
    private final NoTitleImagePreviewComponent ip;
    Runnable exercseShiftedLIstener;
    Runnable oneTenthOfSecondListener;
    Runnable secondListener;
    private final Training src;
    private final KeyEventDispatcher f1;

    TraningWindow(JFrame parent, boolean modal, MainTimer mainTimer, final Training src) {
        super(parent, src.getName(), modal);
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
        jTextField1.setText(Model.getModel().getExamplePluginUrl());
        auitoiterateSpinner.setModel(new SpinnerNumberModel(Model.getModel().getImagesOnTimerSpeed(), 0, 1000, 1));
        trainingsSpinner.setModel(new SpinnerNumberModel(Model.getModel().getTimeShift().getTraining(), 0d, 1000d, 0.05d));
        pausesSpinner.setModel(new SpinnerNumberModel(Model.getModel().getTimeShift().getPause(), 0d, 1000d, 0.05d));
        restsSpinner.setModel(new SpinnerNumberModel(Model.getModel().getTimeShift().getRest(), 0d, 1000d, 0.05d));
        iterationsSpinner.setModel(new SpinnerNumberModel(Model.getModel().getTimeShift().getIterations(), 0d, 1000d, 0.05d));

        auitoiterateSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Model.getModel().setImagesOnTimerSpeed((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });

        trainingsSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Model.getModel().getTimeShift().setTraining((Double) ((JSpinner) e.getSource()).getValue());
            }
        });

        pausesSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Model.getModel().getTimeShift().setPause((Double) ((JSpinner) e.getSource()).getValue());
            }
        });
        restsSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Model.getModel().getTimeShift().setRest((Double) ((JSpinner) e.getSource()).getValue());
            }
        });

        iterationsSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Model.getModel().getTimeShift().setIterations((Double) ((JSpinner) e.getSource()).getValue());
            }
        });

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
                    pauseRestInfoLabel.setText(time.getInformaiveTitle());
                    if (time instanceof PausaTime) {
                        ((BgLabel) timer).cross(true);
                        if (Model.getModel().isLaud()) {
                            if (time instanceof BigRestTime) {
                                SoundProvider.getInstance().getPSendChange().playAsync();
                            } else {
                                SoundProvider.getInstance().getPSendRun().playAsync();
                            }

                        }
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
                        if (Model.getModel().isLaud()) {
                            SoundProvider.getInstance().getPSstart().playAsync();
                        }
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
                soundLogicRuntime(c);
                final String s = TimeUtils.secondsToHours(c.getCurrentValue() + model.getFutureTime()) + "/" + TimeUtils.secondsToHours(model.getTotalTime());
                //System.out.println(s);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        jLabel3.setText(s);
                    }
                });

            }

            private void soundLogicRuntime(BasicTime c) {
                if (!Model.getModel().isLaud()) {
                    return;
                }
                if (c.getCurrentValue() - 2 == 0) {
                    SoundProvider.getInstance().getPSthree().playAsync();
                } else if (c.getCurrentValue() - 1 == 0) {
                    SoundProvider.getInstance().getPStwo().playAsync();
                } else if (c.getCurrentValue() == 0) {
                    SoundProvider.getInstance().getPSone().playAsync();
                } else if (c.getCurrentValue() == c.getOriginalValue() / 2) {
                    if (c instanceof PausaTime) {
                        SoundProvider.getInstance().getPShalfPause().playAsync();
                    } else {
                        SoundProvider.getInstance().getPShalfRun().playAsync();
                    }
                } else if (c.getCurrentValue() == (c.getOriginalValue()) / 4) {
                    if (c instanceof PausaTime) {
                        SoundProvider.getInstance().getPSthreeQatsPause().playAsync();
                    } else {
                        SoundProvider.getInstance().getPSthreeQuatsRun().playAsync();
                    }
                }
            }
        };
        model.setSecondListener(secondListener);
        runAllListeners();
        f1 = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    jComboBox1.setModel(new DefaultComboBoxModel(Packages.SOUND_PACKS));
                    jComboBox1.setSelectedItem(SoundProvider.getInstance().getUsedSoundPack());
                    if (src == null) {
                        exportButton.setEnabled(false);
                    } else {
                        exportButton.setEnabled(true);
                    }

                    allowSkipping.setSelected(Model.getModel().isAllowSkipping());
                    pauseOnChange.setSelected(Model.getModel().isPauseOnChange());
                    pauseOnExercise.setSelected(Model.getModel().isPauseOnExercise());
                    mute.setSelected(!Model.getModel().isLaud());
                    ratioCheckbox.setSelected(Model.getModel().isRatioForced());
                    settings.setModal(true);
                    settings.pack();
                    settings.setVisible(true);
                    return true;
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(f1);
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
        Model.getModel().setLaud(false);
        exercseShiftedLIstener.run();
        //oneTenthOfSecondListener.run();
        secondListener.run();
        Model.getModel().setLaud(!mute.isSelected());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        settings =  new javax.swing.JDialog(this);
        ratioCheckbox = new javax.swing.JCheckBox();
        autoIterateLabel = new javax.swing.JLabel();
        auitoiterateSpinner = new javax.swing.JSpinner();
        mute = new javax.swing.JCheckBox();
        soundPackLabel = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        testSoundsButton = new javax.swing.JButton();
        setSoundPackButton = new javax.swing.JButton();
        languageLabel = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        changeLanguageButton = new javax.swing.JButton();
        tutorialLabel = new javax.swing.JLabel();
        pauseOnExercise = new javax.swing.JCheckBox();
        pauseOnChange = new javax.swing.JCheckBox();
        cheaterLabel = new javax.swing.JLabel();
        allowSkipping = new javax.swing.JCheckBox();
        creditsLabel = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        downloadButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        saveForOfline = new javax.swing.JCheckBox();
        managePluginsButton = new javax.swing.JButton();
        exercisesModLabel = new javax.swing.JLabel();
        trainingsModLabel = new javax.swing.JLabel();
        trainingsSpinner = new javax.swing.JSpinner();
        pausesModLabel = new javax.swing.JLabel();
        pausesSpinner = new javax.swing.JSpinner();
        restsModLabel = new javax.swing.JLabel();
        restsSpinner = new javax.swing.JSpinner();
        iterationsModLabel = new javax.swing.JLabel();
        iterationsSpinner = new javax.swing.JSpinner();
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

        settings.getContentPane().setLayout(new java.awt.GridLayout(0, 1));

        ratioCheckbox.setText("force images to keep ratio");
        ratioCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ratioCheckboxActionPerformed(evt);
            }
        });
        settings.getContentPane().add(ratioCheckbox);

        autoIterateLabel.setText("autoiterate images on timer with speed (s): (0 disabled)");
        settings.getContentPane().add(autoIterateLabel);
        settings.getContentPane().add(auitoiterateSpinner);

        mute.setText("mute");
        mute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muteActionPerformed(evt);
            }
        });
        settings.getContentPane().add(mute);

        soundPackLabel.setText("Soundpack:");
        settings.getContentPane().add(soundPackLabel);

        settings.getContentPane().add(jComboBox1);

        testSoundsButton.setText("Test");
        testSoundsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testSoundsButtonActionPerformed(evt);
            }
        });
        settings.getContentPane().add(testSoundsButton);

        setSoundPackButton.setText("Set soundpack");
        setSoundPackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSoundPackButtonActionPerformed(evt);
            }
        });
        settings.getContentPane().add(setSoundPackButton);

        languageLabel.setText("Language");
        settings.getContentPane().add(languageLabel);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "cs", "en" }));
        settings.getContentPane().add(jComboBox2);

        changeLanguageButton.setText("Change trainings language");
        changeLanguageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeLanguageButtonActionPerformed(evt);
            }
        });
        settings.getContentPane().add(changeLanguageButton);

        tutorialLabel.setText("--  Tutorial mode settings --");
        settings.getContentPane().add(tutorialLabel);

        pauseOnExercise.setText("pause on each new exercise");
        pauseOnExercise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseOnExerciseActionPerformed(evt);
            }
        });
        settings.getContentPane().add(pauseOnExercise);

        pauseOnChange.setText("pause on each new serie");
        pauseOnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseOnChangeActionPerformed(evt);
            }
        });
        settings.getContentPane().add(pauseOnChange);

        cheaterLabel.setText("-- Cheater settings --");
        settings.getContentPane().add(cheaterLabel);

        allowSkipping.setText("allow skipping");
        allowSkipping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowSkippingActionPerformed(evt);
            }
        });
        settings.getContentPane().add(allowSkipping);

        creditsLabel.setText("-- app by judovana --");
        settings.getContentPane().add(creditsLabel);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        settings.getContentPane().add(closeButton);

        exportButton.setText("Export current training to Html");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        settings.getContentPane().add(exportButton);

        downloadButton.setText("Download following jar as trainings");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });
        settings.getContentPane().add(downloadButton);

        jTextField1.setText("file://");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        settings.getContentPane().add(jTextField1);

        saveForOfline.setSelected(true);
        saveForOfline.setText("save for offline usage");
        settings.getContentPane().add(saveForOfline);

        managePluginsButton.setText("Manage plugins");
        managePluginsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                managePluginsButtonActionPerformed(evt);
            }
        });
        settings.getContentPane().add(managePluginsButton);

        exercisesModLabel.setText("Exercise modifiers:");
        settings.getContentPane().add(exercisesModLabel);

        trainingsModLabel.setText("  - Training times modifier:");
        settings.getContentPane().add(trainingsModLabel);
        settings.getContentPane().add(trainingsSpinner);

        pausesModLabel.setText("  - Pause times modifier:");
        settings.getContentPane().add(pausesModLabel);
        settings.getContentPane().add(pausesSpinner);

        restsModLabel.setText("  - Rest times modifier:");
        settings.getContentPane().add(restsModLabel);
        settings.getContentPane().add(restsSpinner);

        iterationsModLabel.setText("  - Iterations modifier");
        settings.getContentPane().add(iterationsModLabel);
        settings.getContentPane().add(iterationsSpinner);

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
        Model.getModel().setLaud(false);
        model.skipForward();
        runAllListeners();
        Model.getModel().setLaud(!mute.isSelected());
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
        Model.getModel().setLaud(false);
        model.jumpBack();
        runAllListeners();
        Model.getModel().setLaud(!mute.isSelected());
    }//GEN-LAST:event_backButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        Model.getModel().save();
        settings.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void muteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_muteActionPerformed
        // TODO add your handling code here:
        Model.getModel().setLaud(!mute.isSelected());
    }//GEN-LAST:event_muteActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        // TODO add your handling code here:
        try {
            if (src != null) {
                File f = src.export(FlashBoulderBalkna.exportDir, new ImagesSaverImpl());
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(f.toURI());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        try {
            Model.getModel().reload(saveForOfline.isSelected(), new URL(jTextField1.getText()));
            FlashBoulderBalkna.hack.reloadTrainings();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_downloadButtonActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextArea1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextArea1MouseClicked
        // TODO add your handling code here:
        imgAndDescription.setVisible(false);
        mainPanel.remove(imgAndDescription);
        mainPanel.setLayout(new GridLayout());

    }//GEN-LAST:event_jTextArea1MouseClicked

    private void testSoundsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testSoundsButtonActionPerformed
        SoundProvider.getInstance().test(jComboBox1.getSelectedItem().toString());                // TODO add your handling code here:
    }//GEN-LAST:event_testSoundsButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(f1);
    }//GEN-LAST:event_formWindowClosing

    private void ratioCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ratioCheckboxActionPerformed
        Model.getModel().setRatioForced(((JCheckBox) evt.getSource()).isSelected());
    }//GEN-LAST:event_ratioCheckboxActionPerformed

    private void pauseOnExerciseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseOnExerciseActionPerformed
        Model.getModel().setPauseOnExercise(((JCheckBox) evt.getSource()).isSelected());
    }//GEN-LAST:event_pauseOnExerciseActionPerformed

    private void pauseOnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseOnChangeActionPerformed
        Model.getModel().setPauseOnChange(((JCheckBox) evt.getSource()).isSelected());
    }//GEN-LAST:event_pauseOnChangeActionPerformed

    private void allowSkippingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allowSkippingActionPerformed
        Model.getModel().setAllowSkipping(((JCheckBox) evt.getSource()).isSelected());
    }//GEN-LAST:event_allowSkippingActionPerformed

    private void changeLanguageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeLanguageButtonActionPerformed
        try {
            Model.getModel().setLanguage((String) jComboBox2.getSelectedItem());
            FlashBoulderBalkna.hack.reloadTrainings();
            SwingTranslator.load((String) jComboBox2.getSelectedItem());
            FlashBoulderBalkna.hack.setLocales();
            this.setLocales();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_changeLanguageButtonActionPerformed

    private void setSoundPackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSoundPackButtonActionPerformed
        Model.getModel().setSoundPack(jComboBox1.getSelectedItem().toString());
    }//GEN-LAST:event_setSoundPackButtonActionPerformed

    private void managePluginsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_managePluginsButtonActionPerformed
        if (Model.getModel().getPluginsDir().exists() && Model.getModel().getPluginsDir().list().length > 0) {
            final JDialog d = new JDialog(this, true);
            d.setTitle(SwingTranslator.R("ManagePlugins"));
            d.setLayout(new GridLayout(0, 1));
            final JList l = new JList(Model.getModel().getPluginsDir().listFiles());
            l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            d.add(l);
            JButton b = new JButton(SwingTranslator.R("DeletePlugin"));
            d.add(b);
            b.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (l.getSelectedValue() != null) {
                        File f = (File) l.getSelectedValue();
                        f.delete();
                        d.dispose();
                    }
                }
            });
            d.pack();
            d.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No plugins");
        }
    }//GEN-LAST:event_managePluginsButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

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
    private javax.swing.JCheckBox allowSkipping;
    private javax.swing.JSpinner auitoiterateSpinner;
    private javax.swing.JLabel autoIterateLabel;
    private javax.swing.JButton backButton;
    private javax.swing.JButton changeLanguageButton;
    private javax.swing.JLabel cheaterLabel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JLabel creditsLabel;
    private javax.swing.JButton downloadButton;
    private javax.swing.JLabel exercisesModLabel;
    private javax.swing.JButton exportButton;
    private javax.swing.JPanel imageHolder;
    private javax.swing.JPanel imgAndDescription;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel iterationsModLabel;
    private javax.swing.JSpinner iterationsSpinner;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<BasicTime> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton managePluginsButton;
    private javax.swing.JCheckBox mute;
    private javax.swing.JLabel nowNextLAbel;
    private javax.swing.JCheckBox pauseOnChange;
    private javax.swing.JCheckBox pauseOnExercise;
    private javax.swing.JLabel pauseRestInfoLabel;
    private javax.swing.JLabel pausesModLabel;
    private javax.swing.JSpinner pausesSpinner;
    private javax.swing.JCheckBox ratioCheckbox;
    private javax.swing.JPanel restAndListPanel;
    private javax.swing.JLabel restsModLabel;
    private javax.swing.JSpinner restsSpinner;
    private javax.swing.JCheckBox saveForOfline;
    private javax.swing.JButton setSoundPackButton;
    private javax.swing.JDialog settings;
    private javax.swing.JButton skipButton;
    private javax.swing.JLabel soundPackLabel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton testSoundsButton;
    private javax.swing.JLabel timer;
    private javax.swing.JPanel timerPanel;
    private javax.swing.JLabel trainingsModLabel;
    private javax.swing.JSpinner trainingsSpinner;
    private javax.swing.JLabel tutorialLabel;
    // End of variables declaration//GEN-END:variables

    private void setLocales() {
        mute.setText(SwingTranslator.R("mute"));
        skipButton.setText(SwingTranslator.R("skipForward"));
        backButton.setText(SwingTranslator.R("jumpBack"));
        // not localised because of logic
        //startButton.setText(SwingTranslator.R("Start"));
        ratioCheckbox.setText(SwingTranslator.R("ForceRaatio"));
        languageLabel.setText(SwingTranslator.R("Language"));
        setSoundPackButton.setText(SwingTranslator.R("SetSoundpack"));
        autoIterateLabel.setText(SwingTranslator.R("Autoiterate"));
        soundPackLabel.setText(SwingTranslator.R("Soundpack"));
        testSoundsButton.setText(SwingTranslator.R("Test"));
        changeLanguageButton.setText(SwingTranslator.R("LanguageChange"));
        tutorialLabel.setText(SwingTranslator.R("TutorialModeLabel"));
        pauseOnExercise.setText(SwingTranslator.R("PauseOnExercise"));
        pauseOnChange.setText(SwingTranslator.R("PauseOnSerie"));
        cheaterLabel.setText(SwingTranslator.R("CheaterSettings"));
        allowSkipping.setText(SwingTranslator.R("Skipping"));
        creditsLabel.setText(SwingTranslator.R("Credits"));
        closeButton.setText(SwingTranslator.R("Close"));
        exportButton.setText(SwingTranslator.R("Export"));
        downloadButton.setText(SwingTranslator.R("Upload"));
        managePluginsButton.setText(SwingTranslator.R("ManagePlugins"));
        exercisesModLabel.setText(SwingTranslator.R("ExerciseModifiers"));
        iterationsModLabel.setText("  - " + SwingTranslator.R("iterationsModifiers"));
        trainingsModLabel.setText("  - " + SwingTranslator.R("TrainingTimesModifier"));
        pausesModLabel.setText("  - " + SwingTranslator.R("PauseTimesModifier"));
        restsModLabel.setText("  - " + SwingTranslator.R("RestTimesModifier"));
        saveForOfline.setText(SwingTranslator.R("SaveForOfline"));

    }

}
