package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.DocFormatConfig;
import org.kuali.ole.model.jpa.DocTypeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sheiks on 28/10/16.
 */
public interface DocTypeConfigRepository extends JpaRepository<DocTypeConfig, Integer> {
}
