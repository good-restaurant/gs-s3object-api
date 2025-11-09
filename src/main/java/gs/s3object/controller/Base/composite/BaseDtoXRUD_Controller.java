package gs.s3object.controller.Base.composite;


import gs.s3object.controller.Base.delete.BaseDeleteDtoController;
import gs.s3object.controller.Base.update.BaseUpdateDtoController;
import gs.s3object.controller.Base.view.BaseViewDtoController;
import gs.s3object.service.A_Impl.BaseCRUD;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public interface BaseDtoXRUD_Controller<D,T,ID> extends
		BaseViewDtoController<D,T,ID>,
				BaseDeleteDtoController<D,T,ID>,
				BaseUpdateDtoController<D,T,ID> {

    BaseCRUD<T,ID> service();
//
//    // DTO 변환 메서드 (컨트롤러에서 서비스를 호출하는 얕은 레이어로 처리)
//    D toDTO(T entity);
//    List<D> toDTO(List<T> entity);
//
//    T fromDTO(D dto);
//    List<T> fromDTO(List<D> dto);
//    //</editor-fold>
}
