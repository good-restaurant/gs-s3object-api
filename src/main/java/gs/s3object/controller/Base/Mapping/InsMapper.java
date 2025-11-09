package gs.s3object.controller.Base.Mapping;

import java.util.List;

public interface InsMapper<INS,T> {
    INS toINS(T entity);
    T fromINS(INS ins);

    List<INS> toINS(List<T> entity);
    List<T> fromINS(List<INS> ins);
}
