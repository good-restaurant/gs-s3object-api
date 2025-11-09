package gs.s3object.controller.Base.update;



import gs.s3object.controller.Base.Mapping.DtoMapper;
import gs.s3object.controller.Base.Mapping.ReqMapper;
import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import gs.s3object.service.A_Exception.MergePropertyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public interface BaseUpdateReqDtoController<REQ, DTO, T, ID> extends BaseServiceInterface<T,ID>,
		                                                                     DtoMapper<DTO, T>, ReqMapper<REQ,T>
{
    /**
     * @param idUpdate request 와 일치하고, 수정하고자 하는 id 값
     * @param request Id와 REQ body 포함해서 데이터 수정
     * @return 수정된 DTO 응답
     * <p>
     * {@code @PatchMapping("/{idUpdate}")}
     * or
     * {@code @PatchMapping("/dto/{idUpdate}")}
     * </p>
     */
    @PatchMapping("/{idUpdate}")
    default ResponseEntity<DTO> updateAsReq(@PathVariable ID idUpdate, @RequestBody REQ request) throws MergePropertyException {
        T update = service().update(fromREQ(request), idUpdate);
        return ResponseEntity.ok(toDTO(update));
    };

}
