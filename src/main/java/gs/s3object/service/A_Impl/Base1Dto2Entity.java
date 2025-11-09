package gs.s3object.service.A_Impl;

import java.util.List;

public interface Base1Dto2Entity<T,ID,DTO,SIM> extends BaseSimpleDto2Entity<T,ID,SIM>{
    DTO toDTO(T entity);
    T fromDTO(DTO dto);

    List<DTO> toDTO(List<T> entities);
    List<T> fromDTO(List<DTO> dtos);
}
