package com.example.dashboard.web;

import com.example.dashboard.domain.DashboardSnapshot;
import com.example.dashboard.domain.DashboardSnapshotRepository;
import com.example.dashboard.service.DashboardAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardAggregatorService aggregatorService;
    private final DashboardSnapshotRepository repository;

    @GetMapping("/latest")
    public ResponseEntity<DashboardSnapshot> getLatest() {
        return repository.findTopByOrderByGetAt().map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/refresh")
    public DashboardSnapshot refreshNow() {
        return aggregatorService.refreshSnapshot();
    }
}
