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

@Getter
@Setter
public class HexTable {
    private JPanel tablePanel;
    private JTable hexTable;
    private JScrollPane columnHeader;

    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();


    public HexTable() {
        $$$setupUI$$$();
        this.hexTable.getTableHeader().setReorderingAllowed(false);
//        this.hexTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.hexTable.setCellSelectionEnabled(true);
        this.hexTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    handleDelete();
                }
            }
        });
        this.hexTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    showPopupMenu(e);
                }
            }
        });

        this.hexTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_C:
                            handleCopy();
                            break;
                        case KeyEvent.VK_V:
                            handlePaste();
                            break;
                    }
                }
            }
        });
    }

    private void showPopupMenu(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copyItem = new JMenuItem("Копировать");
        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handleCopy();
            }
        });
        popupMenu.add(copyItem);

        JMenuItem pasteItem = new JMenuItem("Вставить");
        pasteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handlePaste();
            }
        });
        popupMenu.add(pasteItem);

        JMenuItem deleteItem = new JMenuItem("Удалить");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handleDelete();
            }
        });

        popupMenu.add(deleteItem);

        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void handleCopy() {
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

    private void handlePaste() {
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

    private void handleDelete() {
        int selectedRowCount = hexTable.getSelectedRowCount();
        int selectedColumnCount = hexTable.getSelectedColumnCount();

        if (selectedRowCount > 0 && selectedColumnCount > 0) {
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
        tablePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        columnHeader = new JScrollPane();
        tablePanel.add(columnHeader, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        hexTable = new JTable();
        columnHeader.setViewportView(hexTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return tablePanel;
    }
}