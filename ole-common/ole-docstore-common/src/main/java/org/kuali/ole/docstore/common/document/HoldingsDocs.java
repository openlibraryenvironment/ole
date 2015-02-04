package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
        StringWriter sw = new StringWriter();
        HoldingsDocs holdingsDocs = (HoldingsDocs) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsDocs.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(holdingsDocs, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    public static Object deserialize(String holdingsDocsXml) {

        JAXBElement<HoldingsDocs> holdingsDocsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsDocs.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(holdingsDocsXml.getBytes("UTF-8"));
            holdingsDocsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), HoldingsDocs.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return holdingsDocsElement.getValue();
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
