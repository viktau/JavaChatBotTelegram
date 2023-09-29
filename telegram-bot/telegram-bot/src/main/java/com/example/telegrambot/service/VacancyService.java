package com.example.telegrambot.service;

import com.example.telegrambot.dto.VacancyDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

        VacancyDto middleMaDeveloper = new VacancyDto();
        middleMaDeveloper.setId("2");
        middleMaDeveloper.setTitle("Middle Dev at MA");
        middleMaDeveloper.setShortDescription("Java Core is required");
        vacancies.put("1", middleMaDeveloper);

        VacancyDto SeniorMaDeveloper = new VacancyDto();
        SeniorMaDeveloper.setId("3");
        SeniorMaDeveloper.setTitle("Middle Dev at MA");
        SeniorMaDeveloper.setShortDescription("Join our company");
        vacancies.put("1", SeniorMaDeveloper);
    }
}