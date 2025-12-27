package info.tourkorea.notificationservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://ec2-13-125-87-139.ap-northeast-2.compute.amazonaws.com:8000/notification-service", description = "notification-service-dev"),
                @Server(url = "http://localhost:8000/notification-service", description = "notification-service-local")
        }
)
public class SwaggerConfig {
        @Bean
        public OpenAPI openAPI() {
                SecurityScheme securityScheme = new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP).scheme("Bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER).name("Authorization");

                SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer");

                return new OpenAPI()
                        .components(new Components().addSecuritySchemes("Bearer", securityScheme))
                        .security(List.of(securityRequirement));
        }
}

