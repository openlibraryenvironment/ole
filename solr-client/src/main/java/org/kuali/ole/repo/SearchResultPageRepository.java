package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.SearchFacetPage;
import org.kuali.ole.model.jpa.SearchResultPage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sheiks on 28/10/16.
 */
public interface SearchResultPageRepository extends JpaRepository<SearchResultPage, Integer> {
}
