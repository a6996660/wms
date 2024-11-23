package com.project.wms.mapper.messageMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.wms.entity.message.SessionContext;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SessionContextMapper extends BaseMapper<SessionContext> {
    SessionContext getBySessionId(String sessionId);
    List<SessionContext> getBySessionIdOrderByDate(@Param("sessionId") String sessionId);
}
