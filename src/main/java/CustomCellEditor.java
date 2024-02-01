import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Setter
public class CustomCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private String editedValue;

    public CustomCellEditor() {
        button = new JButton("Edit");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editedValue = JOptionPane.showInputDialog("Enter new value:");
                fireEditingStopped();
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return editedValue;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(String.valueOf(value));
        editedValue = null;
        return button;
    }
}
