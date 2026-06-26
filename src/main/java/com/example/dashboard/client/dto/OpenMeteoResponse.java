package com.example.dashboard.client.dto;

public record OpenMeteoResponse (
        Double latitude,
        Double longitude,
        CurrentWeather currentWeather
) {
    public record CurrentWeather (
            Double temp,
            Double windspeed,
            Integer weathercode
    ) {}

}
