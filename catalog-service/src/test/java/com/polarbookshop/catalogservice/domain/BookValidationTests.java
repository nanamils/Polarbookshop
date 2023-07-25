package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        Book book = Book.of("1234567890", "Title", "Author", 9.90, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assert violations.isEmpty();
    }

    @Test
    void whenIsbnNotDefinedButIncorrectThenValidationFails() {
        Book book = Book.of("", "Title", "Author", 9.90, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        assertThat(constraintViolationMessages).containsOnlyOnceElementsOf(
                List.of("ISBNは必須です", "ISBNの形式が正しくありません")
        );
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        Book book = Book.of("a234567890", "Title", "Author", 9.90, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        violations.forEach(violation -> assertThat(violation.getMessage()).isEqualTo("ISBNの形式が正しくありません"));
    }

    @Test
    void whenTitleIsNotDefinedThenValidationFails() {
        var book = Book.of("1234567890", "", "Author", 9.90, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("タイトルは必須です");
    }

    @Test
    void whenAuthorIsNotDefinedThenValidationFails() {
        var book = Book.of("1234567890", "Title", "", 9.90, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("著者は必須です");
    }

    @Test
    void whenPriceIsNotDefinedThenValidationFails() {
        var book = Book.of("1234567890", "Title", "Author", null, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("価格は必須です");
    }

    @Test
    void whenPriceDefinedButZeroThenValidationFails() {
        var book = Book.of("1234567890", "Title", "Author", 0.0, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("価格は正の数でなければなりません");
    }

    @Test
    void whenPriceDefinedButNegativeThenValidationFails() {
        var book = Book.of("1234567890", "Title", "Author", -9.90, "Publisher");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("価格は正の数でなければなりません");
    }
}
