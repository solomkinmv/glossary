package io.github.solomkinmv.translateservice.domain;

import java.util.Optional;

/**
 * Languages, supported by Translate service.
 */
public enum Language {
    ALBANIAN("sq"),
    ARMENIAN("hy"),
    AZERBAIJANI("az"),
    BELARUSIAN("be"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GEORGIAN("ka"),
    GREEK("el"),
    HUNGARIAN("hu"),
    ITALIAN("it"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SERBIAN("sr"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("es"),
    SWEDISH("sv"),
    TURKISH("tr"),
    UKRAINIAN("uk");

    private final String language;

    Language(String pLanguage) {
        language = pLanguage;
    }

    public static Optional<Language> fromString(String language) {
        for (Language lang : values()) {
            if (lang.getLanguage().equals(language)) {
                return Optional.of(lang);
            }
        }
        return Optional.empty();
    }

    public String getLanguage() {
        return language;
    }
}
