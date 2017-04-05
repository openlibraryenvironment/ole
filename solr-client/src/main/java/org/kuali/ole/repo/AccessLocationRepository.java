package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.AccessLocation;
import org.kuali.ole.model.jpa.CallNumberTypeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sheiks on 10/11/16.
 */
public interface AccessLocationRepository extends JpaRepository<AccessLocation, Integer> {
}
