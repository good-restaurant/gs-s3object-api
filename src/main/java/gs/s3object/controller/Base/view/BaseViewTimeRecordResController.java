package gs.s3object.controller.Base.view;



import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import gs.s3object.model.entity.TimeRecords;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @param <RES> Return DTO
 * @param <T> Entity
 * @param <ID> Entity's ID
 *            입력은 TimeRecords 로 받고, {@code RES } 타입의 DTO 를 전달하는 컨트롤러
 *            필요에 따라서 TimeRecords 를 입력받지 않고 다른 것을 받을 수 있음
 */
@RestController
@RequestMapping("/api/v1")
public interface BaseViewTimeRecordResController<RES,T,ID> extends BaseServiceInterface<T,ID> {

    @PostMapping("/datetime")
    ResponseEntity<List<RES>> readAsTimeRecords(@RequestBody TimeRecords timeRecords);

}
