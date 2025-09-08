package pl.osetoctet.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static pl.osetoctet.user.model.enums.Role.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.upload.url-pattern}")
    private String urlPattern;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(getWhiteListUrl()).permitAll()
                        .requestMatchers(GET, "/api/v1/tag-location").hasRole(ADMIN.name())
                        .requestMatchers(POST, "/api/v1/tag-location").hasRole(ADMIN.name())
                        .requestMatchers(PATCH, "/api/v1/tag-location/**").hasRole(ADMIN.name())
                        .requestMatchers(DELETE, "/api/v1/tag-location/**").hasRole(ADMIN.name())
                        .requestMatchers(GET, "/api/v1/user-summary").hasRole(ADMIN.name())
                        .requestMatchers(GET, "/api/v1/tag-access/public").anonymous()
                        .requestMatchers(GET, "/api/v1/tag-access/user").authenticated()
                        .requestMatchers(POST, "/api/v1/upload").hasRole(ADMIN.name())
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private String[] getWhiteListUrl() {
        return new String[]{
                "/api/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html",
                "/error",
                urlPattern
        };
    }

}
