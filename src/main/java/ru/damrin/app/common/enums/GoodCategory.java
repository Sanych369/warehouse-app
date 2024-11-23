package ru.damrin.app.common.enums;

import lombok.Getter;

@Getter
public enum GoodCategory {

    PENCIL("Карандаш"),
    PEN("Ручка"),
    PAPER("Бумага"),
    RULER("Линейка"),
    FOLDER("Папка"),
    SCISSORS("Ножницы"),
    PAINTS("Краски");

    private final String categoryName;

    GoodCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
