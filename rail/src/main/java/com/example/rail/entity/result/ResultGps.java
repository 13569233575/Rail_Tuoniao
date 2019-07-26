package com.example.rail.entity.result;

import com.example.rail.entity.TblCarGps;
import com.example.rail.entity.carrail.CarRailInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Name: com.example.rail.entity.result
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/23 17:16
 */
@Data
public class ResultGps implements Serializable {

    private TblCarGps tblCarGps;//经纬度坐标点
    private CarRailInfo carRailInfo;//车辆围栏关联信息
}
