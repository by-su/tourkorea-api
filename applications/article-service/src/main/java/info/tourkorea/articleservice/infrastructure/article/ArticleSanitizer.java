package info.tourkorea.articleservice.infrastructure.article;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;


public class ArticleSanitizer {

    public static String sanitize(String content) {
        String sanitizedText = Jsoup.clean(content, Safelist.none().addTags("br"));
        int maxWidth = Math.min(content.length(), 300);

        return StringUtils.abbreviate(sanitizedText, maxWidth);
    }
}
