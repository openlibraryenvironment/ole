package org.kuali.ole.docstore.common.document.content.instance;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.StringWriter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 4/17/14
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsTrees", propOrder = {
        "holdingsTrees"
})

@XStreamAlias("holdingsTrees")
@XmlRootElement(name = "holdingsTrees")
public class HoldingsTrees {

    private static final Logger LOG = Logger.getLogger(HoldingsTrees.class);

    @XmlElement(name = "holdingsTree")
    List<HoldingsTree> holdingsTrees;

    public List<HoldingsTree> getHoldingsTrees() {
        return holdingsTrees;
    }

    public void setHoldingsTrees(List<HoldingsTree> holdingsTrees) {
        this.holdingsTrees = holdingsTrees;
    }

    public String serialize(Object object) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<holdingsTrees>");
        HoldingsTrees holdingsTrees = (HoldingsTrees) object;
        for(HoldingsTree holdingsTree : holdingsTrees.getHoldingsTrees()){
            stringBuilder.append(holdingsTree.serialize(holdingsTree));
        }
        stringBuilder.append("</holdingsTrees>");
        return stringBuilder.toString();
    }

    public static String serialize(HoldingsTrees holdingsTrees) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<holdingsTrees>");
        for(HoldingsTree holdingsTree : holdingsTrees.getHoldingsTrees()){
            stringBuilder.append(holdingsTree.serialize(holdingsTree));
        }
        stringBuilder.append("</holdingsTrees>");
        return stringBuilder.toString();
    }

}
