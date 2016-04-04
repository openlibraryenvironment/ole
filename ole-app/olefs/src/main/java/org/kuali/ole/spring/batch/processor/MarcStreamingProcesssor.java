package org.kuali.ole.spring.batch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SheikS on 4/4/2016.
 */
public class MarcStreamingProcesssor implements Processor {
    private int batchCount = 1;

    private static Logger logger = LoggerFactory.getLogger(MarcStreamingProcesssor.class);

    public MarcStreamingProcesssor() {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Batch : " + batchCount);
        batchCount++;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date date = new Date();

        StringBuilder stringBuilder = new StringBuilder();

        System.out.println("Bulk ingest: Batch Start time : \t" + dateFormat.format(date));
        try {
            if (exchange.getIn().getBody() instanceof List) {
                for (String content : (List<String>) exchange.getIn().getBody()) {
                    stringBuilder.append(content);
                    stringBuilder.append(OleNGConstants.MARC_SPLIT);
                }
            }
            date = new Date();
            System.out.println("Bulk ingest: Batch End time : \t" + dateFormat.format(date));
            List<Record> records = null;
            try {
                records = new MarcRecordUtil().getMarcXMLConverter().convertRawMarchToMarc(stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(records.size());
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setException(e);
            throw e;
        } finally {

        }
    }
}
