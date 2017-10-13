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
package edu.sampleu.bookstore.document.attribs;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableDatepicker;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.jws.WebParam;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Base class for simple attributes which extract values from document content via an xpath expression.
 * Compare to {@link org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute}.
 * In most cases it's simplest to just define an SGXSA. This class exists expressly to aid testing
 * non-SGXSA attributes, and illustrates performing proper validation.
 */
public abstract class XPathSearchableAttribute implements SearchableAttribute {
    protected final Logger log;
    protected final String key;
    protected final String title;
    protected final String xpathExpression;
    protected final String dataType;

    protected XPathSearchableAttribute(String key, String dataType, String xpathExpression) {
        this(key, dataType, xpathExpression, null);
    }

    protected XPathSearchableAttribute(String key, String dataType, String xpathExpression, String title) {
        this.key = key;
        this.dataType = dataType;
        this.xpathExpression = xpathExpression;
        this.log = Logger.getLogger(getClass().getName() + ":" + key);
        this.title = title == null ? log.getName(): title;
    }
    
    @Override
    public String generateSearchContent(@WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
                                        @WebParam(name = "documentTypeName") String documentTypeName,
                                        @WebParam(name = "attributeDefinition") WorkflowAttributeDefinition attributeDefinition) {
        // no custom search content, we just use the document content directly
        return null;
    }

    @Override
    public List<DocumentAttribute> extractDocumentAttributes(@WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
                                                             @WebParam(name = "documentWithContent") DocumentWithContent documentWithContent) {
        List<DocumentAttribute> attribs = new ArrayList<DocumentAttribute>(1);
        String appContent = documentWithContent.getDocumentContent().getApplicationContent();
        XPath xpath = XPathHelper.newXPath();
        try {
            //InputSource source = new StringReader(appContent);
            Element source = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(appContent)))).getDocumentElement();
            String result = (String) xpath.evaluate(xpathExpression, source, XPathConstants.STRING);
            // xpath has no concept of null node, missing text values are the empty string
            if (StringUtils.isNotEmpty(result)) {
                try {
                    attribs.add(createAttribute(this.key, result, this.dataType));
                } catch (ParseException pe) {
                    log.error("Error converting value '" + result + "' to type '" + this.dataType + "'");
                }
            }
        } catch (XPathExpressionException xep) {
            log.error("Error evaluating searchable attribute expression: '" + this.xpathExpression + "'", xep);
        } catch (SAXException se) {
            log.error("Error parsing application content: '" + appContent + "'", se);
        } catch (ParserConfigurationException pce) {
            log.error("Error parsing application content: '" + appContent + "'", pce);
        } catch (IOException ioe) {
            log.error("Error parsing application content: '" + appContent + "'", ioe);
        }
        return attribs;
    }

    /**
     * Creates an DocumentAttribute of the specified type
     */
    protected static DocumentAttribute createAttribute(String name, String value, String dataTypeValue) throws ParseException {
        if (StringUtils.isBlank(dataTypeValue)) {
            return DocumentAttributeFactory.createStringAttribute(name, value);
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_STRING.equals(dataTypeValue)) {
            return DocumentAttributeFactory.createStringAttribute(name, value);
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_DATE.equals(dataTypeValue)) {
            try {
                return DocumentAttributeFactory.createDateTimeAttribute(name, CoreApiServiceLocator.getDateTimeService().convertToDate(value));
            } catch (ParseException pe) {
                // HACK: KRAD is sending us yyyy-MM-dd which is not in the standard format list...
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                Date date = dateFormat.parse(value);
                return DocumentAttributeFactory.createDateTimeAttribute(name, date);
            }
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_LONG.equals(dataTypeValue)) {
            return DocumentAttributeFactory.createIntegerAttribute(name, new BigInteger(value));
        } else if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_FLOAT.equals(dataTypeValue)) {
            return DocumentAttributeFactory.createDecimalAttribute(name, new BigDecimal(value));
        }
        throw new IllegalArgumentException("Invalid dataTypeValue was given: " + dataTypeValue);
    }

    @Override
    public List<RemotableAttributeField> getSearchFields(@WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
                                                         @WebParam(name = "documentTypeName") String documentTypeName) {
        List<RemotableAttributeField> fields = new ArrayList<RemotableAttributeField>();
        RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create(key);
        builder.setLongLabel(this.title);
        builder.setDataType(DocumentSearchInternalUtils.convertValueToDataType(this.dataType));
        if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_DATE.equals(this.dataType)) {
            builder.getWidgets().add(RemotableDatepicker.Builder.create());
        }
        builder = decorateRemotableAttributeField(builder);
        fields.add(builder.build());
        return fields;
    }

    /**
     * Template method for subclasses to customize the remotableattributefield
     * @return modified or new RemotableAttributeField.Builder
     */
    protected RemotableAttributeField.Builder decorateRemotableAttributeField(RemotableAttributeField.Builder raf) {
        return raf;
    }

    @Override
    public List<RemotableAttributeError> validateDocumentAttributeCriteria(@WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
                                                                           @WebParam(name = "documentSearchCriteria") DocumentSearchCriteria documentSearchCriteria) {
        SearchableAttributeValue valueType = DocumentSearchInternalUtils.getSearchableAttributeValueByDataTypeString(this.dataType);
        return DocumentSearchInternalUtils.validateSearchFieldValues(this.key, valueType, documentSearchCriteria.getDocumentAttributeValues().get(key), log.getName(), null, null);
    }

}
