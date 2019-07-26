package com.example.rail.service;

import com.example.rail.entity.result.ResultGps;

/**
 * @Name: com.example.rail.service
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/25 13:39
 */
public interface RailService {

    boolean judgementInCircle(ResultGps resultGps);
}
