package burp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.scanner.AuditResult;
import burp.api.montoya.scanner.ConsolidationAction;
import burp.api.montoya.scanner.ScanCheck;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import burpgpt.gpt.GPTResponse;
import burpgpt.http.GPTClient;
import lombok.Setter;

public class MyScanCheck implements ScanCheck {

    private Logging logging;

    @Setter
    private GPTClient gptClient;

    public MyScanCheck(GPTClient gptClient, Logging logging) {
        this.gptClient = gptClient;
        this.logging = logging;
    }

    @Override
    public AuditResult activeAudit(HttpRequestResponse httpRequestResponse, AuditInsertionPoint auditInsertionPoint) {
        throw new UnsupportedOperationException("Unimplemented method 'activeAudit'");
    }

    @Override
    public AuditResult passiveAudit(HttpRequestResponse httpRequestResponse) {
        try {
            GPTResponse gptResponse = gptClient.identifyVulnerabilities(httpRequestResponse);
            List<AuditIssue> auditIssues = createAuditIssuesFromGPTResponse(gptResponse, httpRequestResponse);
            return AuditResult.auditResult(auditIssues);
        } catch (IOException e) {
            logging.raiseErrorEvent(e.getMessage());
            return AuditResult.auditResult(new ArrayList<>());
        }
    }

    @Override
    public ConsolidationAction consolidateIssues(AuditIssue newIssue, AuditIssue existingIssue) {
        return existingIssue.name().equals(newIssue.name()) ? ConsolidationAction.KEEP_EXISTING
                : ConsolidationAction.KEEP_BOTH;
    }

    private List<AuditIssue> createAuditIssuesFromGPTResponse(GPTResponse gptResponse,
            HttpRequestResponse httpRequestResponse) {
        List<AuditIssue> auditIssues = new ArrayList<>();
        String choicesText = "";

        if (gptResponse.getChoices() != null) {
            choicesText = gptResponse.getChoices().stream()
                    .map(choice -> choice.getText())
                    .collect(Collectors.joining("\n"));

            AuditIssue auditIssue = AuditIssue.auditIssue("GPT-generated vulnerability insights", choicesText,
                    null, httpRequestResponse.request().url(), AuditIssueSeverity.INFORMATION,
                    AuditIssueConfidence.TENTATIVE, null, null,
                    null, httpRequestResponse);
            auditIssues.add(auditIssue);
        }

        return auditIssues;
    }
}
