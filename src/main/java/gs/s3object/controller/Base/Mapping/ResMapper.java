package gs.s3object.controller.Base.Mapping;

import java.util.List;

public interface ResMapper<RES,T> {
    RES toRES(T entity);
    T fromRES(RES res);

    List<RES> toRES(List<T> entity);
    List<T> fromRES(List<RES> res);
}
