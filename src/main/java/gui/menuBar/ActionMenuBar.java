package gui.menuBar;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class ActionMenuBar {
    private JMenuBar actionMenuBar;
    private JMenu actionMenu;
    private JMenu pasteMenu;
    private JMenuItem pasteReplaceItem;
    private JMenuItem pasteWithoutReplaceItem;
    private JMenuItem copyItem;
    private JMenuItem deleteItem;

    public ActionMenuBar() {
        actionMenuBar = new JMenuBar();
        actionMenu = new JMenu("Данные");
        pasteMenu = new JMenu("Вставить");

        pasteReplaceItem = new JMenuItem("С Заменой");
        pasteWithoutReplaceItem = new JMenuItem("Без Замены");
        copyItem = new JMenuItem("Копировать");
        deleteItem = new JMenuItem("Удалить");

        actionMenu.setEnabled(false);

        actionMenu.add(copyItem);
        pasteMenu.add(pasteReplaceItem);
        pasteMenu.add(pasteWithoutReplaceItem);
        actionMenu.add(pasteMenu);
        actionMenu.add(deleteItem);

        actionMenuBar.add(actionMenu);
    }

    // Создание меню "Инструменты"
//        JMenuBar actionMenuBar = new JMenuBar();
//        JMenu actionMenu = new JMenu("Данные");
//        JMenu pasteMenu = new JMenu("Вставить");
//
//        JMenuItem pasteReplaceItem = new JMenuItem("С Заменой");
//        JMenuItem pasteWithoutReplaceItem = new JMenuItem("Без Замены");
//        JMenuItem copyItem = new JMenuItem("Копировать");
//        JMenuItem deleteItem = new JMenuItem("Удалить");
//
//        actionMenu.setEnabled(false);
//
//        actionMenu.add(copyItem);
//        pasteMenu.add(pasteReplaceItem);
//        pasteMenu.add(pasteWithoutReplaceItem);
//        actionMenu.add(pasteMenu);
//        actionMenu.add(deleteItem);
//
//        actionMenuBar.add(actionMenu);
}
