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
                // Здесь вы можете открыть диалоговое окно или что-то еще для редактирования значения
                // Например, JOptionPane.showInputDialog
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
        // Установите значение редактора, например, текст на кнопке
        button.setText(String.valueOf(value));
        editedValue = null; // Сбросите предыдущее редактированное значение
        return button;
    }
}
