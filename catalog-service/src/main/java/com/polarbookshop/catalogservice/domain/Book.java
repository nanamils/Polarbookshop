package com.polarbookshop.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;

public record Book(
        @Id
        Long id,

        @NotBlank(message = "ISBNは必須です")
        //@Pattern(regexp = "^(97([89]))?\\d{9}(\\d|X)$")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "ISBNの形式が正しくありません")
        String isbn,

        @NotBlank(message = "タイトルは必須です")
        String title,

        @NotBlank(message = "著者は必須です")
        String author,

        @NotNull(message = "価格は必須です")
        @Positive(message = "価格は正の数でなければなりません")
        Double price,

        String publisher,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @Version
        int version
) {
        public static Book of(
                String isbn, String title, String author, Double price, String publisher
        ) {
                return new Book(null, isbn, title, author, price, publisher, null, null, 0);
        }
}
