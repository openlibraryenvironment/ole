package org.kuali.ole.report;

import org.apache.camel.ProducerTemplate;
import org.kuali.ole.Constants;
import org.kuali.ole.model.jpa.ReportEntity;
import org.kuali.ole.repo.jpa.ReportDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 14/02/17.
 */
@Component
public class CSVSolrExceptionReportGenerator {
    Logger logger = LoggerFactory.getLogger(CSVSolrExceptionReportGenerator.class);

    @Autowired
    ReportDetailRepository reportDetailRepository;

    @Autowired
    ProducerTemplate producer;

    public String generateReport(List<ReportEntity> reportEntityList, String fileNameToCreate) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<SolrExceptionReportCSVRecord> solrExceptionReportCSVRecords = new ArrayList<>();

        CSVSolrExceptionRecordGenerator CSVSolrExceptionRecordGenerator = new CSVSolrExceptionRecordGenerator();
        for(ReportEntity reportEntity : reportEntityList) {
            SolrExceptionReportCSVRecord solrExceptionReportCSVRecord = CSVSolrExceptionRecordGenerator.prepareMatchingReportCSVRecord(reportEntity, new SolrExceptionReportCSVRecord());
            solrExceptionReportCSVRecords.add(solrExceptionReportCSVRecord);
        }

        stopWatch.stop();
        logger.info("Total time taken to prepare CSVRecords : " + stopWatch.getTotalTimeSeconds());
        logger.info("Total Num of CSVRecords Prepared : " + solrExceptionReportCSVRecords.size());

        if(!CollectionUtils.isEmpty(solrExceptionReportCSVRecords)) {
            producer.sendBodyAndHeader(Constants.CSV_SOLR_EXCEPTION_REPORT_Q, solrExceptionReportCSVRecords, Constants.REPORT_FILE_NAME, fileNameToCreate);
            DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_FOR_FILE_NAME);
            String generatedFileName = fileNameToCreate + "-" + df.format(new Date()) + ".csv";
            return generatedFileName;
        }

        return null;
    }
}
