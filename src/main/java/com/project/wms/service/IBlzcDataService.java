package com.project.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.wms.entity.BlzcData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.wms.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2024-08-29
 */
public interface IBlzcDataService extends IService<BlzcData> {
    IPage pageC(IPage<BlzcData> page);

    IPage pageCC(IPage<BlzcData> page, Wrapper wrapper);
    
    public String changeMoney(Map<String,Object> body);
    
    public Map<String,String> importExcel(MultipartFile file);

    public void export(HttpServletResponse response) throws IOException;
}
