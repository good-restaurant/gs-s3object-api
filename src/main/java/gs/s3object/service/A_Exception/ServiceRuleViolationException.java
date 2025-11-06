package gs.s3object.service.A_Exception;

import gs.s3object.service.A_Exception.statusEnum.ServiceRuleViolationReason;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceRuleViolationException extends ResponseStatusException {

    private static final long serialVersionUID = 1L;
    private final Object id;
    private final Class<?> entityClass;

    // 기본 생성자: 상태 코드와 메시지만을 가져옴
    public ServiceRuleViolationException(ServiceRuleViolationReason reason, Object... args) {
        super(reason.getStatus(), reason.getMessage(args));
        this.id = extractId(args);
        this.entityClass = extractEntityClass(args);
    }

    // 원인 예외 포함하는 생성자
    public ServiceRuleViolationException(ServiceRuleViolationReason reason, Throwable cause, Object... args) {
        super(reason.getStatus(), reason.getMessage(args), cause);
        this.id = extractId(args);
        this.entityClass = extractEntityClass(args);
    }

    public ServiceRuleViolationException(Exception e, String s, Object id, Class<?> entityClass) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, s, e);
        this.id = id;
        this.entityClass = entityClass;
    }

    // Getter 메서드
    public Object getId() {
        return id;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    // 인자에서 ID 추출 (선택적)
    private Object extractId(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Long || arg instanceof Integer || arg instanceof String) {
                return arg;
            }
        }
        return null;
    }

    // 인자에서 클래스 추출 (선택적)
    private Class<?> extractEntityClass(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Class) {
                return (Class<?>) arg;
            }
        }
        return null;
    }
}

