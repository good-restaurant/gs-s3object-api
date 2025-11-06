package gs.s3object.controller.Base.Mapping;


import gs.s3object.service.A_Impl.SingleBase1DtoCRUD;

import java.util.List;

public interface OneDtoMapper<T, ID, D, SIM> extends DtoMapper<D,T>, SimMapper<SIM, T> {

    SingleBase1DtoCRUD<T, ID, D, SIM> service();

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
}
