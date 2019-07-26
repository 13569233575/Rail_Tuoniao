package com.example.rail.kafka;

import com.alibaba.fastjson.JSON;
import com.example.rail.entity.GpsTable;
import com.example.rail.entity.carrail.CarRailInfo;
import com.example.rail.entity.carrail.RailInfo;
import com.example.rail.entity.carrail.RailPolygon;
import com.example.rail.entity.result.ResultGps;
import com.example.rail.pool.CalculateThreadPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @Name: com.example.rail.kafka
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/23 15:03
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RawDataListener {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 实时获取kafka数据(生产一条，监听生产topic自动消费一条)
     * @param record
     * @throws
     */
    @KafkaListener(topicPattern = "${kafka.consumer.topic}", groupId = "${kafka.consumer.group.id}")
    public void listen(ConsumerRecord<?, ?> record) {
        String value = (String) record.value();
        GpsTable gpsTable = JSON.parseObject(value,GpsTable.class);
        if("tbl_car_gps".equals(gpsTable.getTable())){
            if("true".equals(gpsTable.getCommit())){
                if("insert".equals(gpsTable.getType())){
                    ResultGps resultGps = new ResultGps();
                    resultGps.setTblCarGps(gpsTable.getData());
                    String jsonV = (String) redisTemplate.opsForValue().get(resultGps.getTblCarGps().getCarno());
                    try{
                        //redisTemplate.opsForValue().set("浙FJ5333",JSON.toJSONString(setCarRilInfo()));
                        CarRailInfo carRailInfo =JSON.parseObject(jsonV,CarRailInfo.class);
                        if(carRailInfo != null && (carRailInfo.getFenceList() != null && !carRailInfo.getFenceList().isEmpty())){
                            //log.info("执行MAP解析,判定经纬度是否在围栏区域内, 车牌号: " + carRailInfo.getCarNo());
                            resultGps.setCarRailInfo(carRailInfo);
                            CalculateThreadPool.putCalculateGps(resultGps);
                        }
                    }catch (Exception e){
                        log.info("Redis存储数据解析异常//MAP解析异常",e.getMessage());
                    }
                }
            }
        }
    }


    private static CarRailInfo setCarRilInfo(){
        CarRailInfo carRailInfo = new CarRailInfo();
        carRailInfo.setCarNo("浙FJ5333");
        List<RailInfo> railInfoList = new ArrayList<>();
        RailInfo railInfo1 = new RailInfo();
        railInfo1.setFenceId(1);
        railInfo1.setType(1);
        railInfo1.setFenceType(1);
        railInfo1.setPointLot("108.7488494575");
        railInfo1.setPointLat("34.3386216525");
        railInfo1.setRadius("200.52");
        railInfoList.add(railInfo1);

        RailInfo railInfo2 = new RailInfo();
        railInfo2.setFenceId(2);
        railInfo2.setType(2);
        railInfo2.setFenceType(2);
        railInfo2.setTopLot("108.7509085340");
        railInfo2.setTopLat("34.3468419735");
        railInfo2.setBottomLot("108.7481656020");
        railInfo2.setBottomLat("34.3280631679");
        railInfoList.add(railInfo2);

        RailInfo railInfo3 = new RailInfo();
        railInfo3.setFenceId(3);
        railInfo3.setType(3);
        railInfo3.setFenceType(3);
        List<RailPolygon> railPolygonList = new ArrayList<>();
        RailPolygon railPolygon1 = new RailPolygon();
        railPolygon1.setPolygonLat("34.3400923430");
        railPolygon1.setPolygonLot("108.7555233579");
        railPolygon1.setSeqNo(1);
        railPolygonList.add(railPolygon1);
        RailPolygon railPolygon2 = new RailPolygon();
        railPolygon2.setPolygonLat("34.3407832197");
        railPolygon2.setPolygonLot("108.7530122459");
        railPolygon2.setSeqNo(2);
        railPolygonList.add(railPolygon2);
        RailPolygon railPolygon3 = new RailPolygon();
        railPolygon3.setPolygonLat("34.3440786166");
        railPolygon3.setPolygonLot("108.7547717750");
        railPolygon3.setSeqNo(3);
        railPolygonList.add(railPolygon3);
        RailPolygon railPolygon4 = new RailPolygon();
        railPolygon4.setPolygonLat("34.3448404373");
        railPolygon4.setPolygonLot("108.7489138305");
        railPolygon4.setSeqNo(4);
        railPolygonList.add(railPolygon4);
        RailPolygon railPolygon5 = new RailPolygon();
        railPolygon5.setPolygonLat("34.3392772157");
        railPolygon5.setPolygonLot("108.7511883438");
        railPolygon5.setSeqNo(5);
        railPolygonList.add(railPolygon5);
        railInfo3.setPolygonList(railPolygonList);
        railInfoList.add(railInfo3);
        carRailInfo.setFenceList(railInfoList);
        return carRailInfo;
    }
}
