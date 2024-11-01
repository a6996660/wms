package com.project.wms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.wms.common.Result;
import com.project.wms.entity.BlzcData;
import com.project.wms.entity.Loanrecords;
import com.project.wms.entity.User;
import com.project.wms.mapper.BlzcDataMapper;
import com.project.wms.mapper.LoanrecordsMapper;
import com.project.wms.mapper.UserMapper;
import com.project.wms.service.IBlzcDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.wms.service.ILoanrecordsService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2024-08-29
 */
@Service
public class BlzcDataServiceImpl extends ServiceImpl<BlzcDataMapper, BlzcData> implements IBlzcDataService {
    private static String[] fields = new String[] {
            "bank",
            "userName",
            "userType",
            "userId",
            "idc",
            "businessId",
            "firstDate",
            "fafangDate",
            "daoqiDate",
            "blxcDate",
            "fafangMoney",
            "daikuanMoney",
            "bwqxMoney",
            "dkglManager",
            "state",
            "caseId",
            "zhubManager",
            "danbWay",
            "danbName",
            "zbwsbldkje",
            "zbwsbldklx",
            "zbwDate",
            "daikId",
            "fafType",
            "hangyType",
            "daikUse",
            "contactNum",
            "telNum",
            "jiexhjCount",
            "firstJxhjDate",
            "qianxCount",
            "benjyqCount",
            "zuijycCuisDate",
            "zuijycJinzdcDate",
            "firstBulxcDate",
            "bankId"
    };

    @Resource
    private BlzcDataMapper blzcDataMapper;
    //装载ILoanrecordsService
    @Autowired
    private ILoanrecordsService loanrecordsService;
    @Override
    public IPage pageC(IPage<BlzcData> page) {
        return blzcDataMapper.pageC(page);
    }

    @Override
    public IPage pageCC(IPage<BlzcData> page, Wrapper wrapper) {
        return blzcDataMapper.pageCC(page,wrapper);
    }

