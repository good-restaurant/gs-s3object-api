package gs.s3object.service.A_Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gs.s3object.service.A_Exception.MergePropertyException;
import gs.s3object.service.A_Exception.UpperEntityRemoveException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class SingleBase4DtoCRUD<T, ID, DTO, REQ,RES, INS,SIM> implements BaseCRUD<T,ID>, ServiceBase<T,ID, SingleBase4DtoCRUD<T, ID, DTO,REQ,RES,INS, SIM>> {

    protected final JpaRepository<T, ID> repository;
    protected final ServiceHelper<T, ID> helper;
    protected final Base4Dto2Entity<T, ID, DTO, REQ, RES, INS, SIM> mapper;

    protected SingleBase4DtoCRUD(JpaRepository<T, ID> repository, ServiceHelper<T, ID> helper, Base4Dto2Entity<T, ID, DTO, REQ, RES, INS, SIM> mapper) {
        this.repository = repository;
        this.helper = helper;
        this.mapper = mapper;
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];;
    }

    // 리플렉션을 통한 클래스 정보 추출
    // 하위 서비스에서 주입할 필요 없이 리플렉션으로 가져옵니다.
    // 확장을 위해 남겨놓는 클래스
    protected final Class<T> entityClass;

    @Autowired
    protected JPAQueryFactory jpaQueryFactory;

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

    /**
     * 소스 객체의 필드 값을 타겟 객체에 병합합니다.
     * 타겟 객체의 필드 값이 null 이고 소스 객체의 필드 값이 null 이 아닌 경우에만 업데이트합니다.
     * 고려사항 몇 개
     * 1. 인터페이스 내에 있는 default 로직으로 인해 public 으로 할 수 밖에 없지만,
     * 실제 사용은 protected 처럼 내부 메소드로 사용하세요.
     * 2. 이 말은 컨트롤러가 직접 "updateRule" 을 사용하면 안된다는 의미입니다.
     * 보통 update 를 하면 자연스럽게 호출되기 때문에 큰 문제가 안될 겁니다.
     *
     * @param source 소스 객체
     * @param target 타겟 객체
     * @return 업데이트된 타겟 객체
     * @throws MergePropertyException 병합 과정에서 발생한 예외
     */
    @Override
    public T updateRule(T source, T target) throws MergePropertyException {
        return helper.updateRule(source,target);
    }

    public abstract T getQBEEntity(Integer key, String cd, String name);

    public abstract ID getEntityId(T updateEntity);

    public abstract boolean idCheck(T entity, ID id);

    public SingleBase4DtoCRUD<T,ID,DTO,REQ,RES,INS, SIM> getImplService() {
        return this;
    }

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
    public INS toINS(T entity) {return mapper.toINS(entity);}
    public List<INS> toINS(List<T> entities) { return mapper.toINS(entities); }
    public T fromINS(INS dto) { return mapper.fromINS(dto); }
    public List<T> fromINS(List<INS> dtos) { return mapper.fromINS(dtos); }
    //</editor-fold>
}