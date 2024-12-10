package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.db.repository.UserRepository;
import ru.damrin.app.mapper.UserMapper;
import ru.damrin.app.model.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;
  private final SortService sortService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Page<UserDto> getUsersPage(int page, int size, String name, String surname, String position, String email, String sort) {
    final var sortOrder = sortService.getSortOrderForUsers(sort);
    final var pageable = PageRequest.of(page, size, sortOrder);
    return userRepository.findByFilters(name, surname, position, email, pageable).map(userMapper::toDto);
  }

  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  public void deleteUserByEmail(String email) {
    final var user = userRepository.findByEmail(email).orElseThrow(() -> new WarehouseAppException(
        String.format("Пользователь с email %s не найден. Проверьте правильность ввода", email)));
    userRepository.delete(user);
  }

  public void saveUser(UserDto userDto) {
    var userEntity = userMapper.toEntity(userDto);

    if (StringUtils.isEmpty(userEntity.getPassword())) {
      throw new WarehouseAppException("Пароль отсутствует.");
    }

    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    userRepository.save(userEntity);
  }

  public UserDto getUserByEmail(String email) {
    final var userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Пользователь с email %s не найден. Проверьте правильность ввода", email)));
    return userMapper.toDto(userEntity);
  }

  public void updateUser(UserDto userDto) {
    var user = userRepository.findByEmail(userDto.email())
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Пользователь с email %s не найден. Проверьте правильность ввода", userDto.email())));
    var updatedUser = userMapper.partialUpdate(userDto, user);

    if (StringUtils.isEmpty(user.getPassword())) {
      throw new WarehouseAppException("Новый пароль не может быть пустым");
    }

    updatedUser.setPassword(passwordEncoder.encode(userDto.password()));
    userRepository.save(updatedUser);
  }

  public UserDto getProfile(UserEntity userEntity) {
    return userMapper.toDto(userEntity);
  }
}