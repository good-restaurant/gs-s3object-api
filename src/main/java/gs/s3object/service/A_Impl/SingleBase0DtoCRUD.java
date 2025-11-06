package gs.s3object.service.A_Impl;

import gs.s3object.service.A_Exception.MergePropertyException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;

/**
 * @param <T> 엔티티
 * @param <ID> 아이디
 *  이 abstract class 의 목적은 0개 DTO 만 필요한 엔티티에 대한 기본 기능 정의를 위해서 만들어졌습니다.
 *  별로 중요하지 않은 엔티티에 DTO 를 많이 만들기 곤란해서 출현
 *
 */
public abstract class SingleBase0DtoCRUD<T,ID> implements BaseCRUD<T,ID>, ServiceBase<T,ID,SingleBase0DtoCRUD<T, ID>> {

    protected final JpaRepository<T,ID> repository;
    protected final ServiceHelper<T,ID> helper;
    // 0 DTO 는 entity 만 존재하기 때문에, mapper 를 사용하지 않습니다.

    // 하위 서비스에서 주입할 필요 없이 리플렉션으로 가져옵니다.
    // 확장을 위해 남겨놓는 클래스
    protected final Class<T> entityClass;

    protected SingleBase0DtoCRUD(JpaRepository<T, ID> repository, ServiceHelper<T, ID> helper) {
        this.repository = repository;
        this.helper = helper;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public T delete(ID id) {
        T entity = repository.getReferenceById(id);
        repository.deleteById(id);
        return entity;
    }

    @Override
    public T updateRule(T source, T target) throws MergePropertyException {
        return helper.updateRule(source, target);
    }

    @Override
    public JpaRepository<T, ID> getRepository() {
        return repository;
    }

    @Override
    public SingleBase0DtoCRUD<T,ID> getImplService() {
        return this;
    }
}