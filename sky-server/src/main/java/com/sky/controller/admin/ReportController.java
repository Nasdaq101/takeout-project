package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * data statistics api
 */

@RestController
@RequestMapping("/admin/report")
@Api(tags="Data Statistics API Endpoints")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("Turnover Statistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("turnover: begin={}, end={}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }

    @GetMapping("/userStatistics")
    @ApiOperation("User Statistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("user: begin={}, end={}", begin, end);
        return Result.success(reportService.getUserStatistics(begin, end));
    }

    @GetMapping("/orderStatistics")
    @ApiOperation("Order Statistics")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("order: begin={}, end={}", begin, end);
        return Result.success(reportService.getOrderStatistics(begin, end));
    }

    @GetMapping("/top10")
    @ApiOperation("TOP 10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("top 10: begin={}, end={}", begin, end);
        return Result.success(reportService.getSalesTop10(begin, end));
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse resp) {
        reportService.exportExcel(resp);
    }
}
