package burpgpt.gpt;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class GPTResponse {
    private List<Choice> choices;
    private String modelId;
    private String id;
    @SerializedName("created")
    private long createdTimestamp;
    @SerializedName("usage")
    private Usage usage;

    public GPTResponse(List<Choice> choices) {
        this.choices = choices;
    }

    @Getter
    public class Choice {
        private String text;
        private int index;
        private Object logprobs; // or use a specific class structure if needed
        @SerializedName("finish_reason")
        private String finishReason;

        @Override
        public String toString() {
            return "Choice{" +
                    "text='" + text + '\'' +
                    ", index=" + index +
                    ", logprobs=" + logprobs +
                    ", finishReason='" + finishReason + '\'' +
                    '}';
        }
    }

    public List<String> getChoiceTexts() {
        List<String> choiceTexts = new ArrayList<>();
        for (Choice choice : choices) {
            choiceTexts.add(choice.getText());
        }
        return choiceTexts;
    }

    @Getter
    public static class Usage {
        private long promptTokens;
        private long completionTokens;
        private long totalTokens;

        @Override
        public String toString() {
            return "Usage{" +
                    "promptTokens=" + promptTokens +
                    ", completionTokens=" + completionTokens +
                    ", totalTokens=" + totalTokens +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GPTResponse{" +
                "choices=" + choices +
                ", modelId='" + modelId + '\'' +
                ", id='" + id + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", usage=" + usage +
                '}';
    }
}
