package ru.damrin.app.model;

public record UserDto(Long id, String name, String surname, String position, String email, String password) {
}