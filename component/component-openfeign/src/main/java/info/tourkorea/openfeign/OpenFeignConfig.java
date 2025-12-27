package info.tourkorea.openfeign;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.okhttp.OkHttpClient;
import info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import request.UserContext;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients(basePackages = "${base-package.openfeign}")
public class OpenFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Optional<UserContext> userContext = SecurityContextHolderUtil.getUserContext();
            if (userContext.isPresent()) {
                var token = userContext.get().getToken();
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignFormatterRegistrar dateTimeFormatterRegistrar() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }

    @Bean
    public Request.Options options() {
        final int connectTimeoutMillis = 1500;
        final int readTimeoutMillis = 3000;
        return new Request.Options(connectTimeoutMillis, TimeUnit.MILLISECONDS, readTimeoutMillis, TimeUnit.MILLISECONDS, false);
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}

