package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.ReceiptStatusRecord;
import org.kuali.ole.model.jpa.StatisticalSearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sheiks on 10/11/16.
 */
public interface StatisticalSearchRecordRepository extends JpaRepository<StatisticalSearchRecord, Long> {
}
