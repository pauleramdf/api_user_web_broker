package user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class Application {
    @Value("${config.api-stocks-url}")
    private String apiStocksUrl;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return  builder
                .baseUrl(apiStocksUrl)
                .build();
    }
}