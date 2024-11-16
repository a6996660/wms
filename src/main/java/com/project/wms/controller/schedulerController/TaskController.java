package com.project.wms.controller.schedulerController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.wms.common.QueryPageParam;
import com.project.wms.common.Result;
import com.project.wms.common.TenDigitIdGenerator;
import com.project.wms.entity.manage.BlzcData;
import com.project.wms.entity.schedule.Taskschedule;
import com.project.wms.service.SchedulerService.Impl.TaskService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/taskschedule")
public class TaskController {
    //    eIdGenerator 实例，假设工作节点 ID 为 1
    private final TenDigitIdGenerator idGenerator = new TenDigitIdGenerator();
    @Autowired
    private TaskService taskService;
    @Autowired
    private com.project.wms.service.HFWeather.heFengWeatherService heFengWeatherService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return taskService.removeById(id) ? Result.suc() : Result.fail();
    }


    @GetMapping("/list")
    public List<Taskschedule> list() {
        return taskService.list();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Taskschedule taskschedule) {
        return taskService.updateById(taskschedule) ? Result.suc() : Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Taskschedule taskschedule) {
        if (StringUtils.isBlank(taskschedule.getStatus())) {
            taskschedule.setStatus("0");
        }
        if (taskschedule.getId() == null) {
            taskschedule.setId(idGenerator.nextId());
        }
        return taskService.save(taskschedule) ? Result.suc() : Result.fail();
    }

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query) {
       return taskService.listPageC1(query);
    }

    /**
     * 启用任务
     *
     * @return
     */
    @PostMapping("/enableTask")
    public Result enableTask(@RequestBody Taskschedule taskschedule) {
        return taskService.enableTask(taskschedule, "1");
    }

    /**
     * 停用任务
     *
     * @return
     */
    @PostMapping("/stopTask")
    public Result stopTask(@RequestBody Taskschedule taskschedule) {
        return taskService.stopTask(taskschedule, "0");
    }
    

    @PostMapping("/{taskId}")
    public String addTask(@PathVariable String taskId, @RequestBody Map<String, Object> body) {
        if (null == body || !body.containsKey("cronExpression")) {
            return "参数错误";
        }
        String cronExpression = (String) body.get("cronExpression");
        String taskType = "1"; //默认定时任务类型
        if (body.containsKey("taskType")) taskType = (String) body.get("taskType");
        Map<String, Object> requestBody = getRequestBody(body);
        switch (taskType) {
            case "1":
                taskService.addTask(taskId, cronExpression, () -> {
                    System.out.println("任务开始执行");
                    heFengWeatherService.weatherService(requestBody);
                    System.out.println("定时任务执行完毕:" + taskId);
                });
                return "天气任务添加成功";

        }
        return "任务已添加: " + taskId;
    }

    private Map<String, Object> getRequestBody(Map<String, Object> body) {
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


    @DeleteMapping("/{taskId}")
    public String cancelTask(@PathVariable String taskId) {
        taskService.cancelTask(taskId);
        return "任务已取消: " + taskId;
    }
}
