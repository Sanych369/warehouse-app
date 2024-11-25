package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.enums.Position;
import ru.damrin.app.db.entity.WarehouseUserEntity;
import ru.damrin.app.db.repository.WarehouseUserRepository;
import ru.damrin.app.model.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final WarehouseUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public List<UserDto> getAllUsers() {
    List<WarehouseUserEntity> users = userRepository.findAll();
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
    WarehouseUserEntity user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    userRepository.delete(user);
  }

  public void saveUser(UserDto userDto) {
    WarehouseUserEntity newUser = WarehouseUserEntity.builder()
        .name(userDto.name())
        .surname(userDto.surname())
        .email(userDto.email())
        .password(passwordEncoder.encode(userDto.password()))
        .position(Position.valueOf(userDto.position()))
        .build();
    userRepository.save(newUser);
  }

  public UserDto getUserByEmail(String email) {
    WarehouseUserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

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

    WarehouseUserEntity user = userRepository.findByEmail(userDto.email())
        .orElseThrow(() -> new RuntimeException("User not found with email: " + userDto.email()));
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