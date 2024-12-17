package com.example.htaproject2308.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import java.time.LocalDateTime;

@Service
public class ScheduleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

    @Scheduled(cron = "0 * * * * ?") // 매분마다 실행
    @SchedulerLock(name = "ScheduledTask_run")
    public void runTask() throws InterruptedException{

        System.out.println("task1: "+ LocalDateTime.now());
    }
    @Scheduled(cron = "0 * * * * ?") // 매분마다 실행
    @SchedulerLock(name = "ScheduledTask_run")
    public void runTask2() throws InterruptedException{

        System.out.println("task2: "+  LocalDateTime.now());
    }
}
