package ru.damrin.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Аудит JPA конфиг для поддержки аннотаций при персисте.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
