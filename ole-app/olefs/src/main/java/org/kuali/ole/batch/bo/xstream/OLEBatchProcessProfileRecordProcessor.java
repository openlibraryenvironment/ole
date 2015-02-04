package org.kuali.ole.batch.bo.xstream;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.batch.bo.*;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 1/28/14
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileRecordProcessor {
    private static XStream xStream = getXstream();

    private static XStream getXstream() {
        XStream xStream = new XStream();
        xStream.alias("OLEBatchProcessProfileBo", OLEBatchProcessProfileBo.class);
        xStream.omitField(PersistableBusinessObjectBase.class, "newCollectionRecord");
        return xStream;
    }

    public OLEBatchProcessProfileBo fromXML(String fileContent) {
        return (OLEBatchProcessProfileBo) xStream.fromXML(fileContent);
    }

    public String toXml(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        return xStream.toXML(oleBatchProcessProfileBo);
    }
}
