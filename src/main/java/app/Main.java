package app;

import gui.HexTable;
import gui.Tools;
import service.impl.HexTableServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class Main {

    private JPanel mainPanel;

    private HexTable hexTable;

    private JToolBar toolBar;

    public Main() {
        this.mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        createHexTable();
        createToolsManager();
        createMainFrame();
    }

    private void createHexTable() {
//        HexTable newHexTable = new HexTable();
//        JTable table = newHexTable.getHexTable();
//        // Пример добавления данных в таблицу
//        Object[][] data = {
//                {"1", "John Doe", "30"},
//                {"2", "Jane Doe", "25"},
//                {"3", "Bob Smith", "40"}
//        };
//
//        String[] columnNames = {"ID", "Name", "Age"};
//        DefaultTableModel model = new DefaultTableModel(data, columnNames);
//        table.setModel(model);
//
//        table.getTableHeader().setReorderingAllowed(false);
//        table.getTableHeader().setResizingAllowed(false);
//        table.setCellSelectionEnabled(true);
//        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//
//        // Пример настройки модели выбора столбцов
//        TableColumnModel tableColumnModel = table.getColumnModel();
//        ListSelectionModel tableColumnSelectionModel = tableColumnModel.getSelectionModel();
//        tableColumnSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        this.hexTable = newHexTable;
//        this.mainPanel.add(hexTable.getTablePanel(), BorderLayout.CENTER);
//        HexTableServiceImpl hexTableService = new HexTableServiceImpl(hexTable);
        //-----------------------

        HexTable myTable = new HexTable();
        JTable table = myTable.getHexTable();

        // Создаем модель данных
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Address");
        for (int i = 0; i < 16; i++) {
            model.addColumn(String.format("%X", i));
        }

//         Добавляем строки с данными
        for (int i = 0; i < 16; i++) {
            Object[] columnData = {String.format("%X", i), "", ""};  // Значение Address в hex-формате
            model.addColumn(columnData);
        }


        // Устанавливаем модель данных в таблицу
        table.setModel(model);

        // Настройка внешнего вида таблицы
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.hexTable = myTable;
        this.mainPanel.add(hexTable.getTablePanel(), BorderLayout.CENTER);
        HexTableServiceImpl hexTableService = new HexTableServiceImpl(hexTable);
    }


    private void createMainFrame() {
        JFrame frame = new JFrame("Hex editor tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Установка размера фрейма на половину экрана
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        frame.setSize(width, height);

        // Установка положения фрейма по центру экрана
        frame.setLocationRelativeTo(null);
    }

    private void createToolsManager() {
        Tools toolsManager = new Tools(hexTable);  // Передача hexTable в конструктор Tools
        mainPanel.add(toolsManager.getToolBarPanel(), BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
