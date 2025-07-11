package com.example.pincodeapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PincodeapiApplication {

    static {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env") // ensure this file is at the root of your project
                .ignoreIfMissing()
                .load();

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("GOOGLE_API_KEY", dotenv.get("GOOGLE_API_KEY"));
    }


    public static void main(String[] args) {
        SpringApplication.run(PincodeapiApplication.class, args);
    }

}
