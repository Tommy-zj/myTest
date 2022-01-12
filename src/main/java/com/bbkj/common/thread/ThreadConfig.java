package com.bbkj.common.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2022/1/4 20:20
 */
@Configuration
@ComponentScan("com.bbkj.common.thread")
@EnableAsync //启用一步任务
public class ThreadConfig {
    // 这里声明一个bean,类似于xml中的<bean>标签。
    // Executor就是一个线程池
    @Bean
    public Executor getExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(7);
        executor.setQueueCapacity(18);
        executor.initialize();
        return executor;
    }
}
