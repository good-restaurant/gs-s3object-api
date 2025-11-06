package gs.s3object.service.A_Impl;

public interface ServiceBase<T,ID, SELF extends BaseCRUD<T,ID>> extends BaseCRUD<T, ID> {
    SELF getImplService();
}
