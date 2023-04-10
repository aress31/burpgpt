package burpgpt.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.MessageFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import burp.MyBurpExtension;
import burpgpt.utilities.HtmlResourceLoader;

public class AboutView extends JPanel {

    private static final int COPYRIGHT_FONT_SIZE = 12;

    public AboutView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16)); // add 16-pixel padding to the root panel
        add(initComponents(), BorderLayout.WEST);
    }

    private JPanel initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));

        contentPanel.add(createTitleLabel());
        contentPanel.add(createCopyRightLabel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        contentPanel.add(createDescriptionLabel());

        return contentPanel;
    }

    private JLabel createTitleLabel() {
        String title = String.format("<html><h1>%s v%s</h1></html>", MyBurpExtension.EXTENSION,
                MyBurpExtension.VERSION);
        JLabel titleLabel = new JLabel(title);
        titleLabel.putClientProperty("html.disable", null);
        return titleLabel;
    }

    private JLabel createDescriptionLabel() {
        String description = HtmlResourceLoader.loadHtmlContent("aboutDescription.html");
        String formattedDescription = MessageFormat.format(description, MyBurpExtension.EXTENSION);
        // HACK: Need to get the width programatically
        JLabel descriptionLabel = new JLabel("<html><div id='descriptionDiv' style='width: 800px;'>"
                + formattedDescription + "</div></html>");
        descriptionLabel.putClientProperty("html.disable", null);
        return descriptionLabel;
    }

    private JLabel createCopyRightLabel() {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String copyRight = String.format(
                "<html>Copyright &copy; %s - %s Alexandre Teyar, Aegis Cyber &lt;<a href=\"https://aegiscyber.co.uk\">www.aegiscyber.co.uk</a>&gt;. All Rights Reserved.</html>",
                year, year);
        JLabel copyRightLabel = new JLabel(copyRight);
        copyRightLabel.setFont(new Font(copyRightLabel.getFont().getName(), Font.PLAIN, COPYRIGHT_FONT_SIZE));
        copyRightLabel.setForeground(Color.GRAY);
        copyRightLabel.putClientProperty("html.disable", null);
        return copyRightLabel;
    }
}
