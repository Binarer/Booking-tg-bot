package bot.Application;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import java.util.List;
public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start"),
            new BotCommand("/help", "памятка"),
            new BotCommand("/services", "услуги"),
            new BotCommand("/appointments", "мои записи")
    );

    String HELP_TEXT = """
            я бот для записи на стрижки.
            Вот список комманд:
            /start - запустить бота
            /help - памятка для бота
            /appointments - запсии
            """;
}
