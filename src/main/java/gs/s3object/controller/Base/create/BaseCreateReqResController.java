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
public interface BaseCreateReqResController<REQ,RES,T,ID> {

    BaseCRUD<T,ID> service();

    /**
     * @param request 데이터 입력 DTO
     *                초기 데이터 넣는 용도의 DTO. ID는 보통 자동 생성되니, 넣지 않아도 됨.
     * @return response DTO
     *
     */
    @PostMapping
    default ResponseEntity<RES> createAsReq(@RequestBody REQ request){
        T entity = service().save(fromREQ(request));
        RES dto = toRES(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    };

    // DTO 변환 메서드 (컨트롤러에서 서비스를 호출하는 얕은 레이어로 처리)
    RES toRES(T entity);
    List<RES> toRES(List<T> entity);

    T fromREQ(REQ dto);
    List<T> fromREQ(List<REQ> dto);
}
