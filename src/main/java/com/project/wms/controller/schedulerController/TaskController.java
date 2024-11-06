package com.project.wms.controller.schedulerController;

import com.project.wms.service.HFWeather.heFengWeatherService;
import com.project.wms.service.schedulerService.DynamicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final DynamicTaskService dynamicTaskService;
    @Autowired
    private com.project.wms.service.HFWeather.heFengWeatherService heFengWeatherService;
    @Autowired
    public TaskController(DynamicTaskService dynamicTaskService) {
        this.dynamicTaskService = dynamicTaskService;
    }

    @PostMapping("/{taskId}")
    public String addTask(@PathVariable String taskId, @RequestParam String cronExpression) {
        dynamicTaskService.addTask(taskId, cronExpression, () -> {
            // 创建 HashMap
            HashMap<String, Object> requestBody = new HashMap<>();
            // 填充数据
            List<String> cityList = new ArrayList<>();
            cityList.add("北京");
            cityList.add("莱州");
            cityList.add("威海");
            requestBody.put("city", cityList);
            requestBody.put("api", "d55bd7f0e0524eef9f264b2fdc29b4fe");
            requestBody.put("url", "http://192.168.5.139:3001/webhook/msg/v2?token=dingxhui");
            requestBody.put("isRoom", false);
            requestBody.put("name", "丁某某");
            heFengWeatherService.weatherService(requestBody);
            System.out.println("定时任务执行完毕:"+taskId);
        });
        return "任务已添加: " + taskId;
    }

    @DeleteMapping("/{taskId}")
    public String cancelTask(@PathVariable String taskId) {
        dynamicTaskService.cancelTask(taskId);
        return "任务已取消: " + taskId;
    }
}
