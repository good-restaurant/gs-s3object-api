package gs.s3object.service.A_Exception.statusEnum;

import org.springframework.http.HttpStatus;
public enum ServiceRuleViolationReason {

    DUPLICATE_ENTRY(HttpStatus.CONFLICT, "해당 데이터가 이미 존재합니다."),
    CONFLICT(HttpStatus.CONFLICT, "요청에 충돌이 발생하는 데이터가 있습니다."),
    NOT_MATCHED(HttpStatus.BAD_REQUEST, "ID가 일치하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다."),
    NOT_CHANGEABLE(HttpStatus.UNPROCESSABLE_ENTITY, "수정 불가능한 항목입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    UPPER_ENTITY_DELETE_CONFLICT(HttpStatus.CONFLICT, "상위 엔티티 (ID: %s)를 삭제할 수 없습니다. 연관된 하위 항목이 먼저 삭제되어야 합니다."),
    GENERIC_RULE_VIOLATION(HttpStatus.BAD_REQUEST, "%s: 규칙 위반이 발생했습니다. (ID: %s)"),
    USED_IS_NOT_ACCEPTED(HttpStatus.CONFLICT, "사용중인 대상으로는 불가능합니다. (ID: %s) "),
    CANNOT_BE_DELETED(HttpStatus.CONFLICT, "특정 항목은 삭제 불가능합니다."),
    DEPENDENCY_VIOLATION(HttpStatus.CONFLICT, "연관된 데이터로 인해 작업을 수행할 수 없습니다. (ID: %s)"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청이 잘못되었습니다. (사유: %s)"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),
    MISMATCH(HttpStatus.UNAUTHORIZED, "요청 데이터와 현재 데이터가 맞지 않습니다. ");


    private final HttpStatus status;
    private final String message;

    ServiceRuleViolationReason(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage(Object... args) {
        // 메시지에 포맷팅을 적용하여 동적 데이터 포함, args가 없을 경우 기본 메시지만 반환
        return String.format(message, args);
    }
}

