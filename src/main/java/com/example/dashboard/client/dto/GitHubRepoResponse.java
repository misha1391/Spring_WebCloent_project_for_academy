package com.example.dashboard.client.dto;

public record GitHubRepoResponse (
        String full_name,
        int star_count,
        int forks_count,
) {
}
