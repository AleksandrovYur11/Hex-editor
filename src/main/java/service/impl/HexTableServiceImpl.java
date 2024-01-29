package service.impl;

import gui.HexTable;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class HexTableServiceImpl {
    private HexTable hexTable;

    public HexTableServiceImpl(HexTable hexTable) {
        this.hexTable = hexTable;
    }

    public void displayHexData(byte[] fileBytes) {
        JTable table = this.hexTable.getHexTable();
        Object[] columnHeader = new Object[16];
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Address");

        for (int i = 0; i < 16; i++) {
            columnHeader[i] = String.format("%X", i);
            tableModel.addColumn(columnHeader[i]);
        }

        int lineCounter = 0;

        for (int i = 0; i < fileBytes.length; i += 16) {
            String address = String.format("%08X", lineCounter) + ": ";

            String[] rowData = {address};
            for (int j = 0; j < 16 && i + j < fileBytes.length; j++) {
                rowData = Arrays.copyOf(rowData, rowData.length + 1);
                rowData[rowData.length - 1] = String.format("%02X", fileBytes[i + j]);
            }
            tableModel.addRow(rowData);
            lineCounter += 16;
        }
        table.setModel(tableModel);
        setTableCellRenderer();
    }

    public void addColumns(int numberOfColumns) {
        JTable table = this.hexTable.getHexTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount() - 1; // Учитываем только данные, а не Address

        // Создаем новый вектор столбцов
        Vector<String> newColumns = new Vector<>();
        newColumns.add("Address");

        for (int i = 0; i < columnCount + numberOfColumns; i++) {
            newColumns.add(String.format("%X", i));
        }

        // Создаем новую модель с новыми столбцами
        DefaultTableModel newModel = new DefaultTableModel(newColumns, 0);


        Vector<String> rowData = new Vector<>();
        for (int i = 0; i < rowCount; i++) {
//            rowData[i] = String.format("%08X", i * 16) + ": ";
            for (int j = 1; j <= columnCount; j++) {
                rowData.add((String) model.getValueAt(i, j));
            }
//            String[] newRowData = {address};
//
//            for (int k = 0; i + k <  && k < columnCount + numberOfColumns - 1; k++) {
//                newRowData = Arrays.copyOf(rowData, rowData.length + 1);
//                newRowData[newRowData.length - 1] = rowData[k]; // Пустые значения для новых столбцов
//            }

//            newModel.addRow(newRowData);
        }

        int newRowCount = (int) Math.ceil((double) rowData.size() / (double) (newModel.getColumnCount() - 1));
        int index = 0;
        for (int i = 0; i < newRowCount; i++) {
            Vector<String> newRowData = new Vector<>();
            newRowData.add(String.format("%08X", i * 16) + ": ");
            for (int k = 0; k < model.getColumnCount() - 1 + numberOfColumns && index < rowData.size(); k++) {
//                newRowData = Arrays.copyOf(rowData, rowData.size() + 1);
                newRowData.add(rowData.get(index)); // Пустые значения для новых столбцов
                index++;
            }
            newModel.addRow(newRowData);
        }

        table.setModel(newModel);
        setTableCellRenderer();
        table.repaint();
    }

    public byte[] getBytesArray() {
        JTable table = this.hexTable.getHexTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount() - 1;
        int index = 0;

        byte[] bytesArray = new byte[rowCount * columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 1; j <= columnCount; j++) {
                String hexValue = (String) model.getValueAt(i, j);
                byte[] cellBytes = hexStringToByteArray(hexValue);
                if (cellBytes != null) {
                    System.arraycopy(cellBytes, 0, bytesArray, index, cellBytes.length);
                    index += cellBytes.length;
                }
            }
        }
        return bytesArray;
    }

    private static byte[] hexStringToByteArray(String hex) {
        if (hex == null) {
            return null; // Добавлена проверка на null
        }

        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public void setTableCellRenderer() {
        JTable table = this.hexTable.getHexTable();

        // Создаем редактор ячеек для возможности изменения данных
        CustomCellEditor cellEditor = new CustomCellEditor();
        table.setDefaultEditor(Object.class, cellEditor);


        // Создаем общий рендерер для всех столбцов
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        // Устанавливаем общий рендерер для всех столбцов
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
            if (i > 0) {
                table.getColumnModel().getColumn(i).setMinWidth(30);
                table.getColumnModel().getColumn(i).setMaxWidth(30);
            }
        }
    }

}

