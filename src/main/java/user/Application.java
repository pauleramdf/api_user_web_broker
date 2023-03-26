package user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public WebClient webClient(WebClient.Builder builder){
        // quando for no docker "http://apistocks:8083"
        //quando nao for no docker "http://localhost:8083"
        return  builder
                .baseUrl("http://localhost:8083")
                .build();
    }

//    @Configuration
//    @EnableWebSecurity
//    class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.cors().and()
//                    .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
//                    .oauth2ResourceServer().jwt();
//        }
//    }
}