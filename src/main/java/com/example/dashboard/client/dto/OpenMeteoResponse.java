package com.example.dashboard.client.dto;

public record OpenMeteoResponse (
        double latitude,
        double longitude,
        CurrentWeather currentWeather
) {
    public record CurrentWeather (
            double temp,
            double windspeed,
            int weathercode
    ) {}

}
