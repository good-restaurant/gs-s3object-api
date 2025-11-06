package gs.s3object.service.A_Impl;

import java.util.List;

public interface Base2Dto2Entity<T,ID,DTO,REQ,SIM> extends Base1Dto2Entity<T,ID,DTO,SIM> {
    REQ toREQ(T entity);
    T fromREQ(REQ dto);

    List<REQ> toREQ(List<T> entity);
    List<T> fromREQ(List<REQ> dto);

}
