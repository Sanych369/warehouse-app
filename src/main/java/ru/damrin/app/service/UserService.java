package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.enums.Position;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.db.repository.UserRepository;
import ru.damrin.app.model.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public List<UserDto> getAllUsers() {
    List<UserEntity> users = userRepository.findAll();
    return users.stream()
        .map(user -> new UserDto(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getPosition().getPositionName(),
            user.getEmail(),
            user.getPassword()
        ))
        .toList();
  }

  public void deleteUserByEmail(String email) {
    UserEntity user = userRepository.findByEmail(email)
        .orElseThrow(() -> new WarehouseAppException(String.format("Пользователь с email %s не найден. Проверьте правильность ввода", email)));
    userRepository.delete(user);
  }

  public void saveUser(UserDto userDto) {
    UserEntity newUser = UserEntity.builder()
        .name(userDto.name())
        .surname(userDto.surname())
        .email(userDto.email())
        .password(passwordEncoder.encode(userDto.password()))
        .position(Position.valueOf(userDto.position()))
        .build();
    userRepository.save(newUser);
  }

  public UserDto getUserByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new WarehouseAppException(String.format("Пользователь с email %s не найден. Проверьте правильность ввода", email)));

    return new UserDto(
        userEntity.getId(),
        userEntity.getName(),
        userEntity.getSurname(),
        userEntity.getPosition().getPositionName(),
        userEntity.getEmail(),
        userEntity.getPassword()
    );
  }

  public void updateUser(UserDto userDto) {

    UserEntity user = userRepository.findByEmail(userDto.email())
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Пользователь с email %s не найден. Проверьте правильность ввода", userDto.email())));
    user.setName(userDto.name());
    user.setSurname(userDto.surname());
    user.setPosition((Position.valueOf(userDto.position())));
    if (userDto.password() != null && !userDto.password().isEmpty()) {
      user.setPassword(passwordEncoder.encode(userDto.password()));
    }
  }

  public UserDto getProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();
    return getUserByEmail(email);
  }
}