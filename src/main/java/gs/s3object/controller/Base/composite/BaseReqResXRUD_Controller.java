package gs.s3object.controller.Base.composite;

import gs.s3object.controller.Base.delete.BaseDeleteResController;
import gs.s3object.controller.Base.update.BaseUpdateReqResController;
import gs.s3object.controller.Base.view.BaseViewResController;
import gs.s3object.service.A_Impl.BaseCRUD;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
public interface BaseReqResXRUD_Controller<REQ,RES,T,ID> extends
		BaseViewResController<RES,T,ID>,
				BaseUpdateReqResController<REQ,RES,T,ID>,
				BaseDeleteResController<RES,T,ID>
{
    BaseCRUD<T,ID> service();

    // 컨트롤러의 매핑 서비스를 불러오는 메소드
    RES toRES(T entity);
    List<RES> toRES(List<T> entity);

    T fromREQ(REQ req);
    List<T> fromREQ(List<REQ> req);
}
