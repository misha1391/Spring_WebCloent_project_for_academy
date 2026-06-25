package com.example.dashboard.client.dto;

public record IssResponse (
    String message,
    IssPosition issPosition
) {
    public record IssPosition(String latitude, String longitude) {}
}
