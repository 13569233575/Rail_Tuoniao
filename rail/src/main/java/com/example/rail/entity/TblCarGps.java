package com.example.rail.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Name: com.example.rail.entity
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/23 16:36
 */
@Data
public class TblCarGps implements Serializable {

    private String id;
    private String sn;
    private String carno;
    private Double speed;
    private Double miles;
    private Double movement;
    private String TTFF;
    private Double longitude;
    private Double latitude;

    @Override
    public String toString() {
        return "TblCarGps{" +
                "id='" + id + '\'' +
                ", sn='" + sn + '\'' +
                ", carno='" + carno + '\'' +
                ", speed=" + speed +
                ", miles=" + miles +
                ", movement=" + movement +
                ", TTFF='" + TTFF + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
