package bot.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BotConfig {

    @Value("${bot_name}")
    String botName;
    @Value("${bot_owner}")
    long ChatId;
    @Value("${bot.token}")
    String token;
}