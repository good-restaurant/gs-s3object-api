package gs.s3object.controller.Base.Mapping;

import gs.s3object.service.A_Impl.SingleBase3DtoCRUD;

import java.util.List;

public interface ThreeDtoMapper<T, ID, D,REQ,RES, SIM> extends DtoMapper<D,T>,ReqMapper<REQ,T>,SimMapper<SIM,T>, ResMapper<RES,T>  {

    SingleBase3DtoCRUD<T, ID, D,REQ,RES, SIM> service();

    default D toDTO(T entity) {
        return service().toDTO(entity);
    }

    default List<D> toDTO(List<T> entities) {
        return service().toDTO(entities);
    }

    default T fromDTO(D dto) {
        return service().fromDTO(dto);
    }

    default List<T> fromDTO(List<D> dtos) {
        return service().fromDTO(dtos);
    }

    @Override
    default SIM toSIM(T entity) {
        return service().toSIM(entity);
    }

    @Override
    default List<SIM> toSIM(List<T> entities) {
        return service().toSIM(entities);
    }

    @Override
    default T fromSIM(SIM dto) {
        return service().fromSIM(dto);
    }

    @Override
    default List<T> fromSIM(List<SIM> dtos) {
        return service().fromSIM(dtos);
    }

    @Override
    default REQ toREQ(T entity) {
        return service().toREQ(entity);
    }

    @Override
    default T fromREQ(REQ req) {
        return service().fromREQ(req);
    }

    @Override
    default List<REQ> toREQ(List<T> entity) {
        return service().toREQ(entity);
    }

    @Override
    default List<T> fromREQ(List<REQ> req) {
        return service().fromREQ(req);
    }

    @Override
    default RES toRES(T entity) {
        return service().toRES(entity);
    }

    @Override
    default T fromRES(RES res) {
        return service().fromRES(res);
    }

    @Override
    default List<RES> toRES(List<T> entity) {
        return service().toRES(entity);
    }

    @Override
    default List<T> fromRES(List<RES> res) {
        return service().fromRES(res);
    }
}
