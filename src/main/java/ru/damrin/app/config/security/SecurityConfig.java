package ru.damrin.app.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.damrin.app.common.enums.Position;
import ru.damrin.app.common.handler.CustomAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
      new AntPathRequestMatcher("/actuator/"),
      new AntPathRequestMatcher("/swagger-ui/"),
      new AntPathRequestMatcher("/v3/api-docs/"),
      new AntPathRequestMatcher("/css/**"));

  private static final RequestMatcher ALL_URLS = new OrRequestMatcher(
      new AntPathRequestMatcher("/profile/**"),
      new AntPathRequestMatcher("/help/**"));


  private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
      new AntPathRequestMatcher("/admin/**"));

  private static final RequestMatcher STOREKEEPER_URLS = new OrRequestMatcher(
      new AntPathRequestMatcher("/storekeeper/**"));

  private static final RequestMatcher MANAGER_URL = new OrRequestMatcher(
      new AntPathRequestMatcher("/manager/**"));


  private final WareHouseUserDetailsService wareHouseUserDetailsService;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(ALL_URLS).authenticated()
                .requestMatchers(ADMIN_URLS).hasAuthority(Position.ADMIN.name())
                .requestMatchers(STOREKEEPER_URLS).hasAnyAuthority(Position.ADMIN.name(), Position.STOREKEEPER.name())
                .requestMatchers(MANAGER_URL).hasAnyAuthority(Position.ADMIN.name(), Position.MANAGER.name())
                .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/", true)
            .failureHandler(failureHandler())
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .permitAll()
        )
        .authenticationManager(authenticationManager());
    return http.build();
  }

  @Bean
  public CustomAuthenticationFailureHandler failureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(wareHouseUserDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());

    ProviderManager providerManager = new ProviderManager(authenticationProvider);
    providerManager.setEraseCredentialsAfterAuthentication(false);

    return providerManager;
  }
}