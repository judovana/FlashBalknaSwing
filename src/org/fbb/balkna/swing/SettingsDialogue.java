package org.fbb.balkna.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.fbb.balkna.Packages;
import org.fbb.balkna.awt.utils.ImagesSaverImpl;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.settings.Settings;
import org.fbb.balkna.model.SoundProvider;
import org.fbb.balkna.model.primitives.Cycle;
import org.fbb.balkna.model.primitives.Cycles;
import org.fbb.balkna.model.primitives.ExerciseOverrides;
import org.fbb.balkna.model.primitives.Exercises;
import org.fbb.balkna.model.primitives.Training;
import org.fbb.balkna.model.primitives.Trainings;
import org.fbb.balkna.model.primitives.history.Record;
import org.fbb.balkna.model.primitives.history.RecordWithOrigin;
import org.fbb.balkna.model.utils.JavaPluginProvider;
import org.fbb.balkna.swing.locales.SwingTranslator;

/**
 *
 * @author jvanek
 */
public class SettingsDialogue extends JDialog {

    private javax.swing.JCheckBox allowSkipping;
    private javax.swing.JSpinner auitoiterateSpinner;
    private javax.swing.JLabel autoIterateLabel;
    private javax.swing.JButton changeLanguageButton;
    private javax.swing.JLabel cheaterLabel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel creditsLabel;
    private javax.swing.JButton downloadButton;
    private javax.swing.JLabel exercisesModLabel;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton localPlugin;
    private javax.swing.JLabel iterationsModLabel;
    private javax.swing.JSpinner iterationsSpinner;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JButton managePluginsButton;
    private javax.swing.JCheckBox mute;
    private javax.swing.JCheckBox pauseOnChange;
    private javax.swing.JCheckBox pauseOnExercise;
    private javax.swing.JCheckBox alowScreenChange;
    private javax.swing.JCheckBox invertScreenLayout;
    private javax.swing.JLabel pausesModLabel;
    private javax.swing.JSpinner pausesSpinner;
    private javax.swing.JCheckBox ratioCheckbox;
    private javax.swing.JLabel restsModLabel;
    private javax.swing.JSpinner restsSpinner;
    private javax.swing.JCheckBox saveForOfline;
    private javax.swing.JButton setSoundPackButton;
    private javax.swing.JLabel soundPackLabel;
    private javax.swing.JButton testSoundsButton;
    private javax.swing.JLabel trainingsModLabel;
    private javax.swing.JSpinner trainingsSpinner;
    private javax.swing.JLabel tutorialLabel;

    private javax.swing.JTabbedPane bothPannels;
    private javax.swing.JPanel settings;
    private javax.swing.JPanel appearence;
    private javax.swing.JPanel stats;

    private JLabel colorsInfo;
    private JLabel trainingDelimiterSizeLabel;
    private JSpinner trainingDelimiterSize;
    private JLabel trainingDelimiterColorLabel;
    private JLabel trainingDelimiterColor;
    private JLabel selectedItemColorLabel;
    private JLabel selectedItemColor;
    private JLabel mainTimerSizeLabel;
    private JSpinner mainTimerSize;
    private JLabel mainTimerColorLabel;
    private JLabel mainTimerColor;
    private JLabel mainTimerPositionLabelV;
    private JComboBox mainTimerPositionV;
    private JLabel mainTimerPositionLabelH;
    private JComboBox mainTimerPositionH;
    private JCheckBox exCheck;
    private JCheckBox trCheck;
    private JCheckBox cycCheck;
    private JCheckBox saveStats;
    private JCheckBox playLongTermSounds;
    private JCheckBox inhibitSleepAndroid;
    private JButton exDel;
    private JButton trDel;
    private JButton cycDel;
    private JList<RecordWithOrigin> statisticList;
    private JLabel singleExerciseOverrideLabel;
    private JTextField singleExerciseOverride;
    private JLabel singleExerciseParsed;
    private JCheckBox messages;

    private final Training src1;
    private final Cycle src2;

    public SettingsDialogue(final Training src1, Cycle src2) {
        this.src1 = src1;
        this.src2 = src2;
        this.setModal(true);
        init();

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

        prepare();
        setLocales();
    }

