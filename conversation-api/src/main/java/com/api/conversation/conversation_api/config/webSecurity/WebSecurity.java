package com.api.conversation.conversation_api.config.webSecurity;

import com.api.conversation.conversation_api.config.cors.CorsConfiguration;
import com.api.conversation.conversation_api.config.webSecurity.jwt.JWTAuthenticationFilter;
import com.api.conversation.conversation_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurity {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurity(
            JWTAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    private static final String[] AUTH_WHITE_LIST = {
            "/login",
            "/conversation/connect/**",
            "/user/signup"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(httpRequests ->
                        httpRequests.requestMatchers(AUTH_WHITE_LIST).permitAll()
                                .anyRequest().authenticated())
                .csrf(csrf->csrf.disable())
                .cors(cors -> cors // Configuração de CORS declarativa
                        .configurationSource(request -> {
                            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                            corsConfig.addAllowedOrigin("http://localhost:3000");
                            corsConfig.addAllowedMethod("*");
                            corsConfig.addAllowedHeader("*");
                            corsConfig.setAllowCredentials(true);
                            return corsConfig;
                        })
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManag->
                        sessionManag.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }

}
