package ai.springaidemo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Excuse(
    @JsonProperty(required = true) String id,
    @JsonProperty(required = true) String category,
    @JsonProperty(required = true) String tone,
    @JsonProperty(required = true) Integer believability,
    @JsonProperty(required = true) String text,
    List<String> keywords,
    String situation,
    String target,
    List<String> alternatives
) {}