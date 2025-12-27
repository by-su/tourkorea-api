package info.tourkorea.fileservice.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "info.tourkorea.exception",
        "info.tourkorea.openfeign",
        "info.tourkorea.componentsecurity"
})
public class ComponentConfig {


}
