package com.ssafy.trend_gaza.review.entity;


import com.ssafy.trend_gaza.review.exception.CompanionNotFoundException;
import java.util.Arrays;

public enum CompanionType {
    FAMILY("FAMILY"),
    COUPLE("COUPLE"),
    BUSINESS("BUSINESS"),
    FRIEND("FRIEND"),
    SOLO("SOLO");

    private final String value;

    private CompanionType(String value) {
        this.value = value;
    }

    public static CompanionType of(String source) {
        return Arrays.stream(CompanionType.values())
                .filter(companion -> companion.toString().contentEquals(source))
                .findAny()
                .orElseThrow(() -> CompanionNotFoundException.EXCEPTION);
    }

    public String getValue() {
        return value;
    }
}
