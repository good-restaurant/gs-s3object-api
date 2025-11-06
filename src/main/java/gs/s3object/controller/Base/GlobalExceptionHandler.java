package gs.s3object.controller.Base;


import gs.s3object.service.A_Exception.ApiServiceException;
import gs.s3object.service.A_Exception.ServiceRuleViolationException;
import gs.s3object.service.A_Exception.UpperEntityRemoveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Transactional 한 메소드의 에러 핸들링은 이쪽으로, 그 외에는 서비스에서 자체적으로 Exception 을 다룰 수 있습니다.
 * 컨트롤러에 try-catch 로 exception 설정하지 말고 여기에 넣어주세요.
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("DataIntegrityViolationException 발생: {}", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "DATA_INTEGRITY_VIOLATION",
                "Failed to process entity due to data integrity issues. 데이터의 무결성, 중복, 연관관계, 제약조건 등과 관련된 오류입니다.",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)

                .body(errorResponse);
    }

    @ExceptionHandler(UpperEntityRemoveException.class)
    public ResponseEntity<ErrorResponse> handleUpperEntityRemoveException(UpperEntityRemoveException ex) {

        // 관련된 객체가 삭제되어야 함을 의미하는 bad request 메시지가 보통 표시됩니다.
        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.BAD_REQUEST.value(),
                "Not Acceptable operation in System Logic",
                "시스템 로직에 맞지 않는 작업이 수행되었습니다. :: " + ex.getReason(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        logger.error("DataAccessException 발생: {}", ex);

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Database working Error",
                "데이터베이스 작업 수행 과정에서 오류가 발생했습니다. ",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
//        logger.error("AuthenticationException 발생: {}", ex, ex.getMessage());
//
//        ErrorResponse errorResponse = new ErrorResponse (
//                HttpStatus.FORBIDDEN.value(),
//                "NOT AUTHORIZED token or user",
//                "적절하지 않은 권한에 대한 문제입니다. ",
//                ex.getMessage()
//        );
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(errorResponse);
//    }

    @ExceptionHandler(ServiceRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleServiceRuleViolationException(ServiceRuleViolationException ex) {
        // Construct the response message based on the exception's context
        String message = ex.getEntityClass() != null && ex.getId() != null
                ? ex.getEntityClass().getSimpleName() + ": 대상 엔티티의 규칙 위반이 발생했습니다. (ID: " + ex.getId() + ")"
                : ex.getReason();

        // 에러 응답 처리
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatusCode().value(),                // ServiceRuleViolationException 은 자체 에러코드 보유
                "SERVICE_RULE_VIOLATION",                 // 규칙 위반
                message,                                  // 간단한 메시지
                ex.getCause() != null ? ex.getCause().toString() : null // 상세 메시지
        );

        logger.error("ServiceRuleViolationException 발생: {}", message, ex);

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(ApiServiceException.class)
    public ResponseEntity<ErrorResponse> handleApiServiceException(ApiServiceException ex) {

        logger.error("ApiServiceException 발생: {}", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getDetails()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unhandled exception occurred: {}", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "UNHANDLED_EXCEPTION",
                "예외 처리가 이루어지지 않은 일반적인 예외 상황입니다.",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
