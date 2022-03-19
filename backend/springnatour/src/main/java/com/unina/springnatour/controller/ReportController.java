package com.unina.springnatour.controller;

import com.unina.springnatour.dto.report.ReportDto;
import com.unina.springnatour.service.ReportService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Gets a report
     *
     * @param id the identifier of the report
     * @return ReportDTO
     */
    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable Long id) {

        ReportDto reportDto = reportService.getReportById(id);

        return new ResponseEntity<>(reportDto, HttpStatus.OK);
    }

    /**
     * Gets all the reports
     *
     * @return List of ReportDTO
     */
    @GetMapping("/reports")
    public ResponseEntity<List<ReportDto>> getAllReports() {

        List<ReportDto> reportDtoList = reportService.getAllReports();

        if (!reportDtoList.isEmpty()) {
            return new ResponseEntity<>(reportDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the reports
     *
     * @return List of ReportDTO
     */
    @GetMapping("/reports/page")
    public ResponseEntity<List<ReportDto>> getAllReports(
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        List<ReportDto> reportDtoList = reportService.getAllReports(pageNo, pageSize);

        if (!reportDtoList.isEmpty()) {
            return new ResponseEntity<>(reportDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new report
     *
     * @param reportDto the ReportDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/reports/add")
    public ResponseEntity<?> addReport(@RequestBody ReportDto reportDto) {

        reportService.addReport(reportDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates an existing report
     *
     * @param id        the identifier of the report
     * @param reportDto the ReportDTO Object containing the updated report
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/reports/{id}/update")
    public ResponseEntity<?> updateReport(@PathVariable Long id,
                                          @RequestBody ReportDto reportDto) {

        reportService.updateReport(id, reportDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes an existing report
     *
     * @param id the identifier of the report
     * @return HTTP Status OK after deletion
     */
    @DeleteMapping("/reports/{id}/delete")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {

        reportService.deleteReport(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
