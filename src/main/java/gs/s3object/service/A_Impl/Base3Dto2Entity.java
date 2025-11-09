package gs.s3object.service.A_Impl;

import java.util.List;

public interface Base3Dto2Entity<T,ID,DTO,REQ,RES,SIM> extends Base2Dto2Entity<T,ID,DTO,REQ,SIM>{

    RES toRES(T entity);
    T fromRES(RES res);

    List<RES> toRES(List<T> entity);
    List<T> fromRES(List<RES> res);
}