    @Override
    public String changeMoney(Map<String, Object> body) {
        String erroMessage = null;
        if (body.get("user") == null || body.get("data") == null){
            erroMessage = "参数错误";
            return erroMessage;
        }  

        Map<String,Object> dataMap = (Map<String, Object>) body.get ("data");
        List<String> ids = new ArrayList<>();
        ids.add(dataMap.get("id").toString());
        List<BlzcData> bzdatas = this.listByIds(ids);
        if (bzdatas.size() != 1){
            erroMessage = "查询数据错误";
            return erroMessage;
        }
        BlzcData bzdata = bzdatas.get(0);
        //取金额日期
        //贷款余额发生额
        BigDecimal daikuanMoneySub = new BigDecimal("0");
        if(dataMap.get("daikuanMoneySub") != null && !"".equals(dataMap.get("daikuanMoneySub"))){
            daikuanMoneySub = new BigDecimal(dataMap.get("daikuanMoneySub").toString());
        }
        //利息金额发生额
        BigDecimal bwqxMoneySub = new BigDecimal("0");
        if (dataMap.get("bwqxMoneySub") != null && !"".equals(dataMap.get("bwqxMoneySub"))) {
            bwqxMoneySub = new BigDecimal(dataMap.get("bwqxMoneySub").toString());
        }
        //发生日期
        Object dateSub = dataMap.get("dateSub");
        if (bzdata.getDaikuanMoney() == null || bzdata.getBwqxMoney() == null){
            erroMessage = "当前数据缺少贷款余额和利息金额，无法进行收回操作！"; return erroMessage;
        }
        
        //获取贷款余额
        BigDecimal daikuanMoney = new BigDecimal(bzdata.getDaikuanMoney());
        //获取利息金额
        BigDecimal bwqxMoney = new BigDecimal(bzdata.getBwqxMoney());
        
        //贷款余额 - 贷款余额发生额
        daikuanMoney = daikuanMoney.subtract(daikuanMoneySub);
        //利息金额 - 利息金额发生额
        bwqxMoney = bwqxMoney.subtract(bwqxMoneySub);
        //写回
        bzdata.setDaikuanMoney(daikuanMoney.toString());
        bzdata.setBwqxMoney(bwqxMoney.toString());
        //保存数据库
        Result result = this.updateById(bzdata) ? Result.suc() : Result.fail();
        if (result.getCode() != 200) {erroMessage = "更新数据错误！"; return erroMessage;}
        //记录日志
        String jsonString = body.get("user").toString();
        // 使用 Fastjson 将 JSON 字符串转换为 Map
        Map<String, Object> map = JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>(){});
        //组装日志实体
        Loanrecords logger= new Loanrecords();
        logger.setBorrowerName(bzdata.getUserName());
        logger.setLoanBalance(daikuanMoney);
        logger.setLoanBalanceChange(daikuanMoneySub);
        logger.setOverdueInterest(bwqxMoney);
        logger.setOverdueInterestChange(bwqxMoneySub);
        logger.setBusinessNumber(bzdata.getBusinessId());
        //主办客户经理
        logger.setPrimaryClientManager(bzdata.getZhubManager());
        //贷款管理责任人
        logger.setResponsiblePerson(bzdata.getDkglManager());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if(dataMap.get("dateSub") == null || "".equals(dataMap.get("dateSub"))){
            erroMessage = "发生日期不能为空！"; return erroMessage;
        }
        LocalDateTime time = LocalDateTime.parse(dataMap.get("dateSub").toString(), formatter);
        logger.setReceiveTime(time );
        logger.setOperationTime(LocalDateTime.now());
        //操作人
        if (map.get("name") != null) {
            logger.setOperator(map.get("name").toString());
        } else {
            logger.setOperator("");
        }
//        logger.setOperationTime((LocalDateTime) dateSub);
        Result loggerResult = insertLoanrecords(logger);
        if (loggerResult.getCode() != 200){
            erroMessage = "记录日志错误！";
        }
        return erroMessage;
    }

    @Override
    public Map<String, String> importExcel(MultipartFile file) {
        
        List<BlzcData> blzcDataList = new ArrayList<>();
        Map<String, String> resultMap = new HashMap<>();
        //        所属机构	借款人名称	客户类别	客户编号	证件号码	业务编号	首贷日	发放日期	到期日期	不良形成日期	发放金额	贷款余额	表外欠息金额	贷款管理责任人	案件状态	案件编号	主办客户经理	担保方式	担保人_抵质物名称	转表外时不良贷款金额	转表外时不良贷款利息	转表外日期	贷款账号	发放类别	行业分类	贷款用途	联系电话（固话）	联系电话（手机）	借新还旧累计次数	首次借新还旧日期	欠息累计次数	本金逾期累计次数	最近一次催收时间	最近一次尽职调查时间	首次不良形成日期	核心入账机构

        if (file.isEmpty()) {
            return resultMap;
        }
        try {
            // 读取 Excel 文件内容
            Workbook workbook = null;
            workbook = WorkbookFactory.create(file.getInputStream());
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行
            int rowIndex = 0;
            for (Row row : sheet) {
                // 跳过第一行（表头）
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                // 遍历每一列
                String cellStr = "";
                int index = 0;
                Map<String, Object> bizObjectMap = new HashMap<>();
                for (Cell cell : row) {
                    Object cellValue = null;
                    switch (cell.getCellType()) {
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            cellValue = cell.getNumericCellValue();
                            break;
                        case BOOLEAN:
                            cellValue = cell.getNumericCellValue();
                            break;
                        default:
                            cellValue = cell.getNumericCellValue();
                    }
                    if (index == 36){
                        continue;
                    }
                    bizObjectMap.put(fields[index], cellValue);
                    index ++;
//                    cellStr = cellStr + cellValue;
                }
                blzcDataList.add(convertMapToBlzcData(bizObjectMap));
//                System.out.println(cellStr);
            }
            boolean b = this.saveOrUpdateBatch(blzcDataList);
            resultMap.put("code", b ? "200" : "500");
            return resultMap;
        } catch (IOException e) {
            return resultMap;
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        List<BlzcData> data = new ArrayList<>();
        data = this.list();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "data.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        EasyExcel.write(response.getOutputStream(), BlzcData.class).sheet("Sheet1").doWrite(data);
    }

    public Result insertLoanrecords(Loanrecords message){
        if (message == null){
            return new Result();
        }
        return loanrecordsService.save(message) ? Result.suc() : Result.fail();
    }




    private static BlzcData convertMapToBlzcData(Map<String, Object> body) {
        BlzcData bzdata = new BlzcData();
        if(body.get("id") != null){
            bzdata.setId(Long.parseLong(body.get("id").toString()));
        }
        bzdata.setBank(getStringValue(body.get("bank")));
        bzdata.setUserName(getStringValue(body.get("userName")));
        bzdata.setUserType(getStringValue(body.get("userType")));
        bzdata.setUserId(getStringValue(body.get("userId")));
        bzdata.setIdc(getStringValue(body.get("idc")));
        bzdata.setBusinessId(getStringValue(body.get("businessId")));
        bzdata.setFirstDate(getStringValue(body.get("firstDate")));
        bzdata.setFafangDate(getStringValue(body.get("fafangDate")));
        bzdata.setDaoqiDate(getStringValue(body.get("daoqiDate")));
        bzdata.setBlxcDate(getStringValue(body.get("blxcDate")));
        bzdata.setFafangMoney(getStringValue(body.get("fafangMoney")));
        bzdata.setDaikuanMoney(getStringValue(body.get("daikuanMoney")));
        bzdata.setBwqxMoney(getStringValue(body.get("bwqxMoney")));
        bzdata.setDkglManager(getStringValue(body.get("dkglManager")));
        bzdata.setState(getStringValue(body.get("state")));
        bzdata.setCaseId(getStringValue(body.get("caseId")));
        bzdata.setZhubManager(getStringValue(body.get("zhubManager")));
        bzdata.setDanbWay(getStringValue(body.get("danbWay")));
        bzdata.setDanbName(getStringValue(body.get("danbName")));
        bzdata.setZbwsbldkje(getStringValue(body.get("zbwsbldkje")));
        bzdata.setZbwsbldklx(getStringValue(body.get("zbwsbldklx")));
        bzdata.setZbwDate(getStringValue(body.get("zbwDate")));
        bzdata.setDaikId(getStringValue(body.get("daikId")));
        bzdata.setFafType(getStringValue(body.get("fafType")));
        bzdata.setHangyType(getStringValue(body.get("hangyType")));
        bzdata.setDaikUse(getStringValue(body.get("daikUse")));
        bzdata.setContactNum(getStringValue(body.get("contactNum")));
        bzdata.setTelNum(getStringValue(body.get("telNum")));
        bzdata.setJiexhjCount(getStringValue(body.get("jiexhjCount")));
        bzdata.setFirstJxhjDate(getStringValue(body.get("firstJxhjDate")));
        bzdata.setQianxCount(getStringValue(body.get("qianxCount")));
        bzdata.setBenjyqCount(getStringValue(body.get("benjyqCount")));
        bzdata.setZuijycCuisDate(getStringValue(body.get("zuijycCuisDate")));
        bzdata.setZuijycJinzdcDate(getStringValue(body.get("zuijycJinzdcDate")));
        bzdata.setFirstBulxcDate(getStringValue(body.get("firstBulxcDate")));
        bzdata.setBankId(getStringValue(body.get("bankId")));

        return bzdata;
    }

    private static String getStringValue(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof String) {
            return (String) value;
        } else {
            return value.toString();
        }
    }
}
