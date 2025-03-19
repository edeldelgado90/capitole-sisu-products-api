package com.capitole.sisu.products.application.config;

import com.capitole.sisu.products.domain.error.ErrorResponse;
import com.capitole.sisu.products.domain.price.PriceNotFoundException;
import com.capitole.sisu.products.domain.price.PriceOverlappingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void handlePriceNotFoundExceptionReturns404() {
        PriceNotFoundException exception = new PriceNotFoundException("Price not found.");

        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handlePriceNotFoundException(exception);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ErrorResponse errorResponse = responseEntity.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(errorResponse.getError()).isEqualTo("Price not found.");
        assertThat(errorResponse.getTimestamp()).isBefore(LocalDateTime.now());
        assertThat(errorResponse.getRequestId()).isNotNull();
    }

    @Test
    public void handlePriceOverlappingExceptionReturns409() {
        PriceOverlappingException exception = new PriceOverlappingException("Price overlaps.");

        ResponseEntity<ErrorResponse> responseEntity =
                globalExceptionHandler.handlePriceOverlappingException(exception);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        ErrorResponse errorResponse = responseEntity.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(errorResponse.getError()).isEqualTo("Price overlaps.");
        assertThat(errorResponse.getTimestamp()).isBefore(LocalDateTime.now());
        assertThat(errorResponse.getRequestId()).isNotNull();
    }

    @Test
    public void handleMethodArgumentNotValidExceptionReturns400() {
        String errorMessage = "Validation error on argument!";
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }
}
