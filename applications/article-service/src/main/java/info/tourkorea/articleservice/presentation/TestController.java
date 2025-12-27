package info.tourkorea.articleservice.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MessageSourceAccessor messageSourceAccessor;

    @GetMapping("/test")
    public String test() {
        String message = messageSourceAccessor.getMessage("common.test");
        System.out.println(message);
        return message;
    }
}
