package org.kuali.ole.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kuali.ole.model.jpa.ReportEntity;
import org.kuali.ole.repo.jpa.ReportDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sheiks on 14/02/17.
 */
@Component
public class ReportProcessor implements Processor {

    @Autowired
    ReportDetailRepository reportDetailRepository;

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("---- Processor Cmg ---");
        ReportEntity reportEntity = (ReportEntity) exchange.getIn().getBody();
        reportDetailRepository.save(reportEntity);
    }
}