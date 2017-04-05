package org.kuali.ole.repo;

import org.kuali.ole.model.jpa.BibRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 27/10/16.
 */
public interface BibRecordRepository extends JpaRepository<BibRecord, Integer> {

    @Query(value = "select b.BIB_ID from ole_ds_bib_t b where (b.DATE_CREATED between ?1 and ?2) OR (b.DATE_UPDATED between ?1 and ?2)", nativeQuery = true)
    List<Integer> getBibIdByDate(Date from, Date to);

}
