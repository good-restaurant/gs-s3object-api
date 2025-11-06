package gs.s3object.controller.Base.delete.component;

import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import gs.s3object.service.A_Exception.EntityNotFoundException;
import org.springframework.dao.DataAccessException;

public interface DeleteLogic<T,ID> extends BaseServiceInterface<T,ID> {

    default T getDeletedEntity(ID idValue) throws DataAccessException {
        T deletedEntity = service().delete(idValue);
        if (deletedEntity == null) {
            throw new EntityNotFoundException("삭제할 엔티티를 찾을 수 없습니다. Entity not found with idValue: " + idValue);
        }
        return deletedEntity;
    };
}
