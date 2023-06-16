package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.example.commands.Cities;
import org.example.commands.States;
import org.example.models.WeatherModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Service
public class WeatherService {

    public WeatherService() {
    }
    @PostConstruct
    private void fillCities(){
        for(Cities city : Cities.values()){
            cities.add(city.name().toLowerCase());
        }
    }
    private final HashSet<String> cities = new HashSet<>();

    public WeatherService(String city) {
        this.city = city;
    }

    ;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static String DEFAULT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=Moscow&mode=json&appid=2713fc84859cdbf337e5d95b5f08980e";
    @Getter
    private String city = "Moscow";


    public boolean checkCity(String city) {
        return cities.contains(city);
    }

    public boolean setCity(String city) {
        System.out.println(cities);

        if (cities.contains(city)) {
            this.city = city;
            return true;
        } else return false;
    }

    private final static String WEATHER_URL_PREFIX = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final static String WEATHER_URL_SUFFIX = "&mode=json&appid=2713fc84859cdbf337e5d95b5f08980e";

    public WeatherModel getWeatherInCity(String city) {
        city = city.toLowerCase();
        if (!cities.contains(city))return null;


        String s = city.replaceFirst(city.charAt(0)+"",""+ Character.toUpperCase(city.charAt(0)));
        System.out.println(s);
        ResponseEntity<String> response = restTemplate.getForEntity(WEATHER_URL_PREFIX + s + WEATHER_URL_SUFFIX, String.class);
        try {
            JsonNode node = objectMapper.readTree(response.getBody());
            return new WeatherModel(BigDecimal.valueOf(node.path("main").path("temp").asDouble()),
                    BigDecimal.valueOf(node.path("main").path("feels_like").asDouble()),
                    BigDecimal.valueOf(node.path("main").path("pressure").asDouble()));
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public WeatherModel getWeather() {
        ResponseEntity<String> response = restTemplate.getForEntity(WEATHER_URL_PREFIX + getCity() + WEATHER_URL_SUFFIX, String.class);
        try {
            JsonNode node = objectMapper.readTree(response.getBody());
            return new WeatherModel(BigDecimal.valueOf(node.path("main").path("temp").asDouble()),
                    BigDecimal.valueOf(node.path("main").path("feels_like").asDouble()),
                    BigDecimal.valueOf(node.path("main").path("pressure").asDouble()));
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
