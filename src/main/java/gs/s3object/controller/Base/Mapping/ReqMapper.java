package gs.s3object.controller.Base.Mapping;

import java.util.List;

public interface ReqMapper<REQ,T> {
    REQ toREQ(T entity);
    T fromREQ(REQ req);

    List<REQ> toREQ(List<T> entity);
    List<T> fromREQ(List<REQ> req);
}
