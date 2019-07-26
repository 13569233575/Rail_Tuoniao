package com.example.rail.entity.carrail;

import lombok.Data;

import java.io.Serializable;

/**
 * @Name: com.example.rail.entity.carrail
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/26 09:59
 */
@Data
public class RailPolygon implements Serializable {

    private String polygonLot;// 经度
    private String polygonLat;// 纬度
    private Integer seqNo;//编号

}
