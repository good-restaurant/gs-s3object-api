package gs.s3object.service.A_Impl;

import java.util.List;

public interface Base4Dto2Entity<T,ID,DTO,REQ,RES,INS,SIM> extends Base3Dto2Entity<T,ID,DTO,REQ,RES,SIM> {

    INS toINS(T entity);
    T fromINS(INS ins);

    List<INS> toINS(List<T> entity);
    List<T> fromINS(List<INS> ins);

}
