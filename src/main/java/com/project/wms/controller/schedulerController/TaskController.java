package com.project.wms.controller.schedulerController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.wms.common.QueryPageParam;
import com.project.wms.common.Result;
import com.project.wms.common.TenDigitIdGenerator;
import com.project.wms.entity.manage.BlzcData;
import com.project.wms.entity.schedule.Taskschedule;
import com.project.wms.service.SchedulerService.Impl.TaskService;
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
    public Result del(@RequestParam String id){
        return taskService.removeById(id)?Result.suc():Result.fail();
    }


    @GetMapping("/list")
    public List<Taskschedule> list(){
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
        if (StringUtils.isBlank(taskschedule.getStatus())){
            taskschedule.setStatus("0");
        }
        if (taskschedule.getId() == null){
            taskschedule.setId(idGenerator.nextId());
        }
        return taskService.save(taskschedule) ? Result.suc() : Result.fail();
    }
    
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query){
//        HashMap param = query.getParam();
//        String businessId = (String)param.get("businessId");
        Page<Taskschedule> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<Taskschedule> lambdaQueryWrapper = new LambdaQueryWrapper();
//        if(StringUtils.isNotBlank(businessId) && !"null".equals(businessId)){
//            lambdaQueryWrapper.like(Taskschedule::getBusinessId,businessId);
//        }
        //IPage result = iBlzcDataService.pageC(page);
        IPage result = taskService.pageCC(page,lambdaQueryWrapper);
        System.out.println("total=="+result.getTotal());
        return Result.suc(result.getRecords(),result.getTotal());
    }

    /**
     * 启用任务
     * @return
     */
    @PostMapping("/enableTask")
    public Result enableTask(@RequestBody Taskschedule taskschedule) {
        return taskService.enableTask(taskschedule,"1");
    }

    @PostMapping("/{taskId}")
    public String addTask(@PathVariable String taskId, @RequestBody Map<String,Object> body) {
        if (null == body || !body.containsKey("cronExpression")){
            return "参数错误";
        }
        String cronExpression = (String) body.get("cronExpression");
        String taskType = "1"; //默认定时任务类型
        if (body.containsKey("taskType")) taskType = (String) body.get("taskType");
        Map<String, Object> requestBody = getRequestBody(body);
        switch (taskType){
            case "1":
                taskService.addTask(taskId, cronExpression, () -> {
                    System.out.println("任务开始执行");
                    heFengWeatherService.weatherService(requestBody);
                    System.out.println("定时任务执行完毕:" + taskId);
                });
                return "天气任务添加成功";
            case "2":
                return addMessageTask(requestBody, taskId, cronExpression);
                
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
    
    private String addMessageTask(Map<String, Object> body, String taskId, String cronExpression) {
        //获取当前时间并转换为 2024-11-7 23:30:00 格式
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        taskService.addTask(taskId, cronExpression, () -> {
            System.out.println("任务开始执行");
            heFengWeatherService.sendWebhookMessage(time, (Boolean) body.get("isRoom"), body.get("name").toString(), body.get("url").toString());
            System.out.println("定时任务执行完毕:" + taskId);
        });
        return "消息任务添加成功";
        
    }

    @DeleteMapping("/{taskId}")
    public String cancelTask(@PathVariable String taskId) {
        taskService.cancelTask(taskId);
        return "任务已取消: " + taskId;
    }
}