package burpgpt.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import burp.MyBurpExtension;

public class SettingsPanel extends JDialog implements PropertyChangeListener {

    private JTextField apiKeyField;
    private JComboBox<String> modelIdComboBox;
    private JTextArea promptField;

    private String modelId;

    public SettingsPanel(MyBurpExtension myBurpExtension) {
        myBurpExtension.addPropertyChangeListener(this);

        setTitle(String.format("%s Settings", MyBurpExtension.EXTENSION));
        setLayout(new GridBagLayout());
        setResizable(false);
        setMinimumSize(new Dimension(800, 400));

        addApiKeyField(myBurpExtension);
        addModelIdComboBox(myBurpExtension);
        addPromptField(myBurpExtension);
        addPromptDescriptionLabel();
        addApplyButton(myBurpExtension);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void addApiKeyField(MyBurpExtension myBurpExtension) {
        JLabel apiKeyLabel = new JLabel("API Key:");
        apiKeyField = new JTextField(myBurpExtension.getApiKey(), 20);
        add(apiKeyLabel, createConstraints(0, 0));
        add(apiKeyField, createConstraints(1, 0));
    }

    private void addModelIdComboBox(MyBurpExtension myBurpExtension) {
        JLabel modelIdLabel = new JLabel("Model:");
        modelIdComboBox = new JComboBox<>(myBurpExtension.getModelIds().toArray(new String[0]));
        modelIdComboBox.setSelectedItem(myBurpExtension.getModelId());
        modelIdComboBox.addActionListener(e -> modelId = (String) modelIdComboBox.getSelectedItem());
        add(modelIdLabel, createConstraints(0, 1));
        add(modelIdComboBox, createConstraints(1, 1));
    }

    private void addPromptField(MyBurpExtension myBurpExtension) {
        JLabel promptLabel = new JLabel("Prompt:");
        promptField = new JTextArea(myBurpExtension.getPrompt(), 14, 20);
        promptField.setWrapStyleWord(true);
        promptField.setLineWrap(true);
        JScrollPane promptScrollPane = new JScrollPane(promptField);
        add(promptLabel, createConstraints(0, 2));
        add(promptScrollPane, createConstraints(1, 2));
    }

    private void addPromptDescriptionLabel() {
        JLabel promptDescriptionLabel = new JLabel(
                "<html>Refer to the repository (<a href=\"https://github.com/aress31/burpgpt\">https://github.com/aress31/burpgpt</a>) to learn how to optimally set the prompt for the GPT model.</html>");
        promptDescriptionLabel.putClientProperty("html.disable", null);
        add(promptDescriptionLabel, createConstraints(1, 3));
    }

    private void addApplyButton(MyBurpExtension myBurpExtension) {
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applySettings(myBurpExtension));
        applyButton.setBackground(UIManager.getColor("Burp.burpOrange"));
        applyButton.setFont(new Font(applyButton.getFont().getName(), Font.BOLD, applyButton.getFont().getSize()));
        add(applyButton, createConstraints(1, 4));
    }

    private void applySettings(MyBurpExtension myBurpExtension) {
        String newApiKey = apiKeyField.getText().trim();
        String newModelId = (String) modelIdComboBox.getSelectedItem();
        String newPromptText = promptField.getText().trim();

        if (newApiKey.isEmpty() || newModelId.isEmpty() || newPromptText.isEmpty()) {
            JOptionPane.showMessageDialog(SettingsPanel.this, "All fields are required", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        myBurpExtension.updateSettings(newApiKey, newModelId, newPromptText);
        setVisible(false);
    }

    private GridBagConstraints createConstraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = x == 0 ? 0 : 1;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(8, x == 0 ? 16 : 4, 8, x == 0 ? 4 : 16);
        constraints.anchor = y != 4 ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
        constraints.fill = (x == 0 || y == 4) ? GridBagConstraints.NONE : GridBagConstraints.HORIZONTAL;
        return constraints;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("settingsChanged".equals(evt.getPropertyName())) {
            String[] newValues = (String[]) evt.getNewValue();
            apiKeyField.setText(newValues[0]);
            modelIdComboBox.setSelectedItem(newValues[1]);
            promptField.setText(newValues[2]);
        }
    }
}
