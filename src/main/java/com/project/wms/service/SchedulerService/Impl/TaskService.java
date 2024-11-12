package com.project.wms.service.SchedulerService.Impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.wms.entity.schedule.Taskschedule;
import com.project.wms.mapper.scheduleMapper.ScheduleMapper;
import com.project.wms.service.SchedulerService.ITaskService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import com.project.wms.common.Result;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.ScheduledFuture;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService extends ServiceImpl<ScheduleMapper, Taskschedule> implements ITaskService {

    @Resource
    private ScheduleMapper scheduleMapper;
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Override
    public IPage pageC(IPage<Taskschedule> page) {
        return scheduleMapper.pageC(page);
    }

    @Override
    public IPage pageCC(IPage<Taskschedule> page, Wrapper wrapper) {
        return scheduleMapper.pageCC(page,wrapper);
    }

    @Override
    public Result enableTask(Taskschedule taskschedule, String type) {
        Result result = new Result();
        LambdaUpdateWrapper<Taskschedule> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Taskschedule::getId, taskschedule.getId())
                .set(Taskschedule::getStatus, type);
        boolean update = this.update(updateWrapper);
        if (update) {
            return result.suc("修改成功");
        } else {
            result.fail();
            return result;
        }
    }

    

    @Autowired
    public TaskService(TaskScheduler taskScheduler) {
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

