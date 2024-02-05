package gui.menuBar;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class StructureMenuBar {
    private JMenuBar structureMenuBar;
    private JMenu structureMenu;
    private JMenuItem addColumnItem;
    private JMenuItem deleteColumnItem;
    private JMenuItem addRowItem;
    private JMenuItem deleteRowItem;

    public StructureMenuBar() {

        structureMenuBar = new JMenuBar();
        structureMenu = new JMenu("Структура");
        addColumnItem = new JMenuItem("Добавить столбцы");
        deleteColumnItem = new JMenuItem("Удалить столбцы");
        addRowItem = new JMenuItem("Добавить строки");
        deleteRowItem = new JMenuItem("Удалить строки");

        structureMenu.setEnabled(false);

        structureMenu.add(addColumnItem);
        structureMenu.add(deleteColumnItem);
        structureMenu.add(addRowItem);
        structureMenu.add(deleteRowItem);

        structureMenuBar.add(structureMenu);
    }

    //        JMenuBar structureMenuBar = new JMenuBar();
//        JMenu structureMenu = new JMenu("Структура");
//        JMenuItem addColumnItem = new JMenuItem("Добавить столбцы");
//        JMenuItem deleteColumnItem = new JMenuItem("Удалить столбцы");
//        JMenuItem addRowItem = new JMenuItem("Добавить строки");
//        JMenuItem deleteRowItem = new JMenuItem("Удалить строки");
//
//        structureMenu.setEnabled(false);
//
//        structureMenu.add(addColumnItem);
//        structureMenu.add(deleteColumnItem);
//        structureMenu.add(addRowItem);
//        structureMenu.add(deleteRowItem);
//
//        structureMenuBar.add(structureMenu);
}