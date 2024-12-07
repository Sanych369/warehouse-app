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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
      new AntPathRequestMatcher("/api/v1/login/**")
      , new AntPathRequestMatcher("/actuator/**")
      , new AntPathRequestMatcher("/swagger-ui/**")
      , new AntPathRequestMatcher("/v3/api-docs/**")

  );


//    private static final RequestMatcher MANAGER_URLS = new OrRequestMatcher(
//            new AntPathRequestMatcher("")
//    );
//
//    private static final RequestMatcher STOREKEEPER_URLS = new OrRequestMatcher(
//            new AntPathRequestMatcher("")
//    );
//
//    private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
//            new AntPathRequestMatcher("")
//    );

  private final WareHouseUserDetailsService wareHouseUserDetailsService;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


    http
        .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())

//        .authorizeHttpRequests(authorize ->
//                authorize
////                    .requestMatchers(PUBLIC_URLS).permitAll()
//                                .requestMatchers(MANAGER_URLS).hasAnyAuthority(Role.MANAGER, Role.ADMIN)
//                                .requestMatchers(STOREKEEPER_URLS).hasAuthority(Role.STOREKEEPER)
//                                .requestMatchers(ADMIN_URLS).hasAuthority(Role.ADMIN)
//                    .anyRequest().authenticated()
//        )
//                .formLogin(withDefaults())
        .authenticationManager(authenticationManager());
    return http.build();

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