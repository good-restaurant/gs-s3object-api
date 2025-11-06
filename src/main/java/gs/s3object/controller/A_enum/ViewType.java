package gs.s3object.controller.A_enum;


import gs.s3object.service.A_Exception.EntityNotFoundException;

/**
 * 다양한 경우에 맞는 view Type 을 정의해서 조회 할 때 명시적으로 데이터 형태를 DTO 와 연결하도록 함
 * SIMPLE 은 Simple 한 DTO 를 선언할 때 사용함
 */
public enum ViewType {
    // SIMPLE DTO 만 일단 사용함

    SIMPLE,
    DETAILED,
    OTHERS;

    public static ViewType from(String view) {
        try {
            return ViewType.valueOf(view.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Unsupported view type: " + view);
        }
    }
}