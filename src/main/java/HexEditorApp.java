//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//
//public class HexEditorApp extends JFrame {
////    public static void main(String[] args) {
////        JFrame frame = new JFrame("Моё первое окно");
////        JFrame frame = new JFrame("Моё первое окно");
////        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        Dimension size = new Dimension(600, 600);
////        frame.setSize(size);
////
////        JButton button = new JButton("Открыть файл");
////        frame.add(button, BorderLayout.SOUTH);
////        button.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                JFileChooser fileChooser = new JFileChooser();
////                int returnValue = fileChooser.showOpenDialog(null);
////
////                if(returnValue == JFileChooser.APPROVE_OPTION){
////                    System.out.println("Выбранный файл " + fileChooser.getSelectedFile().getName());
////                    JLabel fileLabel = new JLabel(fileChooser.getApproveButtonText());
////                    frame.add(fileLabel, BorderLayout.LINE_END);
////                }
////            }
////        });
////
////        JLabel label = new JLabel("Привет мир!!!");
////        frame.add(label, BorderLayout.NORTH);
////
////        frame.setVisible(true);
////
////    }
//
//    private JTable hexTable;
//    private DefaultTableModel tableModel;
//    private JTextArea hexTextArea;
//
//    private JLabel statusBar;
//
//    public HexEditorApp() {
//        setTitle("Hex Editor");
//        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        String[] MainColumnNames = {"Address", "Data"};
//
//        tableModel = new DefaultTableModel(MainColumnNames, 0);
//        hexTable = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(hexTable);
//
//
//        String[] columnNames = new String[16];
//        for (int i = 0; i < 16; i++) {
//            columnNames[i] = String.format("%02X", i);
//        }
//        hexTextArea = new JTextArea();
//        hexTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//        hexTextArea.setEditable(true);
//
////        JScrollPane scrollPane = new JScrollPane(hexTextArea);
//        add(scrollPane);
//
//        JMenuBar menuBar = new JMenuBar();
//        JMenu fileMenu = new JMenu("Файл");
//        JMenuItem openItem = new JMenuItem("Открыть");
//        JMenuItem saveItem = new JMenuItem("Сохранить");
//        JMenuItem exitItem = new JMenuItem("Выход");
//
//        fileMenu.add(openItem);
//        fileMenu.add(saveItem);
//        fileMenu.add(exitItem);
//        menuBar.add(fileMenu);
//        setJMenuBar(menuBar);
//
//        openItem.addActionListener(actionEvent -> {
//            JFileChooser fileChooser = new JFileChooser();
//            int returnValue = fileChooser.showOpenDialog(null);
//            if (returnValue == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//                try {
//                    byte[] fileBytes = readFileToByteArray(selectedFile);
//                    displayHexData(fileBytes);
////                    String hexRepresentation = getHexRepresentation(fileBytes);
////                    hexTextArea.setText(hexRepresentation);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//
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
//
//        hexTable.addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                Point point = e.getPoint();
//                int row = hexTable.rowAtPoint(point);
//                int column = hexTable.columnAtPoint(point);
//
//                if (row >= 0 && column >= 0) {
//                    Object value = hexTable.getValueAt(row, column);
//                    if (value != null) {
//                        statusBar.setText("Selected byte: " + value.toString());
//                    }
//                }
//            }
//        });
//
//        exitItem.addActionListener(actionEvent -> System.exit(0));
//
//        setVisible(true);
//        // Load file data initially
////        loadFileData("E:\\Work\\HEX-editor\\src\\main\\java\\text.txt");
//    }
//
//
//    public static byte[] readFileToByteArray(File file) throws IOException {
//        FileInputStream fis = new FileInputStream(file);
//        byte[] fileBytes = new byte[(int) file.length()];
//        fis.read(fileBytes);
//        fis.close();
//        return fileBytes;
//    }
//
//
////    -
//
////    private String getHexRepresentation(byte[] byteArray) {
////        StringBuilder hex = new StringBuilder();
////        int lineCounter = 0;
////
////        for (byte b : byteArray) {
////            if (lineCounter % 16 == 0) {
////                hex.append(String.format("%08X", lineCounter)).append(": ");
////            }
////
////            hex.append(String.format("%02X ", b));
////
////            lineCounter++;
////            if (lineCounter % 16 == 0) {
////                hex.append("\n");
////            }
////        }
////
////        return hex.toString();
////    }
////---------------------------------------------------------------------------------------
////    private String getHexRepresentation(byte[] byteArray) {
////        StringBuilder hex = new StringBuilder();
////        int lineCounter = 0;
////
////        for (byte b : byteArray) {
////            if (lineCounter % 16 == 0) {
////                hex.append(String.format("%08X", lineCounter)).append(": ");
////            }
////
////            hex.append(String.format("%02X ", b));
////
////            if (lineCounter % 8 == 7) { // Добавляем перевод строки после каждых 8 байт
////                hex.append("| ");
////            }
////
////            lineCounter++;
////
////            if (lineCounter % 16 == 0) {
////                hex.append("\n");
////            }
////        }
////
////        return hex.toString();
////    }
//
////    private void displayHexData(byte[] fileBytes) {
////        tableModel.setRowCount(0); // Очистка предыдущих данных
////
////        int lineCounter = 0;
////        StringBuilder address = new StringBuilder();
////        StringBuilder data = new StringBuilder();
////
////        for (byte b : fileBytes) {
////            if (lineCounter % 16 == 0) {
////                if (lineCounter != 0) {
////                    Object[] rowData = {address.toString(), data.toString()};
////                    tableModel.addRow(rowData);
////                    address.setLength(0);
////                    data.setLength(0);
////                }
////                address.append(String.format("%08X", lineCounter)).append(": ");
////            }
////
////            data.append(String.format("%02X", b)).append(" ");
////
////            lineCounter++;
////        }
////
////        while (address.length() < 54) {
////            address.append("   ");
////        }
////        Object[] rowData = {address.toString(), data.toString()};
////        tableModel.addRow(rowData);
////    }
////    //------------
////    private void displayHexData(byte[] fileBytes) {
////        tableModel.setRowCount(0); // Очистка предыдущих данных
////
////        int lineCounter = 0;
////
////        for (int i = 0; i < fileBytes.length; i += 16) {
////            String address = String.format("%08X", lineCounter) + ": ";
////            StringBuilder data = new StringBuilder();
////
////            for (int j = 0; j < 16 && i + j < fileBytes.length; j++) {
////                data.append(String.format("%02X", fileBytes[i + j])).append(" ");
////            }
////
////            Object[] rowData = {address, data.toString()};
////            tableModel.addRow(rowData);
////
////            lineCounter += 16;
////        }
////
////        setTableCellRenderer();
////    }
////
////
////    private void setTableCellRenderer() {
////        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
////            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
////                                                           boolean hasFocus, int row, int column) {
////                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
////                setHorizontalAlignment(SwingConstants.CENTER);
////                return c;
////            }
////        };
////
////        for (int i = 0; i < hexTable.getColumnCount(); i++) {
////            hexTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
////        }
////    }
////------------------------------------
//
//    private void setTableCellRenderer() {
//        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                           boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                setHorizontalAlignment(SwingConstants.CENTER);
//                return c;
//            }
//        };
//
//        hexTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
//    }
//
//    private JLabel[] createHexTable(byte[] fileBytes, int startIndex) {
//        JLabel[] labels = new JLabel[16];
//
//        for (int i = startIndex; i < startIndex + 16 && i < fileBytes.length; i++) {
//            JLabel label = new JLabel(String.format("%02X", fileBytes[i]));
//            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//            label.setHorizontalAlignment(SwingConstants.CENTER);
//            labels[i - startIndex] = label;
//        }
//
//        return labels;
//    }
//
//    private void displayHexData(byte[] fileBytes) {
//        tableModel.setRowCount(0);
//
//        int maxAddressLength = calculateMaxAddressLength(fileBytes);
//
//        for (int i = 0; i < fileBytes.length; i += 16) {
//            byte[] rowData = Arrays.copyOfRange(fileBytes, i, Math.min(i + 16, fileBytes.length));
//            tableModel.addRow(new Object[]{String.format("%08X", i), rowData});
//        }
//
//        hexTable.getColumnModel().getColumn(0).setPreferredWidth(maxAddressLength * 8); // Установка ширины столбца
////        hexTable.getColumnModel().getColumn(1).setCellRenderer(new CustomCellTable());
//    }
//
//    private int calculateMaxAddressLength(byte[] fileBytes) {
//        int maxLength = 0;
//        for (int i = 0; i < fileBytes.length; i += 16) {
//            int addressLength = String.format("%08X", i).length();
//            if (addressLength > maxLength) {
//                maxLength = addressLength;
//            }
//        }
//        return maxLength;
//    }
//
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new HexEditorApp();
//            }
//        });
//    }
//}
