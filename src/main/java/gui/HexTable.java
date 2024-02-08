package gui;

;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import lombok.Setter;
import model.HexTableModel;
import service.impl.ByteServiceImpl;
import service.impl.HexTableServiceImpl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Vector;

@Getter
@Setter
public class HexTable {
    private boolean isTableModified = false;
    private File openedFile;
    private JPanel tablePanel;
    private JTable hexTable;
    private JScrollPane scrollTable;
    private JPanel conversionPanel;
    private JLabel byteLabel;
    private JLabel valueByte;
    private JLabel valueShort;
    private JLabel valueUnsignedInt;
    private JLabel shortLabel;
    private JLabel unsignedIntLabel;

    private JMenuBar actionMenuBar;

    private JMenuBar structureMenuBar;

    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private HexTableServiceImpl hexTableService = new HexTableServiceImpl(this);
    private ByteServiceImpl byteService;


    public HexTable() {
//        $$$setupUI$$$();
        hexTable.getTableHeader().setReorderingAllowed(false);
        hexTable.setRowHeight(30);
        hexTable.setCellSelectionEnabled(true);
        hexTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        hexTable.setColumnSelectionAllowed(true);
        hexTable.setRowSelectionAllowed(true);
        hexTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        hexTable.setModel(new HexTableModel());
        hexTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    delete();
                }
            }
        });

//        this.hexTable.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.isControlDown()) {
//                    switch (e.getKeyCode()) {
//                        case KeyEvent.VK_C:
//                            copy();
//                            break;
//                        case KeyEvent.VK_V:
//                            handlePaste();
//                            break;
//                    }
//                }
//            }
//        });

