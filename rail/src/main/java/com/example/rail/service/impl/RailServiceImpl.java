package com.example.rail.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.rail.entity.carrail.RailInfo;
import com.example.rail.entity.carrail.RailPolygon;
import com.example.rail.entity.result.ResultGps;
import com.example.rail.service.RailService;
import com.example.rail.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Name: com.example.rail.service.impl
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/25 13:39
 */
@Service
@Slf4j
public class RailServiceImpl implements RailService {
    @Autowired
    private RedisTemplate redisTemplate;
    private String rail = "_RAIL";
    @Override
    public boolean judgementInCircle(ResultGps resultGps) {
        boolean exist = false;

        List<RailInfo> list = resultGps.getCarRailInfo().getFenceList();
        if(list == null || list.isEmpty())return Boolean.FALSE;

        StringBuffer fenceIds = new StringBuffer();
        StringBuffer turnoverRails = new StringBuffer();
        Map<String,String> map = new ConcurrentHashMap<>();
        map.put("carNo",resultGps.getTblCarGps().getCarno());
        for (RailInfo railInfo : list) {

            /**
             * 1 进区域 2 出区域
             */
            Object object = redisTemplate.opsForValue().get(resultGps.getTblCarGps().getCarno()+rail+"_"+railInfo.getFenceId());
            // 查询redis是否已经执行进出区域的判断
            String turnoverRail = object ==null?"2": (String) object;

            switch (railInfo.getFenceType()){
                //1:圆形,2:矩形,3:多边形
                case 1:
                    exist = MapUtil.isInCircle(resultGps.getTblCarGps().getLongitude(),resultGps.getTblCarGps().getLatitude(),Double.valueOf(railInfo.getPointLot()),Double.valueOf(railInfo.getPointLat()),String.valueOf(railInfo.getRadius()));
                    // 初始默认车辆的都是出区域 当判断上报的经纬度在区域内 需要将出区域更新为进区域 执行更新 推送数据到Redis 发布信息
                    // 如果查询 到的车辆为出区域 当前状态也是出区域 放行

                    // 如果查询到的车辆为进区域 当前状态为出区域 执行更新 推送数据到Redis 发布信息
                    log.info("圆形围栏判断 : " + exist + " _车牌号 : " + resultGps.getCarRailInfo().getCarNo());
                    setInfo(exist,turnoverRail,resultGps,fenceIds,turnoverRails,railInfo);
                    break;
                case 2:
                    exist = MapUtil.isInRectangleArea(resultGps.getTblCarGps().getLongitude(),resultGps.getTblCarGps().getLatitude(),Double.valueOf(railInfo.getTopLot()),Double.valueOf(railInfo.getBottomLot()),Double.valueOf(railInfo.getTopLat()),Double.valueOf(railInfo.getBottomLat()));
                    log.info("矩形围栏判断 : " + exist + " _车牌号 : " + resultGps.getCarRailInfo().getCarNo());
                    setInfo(exist,turnoverRail,resultGps,fenceIds,turnoverRails,railInfo);
                    break;
                case 3:
                    // 多边形判断1
                    List<Point2D.Double> pointList = new ArrayList<>();
                    List<RailPolygon> polygonList  = railInfo.getPolygonList();
                    double polygonPoint_x , polygonPoint_y ;
                    for (RailPolygon railPolygon : polygonList) {
                        polygonPoint_x = Double.valueOf(railPolygon.getPolygonLot());
                        polygonPoint_y = Double.valueOf(railPolygon.getPolygonLat());
                        Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x, polygonPoint_y);
                        pointList.add(polygonPoint);
                    }
                    exist = MapUtil.isInPolygon(resultGps.getTblCarGps().getLongitude(),resultGps.getTblCarGps().getLatitude(),pointList);
                    log.info("多边形围栏判断 : " + exist + " _车牌号 : " + resultGps.getCarRailInfo().getCarNo());
                    setInfo(exist,turnoverRail,resultGps,fenceIds,turnoverRails,railInfo);
                    //多边形判断2
                    //MapUtil.isPointInPolygonBoundary();
                    break;
                    default:
            }
        }
        if(StringUtils.isNotBlank(fenceIds.toString())){
            map.put("fenceIds",fenceIds.toString().substring(0,fenceIds.toString().lastIndexOf(",")));
            map.put("turnoverRails",turnoverRails.toString().substring(0,turnoverRails.toString().lastIndexOf(",")));
            redisTemplate.convertAndSend("RAIL_DEFINITION", JSON.toJSONString(map));
        }
        return exist;
    }


    private String setInfo(boolean exist,String turnoverRail,ResultGps resultGps,StringBuffer fenceIds,StringBuffer turnoverRails,RailInfo railInfo){
        if(exist){
            if("2".equals(turnoverRail)){
                turnoverRail = "1";
                redisTemplate.opsForValue().set(resultGps.getTblCarGps().getCarno()+rail+"_"+railInfo.getFenceId(),turnoverRail);
                fenceIds.append(railInfo.getFenceId()+",");
                turnoverRails.append(turnoverRail+",");
            }
        }else {
            if("1".equals(turnoverRail)){
                turnoverRail = "2";
                redisTemplate.opsForValue().set(resultGps.getTblCarGps().getCarno()+rail+"_"+railInfo.getFenceId(),turnoverRail);
                fenceIds.append(railInfo.getFenceId()+",");
                turnoverRails.append(turnoverRail+",");
            }
        }
        return turnoverRail;
    }
}
