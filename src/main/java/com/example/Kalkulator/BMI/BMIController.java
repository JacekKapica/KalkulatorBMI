package com.example.Kalkulator.BMI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Autor: Jacek Kapica
@Controller
public class BMIController {

    // Logger do rejestrowania informacji i błędów
    private static final Logger logger = LoggerFactory.getLogger(BMIController.class);

    // Obsługuje żądanie GET na stronie głównej
    @GetMapping("/")
    public String index() {
        return "index"; // Wyświetla stronę główną z formularzem
    }

    // Obsługuje żądanie POST do obliczenia BMI
    @PostMapping("/calculate")
    public String calculateBMI(@RequestParam("weight") double weight, @RequestParam("height") double height, Model model) {
        try {
            // Walidacja danych wejściowych
            if (weight <= 0 || weight > 500 || height <= 0 || height > 3) {
                model.addAttribute("error", "Proszę wprowadzić poprawne wartości dla wagi (0-500 kg) i wzrostu (0-3 m).");
                return "index"; // Powrót do formularza z komunikatem o błędzie
            }

            // Obliczanie BMI
            double bmi = weight / (height * height);
            String bmiCategory = getBMICategory(bmi);
            String bmiSuggestion = getBMISuggestion(bmi);

            // Dodanie danych do modelu
            model.addAttribute("bmi", bmi);
            model.addAttribute("bmiCategory", bmiCategory);
            model.addAttribute("bmiSuggestion", bmiSuggestion);
            return "result"; // Wyświetla stronę z wynikami

        } catch (Exception e) {
            logger.error("Błąd podczas obliczania BMI", e);
            model.addAttribute("error", "Wystąpił błąd podczas obliczania BMI. Spróbuj ponownie.");
            return "index"; // Powrót do formularza z komunikatem o błędzie
        }
    }

    // Metoda określająca kategorię BMI
    private String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Niedowaga";
        } else if (bmi < 24.9) {
            return "Waga prawidłowa";
        } else if (bmi < 29.9) {
            return "Nadwaga";
        } else {
            return "Otyłość";
        }
    }

    // Metoda dająca sugestie zdrowotne na podstawie BMI
    private String getBMISuggestion(double bmi) {
        if (bmi < 18.5) {
            return "Skonsultuj się z dietetykiem, aby zwiększyć wagę w zdrowy sposób.";
        } else if (bmi < 24.9) {
            return "Twoja waga jest prawidłowa. Utrzymuj zdrowy styl życia.";
        } else if (bmi < 29.9) {
            return "Rozważ zmianę diety i zwiększenie aktywności fizycznej.";
        } else {
            return "Skonsultuj się z lekarzem w sprawie planu redukcji wagi.";
        }
    }

    // Globalna obsługa wyjątków
    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        logger.error("Wystąpił nieoczekiwany błąd", ex);
        model.addAttribute("error", "Wystąpił nieoczekiwany błąd. Spróbuj ponownie później.");
        return "index"; // Wyświetla stronę główną z komunikatem o błędzie
    }
}
