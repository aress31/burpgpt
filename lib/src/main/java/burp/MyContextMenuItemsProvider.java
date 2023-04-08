// package burp;

// import java.awt.Component;
// import java.awt.event.ActionListener;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;

// import javax.swing.JMenuItem;

// import burp.api.montoya.MontoyaApi;
// import burp.api.montoya.http.message.HttpRequestResponse;
// import burp.api.montoya.logging.Logging;
// import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
// import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
// import burpgpt.gui.SettingsPanel;
// import burpgpt.http.GPTClient;

// public class MyContextMenuItemsProvider implements ContextMenuItemsProvider {

//   private final MontoyaApi montoyaApi;
//   private final MyScanCheck scanCheck;
//   private final GPTClient gptClient;
//   private final Logging logging;

//   private String apiKey = "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";
//   // private String apiKey = "your-api-key-here";
//   private String selectedModel = "davinci";

//   public MyContextMenuItemsProvider(MontoyaApi montoyaApi, GPTClient gptClient, MyScanCheck scanCheck) {
//     this.montoyaApi = montoyaApi;
//     this.logging = montoyaApi.logging();
//     this.scanCheck = scanCheck;
//     this.gptClient = gptClient;

//     updateGptClient();
//     scanCheck.setGptClient(gptClient);
//   }

//   @Override
//   public List<Component> provideMenuItems(ContextMenuEvent contextMenuEvent) {
//     Map<String, ActionListener> menuItemMap = new LinkedHashMap<>();
//     menuItemMap.put("Check for Vulnerabilities", e -> {
//       for (HttpRequestResponse selectedMessage : contextMenuEvent.selectedRequestResponses()) {
//         try {
//           scanCheck.passiveAudit(selectedMessage);
//         } catch (Exception ex) {
//           logging.logToError("Error while identifying vulnerabilities: " + ex.getMessage());
//         }
//       }
//     });
//     menuItemMap.put("Settings", e -> showSettingsPanel());

//     // Create the menu items from the map
//     List<JMenuItem> menuItems = new ArrayList<>();
//     for (Map.Entry<String, ActionListener> entry : menuItemMap.entrySet()) {
//       JMenuItem menuItem = new JMenuItem(entry.getKey());
//       menuItem.addActionListener(entry.getValue());
//       menuItems.add(menuItem);
//     }

//     return new ArrayList<>(menuItems);
//   }

//   private void showSettingsPanel() {
//     // Get a list of available models from OpenAI
//     Map<String, Integer> modelMap = new HashMap<>();

//     try {
//       modelMap = gptClient.getModels(logging);
//       modelMap.forEach((key, value) -> logging.logToOutput(key + " = " + value));
//     } catch (IOException e1) {
//       logging.logToError(e1.getMessage());
//     } catch (Exception e2) {
//       logging.logToError(e2.getMessage());
//       modelMap.put("text-davinci-002", 4096);
//       modelMap.put("text-curie-001", 4096);
//       modelMap.put("text-babbage-001", 4096);
//     }

//     // TODO: Improve this bit
//     SettingsPanel.ApplyButtonListener applyButtonListener = (apiKeyValue, selectedModelValue) -> {
//       apiKey = apiKeyValue;
//       selectedModel = selectedModelValue;
//       updateGptClient();
//     };

//     SettingsPanel settingsPanel = new SettingsPanel(apiKey, selectedModel, modelMap, applyButtonListener);

//     settingsPanel.setVisible(true);
//   }

//   private void updateGptClient() {
//     gptClient.setApiKey(apiKey);
//     gptClient.setSelectedModel(selectedModel);
//   }
// }