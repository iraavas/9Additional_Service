package ru.hpclab.hl.module1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(120000); // таймаут установки соединения: 60 секунд
        factory.setReadTimeout(120000);    // таймаут ожидания ответа: 60 секунд

        return new RestTemplate(factory);
    }
}
