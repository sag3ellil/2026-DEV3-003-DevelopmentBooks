package com.store.books.dtos;

public record ErrorResponseDTO( int status, String error, String message, String path) {}
