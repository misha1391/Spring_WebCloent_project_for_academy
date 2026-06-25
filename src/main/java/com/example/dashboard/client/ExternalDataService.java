package com.example.dashboard.client;

import com.example.dashboard.client.dto.GitHubRepoResponse;
import com.example.dashboard.client.dto.IssResponse;
import com.example.dashboard.client.dto.OpenMeteoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Optional;

/**
 * Слой общения с внешними API. Каждый метод сам ловит свои ошибки
 * и возвращает Optional.empty() при сбое — отказ одного источника
 * не должен ронять остальную агрегацию (см. DashboardAggregatorService).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalDataService {

    private final WebClient webClient;

    @Value("${dashboard.sources.weather-url}")
    private String weatherUrl;

    @Value("${dashboard.sources.iss-url}")
    private String issUrl;

    @Value("${dashboard.sources.github-url}")
    private String githubUrl;

    public Optional<OpenMeteoResponse> fetchWeather() {
        return fetchSafely(weatherUrl, OpenMeteoResponse.class, "Open-Meteo");
    }

    public Optional<IssResponse> fetchIssPosition() {
        return fetchSafely(issUrl, IssResponse.class, "Open Notify");
    }

    public Optional<GitHubRepoResponse> fetchGithubStats() {
        return fetchSafely(githubUrl, GitHubRepoResponse.class, "GitHub");
    }

    private <T> Optional<T> fetchSafely(String url, Class<T> type, String sourceName) {
        try {
            T result = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(type)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.warn("{} недоступен: {}", sourceName, e.getMessage());
            return Optional.empty();
        }
    }
}
