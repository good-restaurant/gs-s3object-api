package gs.s3object.service.A_Impl;

import java.util.List;

public interface BaseSimpleDto2Entity<T,ID,SIM>{
    SIM toSIM(T entity);
    T fromSIM(SIM dto);

    List<SIM> toSIM(List<T> entities);
    List<T> fromSIM(List<SIM> dtos);
}