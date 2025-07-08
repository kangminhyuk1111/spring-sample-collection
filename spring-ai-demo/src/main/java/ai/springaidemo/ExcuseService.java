package ai.springaidemo;

import java.util.Objects;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Service;

@Service
public class ExcuseService {

  private final ChatModel chatModel;
  private final BeanOutputConverter<ExcuseResponse> converter =
      new BeanOutputConverter<>(ExcuseResponse.class);

  public ExcuseService(final ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  public ExcuseResponse generate(String userPrompt) {

    String schema = converter.getJsonSchema();

    Prompt prompt = new Prompt(
        userPrompt + "\n\n(반드시 JSON만 출력)",
        OpenAiChatOptions.builder()
            .responseFormat(new ResponseFormat(
                ResponseFormat.Type.JSON_SCHEMA,
                schema))
            .build()
    );

    ChatResponse res = chatModel.call(prompt);
    String raw = res.getResult().getOutput().getText();

    return converter.convert(Objects.requireNonNull(raw));
  }

  public ExcuseResponse generate(ExcuseRequest excuseRequest) {

    String schema = converter.getJsonSchema();

    String fullPrompt = getPrompt(excuseRequest);

    System.out.println(fullPrompt);

    Prompt prompt = new Prompt(
        fullPrompt,
        OpenAiChatOptions.builder()
            .responseFormat(new ResponseFormat(
                ResponseFormat.Type.JSON_SCHEMA,
                schema))
            .build()
    );

    ChatResponse res = chatModel.call(prompt);
    String raw = res.getResult().getOutput().getText();

    return converter.convert(Objects.requireNonNull(raw));
  }

  private static String getPrompt(final ExcuseRequest excuseRequest) {
    String systemPrompt = """
       당신은 상황에 맞는 현실적이고 믿을 만한 핑계를 만드는 전문가입니다.
       주어진 상황, 대상, 긴급도를 고려하여 적절한 핑계를 생성해주세요.
       """;

    String userPrompt = String.format("""
       다음 조건에 맞는 핑계를 생성해주세요:
       - 상황: %s
       - 대상: %s
       - 긴급도: %s
       
       핑계는 현실적이고 믿을 만하며, 대상과 상황에 적합한 톤으로 작성해주세요.
       핑계 ID는 "excuse_" + 랜덤숫자 형태로 생성해주세요.
       """, excuseRequest.situation(), excuseRequest.target(), excuseRequest.urgency());

    return systemPrompt + "\n\n" + userPrompt + "\n\n(반드시 JSON만 출력)";
  }
}