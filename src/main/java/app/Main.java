package app;

import gui.HexTable;
import gui.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends  JFrame{
    private JFrame frame;
    private JPanel mainPanel;

    private HexTable hexTable;

    private Tools toolBar;

    public Main() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        createHexTable();
        createToolsManager();
        createMainFrame();

        frame.addWindowListener(new WindowAdapter() {
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
        this.mainPanel.add(tablePanel);
    }


    private void createMainFrame() {
        JFrame frame = new JFrame("Hex editor tool");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        frame.setSize(width, height);
        this.frame = frame;

        frame.setLocationRelativeTo(null);
        frame.setContentPane(this.mainPanel);

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
