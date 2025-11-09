package gs.s3object.controller.Base.delete;


import gs.s3object.controller.Base.Mapping.ResMapper;
import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import gs.s3object.controller.Base.delete.component.DeleteLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public interface BaseDeleteResController<RES,T,ID> extends BaseServiceInterface<T,ID>,
		                                                           ResMapper<RES,T>, DeleteLogic<T,ID> {

    /**
     * @param idValue 삭제할 id 정보
     * @return RES 형태로 반환
     * {@code @DeleteMapping("/{idValue}")}
     */
    @DeleteMapping("/{idValue}")
    default ResponseEntity<RES> deleteAsRes(@PathVariable ID idValue){
        T deletedEntity = getDeletedEntity(idValue);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(toRES(deletedEntity));
    }

}