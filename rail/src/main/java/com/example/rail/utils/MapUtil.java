package com.example.rail.utils;

import com.google.type.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * 功能描述: 解析坐标点是否存在围栏中
 *
 * @param:
 * @return: 
 * @auther: zhzy
 * @date: 2019/07/23 16:56
 */
@Component
@Slf4j
public class MapUtil {

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        return s;
    }

    /**
     * 判断一个点是否在圆形区域内
     */
    public static boolean isInCircle(double lng1, double lat1, double lng2, double lat2, String radius) {
        double distance = getDistance(lat1, lng1, lat2, lng2);
        double r = Double.parseDouble(radius);
        if (distance > r) {
            return false;
        } else {
            return true;
        }
    }



    /**
     * 判断是否在多边形区域内
     *
     * @param pointLon
     *            要判断的点的纵坐标
     * @param pointLat
     *            要判断的点的横坐标
     * @param pointList
     *            区域各顶点的横纵坐标放到一个点集合里面
     * @param
     * @return
     */
    public static boolean isInPolygon(double pointLon, double pointLat, List<Point2D.Double> pointList) {
        // 将要判断的横纵坐标组成一个点
        Point2D.Double point = new Point2D.Double(pointLon, pointLat);
        /* 将区域各顶点的横纵坐标放到一个点集合里面
        List<Point2D.Double> pointList = new ArrayList<Point2D.Double>();
        double polygonPoint_x = 0.0, polygonPoint_y = 0.0;
        for (int i = 0; i < lon.length; i++) {
            polygonPoint_x = lon[i];
            polygonPoint_y = lat[i];
            Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x, polygonPoint_y);
            pointList.add(polygonPoint);
        }*/
        return check(point, pointList);
    }

    /**
     * 一个点是否在多边形内
     *
     * @param point
     *            要判断的点的横纵坐标
     * @param polygon
     *            组成的顶点坐标集合
     * @return
     */
    private static boolean check(Point2D.Double point, List<Point2D.Double> polygon) {
        java.awt.geom.GeneralPath peneralPath = new java.awt.geom.GeneralPath();

        Point2D.Double first = polygon.get(0);
        // 通过移动到指定坐标（以双精度指定），将一个点添加到路径中
        peneralPath.moveTo(first.x, first.y);
        polygon.remove(0);
        for (Point2D.Double d : polygon) {
            // 通过绘制一条从当前坐标到新指定坐标（以双精度指定）的直线，将一个点添加到路径中。
            peneralPath.lineTo(d.x, d.y);
        }
        // 将几何多边形封闭
        peneralPath.lineTo(first.x, first.y);
        peneralPath.closePath();
        // 测试指定的 Point2D 是否在 Shape 的边界内。
        return peneralPath.contains(point);
    }


    /**
     * 是否在矩形区域内
     * @Title: isInArea
     * @Description: TODO()
     * @param lat 测试点经度
     * @param lng 测试点纬度
     * @param minLat 纬度范围限制1
     * @param maxLat 纬度范围限制2
     * @param minLng 经度限制范围1
     * @param maxLng 经度范围限制2
     * @return
     * @return boolean
     * @throws
     */
    public static boolean isInRectangleArea(double lng,double lat,double minLng,double maxLng,double minLat,double maxLat){
        if(isInRange(lat, minLat, maxLat)){//如果在纬度的范围内
            if(minLng*maxLng>0){
                if(isInRange(lng, minLng, maxLng)){
                    return true;
                }else {
                    return false;
                }
            }else {
                if(Math.abs(minLng)+Math.abs(maxLng)<180){
                    if(isInRange(lng, minLng, maxLng)){
                        return true;
                    }else {
                        return false;
                    }
                }else{
                    double left = Math.max(minLng, maxLng);
                    double right = Math.min(minLng, maxLng);
                    if(isInRange(lng, left, 180)||isInRange(lng, right,-180)){
                        return true;
                    }else {
                        return false;
                    }
                }
            }
        }else{
            return false;
        }
    }


    /**
     * 判断是否在经纬度范围内
     * @Title: isInRange
     * @Description: TODO()
     * @param point
     * @param left
     * @param right
     * @return
     * @return boolean
     * @throws
     */
    public static boolean isInRange(double point, double left,double right){
        if(point>=Math.min(left, right)&&point<=Math.max(left, right)){
            return true;
        }else {
            return false;
        }
    }



    /**
     * 返回一个点是否在一个多边形边界上
     *
     * @param mPoints 多边形坐标点列表
     * @param point   待判断点
     * @return true 点在多边形边上,false 点不在多边形边上。
     */
    public static boolean isPointInPolygonBoundary(List<LatLng> mPoints, LatLng point) {
        for (int i = 0; i < mPoints.size(); i++) {
            LatLng p1 = mPoints.get(i);
            LatLng p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数

            // point 在p1p2 底部 --> 无交点
            if (point.getLongitude() < Math.min(p1.getLongitude(), p2.getLongitude()))
                continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.getLongitude() > Math.max(p1.getLongitude(), p2.getLongitude()))
                continue;

            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.getLongitude() == p2.getLongitude()) {
                double minX = Math.min(p1.getLatitude(), p2.getLatitude());
                double maxX = Math.max(p1.getLatitude(), p2.getLatitude());
                // point在水平线段p1p2上,直接return true
                if ((point.getLongitude() == p1.getLongitude()) && (point.getLatitude() >= minX && point.getLatitude() <= maxX)) {
                    return true;
                }
            } else { // 求解交点
                double x = (point.getLongitude() - p1.getLongitude()) * (p2.getLatitude() - p1.getLatitude()) / (p2.getLongitude() - p1.getLongitude()) + p1.getLatitude();
                if (x == point.getLatitude()) // 当x=point.x时,说明point在p1p2线段上
                    return true;
            }
        }
        return false;
    }

}
