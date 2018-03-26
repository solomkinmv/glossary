package io.github.solomkinmv.glossary.image.config;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FlickrConfig {
    private static final String FLICKR_SERVER = "www.flickr.com";

    @Bean
    Flickr flickr(@Value("${flickr.key}") String apiKey,
                  @Value("${flickr.secret}") String sharedSecret) {
        REST rest = new REST();
        rest.setHost(FLICKR_SERVER);
        return new Flickr(apiKey, sharedSecret, rest);
    }
}
