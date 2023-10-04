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

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VacanciesBot extends TelegramLongPollingBot {
    @Autowired
    private VacancyService vacancyService;
    private final Map<Long, String> lastShowVacancyLevel = new HashMap<>();

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
                    //System.out.println("ID from callbackData: " + id);
                    showVacancyDescription(id, update);;
                }else if ("backToVacancies".equals(callbackData)){
                    handleBackToVacanciesCommand(update);

                } else if ("backToStartMenu".equals(callbackData)){
                    handleBackToStartCommand(update);

                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleBackToStartCommand(Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Choose your title");
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setReplyMarkup(getStartMenu());
        execute(sendMessage);

    }

    private void handleBackToVacanciesCommand(Update update) throws TelegramApiException {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String level = lastShowVacancyLevel.get(chatId);
        if ("junior".equals(level)){
            showJuniorVacancies(update);
        }else if("middle".equals(level)){
            showMiddleVacancies(update);
        }else if (("senior".equals(level))){
            showSeniorVacancies(update);
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

        lastShowVacancyLevel.put(chatId, "junior");


    }

    private ReplyKeyboard getJuniorVacanciesMenu() {
        List<VacancyDto> vacancies = vacancyService.getJuniorVacancies();
        return getVacanciesMenu(vacancies);

        }

    private void showMiddleVacancies(Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Please choose vacancy");
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(getMiddleVacanciesMenu());
        execute(sendMessage);

        lastShowVacancyLevel.put(chatId, "middle");
    }
    private ReplyKeyboard getMiddleVacanciesMenu() {
        List<VacancyDto> vacancies = vacancyService.getMiddleVacancies();
        return getVacanciesMenu(vacancies);
        }

    private void showSeniorVacancies(Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Please choose vacancy");
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(getSeniorVacanciesMenu());
        execute(sendMessage);

        lastShowVacancyLevel.put(chatId, "senior");
        }
    private ReplyKeyboard getSeniorVacanciesMenu() {
        List<VacancyDto> vacancies = vacancyService.getSeniorVacancies();
        return getVacanciesMenu(vacancies);
       }
 private ReplyKeyboard getVacanciesMenu(List<VacancyDto> vacancies){
     List<InlineKeyboardButton> row = new ArrayList<>();


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
