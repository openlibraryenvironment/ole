package org.kuali.ole.docstore.common.search;

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
 * <p>Java class for searchResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="searchResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="endIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="searchResults" type="{}searchResult" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="startIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="totalRecordCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchResponse", propOrder = {
        "endIndex",
        "pageSize",
        "searchResults",
        "startIndex",
        "totalRecordCount",
        "facetResult",
        "time"
})
@XmlRootElement
public class SearchResponse {

    private static final Logger LOG = Logger.getLogger(SearchResponse.class);
    protected int endIndex;
    protected int pageSize;

    @XmlElementWrapper(name = "searchResults")
    @XmlElement(name = "searchResult")
    protected List<SearchResult> searchResults;
    protected int startIndex;
    protected int totalRecordCount;
    protected FacetResult facetResult;

    protected int time;

    public FacetResult getFacetResult() {
        return facetResult;
    }

    public void setFacetResult(FacetResult facetResult) {
        this.facetResult = facetResult;
    }

    /**
     * Gets the value of the endIndex property.
     *
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Sets the value of the endIndex property.
     *
     */
    public void setEndIndex(int value) {
        this.endIndex = value;
    }

    /**
     * Gets the value of the pageSize property.
     *
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     *
     */
    public void setPageSize(int value) {
        this.pageSize = value;
    }

    /**
     * Gets the value of the searchResults property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchResults property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchResults().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchResult }
     *
     *
     */
    public List<SearchResult> getSearchResults() {
        if (searchResults == null) {
            searchResults = new ArrayList<SearchResult>();
        }
        return this.searchResults;
    }

    /**
     * Gets the value of the startIndex property.
     *
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Sets the value of the startIndex property.
     *
     */
    public void setStartIndex(int value) {
        this.startIndex = value;
    }

    /**
     * Gets the value of the totalRecordCount property.
     *
     */
    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    /**
     * Sets the value of the totalRecordCount property.
     *
     */
    public void setTotalRecordCount(int value) {
        this.totalRecordCount = value;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        SearchResponse searchResponse = (SearchResponse) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchResponse.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(searchResponse, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }


    public Object deserialize(String content) {

        JAXBElement<SearchResponse> SearchResponseElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchResponse.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            SearchResponseElement = jaxbUnmarshaller.unmarshal(new StreamSource(input),SearchResponse.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return SearchResponseElement.getValue();
    }
}
