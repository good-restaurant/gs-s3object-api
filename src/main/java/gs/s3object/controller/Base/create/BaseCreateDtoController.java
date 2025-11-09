package gs.s3object.controller.Base.create;

import gs.s3object.controller.Base.Mapping.DtoMapper;
import gs.s3object.service.A_Impl.BaseCRUD;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public interface BaseCreateDtoController <D,T,ID> extends DtoMapper<D,T> {

    BaseCRUD<T,ID> service();

    /**
     * @param dto DTO 입력. 사용하면 insert 방식으로 추가함
     *               <p>업데이트 용으로 사용하지 못하게 서비스 로직에서 막아야 함</p>
     * @return  DTO 반환
     * {@code @PostMapping}
     */
    @PostMapping
    default ResponseEntity<D> createAsDto(@RequestBody D dto){
        T entity = service().save(fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(entity));
    };

    /**
     * @param dto 다수의 DTO 입력 - 여러 자원을 한 번에 생성
     *               리스트 형태로 insert 방식만 지원.
     * @return 생성된 자원의 DTO 리스트 반환
     *
     */
    @PostMapping("/bulk")
    default ResponseEntity<List<D>> createAsListDto(@RequestBody List<D> dto){
        List<T> entity = service().saveAll(fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(entity));
    };

//    // DTO 변환 메서드 (컨트롤러에서 서비스를 호출하는 얕은 레이어로 처리)
//    D toDTO(T entity);
//    List<D> toDTO(List<T> entity);
//
//    T fromDTO(D dto);
//    List<T> fromDTO(List<D> dto);
}
