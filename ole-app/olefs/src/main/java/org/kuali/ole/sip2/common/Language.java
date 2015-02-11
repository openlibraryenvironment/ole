package org.kuali.ole.sip2.common;

/**
 * This enum defines a set of languages that can be used in SIP2
 * request and response messages. Each language has a code that is
 * used in the communication between the system and the ILS.
 *
 * @author Gayathri A
 */
public enum Language {

    UNKNOWN("000"), ENGLISH("001"), FRENCH("002"), GERMAN("003"),
    ITALIAN("004"), DUTCH("005"), SWEDISH("006"), FINNISH("007"),
    SPANISH("008"), DANISH("009"), PORTUGESE("010"), CANADIAN_FRENCH("011"),
    NORWEGIAN("012"), HEBREW("013"), JAPANESE("014"), RUSSIAN("015"),
    ARABIC("016"), POLISH("017"), GREEK("018"), CHINESE("019"),
    KOREAN("020"), NORTH_AMERICAN_SPANISH("021"), TAMIL("022"),
    MALAY("023"), UNITED_KINGDOM("024"), ICELANDIC("025"), BELGIAN("026"),
    TAIWANESE("027");

    private String value;

    private Language(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
