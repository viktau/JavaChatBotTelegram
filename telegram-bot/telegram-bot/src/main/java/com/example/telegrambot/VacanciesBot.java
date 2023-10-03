package com.example.telegrambot;

import com.example.telegrambot.dto.VacancyDto;
import com.example.telegrambot.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class VacanciesBot extends TelegramLongPollingBot {
    @Autowired
    private VacancyService vacancyService;

    public VacanciesBot() {
        super( "6567178091:AAH_XU6lHjXuTI3HL9EL8OU8gkm-zZ1TN20");
    }
    @Override
    // головна логіка обробки повідомлень від телеграму
    public void onUpdateReceived(Update update) {

        try {
            if (update.getMessage() != null) {
                handleStartCommand((update));
            }
            if (update.getCallbackQuery() != null) {
                String callbackData = update.getCallbackQuery().getData();

                if ("showJuniorVacancies".equals(callbackData)) {
                    showJuniorVacancies(update);
                } else if ("showMiddleVacancies".equals(callbackData)) {
                    showMiddleVacancies(update);
                } else if ("showSeniorVacancies".equals(callbackData)) {
                    showSeniorVacancies(update);
                }else if (callbackData.startsWith("vacancyId=")){
                    String id = callbackData.split("=")[1];
                    System.out.println("ID from callbackData: " + id);
                    showVacancyDescription(id, update);;
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Список кнопок

    private void showVacancyDescription(String id, Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        VacancyDto vacancy = vacancyService.get(id);
        String description = vacancy.getShortDescription();
        sendMessage.setText(description);
        sendMessage.setReplyMarkup(getBackToVacanciesMenu());
        execute(sendMessage);

    }
//Додали ще дві кнопки
    private ReplyKeyboard getBackToVacanciesMenu() {
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton backToVacanciesButton = new InlineKeyboardButton();
        backToVacanciesButton.setText("Back to vacancies");
        backToVacanciesButton.setCallbackData("backToVacancies");
        row.add(backToVacanciesButton);

        InlineKeyboardButton backToStartMenuButton = new InlineKeyboardButton();
        backToStartMenuButton.setText("Back to start menu");
        backToStartMenuButton.setCallbackData("backToStartMenu");
        row.add(backToStartMenuButton);

        return new InlineKeyboardMarkup(List.of(row));
    }

    @Override
    public String getBotUsername() {  return "vptataurov vacancies bot";}

    private void showJuniorVacancies(Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Please choose vacancy");
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(getJuniorVacanciesMenu());
        execute(sendMessage);

    }

    private ReplyKeyboard getJuniorVacanciesMenu() {
        List<InlineKeyboardButton> row = new ArrayList<>();

        List<VacancyDto> vacancies = vacancyService.getJuniorVacancies();
        for (VacancyDto vacancy : vacancies) {
            InlineKeyboardButton vacancyButton = new InlineKeyboardButton();
            vacancyButton.setText(vacancy.getTitle());
            vacancyButton.setCallbackData("vacancyId=" + vacancy.getId());
            row.add(vacancyButton);
        }
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(); //повертає клавіатуру
        keyboard.setKeyboard((List.of(row)));
        return keyboard;
    }

    private void showMiddleVacancies(Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Please choose vacancy");
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(getMiddleVacanciesMenu());
        execute(sendMessage);
    }
    private ReplyKeyboard getMiddleVacanciesMenu() {
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton maVacancy = new InlineKeyboardButton();
        maVacancy.setText("Middle Java dev at MA");
        maVacancy.setCallbackData("vacancyId=3");
        row.add(maVacancy);

        InlineKeyboardButton googleVacancy = new InlineKeyboardButton();
        googleVacancy.setText("Middle Java dev at Google");
        googleVacancy.setCallbackData("vacancyId=4");
        row.add(googleVacancy);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(); //повертає клавіатуру
        keyboard.setKeyboard((List.of(row)));
        return keyboard;
        }
        private void showSeniorVacancies(Update update) throws TelegramApiException {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Please choose vacancy");
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(getSeniorVacanciesMenu());
            execute(sendMessage);
        }
    private ReplyKeyboard getSeniorVacanciesMenu() {
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton maVacancy = new InlineKeyboardButton();
        maVacancy.setText("Senior Java dev at MA");
        maVacancy.setCallbackData("vacancyId=5");
        row.add(maVacancy);

        InlineKeyboardButton googleVacancy = new InlineKeyboardButton();
        googleVacancy.setText("Senior Java dev at Google");
        googleVacancy.setCallbackData("vacancyId=6");
        row.add(googleVacancy);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(); //повертає клавіатуру
        keyboard.setKeyboard((List.of(row)));
        return keyboard;
       }


    private void handleStartCommand(Update update) {
        String text = update.getMessage().getText();
        System.out.println("event received" + text);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Welcome to vacancies bot, chouse your menu");
        sendMessage.setReplyMarkup(getStartMenu());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard getStartMenu() {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton junior = new InlineKeyboardButton();
            junior.setText("Junior");
            junior.setCallbackData("showJuniorVacancies"); //повідомлення від телеграму
            row.add(junior); // виводимо кнопку в рядок

            InlineKeyboardButton middle = new InlineKeyboardButton();
            middle.setText("Middle");
            middle.setCallbackData("showMiddleVacancies");
            row.add(middle);

            InlineKeyboardButton senior = new InlineKeyboardButton();
            senior.setText("Senior");
            senior.setCallbackData("showSeniorVacancies");
            row.add(senior);

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(); //повертає клавіатуру
            keyboard.setKeyboard((List.of(row)));
            return keyboard;

        }
    }
