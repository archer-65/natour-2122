package com.unina.springnatour.repository;

import com.unina.springnatour.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
