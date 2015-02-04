package org.kuali.ole.docstore.common.find;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;


/**
 * <p>Java class for findParams complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="findParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="map">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findParams", propOrder = {
        "map"
})
@XmlRootElement
public class FindParams {

    private static final Logger LOG = Logger.getLogger(FindParams.class);
    @XmlElement(required = true)
    protected FindParams.Map map;

    /**
     * Gets the value of the map property.
     *
     * @return
     *     possible object is
     *     {@link FindParams.Map }
     *
     */
    public FindParams.Map getMap() {
        return map;
    }

    /**
     * Sets the value of the map property.
     *
     * @param value
     *     allowed object is
     *     {@link FindParams.Map }
     *
     */
    public void setMap(FindParams.Map value) {
        this.map = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "entry"
    })
    public static class Map {

        protected List<FindParams.Map.Entry> entry;

        /**
         * Gets the value of the entry property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FindParams.Map.Entry }
         *
         *
         */
        public List<FindParams.Map.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<FindParams.Map.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java class for anonymous complex type.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "key",
                "value"
        })
        public static class Entry {

            protected String key;
            protected String value;

            /**
             * Gets the value of the key property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        FindParams findParams = (FindParams) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(FindParams.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(findParams, sw);
            result = sw.toString();
            System.out.println("Result is " + result);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }


    public Object deserialize(String content) {

        JAXBElement<FindParams> findParamsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(FindParams.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            findParamsElement  = jaxbUnmarshaller.unmarshal(new StreamSource(input),FindParams.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return findParamsElement .getValue();
    }
}
