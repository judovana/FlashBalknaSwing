/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fbb.balkna.swing;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.fbb.balkna.Packages;
import org.fbb.balkna.awt.utils.ImagesSaverImpl;
import org.fbb.balkna.model.Model;
import org.fbb.balkna.model.Settings;
import org.fbb.balkna.model.SoundProvider;
import org.fbb.balkna.model.primitives.Training;
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
    private javax.swing.JLabel creditsLabel;
    private javax.swing.JButton downloadButton;
    private javax.swing.JLabel exercisesModLabel;
    private javax.swing.JButton exportButton;
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
    private final Training src;

    public SettingsDialogue(final Training src) {
        this.src = src;
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
        invertScreenLayout.setSelected(Settings.getSettings().isInvertScreenCompress());
        alowScreenChange.setSelected(Settings.getSettings().isAllowScreenChange());
        ratioCheckbox.setSelected(Model.getModel().isRatioForced());
        this.pack();

    }

    public final  void init() {
        consts();
        this.getContentPane().setLayout(new java.awt.GridLayout(0, 1));
        this.getContentPane().add(ratioCheckbox);

        autoIterateLabel.setText("autoiterate images on timer with speed (s): (0 disabled)");
        this.getContentPane().add(autoIterateLabel);
        this.getContentPane().add(auitoiterateSpinner);

        mute.setText("mute");
        mute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muteActionPerformed(evt);
            }
        });
        this.getContentPane().add(mute);
        
        invertScreenLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Settings.getSettings().setInvertScreenCompress(invertScreenLayout.isSelected());
            }
        });
        this.getContentPane().add(invertScreenLayout);
        
        alowScreenChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Settings.getSettings().setAllowScreenChange(alowScreenChange.isSelected());
            }
        });
        this.getContentPane().add(alowScreenChange);
        

        soundPackLabel.setText("Soundpack:");
        this.getContentPane().add(soundPackLabel);

        this.getContentPane().add(jComboBox1);

        testSoundsButton.setText("Test");
        testSoundsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testSoundsButtonActionPerformed(evt);
            }
        });
        this.getContentPane().add(testSoundsButton);

        setSoundPackButton.setText("Set soundpack");
        setSoundPackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSoundPackButtonActionPerformed(evt);
            }
        });
        this.getContentPane().add(setSoundPackButton);

        languageLabel.setText("Language");
        this.getContentPane().add(languageLabel);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(Packages.LANGUAGES));
        this.getContentPane().add(jComboBox2);

        changeLanguageButton.setText("Change trainings language");
        changeLanguageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeLanguageButtonActionPerformed(evt);
            }
        });
        this.getContentPane().add(changeLanguageButton);

        tutorialLabel.setText("--  Tutorial mode settings --");
        this.getContentPane().add(tutorialLabel);

        pauseOnExercise.setText("pause on each new exercise");
        pauseOnExercise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseOnExerciseActionPerformed(evt);
            }
        });
        this.getContentPane().add(pauseOnExercise);

        pauseOnChange.setText("pause on each new serie");
        pauseOnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseOnChangeActionPerformed(evt);
            }
        });
        this.getContentPane().add(pauseOnChange);

        cheaterLabel.setText("-- Cheater settings --");
        this.getContentPane().add(cheaterLabel);

        allowSkipping.setText("allow skipping");
        allowSkipping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowSkippingActionPerformed(evt);
            }
        });
        this.getContentPane().add(allowSkipping);

        creditsLabel.setText("-- app by judovana --");
        this.getContentPane().add(creditsLabel);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        this.getContentPane().add(closeButton);

        exportButton.setText("Export current training to Html");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        this.getContentPane().add(exportButton);

        downloadButton.setText("Download following jar as trainings");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });
        this.getContentPane().add(downloadButton);

        jTextField1.setText("file://");
        this.getContentPane().add(jTextField1);

        saveForOfline.setSelected(true);
        saveForOfline.setText("save for offline usage");
        this.getContentPane().add(saveForOfline);

        managePluginsButton.setText("Manage plugins");
        managePluginsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                managePluginsButtonActionPerformed(evt);
            }
        });
        this.getContentPane().add(managePluginsButton);

        exercisesModLabel.setText("Exercise modifiers:");
        this.getContentPane().add(exercisesModLabel);

        trainingsModLabel.setText("  - Training times modifier:");
        this.getContentPane().add(trainingsModLabel);
        this.getContentPane().add(trainingsSpinner);

        pausesModLabel.setText("  - Pause times modifier:");
        this.getContentPane().add(pausesModLabel);
        this.getContentPane().add(pausesSpinner);

        restsModLabel.setText("  - Rest times modifier:");
        this.getContentPane().add(restsModLabel);
        this.getContentPane().add(restsSpinner);

        iterationsModLabel.setText("  - Iterations modifier");
        this.getContentPane().add(iterationsModLabel);
        this.getContentPane().add(iterationsSpinner);
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Model.getModel().save();
        this.setVisible(false);
    }

    private void muteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Model.getModel().setLaud(!mute.isSelected());
    }

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {
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
        alowScreenChange.setText(SwingTranslator.R("alowScreenChange"));
        invertScreenLayout.setText(SwingTranslator.R("invertScreenLayout"));

    }

    private void consts() {
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
        invertScreenLayout = new javax.swing.JCheckBox();
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
    }
}
