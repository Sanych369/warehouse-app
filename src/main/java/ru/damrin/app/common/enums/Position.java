package ru.damrin.app.common.enums;

import lombok.Getter;

@Getter
public enum Position {

  ADMIN("Администратор"),
  STOREKEEPER("Кладовщик"),
  MANAGER("Менеджер");

  private final String positionName;

  Position(String positionName) {
    this.positionName = positionName;
  }
}
