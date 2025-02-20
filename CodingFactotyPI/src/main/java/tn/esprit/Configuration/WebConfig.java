package tn.esprit.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Autoriser toutes les routes
                        .allowedOrigins("http://localhost:4200") // Autoriser Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes autorisées
                        .allowedHeaders("*") // Tous les headers sont autorisés
                        .allowCredentials(true); // Permettre l'authentification
            }
        };
    }
}
