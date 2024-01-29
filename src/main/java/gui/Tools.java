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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;


@Getter
@Setter
public class Tools {

    private File openedFile;
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
        this.toolBar.setFloatable(false);

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

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    openedFile = fileChooser.getSelectedFile(); // Сохранение открытого файла
                    try {
                        byte[] fileBytes = byteServiceImpl.readFileToByteArray(openedFile);
                        hexTableServiceImpl.displayHexData(fileBytes);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Получите массив байтов из таблицы
                    byte[] bytesArray = hexTableServiceImpl.getBytesArray();

                    // Сохраните массив байтов в файл
                    byteServiceImpl.saveByteArrayToFile(bytesArray, openedFile.getAbsolutePath());

                    System.out.println("Данные успешно сохранены в файл.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
                }
            }
        });


        addColumn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Запрашиваем у пользователя количество столбцов
                boolean isValidInput = false;
                while (!isValidInput) {
                    String columnsCountString = JOptionPane.showInputDialog("Введите количество столбцов для добавления:");
                    try {

                        if (columnsCountString == null) {
                            break;
                        }
                        // Пробуем преобразовать введенную строку в число
                        int columnsCount = Integer.parseInt(columnsCountString);
                        if (columnsCount > 0) {
                            // Вызываем метод добавления столбцов при правильном вводе
                            hexTableServiceImpl.addColumns(columnsCount);
                            isValidInput = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Введите положительное число.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Введите целое число.");
                    }
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
        addColumn.setHideActionText(false);
        addColumn.setHorizontalAlignment(0);
        addColumn.setText("Добавить стобцы");
        addColumn.setVerticalAlignment(0);
        toolBar.add(addColumn);
        addRow = new JButton();
        addRow.setText("Добавить строки");
        toolBar.add(addRow);
        seacrhOnByte = new JButton();
        seacrhOnByte.setText("Поиск по байту");
        toolBar.add(seacrhOnByte);
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
