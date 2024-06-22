package bot.Application;

import bot.Config.BotConfig;
import bot.Domain.Models.Appoiment.Appointment;
import bot.Domain.Models.Client.Customer;
import bot.Domain.Models.Master.MasterEntity;
import bot.Domain.Models.Service.ServiceEntity;
import bot.Domain.Models.TimeSlot.TimeSlotEntity;
import bot.Domain.Repositories.*;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static bot.Application.BotCommands.LIST_OF_COMMANDS;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private IAppointmentRepository appointmentRepository;
    @Autowired
    private IServicesRepository servicesRepository;
    @Autowired
    private IMasterRepository masterRepository;
    @Autowired
    private ITimeSlotRepository timeSlotRepository;
    @Autowired
    private ICustomerRepository customerRepository;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    @Autowired
    final BotConfig config;

    public Bot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private Map<Long, LocalDate> chatIdToDate = new HashMap<>();
    private Map<Long, LocalTime> chatIdToTime = new HashMap<>();
    private Map<Long, MasterEntity> chatIdToMaster = new HashMap<>();
    private Map<Long, ServiceEntity> chatIdToService = new HashMap<>();
    private Map<Long, ServiceEntity> chatIdToSelectedService = new HashMap<>();

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = null;
        String receivedMessage;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userName = update.getMessage().getFrom().getFirstName();
            saveNewCustomer(chatId, userName);
            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName, null);
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, userName, update.getCallbackQuery());
        }
    }


    /*    private void botAnswerUtils(String receivedMessage, long chatId, String userName, CallbackQuery callbackQuery) {
            switch (receivedMessage) {
                case "services" -> handleServicesCallback(chatId, callbackQuery);
                case "service:","\\d+" -> handleServiceCallback(receivedMessage, chatId, callbackQuery);
                case "master:" -> handleMasterCallback(receivedMessage, chatId, callbackQuery);

                case "/appointments" -> sendUserAppointments(chatId);
                case "date:", "time:", "prevdate:", "nextdate:", "prevtime:", "nexttime:" ->
                        handleCallbackQuery(callbackQuery);
                default -> handleCommand(receivedMessage, chatId, userName);
            }
        }*/
    private void botAnswerUtils(String receivedMessage, long chatId, String userName, CallbackQuery callbackQuery) {
        if (callbackQuery != null && receivedMessage.equals("services")) {
            handleServicesCallback(chatId, callbackQuery);
        } else if (callbackQuery != null && receivedMessage.startsWith("master:")) {
            handleMasterCallback(receivedMessage, chatId, callbackQuery);
        } else if (callbackQuery != null && receivedMessage.matches("\\d+")) {
            handleServiceCallback(receivedMessage, chatId, callbackQuery);
        } else if (callbackQuery != null && receivedMessage.startsWith("date:")) {
            handleCallbackQuery(callbackQuery);
        } else if (callbackQuery != null && receivedMessage.startsWith("time:")) {
            handleCallbackQuery(callbackQuery);
        } else if (callbackQuery != null && receivedMessage.startsWith("prevdate:")) {
            handleCallbackQuery(callbackQuery);
        } else if (callbackQuery != null && receivedMessage.startsWith("nextdate:")) {
            handleCallbackQuery(callbackQuery);
        } else if (callbackQuery != null && receivedMessage.startsWith("prevtime:")) {
            handleCallbackQuery(callbackQuery);
        } else if (callbackQuery != null && receivedMessage.startsWith("nexttime:")) {
            handleCallbackQuery(callbackQuery);
        } else {
            handleCommand(receivedMessage, chatId, userName);
        }
    }

    private void handleServicesCallback(long chatId, CallbackQuery callbackQuery) {
        Iterable<ServiceEntity> services = servicesRepository.findAll();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (ServiceEntity service : services) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(service.getName());
            button.setCallbackData(String.valueOf(service.getId()));
            row.add(button);
            rows.add(row);
        }
        keyboardMarkup.setKeyboard(rows);
        SendMessage servicesMessage = new SendMessage();
        servicesMessage.setChatId(chatId);
        servicesMessage.setText("Список услуг💫:");
        servicesMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(servicesMessage);
            log.info("Services message sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        answerCallbackQuery(callbackQuery);
    }

    private void handleServiceCallback(String receivedMessage, long chatId, CallbackQuery callbackQuery) {
        int selectedServiceId = Integer.parseInt(receivedMessage);
        ServiceEntity selectedService = servicesRepository.findById(selectedServiceId).orElse(null);
        log.info("Selected service: " + selectedService);
        if (selectedService == null) {
            SendMessage errorMessage = new SendMessage();
            errorMessage.setChatId(chatId);
            errorMessage.setText("Выбранная услуга недоступна.");
            try {
                execute(errorMessage);
                log.info("Error message sent");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            List<MasterEntity> masters = masterRepository.findByServices(selectedService);
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            for (MasterEntity master : masters) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(master.getName());
                button.setCallbackData("master:" + master.getId());
                row.add(button);
                rows.add(row);
            }
            keyboardMarkup.setKeyboard(rows);
            SendMessage mastersMessage = new SendMessage();
            mastersMessage.setChatId(chatId);
            mastersMessage.setText("Список мастеров готовых взяться за ващи делишки:");
            mastersMessage.setReplyMarkup(keyboardMarkup);
            try {
                execute(mastersMessage);
                log.info("Masters message sent");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
            chatIdToSelectedService.put(chatId, selectedService);
            chatIdToService.put(chatId, selectedService);
        }
        answerCallbackQuery(callbackQuery);
    }

    private void handleMasterCallback(String receivedMessage, long chatId, CallbackQuery callbackQuery) {
        int selectedMasterId = Integer.parseInt(receivedMessage.substring(7));
        Optional<MasterEntity> selectedMaster = masterRepository.findById(selectedMasterId);
        if (selectedMaster.isEmpty()) {
            SendMessage errorMessage = new SendMessage();
            errorMessage.setChatId(chatId);
            errorMessage.setText("Выбранный мастер недоступен.");
            try {
                execute(errorMessage);
                log.info("Error");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            sendDateOptions(chatId, LocalDate.now());
            chatIdToMaster.put(chatId, selectedMaster.get());
            answerCallbackQuery(callbackQuery);
        }
    }

    private void sendDateOptions(long chatId, LocalDate currentDate) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(InlineKeyboardButton.builder().text("<< prevdate").callbackData("prevdate:" + currentDate.toString()).build());
        for (int i = 0; i < 3; i++) {
            LocalDate date = currentDate.plusDays(i);
            String dateString = date.format(dateFormatter);
            InlineKeyboardButton button = InlineKeyboardButton.builder().text(dateString).callbackData("date:" + date.toString()).build();
            row.add(button);
        }
        row.add(InlineKeyboardButton.builder().text("nextdate >>").callbackData("nextdate:" + currentDate.toString()).build());
        keyboard.add(row);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(keyboard).build();
        SendMessage message = SendMessage.builder().chatId(chatId).text("ПЖ выбери время:").replyMarkup(markup).build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void updateDateOptions(long chatId, int messageId, LocalDate currentDate) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(InlineKeyboardButton.builder().text("<< prevdate").callbackData("prevdate:" + currentDate.toString()).build());
        for (int i = 0; i < 3; i++) {
            LocalDate date = currentDate.plusDays(i);
            String dateString = date.format(dateFormatter);
            InlineKeyboardButton button = InlineKeyboardButton.builder().text(dateString).callbackData("date:" + date.toString()).build();
            row.add(button);
        }
        row.add(InlineKeyboardButton.builder().text("nextdate >>").callbackData("nextdate:" + currentDate.toString()).build());
        keyboard.add(row);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(keyboard).build();
        EditMessageReplyMarkup message = EditMessageReplyMarkup.builder().chatId(chatId).messageId(messageId).replyMarkup(markup).build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendTimeOptions(long chatId, int messageId, LocalDate date, LocalTime currentTime) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(InlineKeyboardButton.builder().text("<< prevtime").callbackData("prevtime:" + date.toString() + "T" + currentTime.toString()).build());
        for (int i = 0; i < 3; i++) {
            LocalTime time = currentTime.plusHours(i);
            String timeString = time.format(timeFormatter);
            InlineKeyboardButton button = InlineKeyboardButton.builder().text(timeString).callbackData("time:" + date.toString() + "T" + time.toString()).build();
            row.add(button);
        }
        row.add(InlineKeyboardButton.builder().text("nexttime >>").callbackData("nexttime:" + date.toString() + "T" + currentTime.toString()).build());
        keyboard.add(row);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboard(keyboard).build();
        EditMessageReplyMarkup message = EditMessageReplyMarkup.builder().chatId(chatId).messageId(messageId).replyMarkup(markup).build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        if (data.startsWith("date:")) {
            LocalDate date = LocalDate.parse(data.substring(5));
            sendTimeOptions(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId(), date, LocalTime.now());
            chatIdToDate.put(callbackQuery.getMessage().getChatId(), date);
        } else if (data.startsWith("time:")) {
            LocalDateTime datetime = LocalDateTime.parse(data.substring(5));
            long chatId = callbackQuery.getMessage().getChatId();
            LocalDate date = datetime.toLocalDate();
            LocalTime time = datetime.toLocalTime();

            MasterEntity selectedMaster = chatIdToMaster.get(chatId);
            ServiceEntity selectedService = chatIdToService.get(chatId);
            LocalDate selectedDate = chatIdToDate.get(chatId);

            if (selectedMaster != null && selectedService != null && selectedDate != null) {
                createTimeSlot(selectedMaster, selectedService, date, time);
                createAppointment(chatId, selectedMaster, selectedService, selectedDate, time);
                String text = "Ваще время записи: " + date.format(dateFormatter) + " " + time.format(timeFormatter);
                SendMessage message = SendMessage.builder().chatId(chatId).text(text).build();
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            chatIdToTime.put(chatId, datetime.toLocalTime());
        } else if (data.startsWith("prevdate:")) {
            LocalDate currentDate = LocalDate.parse(data.substring(9));
            updateDateOptions(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId(), currentDate.minusDays(3));
        } else if (data.startsWith("nextdate:")) {
            LocalDate currentDate = LocalDate.parse(data.substring(9));
            updateDateOptions(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId(), currentDate.plusDays(3));
        } else if (data.startsWith("prevtime:")) {
            LocalDateTime datetime = LocalDateTime.parse(data.substring(9));
            sendTimeOptions(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId(), datetime.toLocalDate(), datetime.toLocalTime().minusHours(3));
        } else if (data.startsWith("nexttime:")) {
            LocalDateTime datetime = LocalDateTime.parse(data.substring(9));
            sendTimeOptions(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId(), datetime.toLocalDate(), datetime.toLocalTime().plusHours(3));
        }
        answerCallbackQuery(callbackQuery);
    }

    private void createAppointment(long chatId, MasterEntity selectedMaster, ServiceEntity selectedService, LocalDate date, LocalTime startTime) {
        TimeSlotEntity selectedTimeSlot = timeSlotRepository.findByMasterAndStartTimeAndDate(selectedMaster, startTime, date);

        if (selectedTimeSlot == null) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("The chosen time slot is not available. Please choose a different date and time.");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
            return;
        }

        Appointment newAppointment = new Appointment();
        newAppointment.setCustomerId((int) chatId);
        newAppointment.setMasterId(selectedMaster.getId());
        newAppointment.setSlotId(selectedTimeSlot.getId());
        newAppointment.setServiceId(selectedService.getId());

        appointmentRepository.save(newAppointment);

        selectedTimeSlot.setBooked(true);
        timeSlotRepository.save(selectedTimeSlot);

        SendMessage successMessage = new SendMessage();
        successMessage.setChatId(chatId);
        successMessage.setText("Вы успешно записались на " + selectedService.getName() + "!");
        try {
            execute(successMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void answerCallbackQuery(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackQuery.getId());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void handleCommand(String receivedMessage, long chatId, String userName) {
        switch (receivedMessage) {
            case "/start" -> startBot(chatId, userName);
            case "/help" -> sendHelpText(chatId);
            case "/appointments" -> sendUserAppointments(chatId);
            default -> {
            }
        }
    }

    private void sendUserAppointments(long chatId) {
        List<Appointment> appointments = appointmentRepository.findAllByCustomerId(Math.toIntExact(chatId));

        if (appointments.isEmpty()) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("нету записей.");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            StringBuilder appointmentsText = new StringBuilder("Ваши записи:\n");

            for (Appointment appointment : appointments) {
                ServiceEntity selectedService = servicesRepository.findById(appointment.getServiceId()).orElse(null);
                String serviceName = selectedService != null ? selectedService.getName() : "";
                Optional<TimeSlotEntity> slot = timeSlotRepository.findById(appointment.getSlotId());
                String master = String.valueOf(masterRepository.findById(appointment.getMasterId()).get().getName());

                appointmentsText.append("Ваш мастер: ").append(master).append("\n")
                        .append("Услуга: ").append(serviceName).append("\n")
                        .append("Дата: ").append(slot.get().getDate()).append("\n")
                        .append("Время: ").append(slot.get().getStartTime()).append(" - ").append(slot.get().getEndTime()).append("\n\n");
            }

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(appointmentsText.toString());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void createTimeSlot(MasterEntity master, ServiceEntity service, LocalDate date, LocalTime startTime) {
        var timeSlotEntity = new TimeSlotEntity();
        timeSlotEntity.setMaster(master);
        timeSlotEntity.setStartTime(startTime);
        timeSlotEntity.setEndTime(startTime.plusMinutes(service.getDuration()));
        timeSlotEntity.setBooked(false);
        timeSlotEntity.setDate(date);

        timeSlotRepository.save(timeSlotEntity);
    }

    private void saveNewCustomer(long chatId, String userName) {
        String telegram = String.valueOf(chatId);
        Customer existingCustomer = customerRepository.findCustomerByTelegram(telegram);

        if (existingCustomer == null) {
            Customer newCustomer = new Customer();
            newCustomer.setName(userName);
            newCustomer.setTelegram(telegram);
            customerRepository.save(newCustomer);
        }
    }


    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hi, " + userName + "!\nЯ бот барбер шопа Алёнка.\nМогу помочь с бронированием записи для стрижки, окрашивания и прочих услуг.\nЖми скорей кнопку УСЛУГИ и смотри услуги🤡.'");
        message.setReplyMarkup(Buttons.servicesMarkup());
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendHelpText(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(BotCommands.HELP_TEXT);
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}