package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.AccessLocation;
import org.kuali.ole.model.jpa.AuthenticationTypeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sheiks on 10/11/16.
 */
public interface AuthenticationTypeRecordRepository extends JpaRepository<AuthenticationTypeRecord, Integer> {
}
