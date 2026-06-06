package org.example.chatapplication.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/h2-console/**")
                    .csrfTokenRepository(
                        CookieCsrfTokenRepository.withHttpOnlyFalse()
                    )
                )

                .cors(cors -> cors.configurationSource(cors()))

                .formLogin(login -> login
                        .loginProcessingUrl("/api/v1/auth/login")
                        .successHandler((req, res, auth) ->
                                res.setStatus(200))                 // don't redirect, return 200
                        .failureHandler((req, res, ex) ->
                                res.sendError(401, "Bad credentials"))
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler((req, res, auth) ->
                                res.setStatus(200))
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (req, res, authException) ->
                                        res.sendError(
                                                HttpServletResponse.SC_UNAUTHORIZED
                                        )
                        )
                )

                // for H2-CONSOLE WEB CONNECTION
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) // Allow frames from the same origin
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/csrf",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource cors() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5500"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", config);

        return src;
    }

}
