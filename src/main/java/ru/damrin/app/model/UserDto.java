package ru.damrin.app.model;

/**
 * DTO получения информации о пользователе.
 */
public record UserDto(Long id, String name, String surname, String position, String email, String password) {
}