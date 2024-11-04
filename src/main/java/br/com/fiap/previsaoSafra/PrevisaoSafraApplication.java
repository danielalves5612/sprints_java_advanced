package br.com.fiap.previsaoSafra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/")
@EnableScheduling 
public class PrevisaoSafraApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrevisaoSafraApplication.class, args);
    }

}
