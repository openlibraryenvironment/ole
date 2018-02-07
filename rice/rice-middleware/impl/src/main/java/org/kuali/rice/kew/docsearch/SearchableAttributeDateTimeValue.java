/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.docsearch;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jdbc.sql.SqlBuilder;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeDateTime;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_DOC_HDR_EXT_DT_T")
//@Sequence(name="KREW_SRCH_ATTR_S",property="searchableAttributeValueId")
@NamedQueries({
	@NamedQuery(name="SearchableAttributeDateTimeValue.FindByDocumentId", query="select s from SearchableAttributeDateTimeValue as s where s.documentId = :documentId"),
	@NamedQuery(name="SearchableAttributeDateTimeValue.FindByKey", query="select s from SearchableAttributeDateTimeValue as s where s.documentId = :documentId and s.searchableAttributeKey = :searchableAttributeKey")
})
public class SearchableAttributeDateTimeValue implements SearchableAttributeValue, Serializable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SearchableAttributeDateTimeValue.class);

    private static final long serialVersionUID = 3045621112943214772L;

    private static final String ATTRIBUTE_DATABASE_TABLE_NAME = "KREW_DOC_HDR_EXT_DT_T";
    private static final boolean DEFAULT_WILDCARD_ALLOWANCE_POLICY = false;
    private static final boolean ALLOWS_RANGE_SEARCH = true;
    private static final boolean ALLOWS_CASE_INSENSITIVE_SEARCH = false;
    private static final String ATTRIBUTE_XML_REPRESENTATION = KewApiConstants.SearchableAttributeConstants.DATA_TYPE_DATE;

    @Id
    @GeneratedValue(generator="KREW_SRCH_ATTR_S")
	@GenericGenerator(name="KREW_SRCH_ATTR_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_SRCH_ATTR_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="DOC_HDR_EXT_DT_ID")
	private String searchableAttributeValueId;
    @Column(name="KEY_CD")
	private String searchableAttributeKey;
	@Column(name="VAL")
	private Timestamp searchableAttributeValue;
    @Transient
    protected String ojbConcreteClass; // attribute needed for OJB polymorphism - do not alter!

    @Column(name="DOC_HDR_ID")
	private String documentId;
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="DOC_HDR_ID", insertable=false, updatable=false)
	private DocumentRouteHeaderValue routeHeader;

    /**
     * Default constructor.
     */
    public SearchableAttributeDateTimeValue() {
        super();
        this.ojbConcreteClass = this.getClass().getName();
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#setupAttributeValue(java.lang.String)
     */
    public void setupAttributeValue(String value) {
        this.setSearchableAttributeValue(convertStringToTimestamp(value));
    }

    private Timestamp convertStringToTimestamp(String value) {
        if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
            return null;
        } else {
            Timestamp t;
            try {
            	t = CoreApiServiceLocator.getDateTimeService().convertToSqlTimestamp(value);
            } catch (ParseException e) {
            	t = null;
            }
            if (t == null) {
                String errorMsg = "Error converting timestamp value '" + value + "' to valid timestamp object.";
                LOG.error("setupAttributeValue() " + errorMsg);
                throw new RuntimeException(errorMsg);
            }
            return t;
        }
    }

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#setupAttributeValue(java.sql.ResultSet, java.lang.String)
	 */
	public void setupAttributeValue(ResultSet resultSet, String columnName) throws SQLException {
		Calendar c = Calendar.getInstance();
		c.clear(Calendar.HOUR);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		this.setSearchableAttributeValue(resultSet.getTimestamp(columnName, c));
	}

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#getSearchableAttributeDisplayValue()
	 */
    public String getSearchableAttributeDisplayValue() {
        return formatAttributeValue(null);
    }

    private String formatAttributeValue(String formatPattern) {
        DateFormat df = getDateFormatToUse(formatPattern);
        return df.format(new Date(getSearchableAttributeValue().getTime()));
    }

    private DateFormat getDateFormatToUse(String parameterFormatPattern) {
        if (StringUtils.isNotBlank(parameterFormatPattern)) {
            return new SimpleDateFormat(parameterFormatPattern);
        }
        return RiceConstants.getDefaultDateFormat();
    }

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#getAttributeDataType()
	 */
	public String getAttributeDataType() {
		return ATTRIBUTE_XML_REPRESENTATION;
	}

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#getAttributeTableName()
	 */
	public String getAttributeTableName() {
		return ATTRIBUTE_DATABASE_TABLE_NAME;
	}

    /* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#allowsWildcardsByDefault()
	 */
	public boolean allowsWildcards() {
		return DEFAULT_WILDCARD_ALLOWANCE_POLICY;
	}

    /* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#allowsCaseInsensitivity()
	 */
	public boolean allowsCaseInsensitivity() {
		return ALLOWS_CASE_INSENSITIVE_SEARCH;
	}

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#allowsRangeSearches()
	 */
	public boolean allowsRangeSearches() {
		return ALLOWS_RANGE_SEARCH;
	}

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#isPassesDefaultValidation()
	 */
    public boolean isPassesDefaultValidation(String valueEntered) {
    	return new SqlBuilder().isValidDate(valueEntered);
        //return (DocSearchUtils.getEntryFormattedDate(valueEntered) != null);
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.docsearch.SearchableAttributeValue#isRangeValid(java.lang.String, java.lang.String)
     */
    public Boolean isRangeValid(String lowerValue, String upperValue) {
        if (allowsRangeSearches()) {
            Timestamp lowerTime = convertStringToTimestamp(lowerValue);
            Timestamp upperTime = convertStringToTimestamp(upperValue);
            if ( (lowerTime != null) && (upperTime != null) ) {
                return (lowerTime.compareTo(upperTime) <= 0);
            }
            return true;
        }
        return null;
    }

	public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }

    public DocumentRouteHeaderValue getRouteHeader() {
        return routeHeader;
    }

    public void setRouteHeader(DocumentRouteHeaderValue routeHeader) {
        this.routeHeader = routeHeader;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getSearchableAttributeKey() {
        return searchableAttributeKey;
    }

    public void setSearchableAttributeKey(String searchableAttributeKey) {
        this.searchableAttributeKey = searchableAttributeKey;
    }

    public Timestamp getSearchableAttributeValue() {
        return searchableAttributeValue;
    }

    public void setSearchableAttributeValue(Timestamp searchableAttributeValue) {
        this.searchableAttributeValue = searchableAttributeValue;
    }

    public String getSearchableAttributeValueId() {
        return searchableAttributeValueId;
    }

    public void setSearchableAttributeValueId(String searchableAttributeValueId) {
        this.searchableAttributeValueId = searchableAttributeValueId;
    }

	//@PrePersist
	public void beforeInsert(){
		OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
	}

    @Override
    public DocumentAttributeDateTime toDocumentAttribute() {
        DateTime dateTime = null;
        if (getSearchableAttributeValue() != null) {
            dateTime = new DateTime(getSearchableAttributeValue().getTime());
        }
        return DocumentAttributeFactory.createDateTimeAttribute(getSearchableAttributeKey(), dateTime);
    }
}

