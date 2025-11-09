package gs.s3object.controller.Base.view;



import gs.s3object.controller.Base.Mapping.DtoMapper;
import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public interface BaseViewSetSimpleController<D,T,ID> extends BaseServiceInterface<T,ID>, DtoMapper<D,T> {

    /**
     * @param idValue 단일 조회 기능
     * @return D DTO
     * <p>
     * {@code @GetMapping("/{id}")}
     * or
     * {@code @GetMapping("/{idValue}/set")}
     * </p>
     *
     * Req, Res 타입의 컨트롤러가 dto 에 SET 을 놓았을 때 관련 정보를 쉽게 가져갈 수 있도록 함.
     */
    @GetMapping("/{idValue}/set")
    default ResponseEntity<D> readAsDtoSet(@PathVariable ID idValue){
        T entity = service().getById(idValue);
        return ResponseEntity.ok(toDTO(entity));
    }


    /**
     * @return D DTO
     * <p>
     * {@code @GetMapping("/set")}
     * or
     * {@code @GetMapping("/all/set")}
     * </p>
     *
     * Req, Res 타입의 컨트롤러가 dto 에 SET 을 놓았을 때 관련 정보를 쉽게 가져갈 수 있도록 함.
     * 모든 항목의 set 을 가져오기 때문에 사용에 주의할 것
     */
    @GetMapping("/all/set")
    default ResponseEntity<List<D>> readAsDtoSetAll(){
        return ResponseEntity.ok(toDTO(service().getAll()));
    }
}
