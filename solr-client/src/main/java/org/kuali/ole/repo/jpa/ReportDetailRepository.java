package org.kuali.ole.repo.jpa;

import org.kuali.ole.model.jpa.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 14/02/17.
 */
public interface ReportDetailRepository extends JpaRepository<ReportEntity, Integer> {

    @Query(value = "select * from report_t where TYPE=?1 and CREATED_DATE >= ?2 and CREATED_DATE <= ?3", nativeQuery = true)
    List<ReportEntity> findByTypeAndDateRange(String type, Date from, Date to);
}
