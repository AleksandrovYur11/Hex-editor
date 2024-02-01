package gui;

import lombok.Getter;
import lombok.Setter;
import service.impl.ByteServiceImpl;
import service.impl.HexTableServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


@Getter
@Setter
public class Tools {

    private File openedFile;
    private JPanel toolBarPanel;
    private JToolBar toolBar;
    private ByteServiceImpl byteServiceImpl;
    private HexTableServiceImpl hexTableServiceImpl;

    private HexTable hexTable;


    public Tools(HexTable hexTable) {
        this.hexTable = hexTable;  // Установка переданного HexTable
        this.byteServiceImpl = new ByteServiceImpl();
        this.hexTableServiceImpl = new HexTableServiceImpl(hexTable);
        $$$setupUI$$$();
        this.toolBar.setFloatable(false);

        JMenuBar fileMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openFileItem = new JMenuItem("Открыть");
        JMenuItem saveFileItem = new JMenuItem("Сохранить");
        JMenuItem saveAsFileItem = new JMenuItem("Сохранить как");
        JMenuItem exitAppItem = new JMenuItem("Выход");

        saveFileItem.setEnabled(false);
        saveAsFileItem.setEnabled(false);

        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(saveAsFileItem);
        fileMenu.addSeparator();
        fileMenu.add(exitAppItem);
        fileMenuBar.add(fileMenu);


        // Создание меню "Инструменты"
        JMenuBar actionMenuBar = new JMenuBar();
        JMenu actionMenu = new JMenu("Данные");
        JMenu pasteMenu = new JMenu("Вставить");

        JMenuItem pasteReplaceItem = new JMenuItem("С Заменой");
        JMenuItem pasteWithoutReplaceItem = new JMenuItem("Без Замены");
        JMenuItem copyItem = new JMenuItem("Копировать");
        JMenuItem deleteItem = new JMenuItem("Удалить");

        actionMenu.setEnabled(false);

        actionMenu.add(copyItem);
        pasteMenu.add(pasteReplaceItem);
        pasteMenu.add(pasteWithoutReplaceItem);
        actionMenu.add(pasteMenu);
        actionMenu.add(deleteItem);

        actionMenuBar.add(actionMenu);

        // Структура
        JMenuBar structureMenuBar = new JMenuBar();
        JMenu structureMenu = new JMenu("Структура");
        JMenuItem addColumnItem = new JMenuItem("Добавить столбцы");
        JMenuItem deleteColumnItem = new JMenuItem("Удалить столбцы");
        JMenuItem addRowItem = new JMenuItem("Добавить строки");
        JMenuItem deleteRowItem = new JMenuItem("Удалить строки");

        structureMenu.setEnabled(false);

        structureMenu.add(addColumnItem);
        structureMenu.add(deleteColumnItem);
        structureMenu.add(addRowItem);
        structureMenu.add(deleteRowItem);

        structureMenuBar.add(structureMenu);

        toolBar.add(fileMenuBar, BorderLayout.WEST);
        toolBar.add(actionMenuBar, BorderLayout.WEST);
        toolBar.add(structureMenuBar, BorderLayout.WEST);

//        this.hexTable.getHexTable().addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
//                    getHexTable().showPopupMenu(e);
//                }
//            }
//        });

        pasteReplaceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.pasteReplace();
            }
        });

        pasteWithoutReplaceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.pasteWithoutReplace();
            }
        });

        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.copy();
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.delete();
            }
        });

//        addColumn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                hexTableServiceImpl.addColumns();
//            }
//        });
        openFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    openedFile = fileChooser.getSelectedFile(); // Сохранение открытого файла
                    try {
                        byte[] fileBytes = byteServiceImpl.readFileToByteArray(openedFile);
                        hexTableServiceImpl.displayHexData(fileBytes);
                        saveFileItem.setEnabled(true);
                        saveAsFileItem.setEnabled(true);
                        structureMenu.setEnabled(true);
                        actionMenu.setEnabled(true);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        exitAppItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        saveFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Получите массив байтов из таблицы
                    byte[] bytesArray = hexTableServiceImpl.getBytesArray();

                    // Сохраните массив байтов в файл
                    ByteServiceImpl.saveByteArrayToFile(bytesArray, openedFile.getAbsolutePath());

                    System.out.println("Данные успешно сохранены в файл.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
                }
            }
        });


        saveAsFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        byte[] bytesArray = hexTableServiceImpl.getBytesArray();

                        ByteServiceImpl.saveByteArrayToFile(bytesArray, selectedFile.getAbsolutePath());

                        openedFile = selectedFile;

                        System.out.println("Данные успешно сохранены в файл: " + selectedFile.getAbsolutePath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
                    }
                }
            }
        });


        addColumnItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableColumnsCount = hexTable.getHexTable().getModel().getColumnCount() - 1;
                boolean isValidInput = false;
                while (!isValidInput) {
                    String columnsCountString = JOptionPane.showInputDialog("Сейчас "
                            + (tableColumnsCount) + " столбец (ов)\n" +
                            "Введите количество столбцов для добавления:");
                    try {

                        if (columnsCountString == null) {
                            break;
                        }
                        int inputColumnsCount = Integer.parseInt(columnsCountString);
                        if (inputColumnsCount > 0) {
                            hexTableServiceImpl.addColumns(inputColumnsCount);
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

        deleteColumnItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableColumnsCount = hexTable.getHexTable().getModel().getColumnCount() - 1;
                boolean isValidInput = false;
                while (!isValidInput) {
                    String columnsCountString = JOptionPane.showInputDialog("Сейчас "
                            + (tableColumnsCount) + " столбец (ов)\n" +
                            "Введите количество столбцов для удаления:");
                    try {

                        if (columnsCountString == null) {
                            break;
                        }
                        int inputColumnsCount = Integer.parseInt(columnsCountString);
                        if (tableColumnsCount - inputColumnsCount <= 0) {
                            JOptionPane.showMessageDialog(null, "Введите число меньше чем "
                                    + (tableColumnsCount));
                        } else if (inputColumnsCount > 0) {
                            hexTableServiceImpl.deleteColumns(inputColumnsCount);
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