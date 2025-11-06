package gs.s3object.controller.Base.delete;

import gs.s3object.controller.Base.Mapping.DtoMapper;
import gs.s3object.controller.Base.delete.component.DeleteLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public interface BaseDeleteDtoController<D,T,ID> extends DeleteLogic<T,ID>,
		                                                         DtoMapper<D,T>
{

    /**
     * @param idValue 삭제할 idValue
     * @return 삭제된 대상의 DTO
     * {@code @DeleteMapping("/{idValue}")}
     *
     */
    @DeleteMapping("/{idValue}")
    default ResponseEntity<D> deleteAsDto(@PathVariable ID idValue){
        T deletedEntity = getDeletedEntity(idValue);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(toDTO(deletedEntity));
    };

}
