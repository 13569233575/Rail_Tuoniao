package com.example.rail.entity.carrail;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Name: com.example.rail.entity.carrail
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/26 09:48
 */
@Data
public class CarRailInfo implements Serializable {

    private String carNo;//车牌号
    List<RailInfo> fenceList;
}
