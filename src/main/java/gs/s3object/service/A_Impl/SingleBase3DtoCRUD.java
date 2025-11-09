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

public abstract class SingleBase3DtoCRUD<T, ID, DTO, REQ,RES,SIM> implements BaseCRUD<T,ID>, ServiceBase<T,ID, SingleBase3DtoCRUD<T, ID, DTO,REQ,RES,SIM>> {

    protected final JpaRepository<T,ID> repository;
    protected final ServiceHelper<T,ID> helper;
    protected final Base3Dto2Entity<T,ID,DTO,REQ,RES,SIM> mapper;

    // 리플렉션을 통한 클래스 정보 추출
    // 하위 서비스에서 주입할 필요 없이 리플렉션으로 가져옵니다.
    // 확장을 위해 남겨놓는 클래스
    protected final Class<T> entityClass;

    @Autowired
    protected JPAQueryFactory jpaQueryFactory;

    protected SingleBase3DtoCRUD(JpaRepository<T, ID> repository, ServiceHelper<T, ID> helper, Base3Dto2Entity<T, ID, DTO, REQ, RES, SIM> mapper) {
        this.repository = repository;
        this.helper = helper;
        this.mapper = mapper;
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    // <editor-fold desc ="base repository method @Override by interface">

    @Override
    public Class<T> getEntityClass() {return entityClass;}
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

    @Override
    public T updateRule(T source, T target) throws MergePropertyException {
        return helper.updateRule(source,target);
    }

    public SingleBase3DtoCRUD<T,ID,DTO,REQ,RES,SIM> getImplService() {
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
        return mapper.fromREQ(dto);
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
    public List<T> fromRES(List<RES> res) {return mapper.fromRES(res);}
    public T fromRES(RES dto) { return mapper.fromRES(dto); }
    public RES toRES(T entity) { return mapper.toRES(entity); }
    public List<RES> toRES(List<T> entities) { return mapper.toRES(entities); }
    //</editor-fold>
}
