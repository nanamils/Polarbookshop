package com.polarbookshop.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Book(
        @NotBlank
        @Pattern(regexp = "^(97([89]))?\\d{9}(\\d|X)$")
        String isbn,
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotNull
        @Positive
        Double price
) { }
