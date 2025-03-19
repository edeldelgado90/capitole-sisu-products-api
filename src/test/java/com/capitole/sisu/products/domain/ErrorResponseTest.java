package com.capitole.sisu.products.domain;

import com.capitole.sisu.products.domain.error.ErrorResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorResponseTest {

    @Test
    public void testErrorResponseCreation() {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String requestId = "abcd-1234";

        ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, requestId);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getTimestamp()).isEqualTo(timestamp);
        assertThat(errorResponse.getStatus()).isEqualTo(status);
        assertThat(errorResponse.getError()).isEqualTo(error);
        assertThat(errorResponse.getRequestId()).isEqualTo(requestId);
    }

    @Test
    public void testErrorResponseDefaultConstructor() {
        ErrorResponse errorResponse = new ErrorResponse(null, 0, null, null);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getTimestamp()).isNull();
        assertThat(errorResponse.getStatus()).isEqualTo(0);
        assertThat(errorResponse.getError()).isNull();
        assertThat(errorResponse.getRequestId()).isNull();
    }

    @Test
    public void testErrorResponseToString() {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String requestId = "abcd-1234";

        ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, requestId);
        String expectedToString = "ErrorResponse(timestamp=" + timestamp + ", status=" + status +
                ", error=" + error + ", requestId=" + requestId + ")";

        assertThat(errorResponse.toString()).isEqualTo(expectedToString);
    }

    @Test
    public void testErrorResponseEqualsAndHashCode() {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String requestId = "abcd-1234";

        ErrorResponse errorResponse1 = new ErrorResponse(timestamp, status, error, requestId);
        ErrorResponse errorResponse2 = new ErrorResponse(timestamp, status, error, requestId);

        assertThat(errorResponse1).isEqualTo(errorResponse2);
        assertThat(errorResponse1.hashCode()).isEqualTo(errorResponse2.hashCode());
    }

    @Test
    public void testErrorResponseNotEqual() {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String requestId = "abcd-1234";

        ErrorResponse errorResponse1 = new ErrorResponse(timestamp, status, error, requestId);
        ErrorResponse errorResponse2 = new ErrorResponse(LocalDateTime.now(), 500, "Internal Server Error", "xyz-5678");

        assertThat(errorResponse1).isNotEqualTo(errorResponse2);
    }
}
