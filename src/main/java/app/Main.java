package app;

import gui.HexTable;
import gui.Tools;

import javax.swing.*;
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
//        this.hexTable = newHexTable;
//        this.mainPanel.add(newHexTable.getTablePanel(), BorderLayout.SOUTH);
        HexTable newHexTable = new HexTable();
        this.hexTable = newHexTable;
//        JScrollPane scrollPane = new JScrollPane(newHexTable.getTablePanel());
        this.mainPanel.add(newHexTable.getTablePanel());
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
