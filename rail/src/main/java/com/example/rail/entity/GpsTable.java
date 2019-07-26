package com.example.rail.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 解析binlog数据
 * @Name: com.example.rail.entity
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/23 16:31
 */
@Data
public class GpsTable implements Serializable {
    private String database;
    private String table;
    private String type;
    private String ts;
    private String xid;
    private String commit;
    private TblCarGps data;

    @Override
    public String toString() {
        return "GpsTable{" +
                "database='" + database + '\'' +
                ", table='" + table + '\'' +
                ", type='" + type + '\'' +
                ", ts='" + ts + '\'' +
                ", xid='" + xid + '\'' +
                ", commit='" + commit + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
