package burp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.menu.Menu;
import burpgpt.gui.MyMenu;
import burpgpt.http.GPTClient;
import lombok.Getter;

public class MyBurpExtension implements BurpExtension, PropertyChangeListener {

    public static final Boolean DEBUG = false;

    public static final String EXTENSION = "BurpGPT";
    public static final String VERSION = "0.1.1";

    private PropertyChangeSupport propertyChangeSupport;
    @Getter
    Logging logging;
    @Getter
    MontoyaApi montoyaApi;

    @Getter
    private String apiKey = "PLEASE_CHANGE_ME_OR_YOU_WILL_MAKE_THE_DEVELOPER_SAD";
    @Getter
    List<String> modelIds = Arrays.asList("davinci", "ada", "babbage", "curie");
    @Getter
    private int maxPromptSize = 1024;
    @Getter
    private String model = modelIds.get(0);
    @Getter
    String prompt = "Please analyze the following HTTP request and response for potential security vulnerabilities, "
            + "specifically focusing on OWASP top 10 vulnerabilities such as SQL injection, XSS, CSRF, and other common web application security threats.\n\n"
            + "Format your response as a bullet list with each point listing a vulnerability name and a brief description, in the format:\n"
            + "- Vulnerability Name: Brief description of vulnerability\n\n"
            + "Exclude irrelevant information.\n\n"
            + "=== Request ===\n"
            + "{REQUEST}\n\n"
            + "=== Response ===\n"
            + "{RESPONSE}\n";

    private GPTClient gptClient;

    @Override
    public void initialize(MontoyaApi montoyaApi) {
        this.montoyaApi = montoyaApi;
        this.logging = montoyaApi.logging();
        this.propertyChangeSupport = new PropertyChangeSupport(this);

        montoyaApi.extension().setName(EXTENSION);
        logging.logToOutput("[+] Extension loaded");

        gptClient = new GPTClient(apiKey, model, prompt, logging);
        MyScanCheck scanCheck = new MyScanCheck(gptClient, logging);

        Menu menu = MyMenu.createMenu(this);
        montoyaApi.userInterface().menuBar().registerMenu(menu);
        logging.logToOutput("[+] Menu added to the menu bar");

        montoyaApi.scanner().registerScanCheck(scanCheck);
        logging.logToOutput("[+] Passive scan check registered");
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    public void updateSettings(String newApiKey, String newModelId, int newMaxPromptSize, String newPrompt) {
        String[] newValues = {
                newApiKey, newModelId, Integer.toString(newMaxPromptSize), newPrompt };
        String[] oldValues = {
                this.apiKey, this.model, Integer.toString(this.maxPromptSize), this.prompt };

        this.apiKey = newApiKey;
        this.model = newModelId;
        this.maxPromptSize = newMaxPromptSize;
        this.prompt = newPrompt;

        this.gptClient.updateSettings(newApiKey, newModelId, newMaxPromptSize, newPrompt);

        propertyChangeSupport.firePropertyChange("settingsChanged", oldValues, newValues);

        if (MyBurpExtension.DEBUG) {
            logging.logToOutput("[*] Updated extension settings:");
            logging.logToOutput(String.format("- apiKey: %s\n" +
                    "- model: %s\n" +
                    "- maxPromptSize: %s\n" +
                    "- prompt: %s",
                    newApiKey, newModelId, newMaxPromptSize, newPrompt));
        }
    }
}
