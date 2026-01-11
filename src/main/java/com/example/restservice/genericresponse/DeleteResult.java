package com.example.restservice.genericresponse;

public record DeleteResult(boolean success, String message, Long id) {
}
