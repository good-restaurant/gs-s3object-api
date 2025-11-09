package gs.s3object.controller.Base.composite.create;


import gs.s3object.controller.Base.create.BaseCreateDtoController;
import gs.s3object.controller.Base.create.BaseCreateDtoInsController;
import gs.s3object.controller.Base.create.BaseCreateReqResController;
import gs.s3object.controller.Base.create.BaseCreateResInsController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @param <INS>
 * @param <REQ>
 * @param <RES>
 * @param <D>
 * @param <T>
 * @param <ID>
 *     기본 생성 컨트롤러에서 필요한 형태의 생성을 오버라이드 할 수 있도록 만들어진 인터페이스입니다.
 *     4개중 하나를 사용하더라도 4DTO 를 받아야 하기 때문에, 사용처가 조금 애매함
 *
 *     기초 인터페이스에 상세 default 지정이 되어있습니다. 참고할 것.
 */
@RestController
@RequestMapping("/api/v1")
public interface BaseCreateController<INS,REQ,RES,D,T,ID> extends
		BaseCreateDtoController<D,T,ID>,
				BaseCreateReqResController<REQ,RES,T,ID>,
				BaseCreateDtoInsController<INS, D, T, ID>,
				BaseCreateResInsController<RES, INS, T, ID>
{

    /**
     * @param dto DTO 입력
     *               사용하면 insert 방식으로 추가함
     *               업데이트 용으로 사용하지 못하게 서비스 로직에서 막아야 함
     * @return  DTO 반환
     *
     *
     * @PostMapping
     * @RequestBody D dto
     */
    @PostMapping
    default ResponseEntity<D> createAsDto(@RequestBody D dto){
        T entity = service().save(fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(entity));
    }
    /**
     * @param request 데이터 입력 DTO
     *                초기 데이터 넣는 용도의 DTO. ID는 보통 자동 생성되니, 넣지 않아도 됨.
     * @return response DTO
     *
     * @PostMapping
     */
    @PostMapping
    default ResponseEntity<RES> createAsReq(@RequestBody REQ request){
        T entity = service().save(fromREQ(request));
        RES dto = toRES(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    /**
     * @param request 데이터 입력 DTO
     *                초기 데이터 넣는 용도의 DTO. ID는 보통 자동 생성되니, 넣지 않아도 됨.
     * @return response DTO
     *
     * @PostMapping
     */
    @PostMapping("/dto")
    default ResponseEntity<D> createAsDtoIns(@RequestBody INS request){
        T entity = service().save(fromINS(request));
        D dto = toDTO(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    /**
     * @param request 데이터 입력 DTO
     *                초기 데이터 넣는 용도의 DTO. ID는 보통 자동 생성되니, 넣지 않아도 됨.
     * @return response DTO
     *
     * @PostMapping
     */
    @Override
    @PostMapping("/res")
    default ResponseEntity<RES> createAsResIns(@RequestBody INS request){
        T entity = service().save(fromINS(request));
        RES dto = toRES(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

}
