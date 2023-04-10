package burpgpt.gui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PlaceholdersView extends JPanel {

    public PlaceholdersView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16)); // add 16-pixel padding to the root panel
        add(initComponents(), BorderLayout.CENTER);
    }

    private JPanel initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));

        contentPanel.add(createIntroLabel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        contentPanel.add(createTableScrollPane());

        return contentPanel;
    }

    private JLabel createIntroLabel() {
        String intro = "<html>"
                + "<div id='descriptionDiv' style='width: 800px'>"
                + "<p>The prompt customization feature provided by the extension allows users to tailor traffic analysis prompts using a placeholder system. "
                + "For optimal results, it is recommended to use the placeholders provided by the extension.</p>"
                + "<br />"
                + "<p>The following table shows the available placeholders and their corresponding values:</p>"
                + "</div>"
                + "</html>";
        JLabel introLabel = new JLabel();
        introLabel.setText(intro);
        introLabel.putClientProperty("html.disable", null);
        return introLabel;
    }

    private Object[][] getTableData() {
        return new Object[][] {
                { "{REQUEST}", "The scanned request." },
                { "{URL}", "The URL of the scanned request." },
                { "{METHOD}", "The HTTP request method used in the scanned request." },
                { "{REQUEST_HEADERS}", "The headers of the scanned request." },
                { "{REQUEST_BODY}", "The body of the scanned request." },
                { "{RESPONSE}", "The scanned response." },
                { "{RESPONSE_HEADERS}", "The headers of the scanned response." },
                { "{RESPONSE_BODY}", "The body of the scanned response." },
                { "{IS_TRUNCATED_PROMPT}",
                        "A boolean value that is programmatically set to true or false to indicate whether the prompt was truncated to the Maximum Prompt Size defined in the Settings." }
        };
    }

    private JScrollPane createTableScrollPane() {
        String[] columnNames = { "Placeholder", "Value" };
        Object[][] data = getTableData();
        NonEditableTableModel tableModel = new NonEditableTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }

    private class NonEditableTableModel extends DefaultTableModel {
        public NonEditableTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
