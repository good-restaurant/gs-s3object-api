package gs.s3object.controller.Base.Mapping;

import java.util.List;

public interface DtoMapper<D,T> {
    D toDTO(T entity);
    List<D> toDTO(List<T> entity);

    T fromDTO(D dto);
    List<T> fromDTO(List<D> dto);
}
