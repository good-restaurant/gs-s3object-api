package gs.s3object.controller.Base.view;

import gs.s3object.controller.A_enum.ViewType;
import gs.s3object.controller.Base.Mapping.SimMapper;
import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import gs.s3object.service.A_Exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @param <SIM> Simple DTO
 * @param <T> Entity
 * @param <ID> Entity 의 ID
 *
 *            <p> Simple DTO 의 요청 - 응답에 대한 인터페이스</p>
 *            <p> 요청 받는 파라미터 -> 없음 </p>
 *            <p> ?view=simple 조건으로 모든 데이터를 출력하도록 함 </p>
 */
@RestController
@RequestMapping("/api/v1")
public interface BaseViewParamDtoController<SIM,T,ID> extends BaseServiceInterface<T,ID>, SimMapper<SIM, T> {

    /**
     * @param view ViewType 요건
     *             SIMPLE ( 2024-11-27)
     *
     * @return 해당 ViewType 별 List DTO.
     *                                  SIMPLE, DETAIL, OTHERS 등등
     *             데이터 타입의 성격을 고려하여 Enum 으로 정의하고 switch 할 것
     *             단, 매우 지엽적인 DTO 의 경우 default 로 정의하는게 더 나쁠수도 있기 때문에, 이를 고려할 것.
     *             매핑 메소드가 인터페이스에서 정의하기 때문에 대부분의 경우 Default 보다는 컨트롤러별 오버라이드가 적절함.
     *             SIMPLE 타입의 경우, 모든 엔티티에 정의해서 요구하고 있기 때문에 발생한 문제
     */
    @GetMapping
    default ResponseEntity<List<?>> readAsParam(@RequestParam(name = "view", required = true) String view){
        ViewType viewType = ViewType.from(view);

        switch (viewType) {
            case SIMPLE:
                List<T> entity = service().getAll();
                return ResponseEntity.ok(toSIM(entity));
            default:
                throw new EntityNotFoundException("Unsupported view type: " + view);
        }
    }
	
	// 컨트롤러의 매핑 서비스를 불러오는 메소드
//    SIM toSIM(T entity);
//    List<SIM> toSIM(List<T> entity);

}
