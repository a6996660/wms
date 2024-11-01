package com.project.wms.entity;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2024-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlzcData implements Serializable {

    private static final long serialVersionUID = 1L;
    @ExcelProperty("所属机构")
    private String bank;
    @TableField("user_name")
    @ExcelProperty("借款人名称")
    private String userName;
    @TableField("user_type")
    @ExcelProperty("客户类别")
    private String userType;
    @TableField("user_id")
    @ExcelProperty("客户编号")
    private String userId;
    @ExcelProperty("证件号码")
    private String idc;
    @TableField("business_id")
    @ExcelProperty("业务编号")
    private String businessId;
    @ExcelProperty("首贷日")
    @TableField("first_date")
    private String firstDate;
    @ExcelProperty("发放日期")
    @TableField("fafang_date")
    private String fafangDate;
    @ExcelProperty("到期日期")
    @TableField("daoqi_date")
    private String daoqiDate;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getFafangDate() {
        return fafangDate;
    }

    public void setFafangDate(String fafangDate) {
        this.fafangDate = fafangDate;
    }

    public String getDaoqiDate() {
        return daoqiDate;
    }

    public void setDaoqiDate(String daoqiDate) {
        this.daoqiDate = daoqiDate;
    }

    public String getBlxcDate() {
        return blxcDate;
    }

    public void setBlxcDate(String blxcDate) {
        this.blxcDate = blxcDate;
    }

    public String getFafangMoney() {
        return fafangMoney;
    }

    public void setFafangMoney(String fafangMoney) {
        this.fafangMoney = fafangMoney;
    }

    public String getDaikuanMoney() {
        return daikuanMoney;
    }

    public void setDaikuanMoney(String daikuanMoney) {
        this.daikuanMoney = daikuanMoney;
    }

    public String getBwqxMoney() {
        return bwqxMoney;
    }

    public void setBwqxMoney(String bwqxMoney) {
        this.bwqxMoney = bwqxMoney;
    }

    public String getDkglManager() {
        return dkglManager;
    }

    public void setDkglManager(String dkglManager) {
        this.dkglManager = dkglManager;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getZhubManager() {
        return zhubManager;
    }

    public void setZhubManager(String zhubManager) {
        this.zhubManager = zhubManager;
    }

    public String getDanbWay() {
        return danbWay;
    }

    public void setDanbWay(String danbWay) {
        this.danbWay = danbWay;
    }

    public String getDanbName() {
        return danbName;
    }

    public void setDanbName(String danbName) {
        this.danbName = danbName;
    }

    public String getZbwsbldkje() {
        return zbwsbldkje;
    }

    public void setZbwsbldkje(String zbwsbldkje) {
        this.zbwsbldkje = zbwsbldkje;
    }

    public String getZbwsbldklx() {
        return zbwsbldklx;
    }

    public void setZbwsbldklx(String zbwsbldklx) {
        this.zbwsbldklx = zbwsbldklx;
    }

    public String getZbwDate() {
        return zbwDate;
    }

    public void setZbwDate(String zbwDate) {
        this.zbwDate = zbwDate;
    }

    public String getDaikId() {
        return daikId;
    }

    public void setDaikId(String daikId) {
        this.daikId = daikId;
    }

    public String getFafType() {
        return fafType;
    }

    public void setFafType(String fafType) {
        this.fafType = fafType;
    }

    public String getHangyType() {
        return hangyType;
    }

    public void setHangyType(String hangyType) {
        this.hangyType = hangyType;
    }

    public String getDaikUse() {
        return daikUse;
    }

    public void setDaikUse(String daikUse) {
        this.daikUse = daikUse;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getJiexhjCount() {
        return jiexhjCount;
    }

    public void setJiexhjCount(String jiexhjCount) {
        this.jiexhjCount = jiexhjCount;
    }

    public String getFirstJxhjDate() {
        return firstJxhjDate;
    }

    public void setFirstJxhjDate(String firstJxhjDate) {
        this.firstJxhjDate = firstJxhjDate;
    }

    public String getQianxCount() {
        return qianxCount;
    }

    public void setQianxCount(String qianxCount) {
        this.qianxCount = qianxCount;
    }

    public String getBenjyqCount() {
        return benjyqCount;
    }

    public void setBenjyqCount(String benjyqCount) {
        this.benjyqCount = benjyqCount;
    }

    public String getZuijycCuisDate() {
        return zuijycCuisDate;
    }

    public void setZuijycCuisDate(String zuijycCuisDate) {
        this.zuijycCuisDate = zuijycCuisDate;
    }

    public String getZuijycJinzdcDate() {
        return zuijycJinzdcDate;
    }

    public void setZuijycJinzdcDate(String zuijycJinzdcDate) {
        this.zuijycJinzdcDate = zuijycJinzdcDate;
    }

    public String getFirstBulxcDate() {
        return firstBulxcDate;
    }

    public void setFirstBulxcDate(String firstBulxcDate) {
        this.firstBulxcDate = firstBulxcDate;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    @ExcelProperty("不良形成日期")
    private String blxcDate;
    @ExcelProperty("发放金额")
    private String fafangMoney;
    @ExcelProperty("贷款余额")
    private String daikuanMoney;
    @ExcelProperty("表外欠息金额")
    private String bwqxMoney;
    @ExcelProperty("贷款管理责任人")
    private String dkglManager;
    @ExcelProperty("案件状态")
    private String state;
    @ExcelProperty("案件编号")
    private String caseId;
    @ExcelProperty("主办客户经理")
    private String zhubManager;
    @ExcelProperty("担保方式")
    private String danbWay;
    @ExcelProperty("担保人_抵质物名称")
    private String danbName;
    @ExcelProperty("转表外时不良贷款金额")
    private String zbwsbldkje;
    @ExcelProperty("转表外时不良贷款利息")
    private String zbwsbldklx;
    @ExcelProperty("转表外日期")
    private String zbwDate;
    @ExcelProperty("贷款账号")
    private String daikId;
    @ExcelProperty("发放类别")
    private String fafType;
    @ExcelProperty("行业分类")
    private String hangyType;
    @ExcelProperty("贷款用途")
    private String daikUse;
    @ExcelProperty("联系电话（固话）")
    private String contactNum;
    @ExcelProperty("联系电话（手机）")
    private String telNum;
    @ExcelProperty("借新还旧累计次数")
    private String jiexhjCount;
    @ExcelProperty("首次借新还旧日期")
    private String firstJxhjDate;
    @ExcelProperty("欠息累计次数")
    private String qianxCount;
    @ExcelProperty("本金逾期累计次数")
    private String benjyqCount;
    @ExcelProperty("最近一次催收时间")
    private String zuijycCuisDate;
    @ExcelProperty("最近一次尽职调查时间")
    private String zuijycJinzdcDate;
    @ExcelProperty("首次不良形成日期")
    private String firstBulxcDate;
    @ExcelProperty("核心入账机构")
    private String bankId;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    //    //贷款余额发生额
//    private String daikuanMoneySub;
//    //利息金额发生额
//    private String bwqxMoneySub;
//    //发生日期
//    private String dateSub;
//
//    public String getBwqxMoneySub() {
//        return bwqxMoneySub;
//    }
//
//    public void setBwqxMoneySub(String bwqxMoneySub) {
//        this.bwqxMoneySub = bwqxMoneySub;
//    }
//
//    public String getDaikuanMoneySub() {
//        return daikuanMoneySub;
//    }
//
//    public void setDaikuanMoneySub(String daikuanMoneySub) {
//        this.daikuanMoneySub = daikuanMoneySub;
//    }
}
