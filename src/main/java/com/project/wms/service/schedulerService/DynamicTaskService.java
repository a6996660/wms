package com.project.wms.service.schedulerService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DynamicTaskService {

    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    public DynamicTaskService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * 动态添加定时任务
     * @param taskId 任务ID
     * @param cronExpression Cron表达式
     * @param runnable 任务逻辑
     */
    public void addTask(String taskId, String cronExpression, Runnable runnable) {
        if (scheduledTasks.containsKey(taskId)) {
            cancelTask(taskId);
        }
        ScheduledFuture<?> future = taskScheduler.schedule(runnable, new CronTrigger(cronExpression));
        scheduledTasks.put(taskId, future);
    }

    /**
     * 取消定时任务
     * @param taskId 任务ID
     */
    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = scheduledTasks.remove(taskId);
        if (future != null) {
            future.cancel(false);
        }
    }
}

