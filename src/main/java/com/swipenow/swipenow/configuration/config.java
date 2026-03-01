package com.swipenow.swipenow.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class config {  // class name capitalized by convention

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // enable CORS
                .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/delete-account").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                 "/Register-User",
                                "/view_friends/**",
                                "/All-User",
                                "/update",
                                "/Login-User",
                                "/profile/**",
                                "/add-friends",
                                "/friends/check",
                                "/profile-pic",
                                "/Get-profile-pic",
                                "/send",
                                "/verify",
                                "/update-password",
                                "/update-username",
                                "/update-email",
                                "/swipe",
                                "/fcm-token",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs/swagger-config",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/upload",
                                "/urls/**",
                                "/allMEMEs",
                                "/daily-memes"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
               .headers(headers -> headers
                       .contentSecurityPolicy(csp -> csp
                               .policyDirectives(
                                       "default-src 'self'; " +
                                               "script-src 'self' 'unsafe-inline'; " +
                                               "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                               "font-src 'self' https://fonts.gstatic.com; " +
                                               "img-src 'self' data: https:; " +
                                               "connect-src 'self' http://localhost:8081 http://localhost:4321 http://192.168.29.171:8081"

                               )
                       )
               )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration config = new CorsConfiguration();
       config.setAllowedOrigins(List.of("http://127.0.0.1:5501", "http://localhost:5501","http://192.168.29.171:5501","https://frontend-rust-iota-qby7aguy8j.vercel.app","https://swipenow.swipenowin.workers.dev","https://swipenow.in"));
       config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
       config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
       config.setAllowCredentials(true); // allows cookies/sessions

       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", config);
       return source;
   }



//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();
//         config.setAllowedOrigins(List.of(
//                 "http://127.0.0.1:5501",
//                 "http://localhost:5501",
//                 "http://192.168.29.171:5501",
//                 "https://frontend-rust-iota-qby7aguy8j.vercel.app",
//                 "https://swipenow.swipenowin.workers.dev"
//         ));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         config.setAllowedHeaders(List.of("*")); // allow all headers
//         config.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);
//         return source;
//     }

}
