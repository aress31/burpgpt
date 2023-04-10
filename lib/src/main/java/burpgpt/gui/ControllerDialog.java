package burpgpt.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import burp.MyBurpExtension;
import burpgpt.gui.views.AboutView;
import burpgpt.gui.views.PlaceholdersView;
import burpgpt.gui.views.SettingsView;

public class ControllerDialog extends JDialog {

    private final MyBurpExtension myBurpExtension;

    private JList<String> listView;

    private final SettingsView settingsView;
    private final PlaceholdersView placeholdersView;
    private final AboutView aboutView;

    private Map<String, JPanel> viewMap = new HashMap<>();

    public ControllerDialog(MyBurpExtension myBurpExtension) {
        this.myBurpExtension = myBurpExtension;

        settingsView = new SettingsView(myBurpExtension);
        placeholdersView = new PlaceholdersView();
        aboutView = new AboutView();

        setupDialog();
        initComponents();
        registerApplyButtonListener();

        myBurpExtension.getMontoyaApi().userInterface().applyThemeToComponent(this);

        pack();
        setLocationRelativeTo(myBurpExtension.getMontoyaApi().userInterface().swingUtils().suiteFrame());
    }

    private void setupDialog() {
        setTitle(String.format("%s Settings", MyBurpExtension.EXTENSION));
        setLayout(new BorderLayout());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        // setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }

    private void initComponents() {
        listView = createListView();

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setPreferredSize(new Dimension(150, 0));
        listPanel.add(new JScrollPane(listView), BorderLayout.CENTER);

        add(listPanel, BorderLayout.WEST);

        viewMap.put("OpenAI API", settingsView);
        viewMap.put("Placeholder reference", placeholdersView);
        viewMap.put("About", aboutView);

        JPanel cardsPanel = new JPanel(new CardLayout());
        for (JPanel view : viewMap.values()) {
            view.setPreferredSize(settingsView.getPreferredSize());
            cardsPanel.add(view, view.getClass().getName());
        }
        add(cardsPanel, BorderLayout.CENTER);

        setDefaultView("OpenAI API");
    }

    private void registerApplyButtonListener() {
        settingsView.setOnApplyButtonClickListener(() -> {
            setVisible(false);
        });
    }

    private void setDefaultView(String viewName) {
        listView.setSelectedValue(viewName, true);
    }

    private JList<String> createListView() {
        String[] listData = { "OpenAI API", "Placeholder reference", "About" };
        JList<String> listView = new JList<>(listData);
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listView.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = listView.getSelectedValue();
                updateView(selectedValue);
            }
        });

        return listView;
    }

    private void updateView(String selectedValue) {
        JPanel selectedPanel = viewMap.get(selectedValue);
        if (selectedPanel != null) {
            CardLayout cardLayout = (CardLayout) (((JPanel) getContentPane().getComponent(1)).getLayout());
            cardLayout.show((JPanel) getContentPane().getComponent(1), selectedPanel.getClass().getName());
        } else {
            // Handle the case when the selected value is not found in the viewMap
            System.err.println("View not found: " + selectedValue);
        }
    }
}
