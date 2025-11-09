package gs.s3object.controller.Base.create;

import gs.s3object.controller.Base.Mapping.DtoMapper;
import gs.s3object.controller.Base.Mapping.ReqMapper;
import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public interface BaseCreateReqDtoController<REQ,DTO,T,ID> extends
		BaseServiceInterface<T,ID>, DtoMapper<DTO,T>, ReqMapper<REQ,T> {
    /**
     * @param request 데이터 입력 DTO
     *                초기 데이터 넣는 용도의 DTO. ID는 보통 자동 생성되니, 넣지 않아도 됨.
     * @return DTO
     */
    @PostMapping
    default ResponseEntity<DTO> createAsDtoReq(@RequestBody REQ request){
        T entity = service().save(fromREQ(request));
        DTO dto = toDTO(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    };
}
