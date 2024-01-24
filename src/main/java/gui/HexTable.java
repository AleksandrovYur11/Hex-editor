package gui;

//import com.intellij.uiDesigner.core.GridConstraints;
//import com.intellij.uiDesigner.core.GridLayoutManager;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class Table extends JFrame {
//    private JTable hexTable;
//    private JPanel tablePanel;
//
//    public Table() {
//        setTitle("Table Form");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        String[] columnNames = {"Column 1", "Column 2", "Column 3"};
//        String[][] data = {{"Data 1", "Data 2", "Data 3"},
//                {"Data 4", "Data 5", "Data 6"},
//                {"Data 7", "Data 8", "Data 9"}};
//
//        JTable table = new JTable(data, columnNames);
//        JScrollPane scrollPane = new JScrollPane(table);
//
//        add(scrollPane, BorderLayout.CENTER);
//
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//    }

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Getter
@Setter
public class HexTable {
    private JPanel tablePanel;
    private JTable hexTable;


    public HexTable() {
    }


//    private HexTable initTable() {
//        HexTable myTable = new HexTable();
//        JTable table = myTable.getHexTable();
//        // Пример добавления данных в таблицу
//        Object[][] data = {
//                {"1", "John Doe", "30"},
//                {"2", "Jane Doe", "25"},
//                {"3", "Bob Smith", "40"}
//        };
//
//        String[] columnNames = {"Address", "Byte", "Age"};
//        DefaultTableModel model = new DefaultTableModel(data, columnNames);
//        table.setModel(model);
//
//        table.getTableHeader().setReorderingAllowed(false);
//        table.getTableHeader().setResizingAllowed(false);
//        table.setCellSelectionEnabled(true);
//        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//
//        // Пример настройки модели выбора столбцов
////        TableColumnModel tableColumnModel = table.getColumnModel();
////        ListSelectionModel tableColumnSelectionModel = tableColumnModel.getSelectionModel();
////        tableColumnSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
////        appPanel.add(myTable.getTablePanel(), BorderLayout.CENTER);
////        table.setVisible(true);
////        appPanel.setVisible(true);
//        return myTable;
//        // Пример установки редактора ячеек (замените TableCellEditor на ваш собственный)
////        myTable.setDefaultEditor(Object.class, new CustomCellEditor());
//    }





    private HexTable initTable() {
        HexTable myTable = new HexTable();
        JTable table = myTable.getHexTable();

        // Создаем модель данных
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);

        // Добавляем столбец "Address"
        model.addColumn("Address");

        // Добавляем столбцы с данными
        String[] columnNames = {"Byte", "Age"};
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }

        // Добавляем строки с данными
        for (int i = 0; i < 16; i++) {
            Object[] rowData = {String.format("%X", i), "", ""};  // Значение Address в hex-формате
            model.addRow(rowData);
        }

        // Настройка внешнего вида таблицы
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        return myTable;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
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
        tablePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        hexTable = new JTable();
        tablePanel.add(hexTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return tablePanel;
    }
}