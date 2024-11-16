package com.project.wms.entity.manage;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author dingxhui
 * @since 2024-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;

    /**
     * 日志名称
     */
    private String name;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 日志内容
     */
    private String message;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 日志时间
     */
    @TableField("createTime")
    private LocalDateTime createtime;

    /**
     * 备注
     */
    private String remark;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
}
