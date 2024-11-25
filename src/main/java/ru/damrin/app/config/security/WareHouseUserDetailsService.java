package ru.damrin.app.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.damrin.app.db.repository.WarehouseUserRepository;

@Service
@RequiredArgsConstructor
public class WareHouseUserDetailsService implements UserDetailsService {

  private final WarehouseUserRepository warehouseUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return warehouseUserRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
  }
}