    private void prepare() {
        jComboBox1.setModel(new DefaultComboBoxModel(Packages.SOUND_PACKS()));
        jComboBox1.setSelectedItem(SoundProvider.getInstance().getUsedSoundPack());
        if (src1 == null && src2 == null) {
            exportButton.setEnabled(false);
        } else {
            exportButton.setEnabled(true);
        }

        allowSkipping.setSelected(Model.getModel().isAllowSkipping());
        pauseOnChange.setSelected(Model.getModel().isPauseOnChange());
        pauseOnExercise.setSelected(Model.getModel().isPauseOnExercise());
        mute.setSelected(!Model.getModel().isLaud());
        invertScreenLayout.setSelected(Settings.getSettings().isInvertScreenCompress());
        alowScreenChange.setSelected(Settings.getSettings().isAllowScreenChange());
        inhibitSleepAndroid.setSelected(Settings.getSettings().isSleepInhibited());
        ratioCheckbox.setSelected(Model.getModel().isRatioForced());
        playLongTermSounds.setSelected(Settings.getSettings().isPlayLongTermSounds());
        this.pack();

    }

    public final void init() {
        consts();

        settings.setLayout(new java.awt.GridLayout(0, 1));
        appearence.setLayout(new java.awt.GridLayout(0, 1));

        bothPannels.add(settings);
        bothPannels.add(appearence);
        bothPannels.add(stats);

        reloadStats();
        stats.setLayout(new BorderLayout());
        JScrollPane jsp = new JScrollPane(statisticList);
        stats.add(jsp);
        JPanel jpp = new JPanel(new GridLayout(2, 4));
        stats.add(jpp, BorderLayout.SOUTH);
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reloadStats();
            }
        };
        exCheck.addActionListener(al);
        trCheck.addActionListener(al);
        cycCheck.addActionListener(al);
        jpp.add(exCheck);
        jpp.add(trCheck);
        jpp.add(cycCheck);
        JButton nextButton = new JButton(SwingTranslator.R("Next"));
        nextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Nearly done! Will be in next update!");
            }
        });
        jpp.add(nextButton);

        messages.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Record.SHOW_MESSAGE = messages.isSelected();
                int i = statisticList.getSelectedIndex();
                reloadStats();
                if (i >= 0) {
                    statisticList.setSelectedIndex(i);
                }
            }
        });

        jpp.add(exDel);
        jpp.add(trDel);
        jpp.add(cycDel);
        jpp.add(messages);

        exDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File[] ff = Exercises.getStatsDir().listFiles();
                for (File f : ff) {
                    f.delete();
                }
                reloadStats();
            }
        });
        trDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File[] ff = Trainings.getStatsDir().listFiles();
                for (File f : ff) {
                    f.delete();
                }
                reloadStats();
            }
        });

        cycDel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File[] ff = Cycles.getStatsDir().listFiles();
                int a = JOptionPane.showConfirmDialog(null, SwingTranslator.R("CylesClearWarning"));
                if (a == JOptionPane.YES_OPTION) {
                    for (File f : ff) {
                        f.delete();
                    }
                    reloadStats();
                }
            }
        });

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(bothPannels, BorderLayout.CENTER);

        JPanel sharedButtons = new JPanel();
        sharedButtons.setLayout(new java.awt.GridLayout(0, 1));
        this.getContentPane().add(sharedButtons, BorderLayout.SOUTH);

        ratioCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ratioCheckboxActionPerformed(e);
            }
        });
        appearence.add(ratioCheckbox);

        autoIterateLabel.setText("autoiterate images on timer with speed (s): (0 disabled)");
        appearence.add(autoIterateLabel);
        appearence.add(auitoiterateSpinner);

        saveStats.setSelected(Model.getModel().isSaveStats());
        settings.add(saveStats);
        saveStats.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Model.getModel().setSaveStats(saveStats.isSelected());
            }
        });

        mute.setText("mute");
        mute.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muteActionPerformed(evt);
            }
        });
        settings.add(mute);

        playLongTermSounds.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Settings.getSettings().setPlayLongTermSounds(playLongTermSounds.isSelected());
            }
        });
        settings.add(playLongTermSounds);

        invertScreenLayout.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Settings.getSettings().setInvertScreenCompress(invertScreenLayout.isSelected());
            }
        });
        appearence.add(invertScreenLayout);

        alowScreenChange.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Settings.getSettings().setAllowScreenChange(alowScreenChange.isSelected());
            }
        });
        inhibitSleepAndroid.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Settings.getSettings().setSleepInhibited(inhibitSleepAndroid.isSelected());
            }
        });
        appearence.add(alowScreenChange);
        appearence.add(inhibitSleepAndroid);

        soundPackLabel.setText("Soundpack:");
        settings.add(soundPackLabel);

        settings.add(jComboBox1);

        testSoundsButton.setText("Test");
        testSoundsButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testSoundsButtonActionPerformed(evt);
            }
        });
        settings.add(testSoundsButton);

        setSoundPackButton.setText("Set soundpack");
        setSoundPackButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSoundPackButtonActionPerformed(evt);
            }
        });
        settings.add(setSoundPackButton);

        languageLabel.setText("Language");
        appearence.add(languageLabel);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(Packages.LANGUAGES));
        appearence.add(jComboBox2);

        changeLanguageButton.setText("Change trainings language");
        changeLanguageButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeLanguageButtonActionPerformed(evt);
            }
        });
        appearence.add(changeLanguageButton);

        tutorialLabel.setText("--  Tutorial mode settings --");
        settings.add(tutorialLabel);

        pauseOnExercise.setText("pause on each new exercise");
        pauseOnExercise.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseOnExerciseActionPerformed(evt);
            }
        });
        settings.add(pauseOnExercise);

        pauseOnChange.setText("pause on each new serie");
        pauseOnChange.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseOnChangeActionPerformed(evt);
            }
        });
        settings.add(pauseOnChange);

        cheaterLabel.setText("-- Cheater settings --");
        settings.add(cheaterLabel);

        allowSkipping.setText("allow skipping");
        allowSkipping.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowSkippingActionPerformed(evt);
            }
        });
        settings.add(allowSkipping);

        creditsLabel.setText("-- app by judovana --");
        sharedButtons.add(creditsLabel);
        creditsLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://flashbb.cz/aktualne"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        sharedButtons.add(closeButton, BorderLayout.SOUTH);

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        sharedButtons.add(resetButton, BorderLayout.SOUTH);

        exportButton.setText("Export current training to Html");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        settings.add(exportButton);

        downloadButton.setText("Download following jar as trainings");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });
        settings.add(downloadButton);

        jTextField1.setText("file://");
        settings.add(jTextField1);

        saveForOfline.setSelected(true);
        saveForOfline.setText("save for offline usage");
        settings.add(saveForOfline);

        managePluginsButton.setText("Manage plugins");
        managePluginsButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                managePluginsButtonActionPerformed(evt);
            }
        });
        settings.add(managePluginsButton);
        localPlugin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jf = new JFileChooser();
                    int x = jf.showOpenDialog(null);
                    if (jf.getSelectedFile() != null && x == JFileChooser.APPROVE_OPTION) {
                        jTextField1.setText(jf.getSelectedFile().toURI().toURL().toExternalForm());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        settings.add(localPlugin);

        settings.add(singleExerciseOverrideLabel);
        settings.add(singleExerciseOverride);
        singleExerciseOverride.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                act();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                act();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                act();
            }

            private void act() {
                Settings.getSettings().setSingleExerciseOverride(singleExerciseOverride.getText());
                singleExerciseParsed.setText(ExerciseOverrides.fakeFromString(Settings.getSettings().getSingleExerciseOverride()).format());
            }
        });
        singleExerciseOverride.setText(Settings.getSettings().getSingleExerciseOverride());
        settings.add(singleExerciseParsed);

        exercisesModLabel.setText("Exercise modifiers:");
        settings.add(exercisesModLabel);

        trainingsModLabel.setText("  - Training times modifier:");
        settings.add(trainingsModLabel);
        settings.add(trainingsSpinner);

        pausesModLabel.setText("  - Pause times modifier:");
        settings.add(pausesModLabel);
        settings.add(pausesSpinner);

        restsModLabel.setText("  - Rest times modifier:");
        settings.add(restsModLabel);
        settings.add(restsSpinner);

        iterationsModLabel.setText("  - Iterations modifier");
        settings.add(iterationsModLabel);
        settings.add(iterationsSpinner);

        appearence.add(colorsInfo);
        appearence.add(trainingDelimiterSizeLabel);
        appearence.add(trainingDelimiterSize);
        trainingDelimiterSize.setValue(Settings.getSettings().getTrainingDelimiterSize());
        trainingDelimiterSize.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Settings.getSettings().setTrainingDelimiterSize((Integer) ((JSpinner) e.getSource()).getValue());
            }
        });
        appearence.add(trainingDelimiterColorLabel);
        appearence.add(trainingDelimiterColor);
        colorPreview(trainingDelimiterColor, Settings.getSettings().getTrainingDelimiterColor());
        trainingDelimiterColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Color q;
                    if (Settings.getSettings().getTrainingDelimiterColor() == null) {
                        q = JColorChooser.showDialog(null, null, null);
                    } else {
                        q = JColorChooser.showDialog(null, null, new Color(Settings.getSettings().getTrainingDelimiterColor()));
                    }
                    if (q != null) {
                        Settings.getSettings().setTrainingDelimiterColor(q.getRGB());
                    }
                } else {
                    Settings.getSettings().setTrainingDelimiterColor(null);
                }
                colorPreview(trainingDelimiterColor, Settings.getSettings().getTrainingDelimiterColor());
            }

        });
        appearence.add(selectedItemColorLabel);
        appearence.add(selectedItemColor);
        colorPreview(selectedItemColor, Settings.getSettings().getSelectedItemColor());
        selectedItemColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Color q;
                    if (Settings.getSettings().getSelectedItemColor() == null) {
                        q = JColorChooser.showDialog(null, null, null);
                    } else {
                        q = JColorChooser.showDialog(null, null, new Color(Settings.getSettings().getSelectedItemColor()));
                    }
                    if (q != null) {
                        Settings.getSettings().setSelectedItemColor(q.getRGB());
                    }
                } else {
                    Settings.getSettings().setSelectedItemColor(null);
                }
                colorPreview(selectedItemColor, Settings.getSettings().getSelectedItemColor());
            }

        });
        appearence.add(mainTimerSizeLabel);
        appearence.add(mainTimerSize);
        mainTimerSize.setValue(Settings.getSettings().getMainTimerSize());
        mainTimerSize.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int nvalue = (Integer) ((JSpinner) e.getSource()).getValue();
                Settings.getSettings().setMainTimerSize(nvalue);
                if (TraningWindow.hack != null) {
                    TraningWindow.hack.setTimerFont();
                }
            }
        });
        appearence.add(mainTimerColorLabel);
        appearence.add(mainTimerColor);
        colorPreview(mainTimerColor, Settings.getSettings().getMainTimerColor());
        mainTimerColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Color q;
                    if (Settings.getSettings().getMainTimerColor() == null) {
                        q = JColorChooser.showDialog(null, null, null);
                    } else {
                        q = JColorChooser.showDialog(null, null, new Color(Settings.getSettings().getMainTimerColor()));
                    }
                    if (q != null) {
                        Settings.getSettings().setMainTimerColor(q.getRGB());
                    }
                } else {
                    Settings.getSettings().setMainTimerColor(null);
                }
                colorPreview(mainTimerColor, Settings.getSettings().getMainTimerColor());
                if (TraningWindow.hack != null) {
                    TraningWindow.hack.setTimerFont();
                }
            }

        });
        appearence.add(mainTimerPositionLabelV);
        mainTimerPositionV.setModel(new DefaultComboBoxModel(Settings.VPOSITIONS));
        mainTimerPositionV.setSelectedItem(Settings.getSettings().getMainTimerPositionV());
        mainTimerPositionV.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.getSettings().setMainTimerPositionV(mainTimerPositionV.getSelectedItem().toString());
                if (TraningWindow.hack != null) {
                    TraningWindow.hack.setTimerFont();
                }
            }
        });
        appearence.add(mainTimerPositionV);
        appearence.add(mainTimerPositionLabelH);
        mainTimerPositionH.setModel(new DefaultComboBoxModel(Settings.HPOSITIONS));
        mainTimerPositionH.setSelectedItem(Settings.getSettings().getMainTimerPositionH());
        mainTimerPositionH.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.getSettings().setMainTimerPositionH(mainTimerPositionH.getSelectedItem().toString());
                if (TraningWindow.hack != null) {
                    TraningWindow.hack.setTimerFont();
                }
            }
        });
        appearence.add(mainTimerPositionH);
    }

    private void colorPreview(JLabel label, Integer value) {
        if (value != null) {
            label.setBackground(new Color(value));
            label.setText("");
        } else {
            label.setText("system");
            label.setBackground(label.getParent().getBackground());
        }
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().save();
        this.setVisible(false);
    }

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().resetDefaults();
        this.setVisible(false);
    }

    private void muteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Model.getModel().setLaud(!mute.isSelected());
    }

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            if (src2 != null) {
                File f = src2.export(FlashBoulderBalkna.exportDir, new ImagesSaverImpl());
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(f.toURI());
                }
            }
            if (src1 != null) {
                File f = src1.export(FlashBoulderBalkna.exportDir, new ImagesSaverImpl());
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(f.toURI());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Model.getModel().reload(saveForOfline.isSelected(), new URL(jTextField1.getText()));
            FlashBoulderBalkna.hack.reloadTrainings();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void testSoundsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SoundProvider.getInstance().test(jComboBox1.getSelectedItem().toString());                // TODO add your handling code here:
    }

    private void ratioCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().setRatioForced(((JCheckBox) evt.getSource()).isSelected());
    }

    private void pauseOnExerciseActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().setPauseOnExercise(((JCheckBox) evt.getSource()).isSelected());
    }

    private void pauseOnChangeActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().setPauseOnChange(((JCheckBox) evt.getSource()).isSelected());
    }

    private void allowSkippingActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().setAllowSkipping(((JCheckBox) evt.getSource()).isSelected());
    }

    private void changeLanguageButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Model.getModel().setLanguage((String) jComboBox2.getSelectedItem());
            FlashBoulderBalkna.hack.reloadTrainings();
            SwingTranslator.load((String) jComboBox2.getSelectedItem());
            FlashBoulderBalkna.hack.setLocales();
            if (TraningWindow.hack != null) {
                TraningWindow.hack.setLocales();
            }
            this.setLocales();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void setSoundPackButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().setSoundPack(jComboBox1.getSelectedItem().toString());
    }

    private void managePluginsButtonActionPerformed(java.awt.event.ActionEvent evt) {
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
                        JavaPluginProvider.getPluginPaths().removePath(f);
                        d.dispose();
                        FlashBoulderBalkna.hack.reloadTrainings();
                    }
                }
            });
            d.pack();
            d.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No plugins");
        }
    }

    private void setLocales() {
        mute.setText(SwingTranslator.R("mute"));
        saveStats.setText(SwingTranslator.R("saveStats"));
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
        closeButton.setFont(closeButton.getFont().deriveFont(Font.BOLD));
        exportButton.setText(SwingTranslator.R("Export"));
        localPlugin.setText(SwingTranslator.R("localPlugin"));
        downloadButton.setText(SwingTranslator.R("Upload"));
        managePluginsButton.setText(SwingTranslator.R("ManagePlugins"));
        exercisesModLabel.setText(SwingTranslator.R("ExerciseModifiers"));
        iterationsModLabel.setText("  - " + SwingTranslator.R("iterationsModifiers"));
        trainingsModLabel.setText("  - " + SwingTranslator.R("TrainingTimesModifier"));
        pausesModLabel.setText("  - " + SwingTranslator.R("PauseTimesModifier"));
        restsModLabel.setText("  - " + SwingTranslator.R("RestTimesModifier"));
        saveForOfline.setText(SwingTranslator.R("SaveForOfline"));
        alowScreenChange.setText(SwingTranslator.R("alowScreenChange"));
        inhibitSleepAndroid.setText(SwingTranslator.R("inhibitSleepAndroid"));
        invertScreenLayout.setText(SwingTranslator.R("invertScreenLayout"));

        resetButton.setText(SwingTranslator.R("resetButton"));

        bothPannels.setTitleAt(0, SwingTranslator.R("settingsTab"));
        bothPannels.setTitleAt(1, SwingTranslator.R("appearenceTab"));
        bothPannels.setTitleAt(2, SwingTranslator.R("statsTab"));

        colorsInfo.setText(SwingTranslator.R("colorsInfo"));
        trainingDelimiterSizeLabel.setText(SwingTranslator.R("trainingDelimiterSizeLabel"));
        trainingDelimiterColorLabel.setText(SwingTranslator.R("trainingDelimiterColorLabel"));
        selectedItemColorLabel.setText(SwingTranslator.R("selectedItemColorLabel"));
        mainTimerSizeLabel.setText(SwingTranslator.R("mainTimerSizeLabel"));
        mainTimerColorLabel.setText(SwingTranslator.R("mainTimerColorLabel"));
        mainTimerPositionLabelV.setText(SwingTranslator.R("mainTimerPositionLabelV"));
        mainTimerPositionLabelH.setText(SwingTranslator.R("mainTimerPositionLabelH"));

        trainingDelimiterColor.setOpaque(true);
        selectedItemColor.setOpaque(true);
        mainTimerColor.setOpaque(true);

        exCheck.setText(SwingTranslator.R("mainTabExercise"));
        trCheck.setText(SwingTranslator.R("mainTabTrainings"));
        cycCheck.setText(SwingTranslator.R("mainTabCycles"));
        exDel.setText(SwingTranslator.R("delete"));
        trDel.setText(SwingTranslator.R("delete"));
        cycDel.setText(SwingTranslator.R("delete"));
        playLongTermSounds.setText(SwingTranslator.R("playLongTermSounds"));

        messages.setText(SwingTranslator.R("messages"));

        singleExerciseOverrideLabel.setText(SwingTranslator.R("singleTrainingOverride"));;

        pack();
    }

    private void consts() {

        closeButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();

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
        alowScreenChange = new javax.swing.JCheckBox();
        inhibitSleepAndroid = new javax.swing.JCheckBox();
        invertScreenLayout = new javax.swing.JCheckBox();
        cheaterLabel = new javax.swing.JLabel();
        allowSkipping = new javax.swing.JCheckBox();
        creditsLabel = new javax.swing.JLabel();
        exportButton = new javax.swing.JButton();
        localPlugin = new javax.swing.JButton();
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

        bothPannels = new JTabbedPane();

        settings = new JPanel();
        appearence = new JPanel();
        stats = new JPanel();
        statisticList = new JList();

        colorsInfo = new JLabel();
        trainingDelimiterSizeLabel = new JLabel();
        trainingDelimiterSize = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        trainingDelimiterColorLabel = new JLabel();
        trainingDelimiterColor = new JLabel();
        selectedItemColorLabel = new JLabel();
        selectedItemColor = new JLabel();
        mainTimerSizeLabel = new JLabel();
        mainTimerSize = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        mainTimerColorLabel = new JLabel();
        mainTimerColor = new JLabel();
        mainTimerPositionLabelV = new JLabel();
        mainTimerPositionV = new JComboBox();
        mainTimerPositionLabelH = new JLabel();
        mainTimerPositionH = new JComboBox();
        saveStats = new JCheckBox();
        cycCheck = new JCheckBox("", true);
        trCheck = new JCheckBox("", true);
        exCheck = new JCheckBox("", false);
        playLongTermSounds = new JCheckBox();

        cycDel = new JButton("");
        trDel = new JButton("");
        exDel = new JButton("");

        singleExerciseOverride = new JTextField();
        singleExerciseOverrideLabel = new JLabel();
        singleExerciseParsed = new JLabel();

        messages = new JCheckBox("", Record.SHOW_MESSAGE);

    }

    private void reloadStats() {
        statisticList.setModel(new AbstractListModel() {

            List<RecordWithOrigin> data = Model.getModel().gatherStatistics(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected());
//            BufferedImage b1 = SwingGraph.generatePassFailGraph(DataProvider.getDataProvider().getDayData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()));
//            BufferedImage b6 = SwingGraph.generatePassFailGraph(DataProvider.getDataProvider().getHourData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()));
//            BufferedImage b2 = SwingGraph.generatePassFailGraph(DataProvider.getDataProvider().getWeekData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()));
//            BufferedImage b3 = SwingGraph.generatePassFailGraph(DataProvider.getDataProvider().getMonthData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()));
//            BufferedImage b4 = SwingGraph.generatePassFailGraph(DataProvider.getDataProvider().getYearData1(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()));
//            BufferedImage b5 = SwingGraph.generatePassFailGraph(DataProvider.getDataProvider().getYearData2(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()));
//            BufferedImage b1 = SwingGraph.generateTimeGraph(DataProvider.getDataProvider().getDayTimeData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()), getColumns());
//            BufferedImage b6 = SwingGraph.generateTimeGraph(DataProvider.getDataProvider().getHourTimeData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()), getColumns());
//            BufferedImage b2 = SwingGraph.generateTimeGraph(DataProvider.getDataProvider().getWeekTimeData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()), getColumns());
//            BufferedImage b3 = SwingGraph.generateTimeGraph(DataProvider.getDataProvider().getMonthTimeData(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()), getColumns());
//            BufferedImage b4 = SwingGraph.generateTimeGraph(DataProvider.getDataProvider().getYearTimeData1(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()), getColumns());
//            BufferedImage b5 = SwingGraph.generateTimeGraph(DataProvider.getDataProvider().getYearTimeData2(exCheck.isSelected(), trCheck.isSelected(), cycCheck.isSelected()), getColumns());

            @Override
            public int getSize() {
                return data.size();
            }

            @Override
            public Object getElementAt(int i) {
                return data.get(i);
            }

            private int getColumns() {
                int columns = 0;
                if (exCheck.isSelected()) {
                    columns++;
                }
                if (trCheck.isSelected()) {
                    columns++;
                }
                if (cycCheck.isSelected()) {
                    columns++;
                }
                return columns;
            }
        });
    }
}
