package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.factory.JAXBContextFactory;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/4/14
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "holdingsDocs", propOrder = {
        "holdingsDocs"
})
@XmlRootElement(name="holdingsDocs")
public class HoldingsDocs {

    private static final Logger LOG = Logger.getLogger(Items.class);
    @XmlElement(name = "holdingsDoc")
    protected List<Holdings> holdingsDocs;


    public static String serialize(Object object) {
        String result = null;
        HoldingsDocs holdingsDocs = (HoldingsDocs) object;
        try {
            StringWriter sw = new StringWriter();
            Marshaller jaxbMarshaller = JAXBContextFactory.getInstance().getMarshaller(HoldingsDocs.class);
            synchronized (jaxbMarshaller) {
                jaxbMarshaller.marshal(holdingsDocs, sw);
            }
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }


    public static Object deserialize(String content) {
        HoldingsDocs holdings = new HoldingsDocs();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(HoldingsDocs.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            synchronized (unmarshaller) {
                holdings = unmarshaller.unmarshal(new StreamSource(input), HoldingsDocs.class).getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return holdings;
    }

    /**
     * Gets the value of the holdingsDocs property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the holdingsDocs property.
     * <p/>
     * <p/>
     * For example, to add a new holdings, do as follows:
     * <pre>
     *    getHoldingsDocs().add(newHoldings);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Bib }
     */
    public List<Holdings> getHoldingsDocs() {
        if (holdingsDocs == null) {
            holdingsDocs = new ArrayList<Holdings>();
        }
        return this.holdingsDocs;
    }
}
