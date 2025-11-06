package gs.s3object.service.A_Impl;


import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gs.s3object.service.A_Exception.MergePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @param <T> 엔티티
 * @param <ID> 아이디
 * @param <DTO> 기본 DTO
 *  이 abstract class 의 목적은 1개 DTO 만 필요한 엔티티에 대한 기본 기능 정의를 위해서 만들어졌습니다.
 *             데이터를 자주 다루지 않거나, 복잡한 구조가 아닐 때 사용합니다.
 *
 */
public abstract class SingleBase1DtoCRUD<T,ID,DTO,SIM> implements BaseCRUD<T,ID>, ServiceBase<T,ID,SingleBase1DtoCRUD<T,ID, DTO, SIM>> {

    protected final JpaRepository<T,ID> repository;
    protected final ServiceHelper<T,ID> helper;
    protected final Base1Dto2Entity<T,ID, DTO,SIM> mapper;

    // 하위 서비스에서 주입할 필요 없이 리플렉션으로 가져옵니다.
    // 확장을 위해 남겨놓는 클래스
    protected final Class<T> entityClass;

    @Autowired
    protected JPAQueryFactory jpaQueryFactory;

    protected SingleBase1DtoCRUD(JpaRepository<T, ID> repository, Base1Dto2Entity<T, ID, DTO,SIM> mapper, ServiceHelper<T, ID> helper) {
        this.repository = repository;
        this.helper = helper;
        this.mapper = mapper;

        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public JpaRepository<T, ID> getRepository() {
        return repository;
    }

    public SingleBase1DtoCRUD<T,ID,DTO,SIM> getImplService() {
        return this;
    }

    @Override
    public T delete(ID id) {
        Optional<T> entity = repository.findById(id);
        repository.deleteById(id);
        return entity.orElse(null);
    }

    /**
     * @param entity 입력 개체
     *
     *              ID 포함 insert 입력시 존재하는 개체가 있는지 확인합니다.
     *               기본적으로 요청에 ID를 입력하지 않고 입력하면, 새 객체로 들어가기 때문에
     *               큰 문제가 안됩니다.
     *
     * @return ID 값
     */
    public abstract ID getEntityId(T entity);

    @Override
    public T updateRule(T source, T target) throws MergePropertyException {
        return helper.updateRule(source, target);
    }

    /**
     * @param id 수정할 ID
     * @param update 대상이 될 업데이트 객체
     * @return 수정된 대상
     * updateRule 을 이용하여 만약 Not null 객체가 update 에 없는 경우,
     * ID 에 있는 정보를 사용하도록 설정함
     *
     * update(기본 컨트롤러 호출) -> updateById(오버라이딩 없이 그대로 사용할 경우 로직)
     * update(오버라이드) -> save
     */
    public T updateById(ID id, T update) throws MergePropertyException {

        Optional<T> exist = repository.findById(id);

        if (exist.isPresent()) {
            T existingEntity = exist.get();
            update = helper.updateRule( existingEntity, update);

            // Id 일치 확인 로직
            if (idCheck(existingEntity, id)) {
                return repository.save(update);
            }
        }

        throw new RuntimeException("요청한 ID와 세부내역의 ID 가 일치하지 않습니다. Request ID and entity's id are not matching");
    }

    public <T> List<T> findByDateRange(EntityPathBase<T> qEntity, DateTimePath<LocalDateTime> dateTimePath, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.selectFrom(qEntity)
                .where(dateTimePath.between(startDate, endDate))
                .fetch();
    }

    protected <T> ExampleMatcher getExampleMatcher(T entity, List<String> propertyNames) {
        return helper.getExampleMatcherBase(entity, propertyNames);
    }

    //<editor-fold desc="DTO / entity 상호 전환 메소드">
    public DTO toDTO(T entity) {
        return mapper.toDTO(entity);
    }

    public List<DTO> toDTO(List<T> entities) {
        return entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public T fromDTO(DTO dto) {
        return mapper.fromDTO(dto);
    }

    public List<T> fromDTO(List<DTO> dtos) {
        return dtos.stream()
                .map(mapper::fromDTO)
                .collect(Collectors.toList());
    }

    public SIM toSIM(T entity) {
        return mapper.toSIM(entity);
    }

    public List<SIM> toSIM(List<T> entities) {
        return mapper.toSIM(entities);
    }

    public T fromSIM(SIM dto) {
        return mapper.fromSIM(dto);
    }

    public List<T> fromSIM(List<SIM> dtos) {
        return mapper.fromSIM(dtos);
    }

    //</editor-fold>

}
