package gui;

import gui.menuBar.ActionMenuBar;
import gui.menuBar.FileMenuBar;
import gui.menuBar.StructureMenuBar;
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
    private FileMenuBar fileMenuBar;
    private ActionMenuBar actionMenuBar;
    private StructureMenuBar structureMenuBar;

    private HexTable hexTable;


    public Tools(HexTable hexTable) {
        this.hexTable = hexTable;  // Установка переданного HexTable
        this.byteServiceImpl = new ByteServiceImpl();
        this.hexTableServiceImpl = new HexTableServiceImpl(hexTable);

        $$$setupUI$$$();
        this.toolBar.setFloatable(false);

        fileMenuBar = new FileMenuBar();
        actionMenuBar = new ActionMenuBar();
        structureMenuBar = new StructureMenuBar();

        toolBar.add(fileMenuBar.getFileMenuBar(), BorderLayout.WEST);
        toolBar.add(actionMenuBar.getActionMenuBar(), BorderLayout.WEST);
        toolBar.add(structureMenuBar.getStructureMenuBar(), BorderLayout.WEST);

//        fileMenuBar.getOpenFileItem().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser fileChooser = new JFileChooser();
//                int returnValue = fileChooser.showOpenDialog(null);
//                if (returnValue == JFileChooser.APPROVE_OPTION) {
//                    openedFile = fileChooser.getSelectedFile(); // Сохранение открытого файла
//                    try {
//                        byte[] fileBytes = byteServiceImpl.readFileToByteArray(openedFile);
//                        hexTableServiceImpl.displayHexData(fileBytes);
//                        fileMenuBar.getSaveFileItem().setEnabled(true);
//                        fileMenuBar.getSaveAsFileItem().setEnabled(true);
//                        structureMenu.setEnabled(true);
//                        actionMenu.setEnabled(true);
//
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        });

        fileMenuBar.getOpenFileItem().addActionListener(actionEvent -> {
            if (hexTable.isTableModified()) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Сохранить изменения перед открытием нового файла?",
                        "Предупреждение", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    saveChangesAndOpenFile();
                } else if (choice == JOptionPane.NO_OPTION) {
                    openFile();
                }
            } else {
                openFile();
            }
        });

        fileMenuBar.getSaveFileItem().addActionListener(actionEvent -> {
            try {
                // Получите массив байтов из таблицы
                byte[] bytesArray = hexTableServiceImpl.getBytesArray();

                // Сохраните массив байтов в файл
                byteServiceImpl.saveByteArrayToFile(bytesArray, openedFile.getAbsolutePath());
                hexTable.setTableModified(false);

                System.out.println("Данные успешно сохранены в файл.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
            }
        });


        fileMenuBar.getSaveAsFileItem().addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    byte[] bytesArray = hexTableServiceImpl.getBytesArray();

                    byteServiceImpl.saveByteArrayToFile(bytesArray, selectedFile.getAbsolutePath());

                    openedFile = selectedFile;
                    hexTable.setTableModified(false);

                    System.out.println("Данные успешно сохранены в файл: " + selectedFile.getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
                }
            }
        });

        fileMenuBar.getExitAppItem().addActionListener(actionEvent -> {
            if (hexTable.isTableModified()) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Сохранить изменения перед выходом?",
                        "Предупреждение", JOptionPane.YES_NO_CANCEL_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        // Получите массив байтов из таблицы
                        byte[] bytesArray = hexTableServiceImpl.getBytesArray();

                        // Сохраните массив байтов в файл
                        byteServiceImpl.saveByteArrayToFile(bytesArray, openedFile.getAbsolutePath());

                        System.out.println("Данные успешно сохранены в файл.");
                        System.exit(0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
                    }

                } else if (choice == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        });

        actionMenuBar.getPasteReplaceItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.pasteReplace();
            }
        });

        actionMenuBar.getPasteWithoutReplaceItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.pasteWithoutReplace();
            }
        });

        actionMenuBar.getCopyItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.copy();
            }
        });

        actionMenuBar.getDeleteItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexTable.delete();
            }
        });

        structureMenuBar.getAddColumnItem().addActionListener(new ActionListener() {
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

        structureMenuBar.getDeleteColumnItem().addActionListener(new ActionListener() {
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

//    private void saveChangesAndExit() {
//        if (openedFile != null) {
//            try {
//                byte[] bytesArray = hexTableServiceImpl.getBytesArray();
//                ByteServiceImpl.saveByteArrayToFile(bytesArray, openedFile.getAbsolutePath());
//                System.out.println("Данные успешно сохранены в файл.");
//                System.exit(0);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
//            }
//        } else {
//            saveAsFile();
//        }
//    }

    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                byte[] bytesArray = hexTableServiceImpl.getBytesArray();
                byteServiceImpl.saveByteArrayToFile(bytesArray, selectedFile.getAbsolutePath());

                openedFile = selectedFile;

                System.out.println("Данные успешно сохранены в файл: " + selectedFile.getAbsolutePath());
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка при сохранении файла.");
            }
        }
    }

    public void handleExit() {
        if (hexTable.isTableModified()) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Сохранить изменения перед выходом?",
                    "Предупреждение", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
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
        }
    }

    private void saveChangesAndOpenFile() {
        saveChanges();  // Метод, сохраняющий изменения
        openFile();
    }

    private void saveChanges() {
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

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            openedFile = fileChooser.getSelectedFile(); // Сохранение открытого файла
            try {
                byte[] fileBytes = byteServiceImpl.readFileToByteArray(openedFile);
                hexTableServiceImpl.displayHexData(fileBytes);
                fileMenuBar.getSaveFileItem().setEnabled(true);
                fileMenuBar.getSaveAsFileItem().setEnabled(true);
                structureMenuBar.getStructureMenu().setEnabled(true);
                actionMenuBar.getActionMenu().setEnabled(true);
                hexTable.setTableModified(false);  // Сброс флага изменений после успешного открытия файла
            } catch (IOException ex) {
                ex.printStackTrace();
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
}