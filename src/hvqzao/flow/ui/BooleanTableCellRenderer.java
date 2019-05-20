package hvqzao.flow.ui;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

public class BooleanTableCellRenderer extends JCheckBox implements TableCellRenderer {

    public BooleanTableCellRenderer() {
        super();
        initialize();
    }

    private void initialize() {
        setOpaque(true);
        putClientProperty("JComponent.sizeVariant", "small");
        SwingUtilities.updateComponentTreeUI(this);
        setLayout(new GridBagLayout());
        setMargin(new Insets(0, 0, 0, 0));
        setHorizontalAlignment(JLabel.CENTER);
        setBorderPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //int modelRow = table.convertRowIndexToModel(row);
        setBackground(ThemeHelper.cellBackground(table.getRowCount(), row, isSelected));
        if (value instanceof Boolean) {
            setSelected((Boolean) value);
        }
        return this;
    }
}
