package com.example.rail.listener;


import com.example.rail.pool.CalculateThreadPool;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 *
 * 功能描述: 监听线程池的执行
 *
 * @param:
 * @return: 
 * @auther: zhzy
 * @date: 2019/07/23 17:30
 */
@Component
public class WebMonitor implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		CalculateThreadPool.externalInitialize(args);
	}
}
