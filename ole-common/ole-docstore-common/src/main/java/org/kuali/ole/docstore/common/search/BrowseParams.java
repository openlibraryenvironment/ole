package org.kuali.ole.docstore.common.search;


import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;


/**
 * <p>Java class for browseParams complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="browseParams">
 *   &lt;complexContent>
 *     &lt;extension base="{}searchParams">
 *       &lt;sequence>
 *         &lt;element name="totalCount" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "browseParams", propOrder = {
        "totalCount"
})
@XmlRootElement
public class BrowseParams
        extends SearchParams
{
    private static final Logger LOG = Logger.getLogger(BrowseParams.class);
    protected long totalCount;

    /**
     * Gets the value of the totalCount property.
     *
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * Sets the value of the totalCount property.
     *
     */
    public void setTotalCount(long value) {
        this.totalCount = value;
    }

   @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        BrowseParams browseParams = (BrowseParams) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BrowseParams.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(browseParams, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {

        JAXBElement<BrowseParams> browseParamsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BrowseParams.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            browseParamsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input),BrowseParams.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return browseParamsElement.getValue();
    }

}
