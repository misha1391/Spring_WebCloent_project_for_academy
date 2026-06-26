package com.example.dashboard.service;

import com.example.dashboard.client.ExternalDataService;
import com.example.dashboard.client.dto.GitHubRepoResponse;
import com.example.dashboard.client.dto.IssResponse;
import com.example.dashboard.client.dto.OpenMeteoResponse;
import com.example.dashboard.domain.DashboardSnapshot;
import com.example.dashboard.domain.DashboardSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardAggregatorService {
    private final ExternalDataService externalDataService;
    private final DashboardSnapshotRepository dashboardSnapshotRepository;

    public DashboardSnapshot refreshSnapshot() {
        CompletableFuture<Optional<OpenMeteoResponse>> weatherFuture =
                CompletableFuture.supplyAsync(externalDataService::fetchWeather);
        CompletableFuture<Optional<IssResponse>> issFuture =
                CompletableFuture.supplyAsync(externalDataService::fetchIssPosition);
        CompletableFuture<Optional<GitHubRepoResponse>> githubFuture =
                CompletableFuture.supplyAsync(externalDataService::fetchGithubStats);
        CompletableFuture.allOf(weatherFuture, issFuture, githubFuture).join();

        DashboardSnapshot snapshot = new DashboardSnapshot();

        weatherFuture.join().ifPresent(w -> {
            snapshot.setTemperatureMoscow(w.currentWeather().temp());
            snapshot.setWindSpeedMoscow(w.currentWeather().windspeed());
        });
        issFuture.join().ifPresent(i -> {
            snapshot.setIssLatitude(i.issPosition().latitude());
            snapshot.setIssLongitude(i.issPosition().longitude());
        });
        githubFuture.join().ifPresent(g -> {
            snapshot.setGitHubStars(g.star_count());
        });
        snapshot.setGottenAt(LocalDateTime.now());
        DashboardSnapshot saved = dashboardSnapshotRepository.save(snapshot);

        log.info("Снапшот #{}, сохранен, погода - {}, МКС - ({}, {}), звезды - {}",
                saved.getId(), saved.getTemperatureMoscow(), saved.getIssLatitude(), saved.getIssLongitude(), saved.getGitHubStars());
        return saved;
    }
}
