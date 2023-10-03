package com.example.telegrambot.service;

import com.example.telegrambot.dto.VacancyDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Зберігання даних в Мар
@Service
public class VacancyService {
    private final Map<String, VacancyDto> vacancies = new HashMap<>();
 // Створення вакансій в ручну
    @PostConstruct
    public void init (){
        VacancyDto juniorMaDeveloper = new VacancyDto();
        juniorMaDeveloper.setId("1");
        juniorMaDeveloper.setTitle("Junior Dev at MA");
        juniorMaDeveloper.setShortDescription("Java Core is required");
        vacancies.put("1", juniorMaDeveloper);

        VacancyDto googleDev = new VacancyDto();
        googleDev.setId("2");
        googleDev.setTitle("Junior Dev at Google");
        googleDev.setShortDescription("Welcom to Google");
        vacancies.put("2",googleDev);


        VacancyDto middleMaDeveloper = new VacancyDto();
        middleMaDeveloper.setId("3");
        middleMaDeveloper.setTitle("Middle Dev at MA");
        middleMaDeveloper.setShortDescription("Spring bot is required");
        vacancies.put("3", middleMaDeveloper);

        VacancyDto google = new VacancyDto();
        google.setId("4");
        google.setTitle("Middle Dev at Google");
        google.setShortDescription("Welcom middle to Google");
        vacancies.put("4",google);

        VacancyDto seniorMaDeveloper = new VacancyDto();
        seniorMaDeveloper.setId("5");
        seniorMaDeveloper.setTitle("Senior Dev at MA");
        seniorMaDeveloper.setShortDescription("Join our company");
        vacancies.put("5", seniorMaDeveloper);

        VacancyDto seniorGoogleDev = new VacancyDto();
        seniorGoogleDev.setId("6");
        seniorGoogleDev.setTitle("Senior Dev at MA");
        seniorGoogleDev.setShortDescription("Join our company Google");
        vacancies.put("6", seniorGoogleDev);
    }

    public List<VacancyDto> getJuniorVacancies(){
        return vacancies.values().stream()
                .filter(v ->v.getTitle().toLowerCase().contains("junior"))
                .toList();

    }
    public VacancyDto get(String id){
        return vacancies.get(id);
    }
}