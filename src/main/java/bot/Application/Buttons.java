package bot.Application;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton SERVICES_BUTTON = new InlineKeyboardButton("Services");

    public static InlineKeyboardMarkup startMarkup() {
        START_BUTTON.setCallbackData("/start");

        List<InlineKeyboardButton> rowInline = List.of(START_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    /*
        public static InlineKeyboardMarkup helpMarkup() {
            HELP_BUTTON.setCallbackData("/help");

            List<InlineKeyboardButton> rowInline = List.of(HELP_BUTTON);
            List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.setKeyboard(rowsInLine);

            return markupInline;
        }
    */
    public static InlineKeyboardMarkup buildConfirmationKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton confirmButton = new InlineKeyboardButton();
        confirmButton.setText("Подтвердить");
        confirmButton.setCallbackData("confirm:");
        row1.add(confirmButton);

        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        cancelButton.setText("Отменить");
        cancelButton.setCallbackData("cancel:");
        row1.add(cancelButton);

        rows.add(row1);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup servicesMarkup() {
        SERVICES_BUTTON.setCallbackData("services");

        List<InlineKeyboardButton> rowInline = List.of(SERVICES_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
