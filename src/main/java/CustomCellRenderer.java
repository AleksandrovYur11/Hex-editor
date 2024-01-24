import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

@Getter
@Setter
public class CustomCellRenderer extends JPanel implements TableCellRenderer {
    private JLabel[] labels;

    public CustomCellRenderer() {
        setLayout(new GridLayout(1, 16));
        labels = new JLabel[16];
        for (int i = 0; i < 16; i++) {
            labels[i] = new JLabel();
            labels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            labels[i].setHorizontalAlignment(SwingConstants.CENTER);
            add(labels[i]);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        byte[] bytes = (byte[]) value;
        for (int i = 0; i < bytes.length; i++) {
            labels[i].setText(String.format("%02X", bytes[i]));
        }
        return this;
    }
}