//        tablePanel.add(conversionPanel, BorderLayout.SOUTH);
//        byteLabel.setText("byte: ");
//        shortLabel.setText("short: ");
//        unsignedIntLabel.setText("unsigned int: ");

        hexTable.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                handleTableKeyPress(evt);
            }
        });

        hexTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTableClick(e);
            }
        });

        scrollTable.getVerticalScrollBar().addAdjustmentListener(this::scrollAdjustmentValueChanged);

        hexTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // При изменениях в таблице устанавливаем флаг изменений
                isTableModified = true;
            }
        });

        // Добавление слушателя событий на таблицу
        hexTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = hexTable.getSelectedRow();
                    int selectedColumn = hexTable.getSelectedColumn();
                    if (selectedRow != -1 && selectedColumn != -1) {
//                        updateSelectedByteLabel(selectedRow, selectedColumn);
                    }
                }
            }
        });

        hexTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = hexTable.getSelectedRow();
                    int selectedColumn = hexTable.getSelectedColumn();
                    if (selectedRow != -1 && selectedColumn != -1) {
//                        updateSelectedByteLabel(selectedRow, selectedColumn);
                    }
                }
            }
        });


        selectFirstByte();
    }

    public void scrollAdjustmentValueChanged(AdjustmentEvent e) {
        if (!e.getValueIsAdjusting()) {
            int scrollBarValue = e.getValue();
            int scrollBarMaximum = e.getAdjustable().getMaximum();
            int scrollBarExtent = e.getAdjustable().getVisibleAmount();

            if (scrollBarMaximum != 0 && scrollBarValue + scrollBarExtent == scrollBarMaximum) {
                try {
                    byte[] nextBytes = byteService.readFileToByteArray(byteService.getFile());
                    loadedTable(nextBytes);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void loadedTable(byte[] bytes) {
        HexTableModel model = (HexTableModel) hexTable.getModel();
        int bytesPerRow = model.getColumnCount() - 1;
        int x = model.getRowCount();

        int emptyCells = bytesPerRow - (x % bytesPerRow);
        if (emptyCells == bytesPerRow) emptyCells = 0;

        int lastRow = model.getRowCount() - 1;
        for (int i = 0; i < emptyCells; i++) {
            model.setValueAt("", lastRow, bytesPerRow - emptyCells + i);
        }

        // Добавляем новые строки, начиная с отсчета последней строки
        int lineCounter = lastRow * bytesPerRow; // Переменная для хранения адреса
        for (int i = emptyCells; i < bytes.length; i += bytesPerRow) {
            Object[] rowData = new Object[bytesPerRow + 1]; // +1 для первого столбца с адресом
            rowData[0] = String.format("%08X", lineCounter) + ": "; // Форматированный адрес
            for (int j = 0; j < bytesPerRow && i + j < bytes.length; j++) {
                rowData[j + 1] = String.format("%02X", bytes[i + j]);
            }
            model.addRow(rowData);
            lastRow++;
            lineCounter += bytesPerRow; // Увеличиваем адрес на количество байтов в строке
        }
    }

    private void selectFirstByte() {
        int row = 0;
        int col = 1;

        if (hexTable.getRowCount() > 0 && hexTable.getColumnCount() > 0) {
            hexTable.changeSelection(row, col, false, false);
//            updateSelectedByteLabel(row, col);
        }
    }


    private void handleTableKeyPress(KeyEvent evt) {
        if (evt.isControlDown()) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_C:
                    copy();
                    break;
                case KeyEvent.VK_V:
                    pasteReplace();
                    break;
            }
        }
    }

    private void handleTableClick(MouseEvent evt) {
        Point point = evt.getPoint();
        int row = hexTable.rowAtPoint(point);
        int col = hexTable.columnAtPoint(point);

        if (row >= 0 && col >= 0) {
//            updateSelectedByteLabel(row, col);
        }
    }

//    private void updateSelectedByteLabel(int row, int col) {
//        HexTableModel model = (HexTableModel) hexTable.getModel();
//        Object cellValue = model.getValueAt(row, col);
//
//        if (cellValue != null && cellValue instanceof String) {
//            byte[] bytes = hexStringToByteArray((String) cellValue);
//            int blockSize = bytes.length;
//
//            // Переводим байты в значения различных типов
//            if (blockSize == 1) {
//                // Преобразуем байт в беззнаковое целое число и добавляем в соответствующий JTextArea
//                int unsignedValue = bytes[0] & 0xFF;
//                valueByte.setText(String.valueOf(unsignedValue));
//            } else if (blockSize == 2) {
//                // Преобразуем два байта в short и добавляем в соответствующий JTextArea
//                short shortValue = ByteBuffer.wrap(bytes).getShort();
//                valueShort.setText(String.valueOf(shortValue));
//            } else if (blockSize == 4) {
//                // Преобразуем четыре байта в int и добавляем в соответствующий JTextArea
//                int intValue = ByteBuffer.wrap(bytes).getInt();
//                valueUnsignedInt.setText(String.valueOf(intValue));
//            } else if (blockSize == 8) {
//                // Преобразуем восемь байт в long и добавляем в соответствующий JTextArea
//                long longValue = ByteBuffer.wrap(bytes).getLong();
//                valueUnsignedInt.setText(String.valueOf(longValue));
//            }
//        }
//    }


    private byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
    //-----------------------------


    public void copy() {
        int selectedRowCount = hexTable.getSelectedRowCount();
        int selectedColumnCount = hexTable.getSelectedColumnCount();

        if (selectedRowCount > 0 && selectedColumnCount > 0) {
            StringBuilder copiedData = new StringBuilder();
            int[] selectedRows = hexTable.getSelectedRows();
            int[] selectedColumns = hexTable.getSelectedColumns();
            DefaultTableModel model = (DefaultTableModel) hexTable.getModel();

            for (int i : selectedRows) {
                for (int j : selectedColumns) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        copiedData.append(value).append("\t");
                    }
                }
                copiedData.append("\n");
            }

            StringSelection stringSelection = new StringSelection(copiedData.toString());
            clipboard.setContents(stringSelection, null);
        }
    }

    public void pasteWithoutReplace() {
        int selectedRowCount = hexTable.getSelectedRowCount();
        int selectedColumnCount = hexTable.getSelectedColumnCount();

        if (selectedRowCount == 1 && selectedColumnCount == 1) {
            try {
                String clipboardData = (String) clipboard.getData(DataFlavor.stringFlavor);

                String[] rows = clipboardData.split("\n");
                int[] selectedRows = hexTable.getSelectedRows();
                int[] selectedColumns = hexTable.getSelectedColumns();

                HexTableModel model = (HexTableModel) hexTable.getModel();
                byte[] originalData = hexTableService.getBytesArray(); // Получаем все данные таблицы в виде массива байтов

                int columnCount = model.getColumnCount() - 1;

                Vector<Byte> byteList = new Vector<>(originalData.length + rows.length * selectedColumnCount);

                int bufferIndex = 0;

                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 1; col <= columnCount; col++) {
                        if (bufferIndex < originalData.length) {
                            if (isSelectedCell(row, col, selectedRows, selectedColumns)) {
                                Arrays.stream(rows)
                                        .map(rowValue -> rowValue.split("\t"))
                                        .flatMap(Arrays::stream)
                                        .filter(this::isHexString)
                                        .map(hexValue -> (byte) Integer.parseInt(hexValue, 16))
                                        .forEach(byteList::add);
                            } else {
                                byteList.add(originalData[bufferIndex++]);
                            }
                        } else {
                            break;
                        }
                    }
                }

                byte[] bytesToInsert = new byte[byteList.size()];
                for (int i = 0; i < byteList.size(); i++) {
                    bytesToInsert[i] = byteList.get(i);
                }

                hexTableService.displayHexData(bytesToInsert);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isSelectedCell(int row, int column, int[] selectedRows, int[] selectedColumns) {
        for (int i = 0; i < selectedRows.length; i++) {
            if (row == selectedRows[i] && column == selectedColumns[i]) {
                return true;
            }
        }
        return false;
    }

    public void pasteReplace() {
        int selectedRowCount = hexTable.getSelectedRowCount();
        int selectedColumnCount = hexTable.getSelectedColumnCount();

        if (selectedRowCount > 0 && selectedColumnCount > 0) {
            try {
                String clipboardData = (String) clipboard.getData(DataFlavor.stringFlavor);

                String[] rows = clipboardData.split("\n");
                int[] selectedRows = hexTable.getSelectedRows();
                int[] selectedColumns = hexTable.getSelectedColumns();

                DefaultTableModel model = (DefaultTableModel) hexTable.getModel();

                int rowCount = Math.min(selectedRows.length, rows.length);
                int colCount = Math.min(selectedColumns.length, rows[0].split("\t").length);

                for (int i = 0; i < rowCount; i++) {
                    String[] values = rows[i].split("\t");
                    for (int j = 0; j < colCount; j++) {
                        if (isHexString(values[j])) {
                            model.setValueAt(values[j], selectedRows[i], selectedColumns[j]);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isHexString(String value) {
        try {
            Long.parseLong(value, 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public void delete() {
        int selectedRowCount = hexTable.getSelectedRowCount();
        int selectedColumnCount = hexTable.getSelectedColumnCount();

        if (selectedRowCount > 0 && selectedColumnCount > 1) {
            int[] selectedRows = hexTable.getSelectedRows();
            int[] selectedColumns = hexTable.getSelectedColumns();

            DefaultTableModel model = (DefaultTableModel) hexTable.getModel();

            for (int i : selectedRows) {
                for (int j : selectedColumns) {
                    model.setValueAt("00", i, j);
                }
            }
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(0, 0));
        scrollTable = new JScrollPane();
        tablePanel.add(scrollTable, BorderLayout.CENTER);
        hexTable = new JTable();
        hexTable.setAutoResizeMode(4);
        scrollTable.setViewportView(hexTable);
        conversionPanel = new JPanel();
        conversionPanel.setLayout(new BorderLayout(0, 0));
        tablePanel.add(conversionPanel, BorderLayout.WEST);
        final JLabel label1 = new JLabel();
        label1.setText("Label");
        conversionPanel.add(label1, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return tablePanel;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */

}