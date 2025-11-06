package gs.s3object.service.A_Exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ApiServiceException extends RuntimeException {

    private final HttpStatusCode status;
    private final String errorCode;
    private final String message;
    private final Object details;

    public ApiServiceException(HttpStatusCode status, String errorCode, String message, Object details) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }


}

