package gui;

import lombok.Getter;
import lombok.Setter;
import service.impl.ByteServiceImpl;
import service.impl.HexTableServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

@Getter
@Setter
public class Tools {
    private JPanel toolBarPanel;
    private JToolBar toolBar;
    private JButton addColumn;
    private JButton addRow;
    private JButton seacrhOnByte;
    private ByteServiceImpl byteServiceImpl;
    private HexTableServiceImpl hexTableServiceImpl;

    private HexTable hexTable;  // Добавлено поле


    public Tools(HexTable hexTable) {
        this.hexTable = hexTable;  // Установка переданного HexTable
        this.byteServiceImpl = new ByteServiceImpl();
        this.hexTableServiceImpl = new HexTableServiceImpl(hexTable);
        $$$setupUI$$$();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openItem = new JMenuItem("Открыть");
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem exitItem = new JMenuItem("Выход");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        toolBar.add(menuBar, BorderLayout.WEST);

        openItem.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    byte[] fileBytes = byteServiceImpl.readFileToByteArray(selectedFile);
                    hexTableServiceImpl.displayHexData(fileBytes);
//                    String hexRepresentation = getHexRepresentation(fileBytes);
//                    hexTextArea.setText(hexRepresentation);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exitItem.addActionListener(actionEvent -> System.exit(0));


//        saveItem.addActionListener(actionEvent -> {
//            JFileChooser fileChooser = new JFileChooser();
//            int returnValue = fileChooser.showSaveDialog(null);
//            if (returnValue == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//                try {
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
//                    writer.write(hexTextArea.getText());
//                    writer.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });

        addColumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Запрашиваем у пользователя количество столбцов
                String columnsCountString = JOptionPane.showInputDialog("Введите количество столбцов для добавления:");

                try {
                    // Пробуем преобразовать введенную строку в число
                    int columnsCount = Integer.parseInt(columnsCountString);
                    if (columnsCount > 0) {
                        // Вызываем метод добавления столбцов при правильном вводе
                        hexTableServiceImpl.addColumns(columnsCount);
                    } else {
                        JOptionPane.showMessageDialog(null, "Пожалуйста, введите положительное число.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Пожалуйста, введите корректное число.");
                }
            }
        });

    }

//    public void addColumns(int numberOfColumns) {
//        JTable table = this.hexTable.getHexTable();
//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//        // Добавляем указанное количество столбцов
//        for (int i = 0; i < numberOfColumns; i++) {
//            model.addColumn(String.format("%X", model.getColumnCount()));
//        }
//
//        // Переносим данные с прошлых строк
//        Vector<Vector<Object>> dataVector = model.getDataVector();
//        for (Vector<Object> row : dataVector) {
//            for (int i = 0; i < numberOfColumns; i++) {
//                row.add(null);  // Добавляем пустые ячейки для новых столбцов
//            }
//        }
//
//        model.fireTableStructureChanged();
//    }

//    public void addColumns(int numberOfColumns) {
//        JTable table = this.hexTable.getHexTable();
//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//        int rowCount = model.getRowCount();
//        int columnCount = model.getColumnCount();
//
//        // Добавляем указанное количество столбцов
//        for (int i = 0; i < numberOfColumns; i++) {
//            model.addColumn(String.format("%X", columnCount + i));
//        }
//
//        // Переносим данные из следующих строк в новые столбцы
//        for (int i = 0; i < rowCount; i++) {
//            Vector<Object> rowData = (Vector<Object>) model.getDataVector().get(i);
//            for (int j = 0; j < numberOfColumns; j++) {
//                // Копируем значение из соответствующей ячейки
//                int columnIndex = columnCount + j;
//                if (columnIndex < rowData.size()) {
//                    rowData.add(rowData.get(columnIndex));
//                } else {
//                    rowData.add(null);  // Если данных нет, добавляем пустую ячейку
//                }
//            }
//
//        }
//
//        // Устанавливаем одинаковую ширину столбцов
//        int columnWidth = table.getPreferredSize().width / model.getColumnCount();
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
//        }
//
//        // Добавляем горизонтальный скролл
//        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//
//        table.repaint();
//    }




    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BorderLayout(0, 0));
        toolBar = new JToolBar();
        toolBarPanel.add(toolBar, BorderLayout.NORTH);
        addColumn = new JButton();
        addColumn.setHorizontalAlignment(0);
        addColumn.setText("Добавить стобцы");
        addColumn.setVerticalAlignment(0);
        toolBarPanel.add(addColumn, BorderLayout.WEST);
        addRow = new JButton();
        addRow.setText("Добавить строки");
        toolBarPanel.add(addRow, BorderLayout.CENTER);
        seacrhOnByte = new JButton();
        seacrhOnByte.setText("Поиск по байту");
        toolBarPanel.add(seacrhOnByte, BorderLayout.EAST);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return toolBarPanel;
    }


//    private ArrayList<JPanel> getPanels(){
//        ArrayList<JPanel> panels = new ArrayList<JPanel>();
//        panels.add(this.getToolBarPanel());
//        panels.add(this.table1.getTablePanel());
//        return panels;
//    }


//    private ArrayList<Component> getComponents() {
//        ArrayList<Component> components = new ArrayList<Component>();
//        components.add(this.getToolBarPanel());
//        components.add(this.getHexTable().getTablePanel());
//        return components;
//    }


}
