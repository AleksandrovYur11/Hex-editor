package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
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

//        actionMenuBar = new JMenuBar();
//        JMenu actionMenu = new JMenu("Данные");
//
//// Создание подменю "Копировать"
////        JMenuBar copyMenuBar = new JMenuBar();
//        JMenu pasteMenu = new JMenu("Вставить");
//
//// Создание пунктов подменю "Копировать"
//        JMenuItem pasteReplaceItem = new JMenuItem("С Заменой");
//        JMenuItem pasteWithoutReplaceItem = new JMenuItem("Без Замены");
//        JMenuItem copyItem = new JMenuItem("Копировать");
//        JMenuItem deleteItem = new JMenuItem("Удалить");
//
//// Добавление пунктов в подменю "Копировать"
//        actionMenu.add(copyItem);
//        pasteMenu.add(pasteReplaceItem);
//        pasteMenu.add(pasteWithoutReplaceItem);
//        actionMenu.add(pasteMenu);
//        actionMenu.add(deleteItem);
//
//// Добавление меню "Инструменты" в панель меню
//        actionMenuBar.add(actionMenu);
//
//
//        structureMenuBar = new JMenuBar();
//        JMenu structureMenu = new JMenu("Структура");
//        JMenuItem addColumnItem = new JMenuItem("Добавить столбцы");
//        JMenuItem deleteColumnItem = new JMenuItem("Удалить столбцы");
//        JMenuItem addRowItem = new JMenuItem("Добавить строки");
//        JMenuItem deleteRowItem = new JMenuItem("Удалить строки");
//
//        structureMenu.add(addColumnItem);
//        structureMenu.add(deleteColumnItem);
//        structureMenu.add(addRowItem);
//        structureMenuBar.add(deleteRowItem);


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

//    public void addMouseListenerToForm(MouseAdapter mouseAdapter) {
//        tablePanel.addMouseListener(mouseAdapter);
//    }
//
//    public void handleFormLeftClick(MouseEvent e) {
//        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
//            showPopupMenu(e);
//        }
//    }

//    public void showPopupMenu(MouseEvent e) {
//        JPopupMenu popupMenu = new JPopupMenu();
//
//        JMenuItem copyItem = new JMenuItem("Копировать");
//        popupMenu.add(copyItem);
//
//        copyItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                handleCopy();
//            }
//        });
//
//        JMenuItem pasteItem = new JMenuItem("Вставить");
//        popupMenu.add(pasteItem);
//        pasteItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                handlePaste();
//            }
//        });
//
//        JMenuItem deleteItem = new JMenuItem("Удалить");
//        popupMenu.add(deleteItem);
//
//        deleteItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                handleDelete();
//            }
//        });
//
//        popupMenu.show(e.getComponent(), e.getX(), e.getY());
//    }

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
                    copiedData.append(model.getValueAt(i, j)).append("\t");
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

    //    private void createUIComponents() {
//        this.scrollTable = new JScrollPane(this.hexTable);
//        scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//    }
}