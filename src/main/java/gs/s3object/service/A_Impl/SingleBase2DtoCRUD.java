package gs.s3object.service.A_Impl;


import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gs.s3object.service.A_Exception.MergePropertyException;
import gs.s3object.service.A_Exception.UpperEntityRemoveException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @param <T> Entity
 * @param <ID> ID 값 (보통 String 이라 가정)
 * @param <DTO> 큰 DTO
 * @param <REQ> 요청 DTO
 *             필요하면 파라미터를 더 넣을수는 있는데, 오버라이드 문제를 야기할 수 있습니다.
 *
 *             주의: 인터페이스에 정의한 요소들은 전부 엔티티 리턴을 전제로 함.
 *             이유는 DTO 사용의 변화무쌍함으로 인해 반환 없는 서비스 메소드가 처리가 곤란한 요소들이 많습니다.
 *
 *             <description>
 *             JPA 기본 메소드를 제외하면 QBE 기반 레포지토리를 이용합니다.
 *             BETWEEN 같은 복잡한 쿼리는 QueryDSL 을 사용합니다.
 *             레포지토리에 findByXYZ 있으면 그게 뭔지 주석으로 적으세요.
 *
 *             작동은 물론이고, 특정 API 서비스가 시나리오에 지정된 필요 이상의 권한을 사용하는지 검사를 해야 합니다.
 *             컨트롤러에 지나지게 많은 로직을 넣으려 하지 마세요.
 *             </description>
 */
public abstract class SingleBase2DtoCRUD<T, ID, DTO, REQ,SIM> implements BaseCRUD<T,ID>, ServiceBase<T,ID,SingleBase2DtoCRUD<T, ID, DTO, REQ,SIM>>{

    protected final JpaRepository<T,ID> repository;
    protected final ServiceHelper<T,ID> helper;
    protected final Base2Dto2Entity<T,ID,DTO,REQ,SIM> mapper;

    // 하위 서비스에서 주입할 필요 없이 리플렉션으로 가져옵니다.
    // 확장을 위해 남겨놓는 클래스
    protected final Class<T> entityClass;

    @Autowired
    protected JPAQueryFactory jpaQueryFactory;

    public SingleBase2DtoCRUD(JpaRepository<T, ID> repository, ServiceHelper<T, ID> helper, Base2Dto2Entity<T, ID, DTO, REQ,SIM> mapper) {
        this.repository = repository;
        this.helper = helper;
        this.mapper = mapper;
        // 리플렉션을 통한 클래스 정보 추출
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    // <editor-fold desc ="base repository method @Override by interface">

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public JpaRepository<T, ID> getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public T update(T entity, ID id){
        if (!id.equals(getEntityId(entity))) {
            throw new IllegalArgumentException("ID mismatch: 요청한 ID 파라미터와 수정 내용의 ID가 서로 다릅니다.");
        }
        try {
            return updateById(id,entity);
        } catch (MergePropertyException e) {
            throw new RuntimeException(e);
        }
    }

    public T updateById(ID id, T update) throws MergePropertyException {

        Optional<T> exist = repository.findById(id);

        if (exist.isPresent()) {
            T existingEntity = exist.get();
            update = helper.updateRule( existingEntity, update);
            // Use the abstract method to compare the entity's ID and the input ID
            if (idCheck(existingEntity, id)) {
                return repository.save(update);
            }
        }
        throw new RuntimeException("요청한 ID와 세부내역의 ID 가 일치하지 않습니다. Request ID and entity's id are not matching");
    }

    @Override
    @Transactional
    public T delete(ID id) throws UpperEntityRemoveException {
        T entity = getById(id);
        if (entity != null) {
            try {
            repository.delete(entity);
            } catch (Exception e) {
            // 연관된 엔티티가 삭제되지 않은 경우 원인 예외와 함께 ID를 포함하여 예외를 던짐
            throw new UpperEntityRemoveException(id, e);
            }
        } else {
            // 엔티티가 존재하지 않을 경우 ID를 포함하여 예외를 던짐
            throw new UpperEntityRemoveException(id);
        }
        return entity;
    }

    @Override
    public void deleteById(ID id) {
        delete(id);
    }

    @Override
    @Transactional
    public void deleteAll(Iterable<ID> ids) {
        repository.deleteAllById(ids);
    }

    public SingleBase2DtoCRUD<T,ID,DTO,REQ,SIM> getImplService() {
        return this;
    }

    // </editor-fold>

    //<editor-fold desc="super method or internal Service method">
    /*
    * 하위 서비스의 슈퍼 메소드 혹은 이 서비스 내에서 다른 서비스를 집어넣는 경우 여기에 넣어주세요
    *
    * */

    public <T> List<T> findByDateRange(EntityPathBase<T> qEntity, DateTimePath<LocalDateTime> dateTimePath, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.selectFrom(qEntity)
                .where(dateTimePath.between(startDate, endDate))
                .fetch();
    }

    //</editor-fold>

    //<editor-fold desc="other abstract">
    public abstract ID getEntityId(T updateEntity);

    @Override
    public T updateRule(T source, T target) throws MergePropertyException {
        return helper.updateRule(source,target);
    }

    public abstract boolean idCheck(T entity, ID id);
    //</editor-fold>

    //<editor-fold desc="DTO / Entity 상호 전환 메소드">
    public DTO toDTO(T entity) {
        return mapper.toDTO(entity);
    }

    public List<DTO> toDTO(List<T> entities) {
        return mapper.toDTO(entities);
    }

    public T fromDTO(DTO dto) {
        return mapper.fromDTO(dto);
    }

    public List<T> fromDTO(List<DTO> dtos) {
        return mapper.fromDTO(dtos);
    }

    public REQ toREQ(T entity) {
        return mapper.toREQ(entity);
    }

    public List<REQ> toREQ(List<T> entities) {
        return mapper.toREQ(entities);
    }

    public T fromREQ(REQ dto) {
        return mapper.fromREQ(dto);
    }

    public List<T> fromREQ(List<REQ> dto) {
        return fromREQ(dto);
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
