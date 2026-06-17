package nl.brianvermeer.demo.wheeliegoodrentals.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/signup").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/terms/**").permitAll()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/cars/edit/**").hasRole("ADMIN")
                        .requestMatchers("/cars").permitAll()
                        .requestMatchers("/bookings").authenticated()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers( "/chatold").permitAll()
                        .requestMatchers("/chat", "/chat/**", "/ws/**", "/topic/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/profile").authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403") // Redirect to custom 403 page
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler())
                )
                .logout(LogoutConfigurer::permitAll
                );




        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            request.getSession().setAttribute("message", "Login successful!");
            response.sendRedirect("/home");
        };
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            String username = request.getParameter("username");
            logger.warn("Login failed for user: {}", username);
            logger.error("Exception: {}", exception.getMessage());
            request.getSession().setAttribute("message", "Login failed. Please try again.");
            response.sendRedirect("/login");
        };
    }
}