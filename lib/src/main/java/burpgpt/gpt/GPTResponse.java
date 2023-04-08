package burpgpt.gpt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

@Getter
public class GPTResponse {
    private List<Choice> choices;
    private List<Vulnerability> vulnerabilities;

    public GPTResponse(List<Choice> choices) {
        this.choices = choices;
        if (choices != null && !choices.isEmpty()) {
            parseVulnerabilities();
        }
    }

    @Getter
    public class Choice {
        private String text;
    }

    private void parseVulnerabilities() {
        vulnerabilities = new ArrayList<>();

        for (Choice choice : choices) {
            String text = choice.getText();
            if (text != null && !text.isBlank()) {
                Pattern pattern = Pattern.compile("- ([^:]+): ([\\s\\S]+?)(?=- \\w|$)");
                Matcher matcher = pattern.matcher(text);

                while (matcher.find()) {
                    String vulnerabilityName = matcher.group(1).trim();
                    String vulnerabilityDescription = matcher.group(2).trim();
                    vulnerabilities.add(new Vulnerability(vulnerabilityName, vulnerabilityDescription));
                }
            }
        }
    }

    @Getter
    public static class Vulnerability {
        private String name;
        private String description;

        public Vulnerability(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}