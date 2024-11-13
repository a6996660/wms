package com.project.wms.service.SchedulerService.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.project.wms.service.HFWeather.heFengWeatherService;

@Service
public class TaskService extends ServiceImpl<ScheduleMapper, Taskschedule> implements ITaskService {

    @Autowired
    private heFengWeatherService heFengWeatherService;

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
        return scheduleMapper.pageCC(page, wrapper);
    }

    @Override
    public Result enableTask(Taskschedule taskschedule, String type) {
        Result result = new Result();
        if (taskschedule.getCron() == null || taskschedule.getTaskid() == null || taskschedule.getParams() == null) {
            return Result.fail("参数错误");
        }
        //获取定时规则
        String cronExpression = taskschedule.getCron();
        String taskType = taskschedule.getTasktype();
        JSONObject params = JSON.parseObject(taskschedule.getParams());
        Map<String, Object> requestBody = getRequestBodyAndCheck(params);
        String taskId = taskschedule.getTaskid();
        String name = taskschedule.getName();
        String message = "";
        switch (taskType) {
            case "1": //想某人发送消息
                message = addTaskType1(cronExpression, taskId, requestBody, name);
                break;
            case "2"://发送天气信息
                message = addTaskType2(cronExpression, taskId, requestBody, name);
                break;
            default:
                return Result.fail("没有该任务类型");
        }

        LambdaUpdateWrapper<Taskschedule> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Taskschedule::getId, taskschedule.getId()).set(Taskschedule::getStatus, type);
        boolean update = this.update(updateWrapper);
        if (update) {
            return result.suc(message);
        } else {
            result.fail(message);
            return result;
        }
    }

    private String addTaskType1(String cronExpression, String taskId, Map<String, Object> body, String name) {
        this.addTask(taskId, cronExpression, () -> {
            System.out.println("[" + name + "]任务开始执行:" + taskId);
            String time = getCurrentTime();
            String message = heFengWeatherService.sendWebhookMessage(time, (Boolean) body.get("isRoom"), body.get("name").toString(), body.get("url").toString());
            System.out.println(message);
            System.out.println("[" + name + "]定时任务执行完毕:" + taskId);
        });
        return name + "任务添加成功";
    }

    private String addTaskType2(String cronExpression, String taskId, Map<String, Object> body, String name) {
        this.addTask(taskId, cronExpression, () -> {
            System.out.println("[" + name + "]任务开始执行:" + taskId);
            heFengWeatherService.weatherService(body);
            System.out.println("[" + name + "]定时任务执行完毕:" + taskId);
        });
        return name + "任务添加成功";
    }

    /**
     * 获取当前时间并转换为 2024-11-7 23:30:00 格式
     */
    public String getCurrentTime() {
        //获取当前时间并转换为 2024-11-7 23:30:00 格式
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        return time;
    }

    private Map<String, Object> getRequestBodyAndCheck(JSONObject body) {
        Map<String, Object> requestBody = new HashMap<>();
        if (body.get("city") == null) throw new RuntimeException("city参数为空");
        if (body.get("api") == null) throw new RuntimeException("api参数为空");
        if (body.get("url") == null) throw new RuntimeException("url参数为空");
        if (body.get("isRoom") == null) throw new RuntimeException("是否为群聊参数为空");
        if (body.get("name") == null) throw new RuntimeException("name参数为空");
        List<String> cityList = (List<String>) body.get("city");
        requestBody.put("city", cityList);
        requestBody.put("api", body.get("api")); //d55bd7f0e0524eef9f264b2fdc29b4fe
        requestBody.put("url", body.get("url")); //http://192.168.5.139:3001/webhook/msg/v2?token=dingxhui
        requestBody.put("isRoom", body.get("isRoom"));
        requestBody.put("name", body.get("name"));
        return requestBody;
    }

    @Override
    public Result stopTask(Taskschedule taskschedule, String type) {
        Result result = new Result();
        if (taskschedule.getTaskid() == null)
            return result.fail("taskid参数丢失");
        String taskId = taskschedule.getTaskid();
        cancelTask(taskId);
        LambdaUpdateWrapper<Taskschedule> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Taskschedule::getId, taskschedule.getId()).set(Taskschedule::getStatus, type);
        boolean update = this.update(updateWrapper);
        if (update) {
            return result.suc(taskschedule.getName() + "任务停止成功");
        } else {
            return result.fail(taskschedule.getName() + "任务停止失败");
        }
    }

    /**
     * 查询当前正在运行的任务
     */
    @Override
    public Result queryEnableTask() {
        Result result = new Result();
        List<String> taskIdList = new ArrayList<>(); 
        for (String key : scheduledTasks.keySet()){
            taskIdList.add(key);
        }
        return result.suc(taskIdList);
    }


    @Autowired
    public TaskService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * 动态添加定时任务
     *
     * @param taskId         任务ID
     * @param cronExpression Cron表达式
     * @param runnable       任务逻辑
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
     *
     * @param taskId 任务ID
     */
    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = scheduledTasks.remove(taskId);
        if (future != null) {
            future.cancel(false);
        }
    }
}

