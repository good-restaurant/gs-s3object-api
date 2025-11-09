package gs.s3object.controller.Base.update;


import gs.s3object.controller.Base.Mapping.DtoMapper;
import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import gs.s3object.service.A_Exception.MergePropertyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * DTO 가 1개 있는 컨트롤러들의 CRUD
 * D = DTO
 * T = Entity
 * 얘를 쓰는 경우는 초기 기능 구상하거나
 * 간단한 api 기능이 필요할 때 씁니다.
 * 서비스도 그에 맞춰서 간단한 것만 구성하는 편이 바람직해요
 * 엔티티 리턴 하지마세요
 */
@RestController
@RequestMapping("/api/v1")
public interface BaseUpdateDtoController<D, T, ID> extends
		BaseServiceInterface<T,ID>, DtoMapper<D,T> {

    /**
     * @param idValue  수정할 idValue
     * @param dto 수정할 내용
     * @return 수정된 엔티티의 DTO
     * {@code @PatchMapping("/{idValue}")}
     */
    @PatchMapping("/{idValue}")
    default ResponseEntity<D> updateAsDto(@PathVariable ID idValue, @RequestBody D dto) throws MergePropertyException {
        T updateEntity = fromDTO(dto);
        T updatedEntity = service().update(updateEntity, idValue);
        return ResponseEntity.ok(toDTO(updatedEntity));
    };
}
