package com.unina.springnatour.service;

import com.unina.springnatour.dto.report.ReportDto;
import com.unina.springnatour.dto.report.ReportMapper;
import com.unina.springnatour.exception.ReportNotFoundException;
import com.unina.springnatour.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    /**
     * Gets a report
     * @param id the identifier of the report
     * @return ReportDTO Object after mapping from Entity, or throws Exception
     */
    public ReportDto getReportById(Long id) {
        return reportMapper.toDto(reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(id)));
    }

    /**
     * Gets all the reports
     * @return a List of ReportDTO Objects after mapping from Entity, or throws Exception
     */
    public List<ReportDto> getAllReports() {
        return reportMapper.toDto(reportRepository.findAll()
                .stream()
                .toList());
    }

    /**
     * Adds a report
     * @param reportDto ReportDTO Object with required fields, mapped to Entity and saved
     */
    public void addReport(ReportDto reportDto) {
        reportRepository.save(reportMapper.toEntity(reportDto));
    }

    /**
     * Updates a report
     * @param id the identifier of the report
     * @param reportDto ReportDTO Object, mapped to Entity, or throws Exception
     */
    public void updateReport(Long id, ReportDto reportDto) {
        reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(id));
    }

    /**
     * Deletes a report
     * @param id the identifier of the report
     */
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
