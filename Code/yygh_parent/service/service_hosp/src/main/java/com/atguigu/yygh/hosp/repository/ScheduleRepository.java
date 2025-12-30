package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Henry Guan
 * @description 排班列表
 * @since 2023-04-07
 */

public interface ScheduleRepository extends MongoRepository<Schedule,String> {

    Schedule findByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> findByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date workdate);

    Schedule findByHosScheduleId(String scheduleId);
}
