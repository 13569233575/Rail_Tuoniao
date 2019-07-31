package com.example.rail.pool;

import com.example.rail.entity.result.ResultGps;
import com.example.rail.service.RailService;
import com.example.rail.service.impl.RailServiceImpl;
import com.example.rail.utils.SpringContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.ApplicationArguments;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @Name: com.cvnavi.comm.jobPool.queue
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-05-2019/05/10 15:24
 */
@Slf4j
@AllArgsConstructor
public class CalculateThreadPool {

    //CPU的核心数
    private static int CPU_NUMBER = Runtime.getRuntime().availableProcessors();
    //存储需要执行的线程
    private static BlockingQueue<ResultGps> calculateBlockingQueue = new LinkedBlockingQueue<>(100000);

    private static final String ART = "Calculate-GPS-Thread";

    public static final ThreadFactory tFactory = new BasicThreadFactory.Builder().namingPattern(ART + "-%d").build();

    //执行退出
    private static volatile boolean stop = false;

    //需要执行的线程池
    private static ExecutorService executorService = null;

    private static RailService railService;
    /**
     *
     * 功能描述: 将任务存储到当前队列中
     *
     * @param:
     * @return: 
     * @auther: zhzy
     * @date: 2019/05/10 15:35
     */
    public static <T> void putCalculateGps(ResultGps resultGps){

        calculateBlockingQueue.add(resultGps);
    }


    public static void externalInitialize(ApplicationArguments context) {
        executorService = new ThreadPoolExecutor(CPU_NUMBER/2,CPU_NUMBER,5, TimeUnit.SECONDS,new SynchronousQueue<>(),tFactory);
        railService = SpringContextHolder.getBean(RailServiceImpl.class);
        externalThreadInitialize();
    }

    public static void externalThreadInitialize(){
        //初始化时向线程池提交5个任务
        for (int i = 0; i < CPU_NUMBER/2; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (!stop) {
                        //执行consume
                        externalSubConsume();
                    }
                }
            });
        }
    }

    static void externalSubConsume() {
        try {
            log.info("数据正常输入, 时间: " + new Date().toString());
            //执行对应的操作
            ResultGps resultGps = calculateBlockingQueue.take();
            if(resultGps != null){
                // 执行对应的数据判断操作
                boolean inCircle = railService.judgementInCircle(resultGps);
                log.info("当前执行的经纬度坐标点判断 : " + inCircle);

            }
        } catch (InterruptedException e) {
            log.info(e.getMessage() + "阻塞线程异常问题");
        }
    }

    public static void ExternalStop() {
        try {
            stop = true;//结束消费
            executorService.shutdownNow();
            log.info("结束消费,让springBoot优雅的关闭");
        } catch (Exception ex) {

        }
    }


}
