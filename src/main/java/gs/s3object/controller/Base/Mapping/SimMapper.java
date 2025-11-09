package gs.s3object.controller.Base.Mapping;

import java.util.List;

public interface SimMapper<SIM,T> {
    SIM toSIM(T entity);
    List<SIM> toSIM(List<T> entities);

    T fromSIM(SIM dto);
    List<T> fromSIM(List<SIM> dtos);
}

