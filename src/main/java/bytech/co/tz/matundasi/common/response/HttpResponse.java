package bytech.co.tz.matundasi.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "message", "statusCode", "status", "data", "timestamp"})
public class HttpResponse<T> {
    private final boolean success;
    private final String message;
    private final int statusCode;
    private final String status;
    private final T data;
    private final Instant timestamp = Instant.now();

    public HttpResponse(boolean success, String message, HttpStatus status) {
        this(success, message, status, null);
    }

    public HttpResponse(boolean success, String message, HttpStatus status, T data) {
        this.success = success;
        this.message = message;
        this.statusCode = status.value();
        this.status = status.name();
        this.data = data;
    }
}
