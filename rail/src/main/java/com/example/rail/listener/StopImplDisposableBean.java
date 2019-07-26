package com.example.rail.listener;

import com.example.rail.pool.CalculateThreadPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

/**
 * @Name: com.example.rail.listener
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/24 16:06
 */
@Component
public class StopImplDisposableBean implements DisposableBean, ExitCodeGenerator {


    @Override
    public void destroy() throws Exception {
        CalculateThreadPool.ExternalStop();
    }

    @Override
    public int getExitCode() {

        return 5;
    }
}
