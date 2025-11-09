package gs.s3object.service.A_Exception;


import gs.s3object.service.A_Exception.statusEnum.ServiceRuleViolationReason;

public class UpperEntityRemoveException extends ServiceRuleViolationException {

    private static final long serialVersionUID = 1L;

    // ID를 포함한 예외 생성자
    public UpperEntityRemoveException(Object id) {
        super(ServiceRuleViolationReason.NOT_FOUND, id);
    }

    // ID와 상세 정보를 포함한 예외 생성자
    public UpperEntityRemoveException(Object id, Throwable cause) {
        super(ServiceRuleViolationReason.UPPER_ENTITY_DELETE_CONFLICT,cause,id);
    }

    // ID와 클래스 정보를 포함한 예외 생성자
    public UpperEntityRemoveException(Class<?> entityClass, Object id) {
        super(ServiceRuleViolationReason.UPPER_ENTITY_DELETE_CONFLICT, entityClass, id);
    }

    // 원인 예외와 함께 ID와 클래스 정보를 포함한 예외 생성자
    public UpperEntityRemoveException(Class<?> entityClass, Object id, Throwable cause) {
        super(ServiceRuleViolationReason.UPPER_ENTITY_DELETE_CONFLICT, cause, entityClass, id);
    }

    // Integer ID와 메시지를 바탕으로 한 위반
    public UpperEntityRemoveException(Integer integer, String s) {
        super(ServiceRuleViolationReason.USED_IS_NOT_ACCEPTED,s,integer);
    }
}



