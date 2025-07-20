package pl.osetoctet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.osetoctet.common.exception.ErrorResponse;
import pl.osetoctet.common.utils.OffsetDateTimeUtils;
import pl.osetoctet.user.UserEntityService;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Value("${application.frontend.url}")
    private String frontendUrl;

    private final MessageSource messageSource;
    private final UserEntityService userEntityService;

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@NonNull HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Accept-Language"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Access-Control-Allow-Origin"));
        corsConfiguration.setAllowedOriginPatterns(List.of(frontendUrl));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userEntityService.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("auth.user.notFound"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> writeErrorResponse(
                response,
                HttpStatus.UNAUTHORIZED,
                messageSource.getMessage("auth.unauthorized", null, request.getLocale()),
                request.getRequestURI()
        );
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> writeErrorResponse(
                response,
                HttpStatus.FORBIDDEN,
                messageSource.getMessage("auth.forbidden", null, request.getLocale()),
                request.getRequestURI()
        );
    }

    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message, String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());

        ErrorResponse responseBody = new ErrorResponse(
                OffsetDateTimeUtils.getCurrentUtcTimestamp(),
                status.value(),
                message,
                path
        );

        mapper.writeValue(response.getWriter(), responseBody);
    }

}
