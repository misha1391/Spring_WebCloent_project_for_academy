package com.example.dashboard.scheduler;

import com.example.dashboard.domain.DashboardSnapshot;
import com.example.dashboard.service.DashboardAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class DashboardScheduler {
    private final DashboardAggregatorService aggregatorService;

    @Scheduled(fixedRateString = "300000")
    public void refresh() {
        aggregatorService.refreshSnapshot();
    }

}
