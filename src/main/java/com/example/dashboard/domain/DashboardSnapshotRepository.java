package com.example.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DashboardSnapshotRepository extends JpaRepository<DashboardSnapshot, Long> {
    Optional<DashboardSnapshot> findTopByOrderByGetAt();
}
