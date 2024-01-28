package service.impl;

import gui.HexTable;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;

public class HexTableServiceImpl {
    private HexTable hexTable;

    public HexTableServiceImpl(HexTable hexTable) {
        this.hexTable = hexTable;
    }
    //------
//    private void setTableCellRenderer(JTable hexTable) {
//        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                           boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                setHorizontalAlignment(SwingConstants.CENTER);
//                return c;
//            }
//        };
//
//        hexTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
//    }
//--------------
//   private void setTableCellRenderer(JTable hexTable) {
//       DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
//           public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                          boolean hasFocus, int row, int column) {
//               Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//               setHorizontalAlignment(SwingConstants.CENTER);
//
//               // В этой части мы проверяем, что текущая ячейка - это первая в строке (Address)
//               if (column == 0) {
//                   setForeground(Color.BLUE); // Выберите цвет, который вам нравится
//                   setFont(getFont().deriveFont(Font.BOLD)); // Делаем шрифт жирным
//               } else {
//                   setForeground(Color.BLACK); // Возвращаем обычный цвет
//                   setFont(getFont().deriveFont(Font.PLAIN)); // Возвращаем обычный стиль шрифта
//               }
//
//               return c;
//           }
//       };
//
//       hexTable.setDefaultRenderer(Object.class, renderer);
//   }
//
//
//
//    private int calculateMaxAddressLength(byte[] fileBytes) {
//        int maxLength = 0;
//        for (int i = 0; i < fileBytes.length; i += 16) {
//            int addressLength = String.format("%08X", i).length();
//            if (addressLength > maxLength) {
//                maxLength = addressLength;
//            }
//        }
//        return maxLength;
//    }
//
//    public void displayHexData(byte[] fileBytes, JTable hexTable) {
//        DefaultTableModel tableModel = new DefaultTableModel();
//
//        tableModel.setColumnIdentifiers(new Object[]{"Address", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"});
//        hexTable.setModel(tableModel);
//
//        int lineCounter = 0;
//
//        for (int i = 0; i < fileBytes.length; i += 16) {
//            String address = String.format("%08X", lineCounter) + ": ";
//
//            String[] rowData = {address};
//
//            for (int j = 0; j < 16 && i + j < fileBytes.length; j++) {
//                rowData = Arrays.copyOf(rowData, rowData.length + 1);
//                rowData[rowData.length - 1] = String.format("%02X", fileBytes[i + j]);
//            }
//
//            tableModel.addRow(rowData);
//
//            lineCounter += 16;
//        }
//
//        setTableCellRenderer(hexTable);
//        ((DefaultTableModel) hexTable.getModel()).fireTableStructureChanged();
//    }

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

//    public void addColumns(int numberOfColumns) {
//        JTable table = this.hexTable.getHexTable();
//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//        int rowCount = model.getRowCount();
//        int columnCount = model.getColumnCount() - 1;
//
//        // Создаем новый вектор столбцов
//        Vector<String> newColumns = new Vector<>();
//        newColumns.add("Address");
//
//        int lineCounter = 0;
//
//        for (int i = 0; i < columnCount + numberOfColumns; i++) {
//            newColumns.add(String.format("%X", i));
//        }
//
//        // Создаем новую модель с новыми столбцами
//        DefaultTableModel newModel = new DefaultTableModel(newColumns, 0);
////        Vector<Object> rowData = new Vector<>();
//
//        for (int i = 0; i < rowCount; i++) {
//
//            String address = String.format("%08X", lineCounter) + ": ";
//
//            String[] rowData = {address};
//            for (int j = 0; j < columnCount; j++) {
//                rowData[j] = (String) model.getValueAt(i, j);
//            }
//            newModel.addRow(rowData);
//        }
//
////        // Переносим данные из старой модели в новую
////        for (int i = 1; i < rowCount; i++) {
//////            Vector<Object> rowData = new Vector<>();
//////            rowData.add(model.getValueAt(i, 0)); // Переносим Address
////
////            // Переносим данные из старой модели в новую
////            for (int j = 1; j < columnCount; j++) {
////                rowData.add(model.getValueAt(i, j));
////            }
////
//////            // Добавляем пустые значения для новых столбцов
//////            for (int j = columnCount; j < newColumns.size(); j++) {
//////                rowData.add(null);
//////            }
////
////            newModel.addRow(rowData);
////        }
//
//        // Устанавливаем новую модель
//        table.setModel(newModel);
//        setTableCellRenderer();
//
//
////        // Устанавливаем одинаковую ширину столбцов
////        int columnWidth = table.getPreferredSize().width / newModel.getColumnCount();
////        for (int i = 0; i < newModel.getColumnCount(); i++) {
////            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
////        }
////
////        // Добавляем горизонтальный скролл
////        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
////        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//    }


//    public void addColumns(int numberOfColumns) {
//        JTable table = this.hexTable.getHexTable();
//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//        int rowCount = model.getRowCount();
//        int columnCount = model.getColumnCount() - 1; // Учитываем только данные, а не Address
//
//        // Создаем новый вектор столбцов
//        Vector<String> newColumns = new Vector<>();
//        newColumns.add("Address");
//
//        for (int i = 0; i < columnCount + numberOfColumns; i++) {
//            newColumns.add(String.format("%X", i));
//        }
//
//        // Создаем новую модель с новыми столбцами
//        DefaultTableModel newModel = new DefaultTableModel(newColumns, 0);
//
//        for (int i = 0; i < rowCount; i++) {
//            String address = String.format("%08X", i * 16) + ": "; // Обновляем адрес
//
//            String[] rowData = {address};
//            for (int j = 1; j < columnCount; j++) {
//                rowData[j] = (String) model.getValueAt(i, 0);
//            }
//            newModel.addRow(rowData);
//        }
//
//        // Устанавливаем новую модель
//        table.setModel(newModel);
//        setTableCellRenderer();
//    }

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


        int newRowCount = (int) Math.ceil((double) rowData.size() / (model.getColumnCount() - 1 + numberOfColumns));
        int index = 0;
        for (int i = 0; i < newRowCount; i++) {
            Vector<String> newRowData = new Vector<>();
            newRowData.add(String.format("%08X", i * 16) + ": ");
            for (int k = 0; k < model.getColumnCount() - 1 + numberOfColumns && index < rowData.size(); k++) {
//                newRowData = Arrays.copyOf(rowData, rowData.size() + 1);
                newRowData.add(rowData.get(index)); // Пустые значения для новых столбцов
                index = index + 1;
            }
            newModel.addRow(newRowData);
        }


        // Устанавливаем новую модель
        table.setModel(newModel);
        setTableCellRenderer();
        table.repaint();
    }


    public void setTableCellRenderer() {
        JTable table = this.hexTable.getHexTable();
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Создаем редактор ячеек для возможности изменения данных
        DefaultCellEditor cellEditor = new DefaultCellEditor(new JTextField());
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
        }
    }
}

