package ru.damrin.app.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.damrin.app.model.LoginRequest;

@Service
@RequiredArgsConstructor
public class LoginService {

  private final AuthenticationManager authenticationManager;

  public String authenticate(LoginRequest loginRequest) {
    final var authenticationToken =
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

    final var authentication = authenticationManager.authenticate(authenticationToken);

    if (authentication.isAuthenticated()) {
      return "Authenticated successfully!";
    }

    return "Authentication failed.";
  }
}