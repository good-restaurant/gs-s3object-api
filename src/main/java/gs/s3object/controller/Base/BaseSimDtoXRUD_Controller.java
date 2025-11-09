package gs.s3object.controller.Base;

import gs.s3object.controller.Base.delete.BaseDeleteDtoController;
import gs.s3object.controller.Base.update.BaseUpdateDtoController;
import gs.s3object.controller.Base.view.BaseViewDtoController;
import gs.s3object.controller.Base.view.BaseViewParamDtoController;
import gs.s3object.service.A_Impl.BaseCRUD;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public interface BaseSimDtoXRUD_Controller<D,SIM,T,ID> extends
		BaseViewDtoController<D,T,ID>,
				BaseDeleteDtoController<D,T,ID>,
				BaseUpdateDtoController<D,T,ID>,
				BaseViewParamDtoController<SIM,T,ID>
{
	BaseCRUD<T,ID> service();
}