package gs.s3object.controller.Base.update;

import gs.s3object.controller.Base.Mapping.ReqMapper;
import gs.s3object.controller.Base.Mapping.ResMapper;
import gs.s3object.service.A_Exception.MergePropertyException;
import gs.s3object.service.A_Impl.BaseCRUD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public interface BaseUpdateReqResController<REQ, RES, T, ID> extends
		ReqMapper<REQ,T>, ResMapper<RES,T> {

    BaseCRUD<T,ID> service();

    /**
     * @param idValue request 와 일치하고, 수정하고자 하는 id 값
     * @param request Id와 REQ body 포함해서 데이터 수정
     * @return 수정된 DTO 응답
     * <p>
     * {@code @PatchMapping("/{idValue}")}
     * or
     * {@code @PatchMapping("/res/{idValue}")}
     * </p>
     */
    @PatchMapping("/{idValue}")
    default ResponseEntity<RES> updateAsReq(@PathVariable ID idValue, @RequestBody REQ request) throws MergePropertyException {
        T update = service().update(fromREQ(request),idValue);
        return ResponseEntity.ok(toRES(update));
    }

}
