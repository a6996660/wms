package com.project.wms.service.SchedulerService.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.wms.common.QueryPageParam;
import com.project.wms.entity.schedule.Taskschedule;
import com.project.wms.mapper.scheduleMapper.ScheduleMapper;
import com.project.wms.service.ChatGPT.IDouBaoApi;
import com.project.wms.service.ManageService.ILoanrecordsService;
import com.project.wms.service.ManageService.ILogService;
import com.project.wms.service.SchedulerService.ITaskService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
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
    //日志记录
    @Autowired
    private ILoanrecordsService loanrecordsService;
    //加载Log
    @Autowired
    private ILogService logService;
    
    @Autowired
    private IDouBaoApi douBaoApi;
    
    @Value("${wechat.config.url}")
    private String weChatUrl;

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
        try {
            Result result = new Result();
            if (taskschedule.getCron() == null || taskschedule.getTaskid() == null || taskschedule.getParams() == null) {
                return Result.fail("参数错误");
            }
            //获取定时规则
            String cronExpression = taskschedule.getCron();
            String taskType = taskschedule.getTasktype();
            JSONObject params = JSON.parseObject(taskschedule.getParams());
            Map<String, Object> requestBody = getRequestBodyAndCheck(taskType,params);
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
                case "3": //发送DouBao信息
                    message = addTaskType3(cronExpression, taskId, requestBody, name);
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
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    private String addTaskType1(String cronExpression, String taskId, Map<String, Object> body, String name) {
        this.addTask(taskId, cronExpression, () -> {
            System.out.println("[" + name + "]任务开始执行:" + taskId);
            String time = getCurrentTime();
            String message = heFengWeatherService.sendWebhookMessage(time, (Boolean) body.get("isRoom"), body.get("name").toString(), weChatUrl);
            System.out.println(message);
            logService.insertLog("定时任务", "1", message, "system", name);
            System.out.println("[" + name + "]定时任务执行完毕:" + taskId);
        });
        return name + "任务添加成功";
    }

    private String addTaskType2(String cronExpression, String taskId, Map<String, Object> body, String name) {
        this.addTask(taskId, cronExpression, () -> {
            System.out.println("[" + name + "]任务开始执行:" + taskId);
            String message = heFengWeatherService.weatherService(body);
            System.out.println(message);
            logService.insertLog("定时任务", "1", message, "system", name);
            System.out.println("[" + name + "]定时任务执行完毕:" + taskId);
        });
        return name + "任务添加成功";
    }
    /*
    {
        "data":[
            {
                "name":"丁某某",
                "isRoom":false,
                "message":"给大家道个早安并分享名著的一段话"
            }
        ]
    }
   
     */
    
    
    private String addTaskType3(String cronExpression, String taskId, Map<String, Object> body, String name) {
        this.addTask(taskId, cronExpression, () -> {
            System.out.println("[" + name + "]任务开始执行:" + taskId);
            List<Map<String, Object>> list = (List<Map<String, Object>>) body.get("data");
            String errorMessage = "";
            for (Map<String, Object> map : list){
                String sendName = (String) map.get("name");
                Boolean isRoom = (Boolean) map.get("isRoom");
                String message = (String) map.get("message");
                Map<String, String> chatParams = new HashMap<>();
                chatParams.put("message", message);
                String sendMessage = douBaoApi.chatGPT2(chatParams, taskId);
                errorMessage += heFengWeatherService.sendWebhookMessage(sendMessage, isRoom, sendName, weChatUrl);
            }
   
            System.out.println(errorMessage);
            logService.insertLog("定时任务", "3", errorMessage, "system", name);
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

    private Map<String, Object> getRequestBodyAndCheck(String taskType,JSONObject body) {
        Map<String, Object> requestBody = new HashMap<>();
        if (taskType.equals("2")) {
            if (body.get("city") == null) throw new RuntimeException("city参数为空");
            List<String> cityList = (List<String>) body.get("city");
            requestBody.put("city", cityList);
            if (body.get("api") == null) throw new RuntimeException("api参数为空");
            requestBody.put("api", body.get("api")); //d55bd7f0e0524eef9f264b2fdc29b4fe
        } else if (taskType.equals("1")) {
            if (body.get("isRoom") == null) throw new RuntimeException("是否为群聊参数为空");
            if (body.get("name") == null) throw new RuntimeException("name参数为空");
        } else if (taskType.equals("3")) {
            return body;
        }
//        if (body.get("url") == null) throw new RuntimeException("url参数为空");
//        requestBody.put("url", body.get("url")); //http://192.168.5.139:3001/webhook/msg/v2?token=dingxhui
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
    public List<String> queryEnableTask() {
        List<String> taskIdList = new ArrayList<>(); 
        for (String key : scheduledTasks.keySet()){
            taskIdList.add(key);
        }
        return taskIdList;
    }

    @Override
    public Result listPageC1(QueryPageParam query) {
        HashMap param = query.getParam();
        String name = MapUtils.getString(param, "name");
        Page<Taskschedule> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<Taskschedule> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (name != null) {
            lambdaQueryWrapper.like(Taskschedule::getName, name);
        }
        //IPage result = iBlzcDataService.pageC(page);
        IPage result = this.pageCC(page, lambdaQueryWrapper);
        //查找正在运行的调度任务
        List<String> enableTaskList = queryEnableTask();
        //从内存中获取任务状态
        List<Taskschedule> records = (List<Taskschedule>) result.getRecords();
        records.forEach(item -> {
            if (enableTaskList.contains(item.getTaskid())) {
                item.setStatus("1");
            } else {
                item.setStatus("0");
            }
        });
        System.out.println("total==" + result.getTotal());
        return Result.suc(records, result.getTotal());
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

