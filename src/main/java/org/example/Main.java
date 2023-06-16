package org.example;

import org.example.utility.Translator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    public static void test(String[] args){
        try {
            System.out.println(Translator.translateRuToEn("Привет мир"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}