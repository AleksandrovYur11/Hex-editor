package app;

import gui.HexTable;
import gui.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends  JFrame{

    private JPanel mainPanel;

    private HexTable hexTable;

    private Tools toolBar;

    public Main() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        createHexTable();
        createToolsManager();
        createMainFrame();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                toolBar.handleExit();
            }
        });
    }

    private void createHexTable() {
        HexTable newHexTable = new HexTable();
        this.hexTable = newHexTable;

        Dimension originalSize = this.mainPanel.getSize();
        JPanel tablePanel = newHexTable.getTablePanel();

        tablePanel.setSize(originalSize);

        //        JScrollPane scrollPane = new JScrollPane(newHexTable.getTablePanel());
        this.mainPanel.add(tablePanel, BorderLayout.CENTER);
    }


    private void createMainFrame() {
        setTitle("Hex editor tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(this.mainPanel);
        setVisible(true);
    }


    private void createToolsManager() {
        Tools toolsManager = new Tools(hexTable);
        toolBar = toolsManager;
        mainPanel.add(toolsManager.getToolBarPanel(), BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
