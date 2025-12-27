package info.tourkorea.userservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "info.tourkorea.componentemail",
        "info.tourkorea.componentsecurity",
        "info.tourkorea.exception",
        "info.tourkorea.componentjpa",
        "info.tourkorea.openfeign"
})
public class ComponentConfig {
}
