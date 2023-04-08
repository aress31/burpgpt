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

    public static final String EXTENSION = "BurpGPT";
    public static final Boolean DEBUG = false;

    private PropertyChangeSupport propertyChangeSupport;
    @Getter
    Logging logging;

    @Getter
    private String apiKey = "PLEASE_CHANGE_ME_OR_YOU_WILL_MAKE_THE_DEVELOPER_SAD";
    @Getter
    List<String> modelIds = Arrays.asList("davinci", "curie", "babbage");
    @Getter
    private String modelId = modelIds.get(0);
    @Getter
    private String prompt = "Analyze the following HTTP request and response for potential vulnerabilities, "
            + "including but not limited to OWASP top 10 vulnerabilities,such as SQL injection, XSS, CSRF, and more. "
            + "Other common web application security threats should also be considered.\n"
            + "Is Truncated Prompt: {IS_TRUNCATED_PROMPT}\n"
            + "The request was made using the following information:\n"
            + "URL: {URL}\n"
            + "Request Method: {METHOD}\n"
            + "Request Headers: {REQUEST_HEADERS}\n"
            + "Request Body: {REQUEST_BODY}\n"
            + "The response contains the following information:\n"
            + "Response Headers: {RESPONSE_HEADERS}\n"
            + "Response Body: {RESPONSE_BODY}\n"
            + "Please describe any identified vulnerabilities and explain how they could be exploited.\n"
            + "Each vulnerability should be formatted as follows:\n"
            + "- Vulnerability Name: Brief description of vulnerability\n"
            + "For example:\n"
            + "- SQL Injection: The application is vulnerable to SQL injection attacks due to unsanitized user input in the search form.";;

    private GPTClient gptClient;

    @Override
    public void initialize(MontoyaApi montoyaApi) {
        this.logging = montoyaApi.logging();
        this.propertyChangeSupport = new PropertyChangeSupport(this);

        montoyaApi.extension().setName(EXTENSION);
        logging.logToOutput("[+] Extension loaded");

        gptClient = new GPTClient(apiKey, modelId, prompt, logging);
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

    public void updateSettings(String newApiKey, String newModelId, String newPrompt) {
        String[] newValues = { newApiKey, newModelId, newPrompt };
        String[] oldValues = { this.apiKey, this.modelId, this.prompt };

        this.apiKey = newApiKey;
        this.modelId = newModelId;
        this.prompt = newPrompt;

        this.gptClient.updateSettings(newApiKey, newModelId, newPrompt);

        propertyChangeSupport.firePropertyChange("settingsChanged", oldValues, newValues);

        if (MyBurpExtension.DEBUG) {
            logging.logToOutput("[*] Updated extension settings:");
            logging.logToOutput(String.format("- apiKey: %s\n" +
                    "- modelId: %s\n" +
                    "- prompt: %s",
                    newApiKey, newModelId, newPrompt));
        }
    }
}
