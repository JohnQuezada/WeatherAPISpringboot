package com.careerdevs.weatherapi.controllers;


import com.careerdevs.weatherapi.models.CurrentWeather;
import com.careerdevs.weatherapi.models.CurrentWeatherReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/current")
public class CurrentWeatherController {

    @Autowired
    private Environment env;


    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @GetMapping("/city/{cityName}")
    public ResponseEntity<?> getCurrentWeatherByCityPV(RestTemplate restTemplate, @PathVariable String cityName) {

        try {
            String units = "imperial";
            String apiKey = env.getProperty("OW_API_KEY");
            String queryString = "?q=" + cityName + "&appid=" + apiKey + "&units=" + units;
            String openWeatherURL = BASE_URL + queryString;

            CurrentWeather openWeatherResponse = restTemplate.getForObject(openWeatherURL, CurrentWeather.class);

            assert openWeatherResponse != null;
//            System.out.println("City: " + openWeatherResponse.getName());
//            System.out.println("Temp: " + openWeatherResponse.getMain());
//            System.out.println("Description: " + openWeatherResponse.getWeather()[0].getDescription());
            CurrentWeatherReport report = new CurrentWeatherReport(
                    openWeatherResponse.getName(),
                    openWeatherResponse.getCoord(),
                    openWeatherResponse.getMain(),
                    openWeatherResponse.getWeather()[0],
                    units
            );

            System.out.println(report);

            return ResponseEntity.ok(report.toString());

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("City Not Found: " + cityName);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }



}
