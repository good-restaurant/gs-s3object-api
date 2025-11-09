package gs.s3object.service.A_Impl;


import gs.s3object.service.A_Exception.MergePropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.ExampleMatcher;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 *
 * 복잡한 로직이지만 공통 서비스 CRUD 에서 사용하는 서비스 헬퍼 메소드 입니다.
 * protected final ServiceHelper<T,ID> helper; 처럼 사용합니다.
 * 레포지토리와 관련된 메소드는 이곳에 넣지 않습니다.
 * 컨트롤러가 직접 받지 않습니다. 따라서 이 메소드의 기본 설정은 protected 혹은 private 입니다.
 *
 * @param <T> entity
 * @param <ID> entity's key
 *
 */
@Component
public class ServiceHelper<T, ID> {

    private Class<T> entityClass;
    private static final Logger logger = LoggerFactory.getLogger(ServiceHelper.class);

    // 캐시된 필드 정보
    private static final Map<Class<?>, List<Field>> classFieldsCache = new ConcurrentHashMap<>();

    public ServiceHelper() {
        // 리플렉션을 통해 제네릭 타입 T의 구체 클래스 추출
        Type parameterizedType = this.getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) parameterizedType.getClass();
    }

    //<editor-fold desc="업데이트 규칙 적용">

    /**
     * 소스 객체의 필드 값을 타겟 객체에 병합합니다.
     * 타겟 객체의 필드 값이 null이고 소스 객체의 필드 값이 null이 아닌 경우에만 업데이트합니다.
     *
     * @param source 소스 객체
     * @param target 타겟 객체
     * @return 업데이트된 타겟 객체
     * @throws MergePropertyException 병합 과정에서 발생한 예외
     */
    protected T updateRule(T source, T target) throws MergePropertyException {
        if (source == null || target == null) {
            return target; // 필요에 따라 적절히 처리
        }

        List<Field> fields = getAllFields(source.getClass());

        for (Field field : fields) {
            try {
                Object sourceValue = getFieldValue(source, field);
                Object targetValue = getFieldValue(target, field);

                if (targetValue == null && sourceValue != null) {
                    setFieldValue(target, field, sourceValue);
                }
            } catch (IllegalAccessException e) {
                logger.error("속성 목록 확인 실패: {}", field.getName(), e);
                throw new MergePropertyException("Failed to merge properties for field: " + field.getName(), e);
            }
        }
        return target;
    }

    /**
     * 모든 필드를 포함하여 클래스의 필드 목록을 반환합니다.
     *
     * @param type 클래스 타입
     * @return 필드 목록
     */
    private List<Field> getAllFields(Class<?> type) {
        return classFieldsCache.computeIfAbsent(type, this::retrieveAllFields);
    }

    /**
     * 클래스와 상위 클래스의 모든 필드를 재귀적으로 수집합니다.
     *
     * @param type 클래스 타입
     * @return 필드 목록
     */
    private List<Field> retrieveAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    /**
     * 객체의 특정 필드 값을 반환합니다.
     *
     * @param obj   객체
     * @param field 필드
     * @return 필드 값
     * @throws IllegalAccessException 접근 불가 시 예외
     */
    private Object getFieldValue(T obj, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 객체의 특정 필드 값을 설정합니다.
     *
     * @param obj   객체
     * @param field 필드
     * @param value 설정할 값
     * @throws IllegalAccessException 접근 불가 시 예외
     */
    private void setFieldValue(T obj, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(obj, value);
    }
    //</editor-fold>

    // 클래스의 속성 목록을 가져오는 메소드
    protected List<String> getAttributeNames(Class<?> clazz) {
        List<String> attributeNames = new ArrayList<>();
        while (clazz != null) { // Traverse class hierarchy
            for (Field field : clazz.getDeclaredFields()) {
                attributeNames.add(field.getName());
            }
            clazz = clazz.getSuperclass(); // Move to superclass
        }
        return attributeNames;
    }

    /**
     * @param entity 객체
     * @param propertyNames getAttributeNames(Class<?> clazz) 메소드를 통해 가져온 속성 목록
     * @param <T> 대상 객체의 클래스
     *
     * @return QBE 를 위한 ExampleMatcher
     */
    protected <T> ExampleMatcher getExampleMatcherBase(T entity, List<String> propertyNames) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(); // 기본 매처 설정
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass(), Object.class);
            for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
                String propertyName = propertyDesc.getName();
                if (propertyNames.contains(propertyName)) {
                    Object value = propertyDesc.getReadMethod().invoke(entity);
                    if (value != null && hasValidValue(value)) {
                        // contains() 조건을 사용하여 부분 검색 설정
                        matcher = matcher.withMatcher(propertyName,
                                ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matcher;
    }


    private boolean hasValidValue(Object value) {
        if (value instanceof String) {
            return !((String) value).isEmpty();
        } else if (value instanceof Collection) {
            return !((Collection<?>) value).isEmpty();
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        } else if (value instanceof Number) {
            // 숫자가 0이 아닌 경우 유효하다고 판단
            return ((Number) value).doubleValue() != 0.0;
        } else if (value instanceof Boolean) {
            // Boolean 타입은 값이 null이 아닌 경우 유효하다고 판단
            return true;
        } else {
            // 그 외의 객체들은 null이 아닌 경우 유효하다고 판단
            return true;
        }
    }

//    // jwt 엑세스 토큰을 확인. 해당 메소드를 통해 확인한 엑세스 토큰 전송.
//    // 엑세스 토큰을 서버간 통신으로 할 때에는 최소한 인터넷 영역에선 https 를 사용할 것
//    protected String getUserAccessToken() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getCredentials() instanceof Jwt) {
//            Jwt jwt = (Jwt) authentication.getCredentials();
//            return jwt.getTokenValue();
//        } else {
//            throw new IllegalStateException("No authentication token found");
//        }
//    }

    //<editor-fold desc="속성 빌더">

    /**
     * @param setter 빌더에 적용할 setter 메소드 참조입니다.
     * @param value 설정할 값입니다. 값이 null 인 경우 setter 가 호출되지 않습니다.
     * @param <T> 설정할 값의 타입입니다.
     *           <p>값이 null 이 아닐 경우에만 빌더의 setter 메소드를 호출합니다. 반환값은 없습니다.</p>
     */
    public <T> void nonNullSetter(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
    //</editor-fold>
}



