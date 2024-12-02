package ru.damrin.app.model;

public record UserDto(Long id, String name, String surname, String position, String email, String password) {
  public UserDto(Long id, String name, String surname, String position, String email) {
    this(id, name, surname, position, email, "***hidden***");
  }
}