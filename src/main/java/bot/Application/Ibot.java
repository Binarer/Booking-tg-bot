package bot.Application;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface Ibot {
    void SendServices();
    void SendMasters();
    void startBot(long chatId, String userName);
    void sendHelpText(long chatId);
}
