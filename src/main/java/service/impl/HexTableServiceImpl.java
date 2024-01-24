package service.impl;

import gui.HexTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class HexTableServiceImpl {
    private HexTable hexTable;

    public HexTableServiceImpl(HexTable hexTable) {
        this.hexTable = hexTable;
    }

    public void displayHexData(byte[] fileBytes, JTable hexTable) {
        DefaultTableModel tableModel = (DefaultTableModel) hexTable.getModel();
        tableModel.setRowCount(0); // Очистка строк

        int lineCounter = 0;

        for (int i = 0; i < fileBytes.length; i += 16) {
            String address = String.format("%08X", lineCounter) + ": ";
            StringBuilder data = new StringBuilder();

            for (int j = 0; j < 16 && i + j < fileBytes.length; j++) {
                data.append(String.format("%02X", fileBytes[i + j])).append(" ");
            }

            Object[] rowData = {address, data.toString()};
            tableModel.addRow(rowData);

            lineCounter += 16;
        }

        setTableCellRenderer(hexTable);

    }
    private void setTableCellRenderer(JTable hexTable) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        hexTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
    }

    private int calculateMaxAddressLength(byte[] fileBytes) {
        int maxLength = 0;
        for (int i = 0; i < fileBytes.length; i += 16) {
            int addressLength = String.format("%08X", i).length();
            if (addressLength > maxLength) {
                maxLength = addressLength;
            }
        }
        return maxLength;
    }
}
