package info.tourkorea.articleservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "info.tourkorea.componentjpa",
        "info.tourkorea.componentsecurity",
        "info.tourkorea.exception",
        "info.tourkorea.openfeign",
        "info.tourkorea.componentweb"
})
public class ComponentConfig {
}
