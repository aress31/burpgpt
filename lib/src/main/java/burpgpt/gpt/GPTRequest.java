package burpgpt.gpt;

import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import lombok.Getter;

public class GPTRequest {
    @Getter
    private String prompt;
    @Getter
    private String modelId;
    @Getter
    private final int n;
    @Getter
    private final int maxTokens;
    private final String url;
    private final String method;
    private final String requestHeaders;
    private final String requestBody;
    private final String responseHeaders;
    private final String responseBody;

    private final String request;
    private final String response;

    public GPTRequest(HttpRequest httpRequest, HttpResponse httpResponse, String modelId, int n, int maxTokens) {
        this.url = httpRequest.url();
        this.method = httpRequest.method();
        this.requestHeaders = httpRequest.headers().toString();
        this.requestBody = httpRequest.bodyToString();
        this.responseHeaders = httpResponse.headers().toString();
        this.responseBody = httpResponse.bodyToString();

        this.request = httpRequest.toString();
        this.response = httpResponse.toString();

        this.modelId = modelId;
        this.n = n;
        this.maxTokens = maxTokens;
    }

    public void setPrompt(String prompt) {
        String[] placeholders = { "{REQUEST}", "{RESPONSE}", "{IS_TRUNCATED_PROMPT}", "{URL}", "{METHOD}",
                "{REQUEST_HEADERS}", "{REQUEST_BODY}",
                "{RESPONSE_HEADERS}", "{RESPONSE_BODY}" };
        String[] replacements = { request, response, Boolean.toString(prompt.length() > maxTokens), url, method,
                requestHeaders,
                requestBody, responseHeaders, responseBody };

        for (int i = 0; i < placeholders.length; i++) {
            prompt = prompt.replace(placeholders[i], replacements[i]);
        }

        if (prompt.length() > maxTokens) {
            prompt = prompt.substring(0, maxTokens);
        }

        this.prompt = prompt;
    }
}
