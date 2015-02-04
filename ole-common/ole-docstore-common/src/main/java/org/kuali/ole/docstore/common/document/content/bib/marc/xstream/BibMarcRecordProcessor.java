package org.kuali.ole.docstore.common.document.content.bib.marc.xstream;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibMarcRecordProcessor {

    private static final Logger LOG = Logger.getLogger(BibMarcRecordProcessor.class);

    private static XStream xStream = getXstream();
    private static XStream xStreamObj = buildXstream();

    private final static String RECORD = "record";
    private final static String CONTROLFIELD = "controlfield";
    private final static String DATAFIELD = "datafield";
    private final static String SUBFIELD = "subfield";
    private final static String CONTROLFIELDS = "controlfields";
    private final static String DATAFIELDS = "datafields";
    private final static String SUBFIELDS = "subFields";
    private final static String LEADER = "leader";

    private int errCnt;
    private int successCnt;

    private Map<String, String> valuesMap = new HashMap<String, String>();
    private StringBuilder errBuilder;
    private static final String ERR_BIB = "Error BIB:: ";
    private static final String TIME_STAMP = " ::TIME STAMP:: ";
    private static final String ERR_CAUSE = " ::Error Caused:: ";
    private static final String lineSeparator = System.getProperty("line.separator");

    public BibMarcRecordProcessor() {
    }

    public BibMarcRecordProcessor(StringBuilder errBuilder) {
        this.errBuilder = errBuilder;
    }
    private static XStream getXstream() {
        XStream xStream = new XStream();
        xStream.alias("collection", BibMarcRecords.class);
        xStream.alias("record", BibMarcRecord.class);
        xStream.alias("controlfield", ControlField.class);
        xStream.alias("datafield", DataField.class);
        xStream.alias("subfield", SubField.class);
        xStream.addImplicitCollection(BibMarcRecord.class, "dataFields", DataField.class);
        xStream.addImplicitCollection(BibMarcRecord.class, "controlFields", ControlField.class);
        xStream.addImplicitCollection(BibMarcRecords.class, "records");
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new SubFieldConverter());
        xStream.registerConverter(new ControlFieldConverter());
        return xStream;
    }


    private static XStream buildXstream() {
        XStream xStream = new XStream();
        xStream.alias(RECORD, BibMarcRecord.class);
        xStream.alias(CONTROLFIELD, ControlField.class);
        xStream.alias(DATAFIELD, DataField.class);
        xStream.alias(SUBFIELD, SubField.class);
        xStream.addImplicitCollection(BibMarcRecord.class, "dataFields", DataField.class);
        xStream.addImplicitCollection(BibMarcRecord.class, "controlFields", ControlField.class);
        xStream.addImplicitCollection(BibMarcRecords.class, "records");
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new ControlFieldConverter());
        return xStream;
    }

    public BibMarcRecords fromXML(String fileContent) {
        return (BibMarcRecords) xStream.fromXML(fileContent);
    }

//    public BibMarcRecord fromXMLtoObject(String fileContent) {
//        return (BibMarcRecord) xStreamObj.fromXML(fileContent);
//    }

    public String toXml(BibMarcRecords bibMarcRecords) {
        return xStream.toXML(bibMarcRecords);

    }

    public String generateXML(BibMarcRecord bibMarcRecord) {
        StringBuffer stringBuffer = new StringBuffer();
        try{
            stringBuffer.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
            stringBuffer.append("\n");
            String xml = xStreamObj.toXML(bibMarcRecord);
            stringBuffer.append(xml);
            stringBuffer.append("\n");
            stringBuffer.append("</collection>");
            successCnt++;
        } catch (Exception ex) {
            LOG.error("Exception :", ex);
            errBuilder.append(ERR_BIB).append(TIME_STAMP)
                    .append(new Date()).append(ERR_CAUSE).append(ex.getMessage()).append(" ::At:: ").append("generateXML() for mrc").append(lineSeparator);
            errCnt++;
            return null;
        }
        return stringBuffer.toString();
    }

    public String generateXML(List<BibMarcRecord> bibMarcRecords) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        stringBuilder.append("\n");
        for (BibMarcRecord record : bibMarcRecords) {
            try {
                if (record != null) {
                    stringBuilder.append(xStreamObj.toXML(record));
                    stringBuilder.append("\n");
                    successCnt++;
                }
            } catch (Exception ex) {
                LOG.error("Exception :", ex);
                errBuilder.append(ERR_BIB).append(TIME_STAMP)
                        .append(new Date()).append(ERR_CAUSE).append(ex.getMessage()).append(" ::At:: ").append("generateXML() for mrc").append(lineSeparator);
                errCnt++;
                return null;
            }
        }
        stringBuilder.append("</collection>");
        return stringBuilder.toString();
    }


    public String generateXML(List<BibMarcRecord> bibMarcRecords, Boolean isFirstBatch, Boolean isLastBatch) {
        StringBuilder stringBuilder = new StringBuilder();
        if(isFirstBatch){
            stringBuilder.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        }

        stringBuilder.append("\n");
        for (BibMarcRecord record : bibMarcRecords) {
            try {
                if (record != null) {
                    stringBuilder.append(xStreamObj.toXML(record));
                    stringBuilder.append("\n");
                    successCnt++;
                }
            } catch (Exception ex) {
                LOG.error("Exception :", ex);
                errBuilder.append(ERR_BIB).append(TIME_STAMP)
                        .append(new Date()).append(ERR_CAUSE).append(ex.getMessage()).append(" ::At:: ").append("generateXML() for mrc").append(lineSeparator);
                errCnt++;
                return null;
            }
        }
        if(isLastBatch) {
            stringBuilder.append("</collection>");
        }

        return stringBuilder.toString();
    }

    public int getErrCnt() {
        return errCnt;
    }

    public int getSuccessCnt() {
        return successCnt;
    }
    public Map<String, String> valuesMapFromXML(String marcXMLContent) {
        BibMarcRecords  bibMarcRecordCollection= fromXML(marcXMLContent);
        List<BibMarcRecord> records = bibMarcRecordCollection.getRecords();
        for (Iterator<BibMarcRecord> iterator = records.iterator(); iterator.hasNext(); ) {
            BibMarcRecord record = iterator.next();
            valuesMap.put("leader", record.getLeader());
            List<ControlField> controlFields = record.getControlFields();
            for (Iterator<ControlField> controlFieldIterator = controlFields.iterator(); controlFieldIterator.hasNext(); ) {
                ControlField controlField = controlFieldIterator.next();
                valuesMap.put(controlField.getTag(), controlField.getValue());
            }
            List<DataField> datafields = record.getDataFields();
            for (Iterator<DataField> dataFieldIterator = datafields.iterator(); dataFieldIterator.hasNext(); ) {
                DataField dataField = dataFieldIterator.next();
                String tag = dataField.getTag();
                List<SubField> subfields = dataField.getSubFields();
                for (Iterator<SubField> marcSubFieldIterator = subfields.iterator(); marcSubFieldIterator.hasNext(); ) {
                    SubField subField = marcSubFieldIterator.next();
                    valuesMap.put(tag + subField.getCode(), subField.getValue());
                }
            }
        }
        return valuesMap;
    }

}