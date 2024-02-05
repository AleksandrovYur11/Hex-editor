package gui.menuBar;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class FileMenuBar {
    private JMenuBar fileMenuBar;
    private JMenu fileMenu;
    private JMenuItem openFileItem;
    private JMenuItem saveFileItem;
    private JMenuItem saveAsFileItem;
    private JMenuItem exitAppItem;

    public FileMenuBar() {
        fileMenuBar = new JMenuBar();
        fileMenu = new JMenu("Файл");
        openFileItem = new JMenuItem("Открыть");
        saveFileItem = new JMenuItem("Сохранить");
        saveAsFileItem = new JMenuItem("Сохранить как");
        exitAppItem = new JMenuItem("Выход");

        saveFileItem.setEnabled(false);
        saveAsFileItem.setEnabled(false);

        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(saveAsFileItem);
        fileMenu.addSeparator();
        fileMenu.add(exitAppItem);
        fileMenuBar.add(fileMenu);
    }



}
