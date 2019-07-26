package com.example.rail.entity.carrail;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Name: com.example.rail.entity.carrail
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/26 09:50
 */
@Data
public class RailInfo implements Serializable {

    private Integer fenceId;//围栏ID
    /**
     * 1:提箱点,2:门点,3:还箱点
     */
    private Integer type;
    private Integer seqNo;//保留字段
    /**
     * 1:圆形,2:矩形,3:多边形
     */
    private Integer fenceType;
    private String pointLot;// 中心点经度（圆形围栏）
    private String pointLat;// 中心点纬度（圆形围栏）
    private String radius;// 圆心半径
    private String topLot;// 左上经度（矩形围栏）
    private String topLat;// 左上纬度（矩形围栏）
    private String bottomLot;// 右下经度（矩形围栏）
    private String bottomLat;// 右下纬度（矩形围栏）
    private List<RailPolygon> polygonList;//多边形围栏


}
