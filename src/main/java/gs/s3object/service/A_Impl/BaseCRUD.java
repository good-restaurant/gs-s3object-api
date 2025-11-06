package gs.s3object.service.A_Impl;

import gs.s3object.service.A_Exception.MergePropertyException;
import gs.s3object.service.A_Exception.ServiceRuleViolationException;
import gs.s3object.service.A_Exception.statusEnum.ServiceRuleViolationReason;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @param <T> 엔티티
 * @param <ID> 의 ID
 *            기본 CRUD 인터페이스
 *            필요에 따라서 DTO 가 첨가된 인터페이스를 별도로 정의해서 N Dto CRUD 추상 클래스에서 처리 가능
 *
 */
public interface BaseCRUD<T,ID> {

    Map<Class<?>, List<Field>> fieldCache = new ConcurrentHashMap<>();

    //<editor-fold desc="default method, 특별한 문제가 없으면 이대로 사용 가능">
    // 기본 CRUD 메서드를 default 로 구현
    default List<T> getAll() {
        return getRepository().findAll();
    }

    default Page<T> getAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    default List<T> getAll(Sort sort) {
        return getRepository().findAll(sort);
    }

    default T getById(ID id) {
        return getRepository().findById(id).orElse(null);
    }

    default boolean existsById(ID id) {
        if (id == null) return false;

        return getRepository().existsById(id);
    }

    @Transactional
    default T save(T entity) {
        if (existsById(getEntityId(entity))) {
            throw new ServiceRuleViolationException(
                    ServiceRuleViolationReason.GENERIC_RULE_VIOLATION,
                    "해당 엔티티는 이미 존재합니다. Entity already exists: " + entity
            );
        }
        return getRepository().save(entity);
    }

    @Transactional
    default T update(T entity, ID id) throws MergePropertyException {
        return updateById(id, entity);
    }

    @Transactional
    default List<T> saveAll(Iterable<T> entities) {

        // 1. 저장하려는 엔티티의 ID 추출
        Set<ID> idsToCheck = StreamSupport.stream(entities.spliterator(), false)
                .map(this::getEntityId)
                .collect(Collectors.toSet());

        // 2. 데이터베이스에서 이미 존재하는 ID 조회
        Set<ID> existingIds = new HashSet<>(getRepository().findAllById(idsToCheck)
                .stream()
                .map(this::getEntityId)
                .collect(Collectors.toSet()));

        // 3. 이미 존재하는 ID가 있는 경우 예외 발생
        entities.forEach(entity -> {
            if (existingIds.contains(getEntityId(entity))) {
                throw new ServiceRuleViolationException(
                        ServiceRuleViolationReason.GENERIC_RULE_VIOLATION,
                        "해당 엔티티는 이미 존재합니다. Entity already exists: " + entity
                );
            }
        });

        // 4. 문제 없는 엔티티들을 저장
        return getRepository().saveAll(entities);
    }

    default void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    default void deleteAll(Iterable<ID> ids) {
        ids.forEach(this::deleteById);
    }

    default long count() {
        return getRepository().count();
    }

    default List<T> queryByQBE(Integer key,String cd, String name, Class<T> entityClass) {
        // QBE 쿼리 기본 메소드
        T entity = getQBEEntity(key, cd, name);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // LIKE '%value%' 조건
                .withIgnoreCase(); // 대소문자 무시

        Example<T> example = Example.of(entity, matcher);
        return getRepository().findAll(example);
    }

    //</editor-fold>

    //<editor-fold desc="구현이 필요한 인터페이스">

    // 제네릭 컨트롤러 인터페이스에서 클래스 정보를 확인해야 할 때, 리플렉션 혹은 개별 최종 서비스에서 불러옴
    Class<T> getEntityClass();

    // 필요한 repository 를 각 서비스가 제공하도록 정의
    JpaRepository<T, ID> getRepository();

    /**
     * 기본 QBE 로직으로 확인하고자 하는 entity 의 성격을 통해 조회를 진행합니다.
     * <p>key / cd / name 의 용도가 구현체마다 다릅니다. 개별 오버라이드를 통해 설명을 확인합니다.</p>
     *
     * @param key 보통 상위 객체나 연관객체의 key 값
     * @param cd 대상 객체의 코드 값 (있는경우)
     * @param name 대상 객체의 이름 값 (있는경우)
     * @return 조회하고자 하는 객체의 형태 (sql 의 Like 조건과 유사하게 검색, QBE 로직에 따라 다름)
     */
    T getQBEEntity(Integer key, String cd, String name);

    T delete(ID id);

    // 엔티티의 ID를 가져오는 메소드
    ID getEntityId(T entity);

    //</editor-fold>

    //<editor-fold desc="헬퍼, 규칙 메소드">

    /**
     * 소스 객체의 필드 값을 타겟 객체에 병합합니다.
     * 타겟 객체의 필드 값이 null 이고 소스 객체의 필드 값이 null 이 아닌 경우에만 업데이트합니다.
     * 고려사항 몇 개
     *      1. 인터페이스 내에 있는 default 로직으로 인해 public 으로 할 수 밖에 없지만,
     *          실제 사용은 protected 처럼 내부 메소드로 사용하세요.
     *      2. 이 말은 컨트롤러가 직접 "updateRule" 을 사용하면 안된다는 의미입니다.
     *          보통 update 를 하면 자연스럽게 호출되기 때문에 큰 문제가 안될 겁니다.
     *
     * @param source 소스 객체
     * @param target 타겟 객체
     * @return 업데이트된 타겟 객체
     * @throws MergePropertyException 병합 과정에서 발생한 예외
     */
    T updateRule(T source, T target) throws MergePropertyException;

    /**
     * ID로 엔티티를 찾고 업데이트하는 기본 로직을 제공합니다.
     * 소스 객체(entity)의 값을 기존 엔티티(existingEntity)에 병합합니다.
     *
     * @param id 업데이트할 엔티티의 ID
     * @param entity 업데이트할 소스 엔티티
     * @return 업데이트된 엔티티
     */
    default T updateById(ID id, T entity) throws MergePropertyException {
        T exist = getRepository().findById(id).orElse(null);

        // ID가 일치하는지 먼저 확인
        if (!idCheck(exist, id)) {
            throw new ServiceRuleViolationException(ServiceRuleViolationReason.NOT_MATCHED, "요청한 ID와 세부내역의 ID가 일치하지 않습니다. Request ID and entity's id are not matching");
        }

        T updatedEntity;
        try {
            // updateRule 사용: 소스를 기존 엔티티에 병합
            updatedEntity = updateRule(exist, entity);
        } catch (MergePropertyException e) {
            throw new ServiceRuleViolationException(ServiceRuleViolationReason.GENERIC_RULE_VIOLATION, e, id);
        }

        // 병합 후 저장
        return getRepository().save(updatedEntity);
    }

    /**
     * ID 확인 메소드: 기본적으로 기존 엔티티의 ID와 요청 ID를 비교합니다.
     *
     * @param entity 업데이트할 엔티티
     * @param id 요청된 ID
     * @return ID가 일치하면 true, 그렇지 않으면 false
     */
    default boolean idCheck(T entity, ID id) {
        return getEntityId(entity).equals(id);
    }

    //</editor-fold>

}
