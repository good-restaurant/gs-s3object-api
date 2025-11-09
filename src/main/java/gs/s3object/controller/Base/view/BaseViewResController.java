package gs.s3object.controller.Base.view;


import gs.s3object.controller.Base.a_service.BaseServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p> Response DTO 가 있는 컨트롤러들의 기본동작 정의 </p>
 * <p> RES = Response DTO </p>
 * <p> T = Entity </p>
 * <p> 엔티티 리턴 하지마세요
 */
@RestController
@RequestMapping("/api/v1")
public interface BaseViewResController <RES,T,ID> extends BaseServiceInterface<T,ID> {

    /**
     * @param idValue 단일 조회 기능
     * @return D DTO
     * <p>
     * {@code @GetMapping("/{id}")}
     * or
     * {@code @GetMapping("/res/{id}")}
     * </p>
     */
    @GetMapping("/{idValue}")
    default ResponseEntity<RES> readAsRes(@PathVariable ID idValue){
        T entity = service().getById(idValue);
        return ResponseEntity.ok(toRES(entity));
    }
	
	/**
     * @return 전체 목록을 RES DTO 의 List 형태로 나옵니다
     * @GetMapping
     * or
     * {@code @GetMapping("/res")}
     */
    @GetMapping("/all")
    default ResponseEntity<List<RES>> listAllAsRes(){
        List<T> entities = service().getAll();
        return ResponseEntity.ok(toRES(entities));
    }
	
	/**
     * @param page 페이지 몇 페이지
     * @param size 페이지당 사이즈
     * @return 페이지 형태의 DTO 를 반환홥니다.
     * 전체 항목이 사이즈보다 작은 경우, 출력이 제대로 안됩니다.
     * {@code @GetMapping("/page") or @GetMapping("/res/page")}
     */
    @GetMapping("/page")
    default ResponseEntity<Page<RES>> listPageAsRes(@RequestParam int page, @RequestParam int size){
        Page<T> entities = service().getAll(PageRequest.of(page, size));
        Page<RES> dtoPage = entities.map(this::toRES);
        return ResponseEntity.ok(dtoPage);
    }
	
	/**
     * @param key 키 값 (일치)
     * @param cd   코드 (부분 일치)
     * @param name 이름 (부분 일치)
     * @return RES DTO 목록 반환
     * {@code @GetMapping("/search")}
     * <p>or</p>
     * {@code @GetMapping("/res/search")}
     *
     */
    @GetMapping("/search")
    default ResponseEntity<List<RES>> searchAsRes(
            @RequestParam(required = false) Integer key,
            @RequestParam(required = false) String cd,
            @RequestParam(required = false) String name
    ){
        Class<T> clazz = service().getEntityClass(); // 구현 클래스에서 제공된 T의 클래스 타입 사용
        List<RES> dto = toRES(service().queryByQBE(key, cd, name, clazz));
        return ResponseEntity.ok(dto);
    }
	
	// 컨트롤러의 매핑 서비스를 불러오는 메소드
    RES toRES(T entity);
    List<RES> toRES(List<T> entity);
}
