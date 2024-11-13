package com.project.wms.service.SchedulerService;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.wms.common.Result;
import com.project.wms.entity.schedule.Taskschedule;

import java.util.Map;

public interface ITaskService extends IService<Taskschedule> {

    IPage pageC(IPage<Taskschedule> page);

    IPage pageCC(IPage<Taskschedule> page, Wrapper wrapper);

    Result enableTask(Taskschedule taskschedule, String type);

    Result stopTask(Taskschedule taskschedule, String number);

    Result queryEnableTask();
}
