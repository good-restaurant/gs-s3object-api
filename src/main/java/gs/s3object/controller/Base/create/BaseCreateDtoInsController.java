package gs.s3object.controller.Base.create;

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
public interface BaseCreateDtoInsController<INS, D, T, ID> {

    BaseCRUD<T,ID> service();

    /**
     * @param request 데이터 입력 DTO
     *                초기 데이터 넣는 용도의 DTO. ID는 보통 자동 생성되니, 넣지 않아도 됨.
     * @return response DTO
     *
     */
    @PostMapping
    default ResponseEntity<D> createAsDtoIns(@RequestBody INS request){
        T entity = service().save(fromINS(request));
        D dto = toDTO(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    };

    D toDTO(T entity);
    List<D> toDTO(List<T> entity);

    T fromINS(INS dto);
    List<T> fromINS(List<INS> dto);
}