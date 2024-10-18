package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //store date from begin to end
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date: dateList) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", Orders.COMPLETED);
            map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date: dateList) {
            Map<String, Object> map = new HashMap<>();
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer totalUser = userMapper.countByMap(map);

            map.put("begin", LocalDateTime.of(date,LocalTime.MIN));
            Integer newUser = userMapper.countByMap(map);
            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> validOrderCountList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        for (LocalDate date: dateList) {
            Map<String, Object> map = new HashMap<>();
            map.put("begin", LocalDateTime.of(date,LocalTime.MIN));
            map.put("end", LocalDateTime.of(date, LocalTime.MAX));
            map.put("status", null);
            Integer orderCount = orderMapper.countByMap(map);

            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(map);

            validOrderCountList.add(validOrderCount);
            orderCountList.add(orderCount);
        }

        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate = 0.0;

        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(LocalDateTime.of(begin,LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers,","))
                .build();
    }

    @Override
    public void exportExcel(HttpServletResponse resp) {
        //get data from sql - 30 days
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));

        //write data into excel
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("templates/template.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(in);
            XSSFSheet report = excel.getSheetAt(0);
            //XSSFSheet report = excel.getSheet("report.xlsx");

            report.getRow(1).getCell(1).setCellValue("time: "+ begin + " to " + end);
            report.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
            report.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            report.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());
            report.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            report.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());

            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                workspaceService.getBusinessData(LocalDateTime.of(date,LocalTime.MIN), LocalDateTime.of(date,LocalTime.MAX));
                report.getRow(i+7).getCell(1).setCellValue(date.toString());
                report.getRow(i+7).getCell(2).setCellValue(businessDataVO.getTurnover());
                report.getRow(i+7).getCell(3).setCellValue(businessDataVO.getValidOrderCount());
                report.getRow(i+7).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
                report.getRow(i+7).getCell(5).setCellValue(businessDataVO.getUnitPrice());
                report.getRow(i+7).getCell(6).setCellValue(businessDataVO.getNewUsers());
            }

            //export excel and allow download through output stream
            ServletOutputStream out = resp.getOutputStream();
            excel.write(out);

            out.flush();
            out.close();
            excel.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}