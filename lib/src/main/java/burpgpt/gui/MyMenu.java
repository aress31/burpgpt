package burpgpt.gui;

import burp.MyBurpExtension;
import burp.api.montoya.ui.menu.BasicMenuItem;
import burp.api.montoya.ui.menu.Menu;

public class MyMenu {

    public static Menu createMenu(MyBurpExtension myBurpExtension) {
        BasicMenuItem basicMenuItem = BasicMenuItem.basicMenuItem("Settings").withAction(() -> {
            SettingsPanel settingsPanel = new SettingsPanel(myBurpExtension);
            settingsPanel.setVisible(true);
        });
        Menu menu = Menu.menu(MyBurpExtension.EXTENSION).withMenuItems(basicMenuItem);
        return menu;
    }
}
