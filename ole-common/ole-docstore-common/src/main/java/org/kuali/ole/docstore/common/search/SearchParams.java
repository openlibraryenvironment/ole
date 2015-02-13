package org.kuali.ole.docstore.common.search;


import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocType;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;


/**
 * <p>Java class for searchParams complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="searchParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="searchConditions" type="{}searchCondition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="searchResultFields" type="{}searchResultField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sortConditions" type="{}sortCondition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="startIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchParams", propOrder = {
        "docType",
        "pageSize",
        "searchConditions",
        "facetConditions",
        "searchResultFields",
        "sortConditions",
        "facetFields",
        "startIndex",
        "facetLimit",
        "facetPrefix",
        "facetSort",
        "facetOffset"
})

@XmlRootElement
public class SearchParams {

    private static final Logger LOG = Logger.getLogger(SearchParams.class);
    protected String docType = "";
    protected int pageSize = 0;
    @XmlElementWrapper(name = "facetConditions")
    @XmlElement(name = "facetCondition")
    protected List<FacetCondition> facetConditions;
    @XmlElementWrapper(name = "searchConditions")
    @XmlElement(name = "searchCondition")
    protected List<SearchCondition> searchConditions=new ArrayList<SearchCondition>();
    @XmlElementWrapper(name = "searchResultFields")
    @XmlElement(name = "searchResultField")
    protected List<SearchResultField> searchResultFields;
    @XmlElementWrapper(name = "sortConditions")
    @XmlElement(name = "sortCondition")
    protected List<SortCondition> sortConditions;
    protected TreeSet<String> facetFields;
    protected int startIndex = 0;
    protected int facetLimit =0;
    protected String facetPrefix;
    protected String facetSort = "count";
    protected int facetOffset = 0;

    public int getFacetLimit() {
        return facetLimit;
    }

    public SearchParams() {

    }



    public void setFacetLimit(int facetLimit) {
        this.facetLimit = facetLimit;
    }

    /**
     * Gets the value of the facetCondtions property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the facetCondtions property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFacetCondtions().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link FacetCondition}
     */
    public List<FacetCondition> getFacetConditions() {
        if (facetConditions == null) {
            facetConditions = new ArrayList<FacetCondition>();
        }
        return this.facetConditions;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * Gets the value of the pageSize property.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     */
    public void setPageSize(int value) {
        this.pageSize = value;
    }

    /**
     * Gets the value of the searchConditions property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchConditions property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchConditions().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchCondition }
     */
    public List<SearchCondition> getSearchConditions() {
        if (searchConditions == null) {
            searchConditions = new ArrayList<SearchCondition>();
        }
        return this.searchConditions;
    }

    /**
     * Gets the value of the searchResultFields property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchResultFields property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchResultFields().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchResultField }
     */
    public List<SearchResultField> getSearchResultFields() {
        if (searchResultFields == null) {
            searchResultFields = new ArrayList<SearchResultField>();
        }
        return this.searchResultFields;
    }

    /**
     * Gets the value of the sortConditions property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sortConditions property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSortConditions().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link SortCondition }
     */
    public List<SortCondition> getSortConditions() {
        if (sortConditions == null) {
            sortConditions = new ArrayList<SortCondition>();
        }
        return this.sortConditions;
    }

    public TreeSet<String> getFacetFields() {
        if(facetFields == null) {
            facetFields = new TreeSet<>();
        }
        return facetFields;
    }

    /**
     * Gets the value of the startIndex property.
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Sets the value of the startIndex property.
     */
    public void setStartIndex(int value) {
        this.startIndex = value;
    }

    public String getFacetPrefix() {
        return facetPrefix;
    }

    public void setFacetPrefix(String facetPrefix) {
        this.facetPrefix = facetPrefix;
    }

    public String getFacetSort() {
        return facetSort;
    }

    public void setFacetSort(String facetSort) {
        this.facetSort = facetSort;
    }

    public int getFacetOffset() {
        return facetOffset;
    }

    public void setFacetOffset(int facetOffset) {
        this.facetOffset = facetOffset;
    }

    public SearchCondition buildSearchCondition(String searchScope, SearchField searchField, String operator) {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchScope(searchScope);
        searchCondition.setSearchField(searchField);
        searchCondition.setOperator(operator);
        return searchCondition;
    }

    public SearchField buildSearchField(String docType, String fieldName, String fieldValue) {
        SearchField searchField = new SearchField();
        searchField.setDocType(docType);
        searchField.setFieldName(fieldName);
        searchField.setFieldValue(fieldValue);
        return searchField;
    }

    public SortCondition buildSortCondition(String sortField, String sortOrder) {
        SortCondition sortCondition = new SortCondition();
        sortCondition.setSortOrder(sortOrder);
        sortCondition.setSortField(sortField);
        return sortCondition;
    }

    public SearchResultField buildSearchResultField(String docType, String fieldName) {
        SearchResultField searchResultField = new SearchResultField();
        searchResultField.setDocType(docType);
        searchResultField.setFieldName(fieldName);
        return searchResultField;
    }

    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        SearchParams searchParams = (SearchParams) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchParams.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(searchParams, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return result;
    }

    public Object deserialize(String content) {

        JAXBElement<SearchParams> SearchParamsElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchParams.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            SearchParamsElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), SearchParams.class);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        return SearchParamsElement.getValue();
    }

    public void buildSearchParams(SearchParams searchParams, String docType){

        if(DocType.BIB.getCode().equalsIgnoreCase(docType)){
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_sort"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "JournalTitle_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Author_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Publisher_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ISBN_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ISSN_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Subject_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Publisher_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "PublicationDate_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Edition_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Format_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Language_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Description_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "FormGenre_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "staffOnlyFlag"));
        } else if(DocType.HOLDINGS.getCode().equalsIgnoreCase(docType)){
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_sort"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CallNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CallNumberPrefix_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ClassificationPart_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingOrder_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingOrderCode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingSchemeCode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingSchemeValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Uri_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ReceiptStatus_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CopyNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "itemIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "holdingsIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "LocationLevelName_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "HoldingsNote_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "staffOnlyFlag"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "isSeries"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "isAnalytic"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "isBoundwith"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ExtentOfOwnership_Type_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ExtentOfOwnership_Note_Value_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ExtentOfOwnership_Note_Type_display"));
        } else if(DocType.EHOLDINGS.getCode().equalsIgnoreCase(docType)){
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_sort"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "AccessStatus_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Platform_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Imprint_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "StatisticalSearchingFullValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "StatisticalSearchingCodeValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "holdingsIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "staffOnlyFlag"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "LocationLevelName_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CallNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CallNumberPrefix_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingSchemeCode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingSchemeValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Access_Password_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Access_UserName_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "AccessLocation_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Admin_Password_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Admin_url_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Admin_UserName_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Authentication_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CoverageDate_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DonorCode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DonorPublic_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DonorNote_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "E_Publisher_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "HoldingsNote_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ILL_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ItemPart_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Link_Text_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "NumberOfSimultaneousUses_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "PerpetualAccess_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Persist_Link_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Proxied_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ReceiptStatus_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Subscription_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Url_display"));
        } else if(DocType.ITEM.getCode().equalsIgnoreCase(docType)){
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_sort"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CallNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CallNumberPrefix_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ClassificationPart_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingOrder_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingOrderCode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingSchemeCode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ShelvingSchemeValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "holdingsIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "id"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ItemBarcode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ItemStatus_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ItemUri_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CopyNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "CopyNumberLabel_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "VolumeNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "VolumeNumberLabel_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Enumeration_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "Chronology_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "staffOnlyFlag"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "isAnalytic"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ItemTypeCodeValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "ItemTypeFullValue_display"));

        }
    }

}
