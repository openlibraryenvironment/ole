package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.ExtentOfOwnerShipTypeRecord;
import org.kuali.ole.model.jpa.ReceiptStatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sheiks on 10/11/16.
 */
public interface ReceiptStatusRecordRepository extends JpaRepository<ReceiptStatusRecord, String> {
}
