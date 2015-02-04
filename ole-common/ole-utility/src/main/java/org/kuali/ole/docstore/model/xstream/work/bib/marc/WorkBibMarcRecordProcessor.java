package org.kuali.ole.docstore.model.xstream.work.bib.marc;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 12/12/11
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibMarcRecordProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibMarcRecordProcessor.class);
    // private static  XStream xStream = getXstream();
    private static XStream xStream = getXstream();

    private static XStream getXstream() {
        XStream xStream = new XStream();
        xStream.alias("collection", WorkBibMarcRecords.class);
        xStream.alias("record", WorkBibMarcRecord.class);
        xStream.alias("controlfield", ControlField.class);
        xStream.alias("datafield", DataField.class);
        xStream.alias("subfield", SubField.class);
        xStream.addImplicitCollection(WorkBibMarcRecord.class, "dataFields", DataField.class);
        xStream.addImplicitCollection(WorkBibMarcRecord.class, "controlFields", ControlField.class);
        xStream.addImplicitCollection(WorkBibMarcRecords.class, "records");
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new SubFieldConverter());
        xStream.registerConverter(new ControlFieldConverter());
        return xStream;
    }

    public WorkBibMarcRecords fromXML(String fileContent) {
//        fileContent = fileContent.replaceAll("&", "&amp;");
//        fileContent = fileContent.replaceAll("< ", "&lt; ");
        return (WorkBibMarcRecords) xStream.fromXML(fileContent);
    }

    public String toXml(WorkBibMarcRecords workBibMarcRecords) {
        //String xml = xStream.toXML(workBibMarcRecords);
        return xStream.toXML(workBibMarcRecords);
    }

}
