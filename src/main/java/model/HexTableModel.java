package model;

import lombok.Getter;
import lombok.Setter;

import javax.swing.table.DefaultTableModel;

@Getter
@Setter
public class HexTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }
}
