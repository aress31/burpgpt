package burpgpt.gui.views;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import burp.MyBurpExtension;

public class SettingsView extends JPanel implements PropertyChangeListener {

    private MyBurpExtension myBurpExtension;

    private JTextField apiKeyField;
    private JComboBox<String> modelIdComboBox;
    private JSpinner maxPromptSizeField;
    private JTextArea promptField;

    private String model;

    public interface OnApplyButtonClickListener {
        void onApplyButtonClick();
    }

    private OnApplyButtonClickListener onApplyButtonClickListener;

    public void setOnApplyButtonClickListener(OnApplyButtonClickListener onApplyButtonClickListener) {
        this.onApplyButtonClickListener = onApplyButtonClickListener;
    }

    public SettingsView(MyBurpExtension myBurpExtension) {
        this.myBurpExtension = myBurpExtension;

        setLayout(new GridBagLayout());
        initComponents();

        myBurpExtension.addPropertyChangeListener(this);
    }

    private void initComponents() {
        createApiKeyField(0);
        createModelIdComboBox(1);
        createMaxPromptSizeField(2);
        createPromptField(3);
        createPromptDescriptionLabel(4);
        createApplyButton(5);
    }

    private void createApiKeyField(int y) {
        JLabel apiKeyLabel = new JLabel("API key:");
        apiKeyField = new JTextField(myBurpExtension.getApiKey(), 20);
        add(apiKeyLabel, createGridBagConstraints(0, y));
        add(apiKeyField, createGridBagConstraints(1, y));
    }

    private void createModelIdComboBox(int y) {
        JLabel modelIdLabel = new JLabel("Model:");
        modelIdComboBox = new JComboBox<>(myBurpExtension.getModelIds().toArray(new String[0]));
        modelIdComboBox.setSelectedItem(myBurpExtension.getModel());
        modelIdComboBox.addActionListener(e -> model = (String) modelIdComboBox.getSelectedItem());
        add(modelIdLabel, createGridBagConstraints(0, y));
        add(modelIdComboBox, createGridBagConstraints(1, y));
    }

    private void createMaxPromptSizeField(int y) {
        JLabel maxPromptSizeLabel = new JLabel("Maximum prompt size:");
        maxPromptSizeField = new JSpinner(
                new SpinnerNumberModel(myBurpExtension.getMaxPromptSize(), 1, Integer.MAX_VALUE, 1));
        add(maxPromptSizeLabel, createGridBagConstraints(0, y));
        add(maxPromptSizeField, createGridBagConstraints(1, y));
    }

    private void createPromptField(int y) {
        JLabel promptLabel = new JLabel("Prompt:");
        promptField = new JTextArea(myBurpExtension.getPrompt(), 14, 20);
        promptField.setWrapStyleWord(true);
        promptField.setLineWrap(true);
        JScrollPane promptScrollPane = new JScrollPane(promptField);
        add(promptLabel, createGridBagConstraints(0, y));
        add(promptScrollPane, createGridBagConstraints(1, y));
    }

    private void createPromptDescriptionLabel(int y) {
        JLabel promptDescriptionLabel = new JLabel(
                "<html>Refer to the repository (<a href=\"https://github.com/aress31/burpgpt\">https://github.com/aress31/burpgpt</a>) to learn how to optimally set the prompt for the GPT model.</html>");
        promptDescriptionLabel.putClientProperty("html.disable", null);
        add(promptDescriptionLabel, createGridBagConstraints(1, y));
    }

    private void createApplyButton(int y) {
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applySettings());
        applyButton.setBackground(UIManager.getColor("Burp.burpOrange"));
        applyButton.setFont(new Font(applyButton.getFont().getName(), Font.BOLD, applyButton.getFont().getSize()));
        add(applyButton, createGridBagConstraints(1, y));
    }

    private void applySettings() {
        String newApiKey = apiKeyField.getText().trim();
        String newModelId = (String) modelIdComboBox.getSelectedItem();
        int newMaxPromptSize = (int) maxPromptSizeField.getValue();
        String newPromptText = promptField.getText().trim();

        if (newApiKey.isEmpty() || newModelId.isEmpty() || newPromptText.isEmpty() || newMaxPromptSize <= 0) {
            JOptionPane.showMessageDialog(SettingsView.this,
                    "All fields are required and max prompt size must be greater than 0", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        myBurpExtension.updateSettings(newApiKey, newModelId, newMaxPromptSize, newPromptText);

        if (onApplyButtonClickListener != null) {
            onApplyButtonClickListener.onApplyButtonClick();
        }
    }

    private GridBagConstraints createGridBagConstraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = x == 0 ? 0 : 1;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(16, x == 0 ? 16 : 4, 16, x == 0 ? 4 : 16);
        constraints.anchor = y != 5 ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
        constraints.fill = (x == 0 || y == 5) ? GridBagConstraints.NONE : GridBagConstraints.HORIZONTAL;
        return constraints;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("settingsChanged".equals(evt.getPropertyName())) {
            String[] newValues = (String[]) evt.getNewValue();
            apiKeyField.setText(newValues[0]);
            modelIdComboBox.setSelectedItem(newValues[1]);
            maxPromptSizeField.setValue(Integer.parseInt(newValues[2]));
            promptField.setText(newValues[3]);
        }
    }
}
