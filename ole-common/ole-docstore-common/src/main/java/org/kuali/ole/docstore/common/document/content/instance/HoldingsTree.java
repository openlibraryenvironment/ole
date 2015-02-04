package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 4/17/14
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsTree", propOrder = {
        "oleHoldings",
        "items"
})

@XStreamAlias("holdingsTree")
@XmlRootElement(name = "holdingsTree")
public class HoldingsTree {

    private static final Logger LOG = Logger.getLogger(HoldingsTree.class);

    @XmlElement(name ="oleHoldings")
    private OleHoldings oleHoldings;

    @XmlElement(name = "items")
    private Items items;

    public OleHoldings getOleHoldings() {
        return oleHoldings;
    }

    public void setOleHoldings(OleHoldings oleHoldings) {
        this.oleHoldings = oleHoldings;
    }

    /*public HoldingsSerialReceiving getHoldingsSerialReceiving() {
        return holdingsSerialReceiving;
    }

    public void setHoldingsSerialReceiving(HoldingsSerialReceiving holdingsSerialReceiving) {
        this.holdingsSerialReceiving = holdingsSerialReceiving;
    }*/

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public String serialize(Object object) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<holdingsTree>");
        HoldingsTree holdingsTree = (HoldingsTree) object;
        stringBuilder.append(holdingsTree.getOleHoldings().serialize(holdingsTree.getOleHoldings()));
        stringBuilder.append(holdingsTree.getItems().serialize(holdingsTree.getItems()));
        stringBuilder.append("</holdingsTree>");
        return stringBuilder.toString();
    }

    public String serialize(HoldingsTree holdingsTree) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<holdingsTree>");
        stringBuilder.append(holdingsTree.getOleHoldings().serialize(holdingsTree.getOleHoldings()));
        stringBuilder.append(holdingsTree.getItems().serialize(holdingsTree.getItems()));
        stringBuilder.append("</holdingsTree>");
        return stringBuilder.toString();
    }
}
