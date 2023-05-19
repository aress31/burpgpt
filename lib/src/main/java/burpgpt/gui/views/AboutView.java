package burpgpt.gui.views;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import burp.MyBurpExtension;
import burpgpt.utilities.HtmlResourceLoader;

public class AboutView extends JPanel {

    private static final int COPYRIGHT_FONT_SIZE = 12;
    private static final String WEBSITE = "https://burpgpt.app/#pricing";

    public AboutView() {
        setLayout(new GroupLayout(this));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        initComponents();
        enableTooltips();
    }

    public void initComponents() {
        JLabel titleLabel = createTitleLabel();
        JLabel copyRightLabel = createCopyRightLabel();
        JLabel descriptionLabel = createDescriptionLabel();
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionLabel);
        descriptionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        descriptionScrollPane.setBorder(BorderFactory.createEmptyBorder());
        JButton upgradeButton = createUpgradeButton();

        GroupLayout layout = (GroupLayout) getLayout();

        GroupLayout.Group horizontalGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(titleLabel)
                .addComponent(copyRightLabel)
                .addComponent(descriptionScrollPane)
                .addComponent(upgradeButton);
        layout.setHorizontalGroup(horizontalGroup);

        GroupLayout.Group verticalGroup = layout.createSequentialGroup()
                .addComponent(titleLabel)
                .addComponent(copyRightLabel)
                .addGap(16)
                .addComponent(descriptionScrollPane)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(upgradeButton);
        layout.setVerticalGroup(verticalGroup);
    }

    private JLabel createTitleLabel() {
        String title = String.format("<html><h1>%s v%s</h1></html>", MyBurpExtension.EXTENSION,
                MyBurpExtension.VERSION);
        JLabel titleLabel = new JLabel(title);
        titleLabel.putClientProperty("html.disable", null);
        return titleLabel;
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

    private JLabel createDescriptionLabel() {
        String description = HtmlResourceLoader.loadHtmlContent("aboutDescription.html");
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.putClientProperty("html.disable", null);
        return descriptionLabel;
    }

    private JButton createUpgradeButton() {
        JButton upgradeButton = new JButton("Upgrade to the Pro edition");
        upgradeButton.setToolTipText("Upgrade to the Pro edition by visiting our official website");
        upgradeButton.setBackground(UIManager.getColor("Burp.burpOrange"));
        upgradeButton.setForeground(Color.WHITE);
        upgradeButton.setFont(upgradeButton.getFont().deriveFont(Font.BOLD));
        upgradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(WEBSITE));
                } catch (IOException | URISyntaxException e1) {
                    // pass
                }
            }
        });
        return upgradeButton;
    }

    private void enableTooltips() {
        ToolTipManager.sharedInstance().setInitialDelay(0);
    }
}
