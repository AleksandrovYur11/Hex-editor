package gui;

;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import lombok.Setter;
import model.HexTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.nio.ByteBuffer;

@Getter
@Setter
public class HexTable {
    private boolean isTableModified = false;
    private JPanel tablePanel;
    private JTable hexTable;
    private JScrollPane scrollTable;
    private JPanel conversionPanel;
    private JLabel selectedByteLabel;

    private JMenuBar actionMenuBar;

    private JMenuBar structureMenuBar;

    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();


    public HexTable() {
        $$$setupUI$$$();
        this.hexTable.getTableHeader().setReorderingAllowed(false);
        this.hexTable.setRowHeight(30);
        this.hexTable.setCellSelectionEnabled(true);
        this.hexTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        this.hexTable.setModel(new HexTableModel());

        this.hexTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    delete();
                }
            }
        });
//        this.hexTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
//                    showPopupMenu(e);
//                }
//            }
//        });

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

//        conversionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tablePanel.add(conversionPanel, BorderLayout.SOUTH);

        // Добавляем метку для отображения выбранного байта
//        selectedByteLabel = new JLabel("Выбранный байт: ");
        selectedByteLabel.setText("Выбранный байт: ");
//        conversionPanel.add(selectedByteLabel);


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

        // Выбираем первый байт при запуске
        selectFirstByte();
    }

    private void selectFirstByte() {
        int row = 0;
        int col = 1;

        if (hexTable.getRowCount() > 0 && hexTable.getColumnCount() > 0) {
            hexTable.setRowSelectionInterval(row, row);
            hexTable.setColumnSelectionInterval(col, col);
            updateSelectedByteLabel(row, col);
        }
    }

    private void handleTableKeyPress(KeyEvent evt) {
        if (evt.isControlDown()) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_C:
                    copy();
                    break;
                case KeyEvent.VK_V:
                    pasteWithoutReplace();
                    break;
            }
        }
    }

    private void handleTableClick(MouseEvent evt) {
        Point point = evt.getPoint();
        int row = hexTable.rowAtPoint(point);
        int col = hexTable.columnAtPoint(point);

        if (row >= 0 && col >= 0) {
            updateSelectedByteLabel(row, col);
        }
    }

    private void updateSelectedByteLabel(int row, int col) {
        DefaultTableModel model = (DefaultTableModel) hexTable.getModel();
        Object cellValue = model.getValueAt(row, col);

        if (cellValue != null && cellValue instanceof String) {
            // Преобразовываем строку в массив байт
            byte[] bytes = hexStringToByteArray((String) cellValue);

            // Определяем размер блока данных (2, 4 или 8 байт)
            int blockSize = bytes.length;

            // Определяем знаковый или беззнаковый формат
            boolean isSigned = false;
            if (cellValue.toString().startsWith("-")) {
                isSigned = true;
            }

            // Определяем значение в десятичном формате
            long decimalValue = 0;
            if (blockSize == 2) {
                decimalValue = ByteBuffer.wrap(bytes).getShort();
            } else if (blockSize == 4) {
                decimalValue = ByteBuffer.wrap(bytes).getInt();
            } else if (blockSize == 8) {
                decimalValue = ByteBuffer.wrap(bytes).getLong();
            }

            // Обновляем метку
            selectedByteLabel.setText("Выбранный байт: " + (isSigned ? decimalValue : (decimalValue & 0xFFFFFFFFL)));
        }


        hexTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // При изменениях в таблице устанавливаем флаг изменений
                isTableModified = true;
            }
        });
    }

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

//    public void copy() {
//        int selectedRowCount = hexTable.getSelectedRowCount();
//        int selectedColumnCount = hexTable.getSelectedColumnCount();
//
//        if (selectedRowCount > 0 && selectedColumnCount > 0) {
//            StringBuilder copiedData = new StringBuilder();
//
//            int[] selectedRows = hexTable.getSelectedRows();
//            int[] selectedColumns = hexTable.getSelectedColumns();
//
//            DefaultTableModel model = (DefaultTableModel) hexTable.getModel();
//
//            for (int i : selectedRows) {
//                for (int j : selectedColumns) {
//                    copiedData.append(model.getValueAt(i, j)).append("\t");
//                }
//                copiedData.append("\n");
//            }
//
//            StringSelection stringSelection = new StringSelection(copiedData.toString());
//            clipboard.setContents(stringSelection, null);
//        }
//    }

    public void copy() {
        int selectedRowCount = hexTable.getSelectedRowCount();
        int selectedColumnCount = hexTable.getSelectedColumnCount();

        if (selectedRowCount > 0 && selectedColumnCount > 0) {
            StringBuilder copiedData = new StringBuilder();

            int[] selectedRows = hexTable.getSelectedRows();
            int[] selectedColumns = hexTable.getSelectedColumns();

            HexTableModel model = (HexTableModel) hexTable.getModel();

            for (int i : selectedRows) {
                for (int j : selectedColumns) {
                    Object cellValue = model.getValueAt(i, j);

                    // Проверяем, что ячейка не null и не пуста
                    if (cellValue != null && !cellValue.toString().isEmpty()) {
                        copiedData.append(cellValue).append("\t");
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
                        model.setValueAt(values[j], selectedRows[i], selectedColumns[j]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                        // Заменяем значения в выделенных ячейках на значения из буфера обмена
                        model.setValueAt(values[j], selectedRows[i], selectedColumns[j]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    // Заменяем выделенные ячейки на 0
                    model.setValueAt("00", i, j);
                }
            }
        }
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
        conversionPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tablePanel.add(conversionPanel, BorderLayout.SOUTH);
        selectedByteLabel = new JLabel();
        selectedByteLabel.setText("Label");
        conversionPanel.add(selectedByteLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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