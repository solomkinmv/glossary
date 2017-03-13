package io.github.solomkinmv.glossary.service.config;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FlickrConfig {
    private static final String FLICKR_SERVER = "www.flickr.com";

    @Value("${flickr.key}")
    private String apiKey;

    @Value("${flickr.secret}")
    private String sharedSecret;

    @Bean
    Flickr flickr() {
        REST rest = new REST();
        rest.setHost(FLICKR_SERVER);
        return new Flickr(apiKey, sharedSecret, rest);
    }
}
