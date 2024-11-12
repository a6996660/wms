package com.project.wms.entity.manage;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @author jobob
 * @since 2024-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("loanRecords")
public class Loanrecords implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    private String id;

    /**
     * 借款人名称
     */
    @TableField("borrower_name")
    @ExcelProperty("借款人名称")
    private String borrowerName;

    /**
     * 业务编号
     */
    @TableField("business_number")
    @ExcelProperty("业务编号")
    private String businessNumber;

    /**
     * 贷款余额
     */
    @TableField("loan_balance")
    @ExcelProperty("贷款余额")
    private BigDecimal loanBalance;

    /**
     * 贷款余额发生额
     */
    @TableField("loan_balance_change")
    @ExcelProperty("贷款余额发生额")
    private BigDecimal loanBalanceChange;

    /**
     * 表外欠息金额
     */
    @TableField("overdue_interest")
    @ExcelProperty("表外欠息金额")
    private BigDecimal overdueInterest;

    /**
     * 表外欠息金额发生额
     */
    @TableField("overdue_interest_change")
    @ExcelProperty("表外欠息金额发生额")
    private BigDecimal overdueInterestChange;

    /**
     * 主办客户经理
     */
    @TableField("primary_client_manager")
    @ExcelProperty("主办客户经理")
    private String primaryClientManager;

    /**
     * 管理责任人
     */
    @TableField("responsible_person")
    @ExcelProperty("管理责任人")
    private String responsiblePerson;

    /**
     * 操作人
     */
    @ExcelProperty("操作人")
    private String operator;

    /**
     * 操作时间
     */
    @TableField("operation_time")
    @ExcelProperty("操作时间")
    private LocalDateTime operationTime;

    /**
     * 收回
     */
    @TableField("receive_time")
    @ExcelProperty("收回时间")
    private LocalDateTime receiveTime;
    
    private String remark;

    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //getter
    public String getRemark() {
        return remark;
    }
    //setter
    public void setRemarks(String remark) {
        this.remark = remark;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public BigDecimal getLoanBalanceChange() {
        return loanBalanceChange;
    }

    public void setLoanBalanceChange(BigDecimal loanBalanceChange) {
        this.loanBalanceChange = loanBalanceChange;
    }

    public BigDecimal getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(BigDecimal overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public BigDecimal getOverdueInterestChange() {
        return overdueInterestChange;
    }

    public void setOverdueInterestChange(BigDecimal overdueInterestChange) {
        this.overdueInterestChange = overdueInterestChange;
    }

    public String getPrimaryClientManager() {
        return primaryClientManager;
    }

    public void setPrimaryClientManager(String primaryClientManager) {
        this.primaryClientManager = primaryClientManager;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }
}
