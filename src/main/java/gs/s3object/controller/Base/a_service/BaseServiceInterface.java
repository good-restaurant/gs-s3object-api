package gs.s3object.controller.Base.a_service;

import gs.s3object.service.A_Impl.BaseCRUD;

public interface BaseServiceInterface<T,ID>  {
    BaseCRUD<T,ID> service();
}
