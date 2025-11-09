package gs.s3object.controller.Base.composite;

import gs.s3object.controller.Base.delete.BaseDeleteReqController;
import gs.s3object.controller.Base.update.BaseUpdateReqDtoController;
import gs.s3object.controller.Base.view.BaseViewDtoController;
import gs.s3object.service.A_Impl.BaseCRUD;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public interface BaseReqDtoXRUD_Controller<REQ,DTO,T,ID> extends
		BaseViewDtoController<DTO,T,ID>,
				BaseUpdateReqDtoController<REQ,DTO,T,ID>,
				BaseDeleteReqController<REQ, T, ID>
{

    BaseCRUD<T,ID> service();

}
