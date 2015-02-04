package org.kuali.ole.docstore.document;

import org.apache.commons.net.ntp.TimeStamp;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 6/26/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDocumentManager implements DocumentManager {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocumentManager.class);
    public Date getDate(String date) {
        try {
            if (!"".equals(date) && date != null) {
                DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");
                Date parseDate = df.parse(date);
                return parseDate;
            }
        } catch (Exception e) {
            try {
                if (!"".equals(date) && date != null) {
                    DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
                    Date parseDate = df.parse(date);
                    return parseDate;
                }
            } catch (Exception e1) {
                LOG.info("Exception : ", e1);
            }
        }
        return null;
    }

    public void addResourceId(RequestDocument requestDocument, ResponseDocument respDoc) {

    }


    public Timestamp getTimeStampFromString(String date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Timestamp createdDate = null;
        try {
            createdDate = new Timestamp(df.parse(date).getTime());
        } catch (ParseException e) {
            LOG.info("Exception : ", e);
        }
        return createdDate;
    }
}
