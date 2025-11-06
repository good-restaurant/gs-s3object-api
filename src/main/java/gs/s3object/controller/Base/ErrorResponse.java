package gs.s3object.controller.Base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String errorCode; // A code identifying the error type
    private String message;   // User-friendly error message
    private Object details;   // Additional details (optional, e.g., entity type, ID)

}
