package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.DocTypeConfig;
import org.kuali.ole.model.jpa.SearchFacetPage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sheiks on 28/10/16.
 */
public interface SearchFacetPageRepository extends JpaRepository<SearchFacetPage, Integer> {
}
