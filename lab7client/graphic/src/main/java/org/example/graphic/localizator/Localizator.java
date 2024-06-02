package org.example.graphic.localizator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Localizator {
    @Getter
    @Setter
    private ResourceBundle bundle = ResourceBundle.getBundle("locales.gui");

    @Getter
    private final static Localizator instance = new Localizator();

    public Localizator(ResourceBundle bundle) {
        this.bundle = bundle;
    }


    public String getKeyString(String key) {
        return bundle.getString(key);
    }

    public String getDate(LocalDate date) {
        if (date == null) return "null";
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(bundle.getLocale());
        return date.format(formatter);
    }

    public String getDate(LocalDateTime date) {
        if (date == null) return "null";
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(bundle.getLocale());
        return date.format(formatter);
    }
}