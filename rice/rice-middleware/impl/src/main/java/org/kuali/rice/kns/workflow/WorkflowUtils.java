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
package org.kuali.rice.kns.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.rule.xmlrouting.WorkflowFunctionResolver;
import org.kuali.rice.kew.rule.xmlrouting.WorkflowNamespaceContext;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.UrlFactory;
import org.w3c.dom.Document;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class WorkflowUtils {
    private static final String XPATH_ROUTE_CONTEXT_KEY = "_xpathKey";
    public static final String XSTREAM_SAFE_PREFIX = "wf:xstreamsafe('";
    public static final String XSTREAM_SAFE_SUFFIX = "')";
    public static final String XSTREAM_MATCH_ANYWHERE_PREFIX = "//";
    public static final String XSTREAM_MATCH_RELATIVE_PREFIX = "./";

	private WorkflowUtils() {
		throw new UnsupportedOperationException("do not call");
	}
    
    /**
     *
     * This method sets up the XPath with the correct workflow namespace and resolver initialized. This ensures that the XPath
     * statements can use required workflow functions as part of the XPath statements.
     *
     * @param document - document
     * @return a fully initialized XPath instance that has access to the workflow resolver and namespace.
     *
     */
    public final static XPath getXPath(Document document) {
        XPath xpath = getXPath(RouteContext.getCurrentRouteContext());
        xpath.setNamespaceContext(new WorkflowNamespaceContext());
        WorkflowFunctionResolver resolver = new WorkflowFunctionResolver();
        resolver.setXpath(xpath);
        resolver.setRootNode(document);
        xpath.setXPathFunctionResolver(resolver);
        return xpath;
    }

    public final static XPath getXPath(RouteContext routeContext) {
        if (routeContext == null) {
            return XPathFactory.newInstance().newXPath();
        }
        if (!routeContext.getParameters().containsKey(XPATH_ROUTE_CONTEXT_KEY)) {
            routeContext.getParameters().put(XPATH_ROUTE_CONTEXT_KEY, XPathFactory.newInstance().newXPath());
        }
        return (XPath) routeContext.getParameters().get(XPATH_ROUTE_CONTEXT_KEY);
    }

    /**
     * This method will do a simple XPath.evaluate, while wrapping your xpathExpression with the xstreamSafe function. It assumes a
     * String result, and will return such. If an XPathExpressionException is thrown, this will be re-thrown within a
     * RuntimeException.
     *
     * @param xpath A correctly initialized XPath instance.
     * @param xpathExpression Your XPath Expression that needs to be wrapped in an xstreamSafe wrapper and run.
     * @param item The document contents you will be searching within.
     * @return The string value of the xpath.evaluate().
     */
    public static final String xstreamSafeEval(XPath xpath, String xpathExpression, Object item) {
        String xstreamSafeXPath = xstreamSafeXPath(xpathExpression);
        String evalResult = "";
        try {
            evalResult = xpath.evaluate(xstreamSafeXPath, item);
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException("XPathExpressionException occurred on xpath: " + xstreamSafeXPath, e);
        }
        return evalResult;
    }

    /**
     * This method wraps the passed-in XPath expression in XStream Safe wrappers, so that XStream generated reference links will be
     * handled correctly.
     *
     * @param xpathExpression The XPath Expression you wish to use.
     * @return Your XPath Expression wrapped in the XStreamSafe wrapper.
     */
    public static final String xstreamSafeXPath(String xpathExpression) {
        return new StringBuilder(XSTREAM_SAFE_PREFIX).append(xpathExpression).append(XSTREAM_SAFE_SUFFIX).toString();
    }

    /**
     * This method returns a label from the data dictionary service
     *
     * @param businessObjectClass - class where the label should come from
     * @param attributeName - name of the attribute you need the label for
     * @return the label from the data dictionary for the given Class and attributeName or null if not found
     */
    public static final String getBusinessObjectAttributeLabel(Class businessObjectClass, String attributeName) {
        return KRADServiceLocatorWeb.getDataDictionaryService().getAttributeLabel(businessObjectClass, attributeName);
    }


    /**
     * This method builds a workflow-lookup-screen Row of type TEXT, with no quickfinder/lookup.
     *
     * @param propertyClass The Class of the BO that this row is based on. For example, Account.class for accountNumber.
     * @param boPropertyName The property name on the BO that this row is based on. For example, accountNumber for
     *        Account.accountNumber.
     * @param workflowPropertyKey The workflow-lookup-screen property key. For example, account_nbr for Account.accountNumber. This
     *        key can be anything, but needs to be consistent with what is used for the row/field key on the java attribute, so
     *        everything links up correctly.
     * @return A populated and ready-to-use workflow lookupable.Row.
     */
    public static Row buildTextRow(Class propertyClass, String boPropertyName, String workflowPropertyKey) {
        if (propertyClass == null) {
            throw new IllegalArgumentException("Method parameter 'propertyClass' was passed a NULL value.");
        }
        if (StringUtils.isBlank(boPropertyName)) {
            throw new IllegalArgumentException("Method parameter 'boPropertyName' was passed a NULL or blank value.");
        }
        if (StringUtils.isBlank(workflowPropertyKey)) {
            throw new IllegalArgumentException("Method parameter 'workflowPropertyKey' was passed a NULL or blank value.");
        }
        List<Field> fields = new ArrayList<Field>();
        Field field;
        field = FieldUtils.getPropertyField(propertyClass, boPropertyName, false);
        fields.add(field);
        return new Row(fields);
    }

    /**
     * This method builds a workflow-lookup-screen Row of type TEXT, with the attached lookup icon and functionality.
     *
     * @param propertyClass The Class of the BO that this row is based on. For example, Account.class for accountNumber.
     * @param boPropertyName The property name on the BO that this row is based on. For example, accountNumber for
     *        Account.accountNumber.
     * @param workflowPropertyKey The workflow-lookup-screen property key. For example, account_nbr for Account.accountNumber. This
     *        key can be anything, but needs to be consistent with what is used for the row/field key on the java attribute, so
     *        everything links up correctly.
     * @return A populated and ready-to-use workflow lookupable.Row, which includes both the property field and the lookup icon.
     */
    public static Row buildTextRowWithLookup(Class propertyClass, String boPropertyName, String workflowPropertyKey) {
        return buildTextRowWithLookup(propertyClass, boPropertyName, workflowPropertyKey, null);
    }

    /**
     * This method builds a workflow-lookup-screen Row of type TEXT, with the attached lookup icon and functionality.
     *
     * @param propertyClass The Class of the BO that this row is based on. For example, Account.class for accountNumber.
     * @param boPropertyName The property name on the BO that this row is based on. For example, accountNumber for
     *        Account.accountNumber.
     * @param workflowPropertyKey The workflow-lookup-screen property key. For example, account_nbr for Account.accountNumber. This
     *        key can be anything, but needs to be consistent with what is used for the row/field key on the java attribute, so
     *        everything links up correctly.
     * @param fieldConversionsByBoPropertyName A list of extra field conversions where the key is the business object property name
     *        and the value is the workflow property key
     * @return A populated and ready-to-use workflow lookupable.Row, which includes both the property field and the lookup icon.
     */
    public static Row buildTextRowWithLookup(Class propertyClass, String boPropertyName, String workflowPropertyKey, Map fieldConversionsByBoPropertyName) {
        if (propertyClass == null) {
            throw new IllegalArgumentException("Method parameter 'propertyClass' was passed a NULL value.");
        }
        if (StringUtils.isBlank(boPropertyName)) {
            throw new IllegalArgumentException("Method parameter 'boPropertyName' was passed a NULL or blank value.");
        }
        if (StringUtils.isBlank(workflowPropertyKey)) {
            throw new IllegalArgumentException("Method parameter 'workflowPropertyKey' was passed a NULL or blank value.");
        }
        Field field;
        field = FieldUtils.getPropertyField(propertyClass, boPropertyName, false);

        List<Field> fields = new ArrayList<Field>();
        fields.add(field);
        return new Row(fields);
    }

    /**
     * This method builds a workflow-lookup-screen Row of type DROPDOWN.
     *
     * @param propertyClass The Class of the BO that this row is based on. For example, Account.class for accountNumber.
     * @param boPropertyName The property name on the BO that this row is based on. For example, accountNumber for
     *        Account.accountNumber.
     * @param workflowPropertyKey The workflow-lookup-screen property key. For example, account_nbr for Account.accountNumber. This
     *        key can be anything, but needs to be consistent with what is used for the row/field key on the java attribute, so
     *        everything links up correctly.
     * @param optionMap The map of value, text pairs that will be used to constuct the dropdown list.
     * @return A populated and ready-to-use workflow lookupable.Row.
     */
    public static Row buildDropdownRow(Class propertyClass, String boPropertyName, String workflowPropertyKey, Map<String, String> optionMap, boolean addBlankRow) {
        if (propertyClass == null) {
            throw new IllegalArgumentException("Method parameter 'propertyClass' was passed a NULL value.");
        }
        if (StringUtils.isBlank(boPropertyName)) {
            throw new IllegalArgumentException("Method parameter 'boPropertyName' was passed a NULL or blank value.");
        }
        if (StringUtils.isBlank(workflowPropertyKey)) {
            throw new IllegalArgumentException("Method parameter 'workflowPropertyKey' was passed a NULL or blank value.");
        }
        if (optionMap == null) {
            throw new IllegalArgumentException("Method parameter 'optionMap' was passed a NULL value.");
        }
        List<Field> fields = new ArrayList<Field>();
        Field field;
        field = FieldUtils.getPropertyField(propertyClass, boPropertyName, false);
        fields.add(field);
        return new Row(fields);
    }
}
