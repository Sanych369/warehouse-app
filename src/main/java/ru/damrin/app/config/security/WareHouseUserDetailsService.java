package ru.damrin.app.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class WareHouseUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws WarehouseAppException {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Пользователь с email %s не найден. Проверьте правильность ввода", email)));
  }
}
