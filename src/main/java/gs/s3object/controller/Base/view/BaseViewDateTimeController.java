package gs.s3object.controller.Base.view;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public interface BaseViewDateTimeController<RES,D,T> {

    /**
     * @param start 시작날짜, SimpleDateTimeFormat 을 강제하므로 yyyymmdd 형태만 받습니다.
     * @param end 끝 날짜, SimpleDateTimeFormat 을 강제하므로 yyyymmdd 형태만 받습니다.
     * @return List<DTO>  일반 DTO 혹은 RES DTO 중 1개
     */
    @GetMapping("/date")
    ResponseEntity<List<?>> readAsDateTime(
        @RequestParam(name = "fdate", required = true) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start,
        @RequestParam(name = "tdate", required = true) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end
    );
}
