package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/10/12
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInstanceRecordHandler {
    public String generateXML(OleHoldings oleHolding, Item oleItem) {
        Instance oleInstance = getOleInstance(oleHolding, oleItem);
        InstanceCollection oleInstanceCollection = new InstanceCollection();
        oleInstanceCollection.setInstance(Arrays.asList(oleInstance));
        return toXML(oleInstanceCollection);
    }

    public Instance getOleInstance(OleHoldings oleHolding, Item oleItem) {
        Instance oleInstance = new Instance();
        oleInstance.getItems().getItem().add(oleItem);
        oleInstance.setOleHoldings(oleHolding);
        DateFormat df = new SimpleDateFormat("mm-DD-yyyy");
        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setLastUpdated(String.valueOf(df.format(new Date())));
        additionalAttributes.setDateEntered(String.valueOf(df.format(new Date())));
        extension.setContent(Arrays.<Object>asList(additionalAttributes));
        oleInstance.setExtension(extension);
        return oleInstance;
    }

    private String toXML(InstanceCollection oleInstanceCollection) {
        XStream xs = new XStream();
        xs.autodetectAnnotations(true);
        xs.processAnnotations(InstanceCollection.class);
        String xml = xs.toXML(oleInstanceCollection);
        return xml;
    }
}
