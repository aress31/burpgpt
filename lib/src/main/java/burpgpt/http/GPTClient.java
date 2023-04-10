package burpgpt.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import burp.MyBurpExtension;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.logging.Logging;
import burpgpt.gpt.GPTRequest;
import burpgpt.gpt.GPTResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class GPTClient {

  private String apiKey;
  private String model;
  private int maxPromptSize;
  private String prompt;
  private final OkHttpClient client;
  private final Gson gson;
  private Logging logging;

  public GPTClient(String apiKey, String model, String prompt, Logging logging) {
    this.apiKey = apiKey;
    this.model = model;
    this.prompt = prompt;
    this.logging = logging;
    client = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build();
    gson = new Gson();
  }

  public void updateSettings(String newApiKey, String newModelId, int newMaxPromptSize, String newPrompt) {
    this.apiKey = newApiKey;
    this.model = newModelId;
    this.maxPromptSize = newMaxPromptSize;
    this.prompt = newPrompt;
  }

  public Pair<GPTRequest, GPTResponse> identifyVulnerabilities(HttpRequestResponse selectedMessage) throws IOException {
    HttpRequest selectedRequest = selectedMessage.request();
    HttpResponse selectedResponse = selectedMessage.response();

    if (MyBurpExtension.DEBUG) {
      logging.logToOutput("[*] Selected request:");
      logging.logToOutput(String.format("- url: %s\n" +
          "- method: %s\n" +
          "- headers: %s\n" +
          "- body: %s",
          selectedRequest.url(),
          selectedRequest.method(),
          selectedRequest.headers().toString(),
          selectedRequest.bodyToString()));

      logging.logToOutput("[*] Selected response:");
      logging.logToOutput(String.format("- headers: %s\n" +
          "- body: %s",
          selectedResponse.headers().toString(),
          selectedResponse.bodyToString()));
    }

    // This code sends the selected request/response information to ChatGPT
    // and receives a list of potential vulnerabilities in response.
    // TODO: Add a field to specify the maxTokens value
    try {
      GPTRequest gptRequest = new GPTRequest(selectedRequest, selectedResponse, model, 1, maxPromptSize);
      GPTResponse gptResponse = getCompletions(gptRequest, apiKey, model, prompt);
      return Pair.of(gptRequest, gptResponse);
    } catch (IOException e) {
      throw e;
    }
  }

  private GPTResponse getCompletions(GPTRequest gptRequest, String apiKey, String model, String prompt)
      throws IOException {
    gptRequest.setPrompt(prompt);

    String apiEndpoint = "https://api.openai.com/v1/completions";
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("prompt", gptRequest.getPrompt());
    jsonObject.addProperty("max_tokens", gptRequest.getMaxPromptSize());
    jsonObject.addProperty("n", gptRequest.getN());
    jsonObject.addProperty("model", model);
    String jsonBody = gson.toJson(jsonObject);

    RequestBody body = RequestBody.create(jsonBody, JSON);
    Request request = new Request.Builder()
        .url(apiEndpoint)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .post(body)
        .build();

    if (MyBurpExtension.DEBUG) {
      // Write the request body to a buffer
      Buffer buffer = new Buffer();
      request.body().writeTo(buffer);

      logging.logToOutput("[+] Completion request sent:");
      logging.logToOutput(String.format("- request: %s\n" +
          "- requestBody: %s", request, buffer.readUtf8()));
    }

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        handleErrorResponse(response);
      } else {
        String responseBody = response.body().string();

        if (MyBurpExtension.DEBUG) {
          logging.logToOutput("[+] Completion response received:");
          logging.logToOutput(String.format("- responseBody: %s",
              responseBody));
        }

        return gson.fromJson(responseBody, GPTResponse.class);
      }
    } catch (IOException e) {
      throw new IOException(e);
    }

    return null;
  }

  private void handleErrorResponse(Response response) throws IOException {
    int statusCode = response.code();
    String responseBody = response.body().string();

    switch (statusCode) {
      case 400:
        throw new IOException(String.format("Bad request (400): %s", responseBody));
      case 401:
        throw new IOException(String.format("Unauthorized (401): %s", responseBody));
      case 429:
        throw new IOException(String.format("Too many requests (429): %s", responseBody));
      default:
        throw new IOException(
            String.format("Unhandled response code (%d): %s", statusCode, responseBody));
    }
  }
}
