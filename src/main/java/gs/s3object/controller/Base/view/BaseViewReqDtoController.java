package gs.s3object.controller.Base.view;

import gs.s3object.controller.Base.Mapping.DtoMapper;
import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import gs.s3object.service.A_Exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @param <REQ> req 파라미터  필요시
 * @param <DTO> 응답
 * @param <T> 대상 엔티티
 * @param <ID> 관련 ID값
 */
@RestController
@RequestMapping("/api/v1")
public interface BaseViewReqDtoController<REQ, DTO, T, ID> extends
		BaseServiceInterface<T,ID>, DtoMapper<DTO, T> {

    /**
     * @param idValue 단일 조회 기능
     * @return D DTO
     * @GetMapping("/{idValue}")
     */
    @GetMapping("/{idValue}")
    default ResponseEntity<DTO> readAsDto(@PathVariable ID idValue) {
        T entity = service().getById(idValue);
        if (entity == null) {
            throw new EntityNotFoundException("엔티티를 찾을 수 없습니다. Entity not found with idValue: " + idValue);
        }
        return ResponseEntity.ok(toDTO(entity));
    }

    /**
     * @return 전체 목록을 기본 DTO 의 List 형태로 나옵니다
     * @GetMapping
     */
    @GetMapping("/all")
    default ResponseEntity<List<DTO>> listAllAsDto(){
        List<T> entities = service().getAll();
        return ResponseEntity.ok(toDTO(entities));
    }
	
	/**
     * @param page 페이지 몇 페이지
     * @param size 페이지당 사이즈
     * @return 페이지 형태의 DTO 를 반환홥니다.
     * 전체 항목이 사이즈보다 작은 경우, 출력이 제대로 안됩니다.
     * {@code @GetMapping("/page")}
     */
    @GetMapping("/page")
    default ResponseEntity<Page<DTO>> listPageAsDto(@RequestParam int page, @RequestParam int size){
        Page<T> entities = service().getAll(PageRequest.of(page, size));
        Page<DTO> dtoPage = entities.map(this::toDTO);
        return ResponseEntity.ok(dtoPage);
    }
	
	/**
     * @param key 키 값 (보통 상위, 연관 키 값, 엔티티의 키값일 수도 있음)
     * @param cd   코드 (부분 일치)
     * @param name 이름 (부분 일치)
     * @return DTO 목록 반환
     * {@code @GetMapping("/search")}
     */
    @GetMapping("/search")
    default ResponseEntity<List<DTO>> searchAsDto(@RequestParam(required = false) Integer key,
                                                @RequestParam(required = false) String cd,
                                                @RequestParam (required = false) String name){
        Class<T> clazz = service().getEntityClass(); // 구현 클래스에서 제공된 T의 클래스 타입 사용
        List<T> entities = service().queryByQBE(key,cd, name, clazz );
        return ResponseEntity.ok(toDTO(entities));
    }
}
