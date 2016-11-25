package org.fbb.balkna.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.net.URI;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.fbb.balkna.model.settings.PluginlistProvider;
import org.fbb.balkna.swing.locales.SwingTranslator;

/**
 *
 * @author jvanek
 */
public class KnownPlugins extends JDialog {

    private SettingsDialogue master;

    public KnownPlugins() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle(SwingTranslator.R("PPkp"));
        final JTable t = new JTable();
        PluginlistProvider.LoadedPlugins l = PluginlistProvider.obtain();
        List<PluginlistProvider.ParsedLine> r = l.getResult();
        DefaultTableModel dt = new DefaultTableModel(new Object[]{
            SwingTranslator.R("PPstate"),
            SwingTranslator.R("PPdesc"),
            SwingTranslator.R("PPhomePage"),
            SwingTranslator.R("PPlink"),}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (PluginlistProvider.ParsedLine parsedLine : r) {
            dt.addRow(new Object[]{
                parsedLine.getState(),
                parsedLine.getDescription(),
                parsedLine.getHomePage().toExternalForm(),
                parsedLine.getUrl().toExternalForm(),});
        }
        t.setModel(dt);
        t.getColumnModel().getColumn(0).setCellRenderer(new ColoredLabelRenderer());
        t.getColumnModel().getColumn(2).setCellRenderer(new UnderscoredButtonRenderer());
        t.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        t.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = t.rowAtPoint(evt.getPoint());
                int col = t.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    Object val = t.getValueAt(row, col);
                    if (col == 2) {
                        try {
                            Desktop.getDesktop().browse(new URI((String) val));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                    }
                    if (col == 3) {
                        master.setPluginUrl((String) val);
                        KnownPlugins.this.setVisible(false);
                        KnownPlugins.this.dispose();
                    }
                }
            }
        });
        this.add(new JScrollPane(t));
        this.add(new JLabel(l.getSource().getResolution()), BorderLayout.NORTH);
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KnownPlugins().setVisible(true);
            }
        });
    }

    void setMaster(SettingsDialogue aThis) {
        this.master = aThis;
    }

    private static class ColoredLabelRenderer implements TableCellRenderer {

        public ColoredLabelRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            PluginlistProvider.PluginState s = (PluginlistProvider.PluginState) value;
            JLabel l = new JLabel(s.getResolution());
            if (s == PluginlistProvider.PluginState.STABLE) {
                l.setForeground(Color.GREEN);
            }
            if (s == PluginlistProvider.PluginState.TESTING) {
                l.setForeground(Color.YELLOW);
            }
            if (s == PluginlistProvider.PluginState.UNKNOWN) {
                l.setForeground(Color.RED);
            }
            return l;
        }
    }

    private static class UnderscoredButtonRenderer implements TableCellRenderer {

        public UnderscoredButtonRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton b = new JButton("<html><body><u>" + value + "</body></html>");
            return b;
        }
    }

    private static class ButtonRenderer implements TableCellRenderer {

        public ButtonRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton b = new JButton("<html><body>" + SwingTranslator.R("PPuse") + ":<i>&nbsp;" + value + "</i></body></html>");
            return b;
        }
    }

}
