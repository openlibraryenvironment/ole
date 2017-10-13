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
package org.kuali.rice.krad.datadictionary.uif;

import com.google.common.collect.Maps;
import org.kuali.rice.krad.datadictionary.DictionaryBeanBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Common base class for dictionary objects that can contain dynamic expressions within the
 * property value
 *
 * <p>
 * Should be extended by other classes to provide property expression support
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifDictionaryBeanBase extends DictionaryBeanBase implements UifDictionaryBean {

    private Map<String, String> expressionGraph;
    private Map<String, String> refreshExpressionGraph;
    private Map<String, String> propertyExpressions;

    public UifDictionaryBeanBase() {
        expressionGraph = new HashMap<String, String>();
        refreshExpressionGraph = new HashMap<String, String>();
        propertyExpressions = new HashMap<String, String>();
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#getExpressionGraph()
     */
    public Map<String, String> getExpressionGraph() {
        return expressionGraph;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#setExpressionGraph(java.util.Map<java.lang.String,java.lang.String>)
     */
    public void setExpressionGraph(Map<String, String> expressionGraph) {
        this.expressionGraph = expressionGraph;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#getRefreshExpressionGraph()
     */
    public Map<String, String> getRefreshExpressionGraph() {
        return refreshExpressionGraph;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#setRefreshExpressionGraph(java.util.Map<java.lang.String,java.lang.String>)
     */
    public void setRefreshExpressionGraph(Map<String, String> refreshExpressionGraph) {
        this.refreshExpressionGraph = refreshExpressionGraph;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#getPropertyExpressions
     */
    public Map<String, String> getPropertyExpressions() {
        return propertyExpressions;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#setPropertyExpressions
     */
    public void setPropertyExpressions(Map<String, String> propertyExpressions) {
        this.propertyExpressions = propertyExpressions;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#getPropertyExpression
     */
    public String getPropertyExpression(String propertyName) {
        if (this.propertyExpressions.containsKey(propertyName)) {
            return this.propertyExpressions.get(propertyName);
        }

        return null;
    }

    @Override
    protected <T> void copyProperties(T dictionaryBaseBean) {
        super.copyProperties(dictionaryBaseBean);
        UifDictionaryBeanBase uifDictionaryBeanBaseCopy = (UifDictionaryBeanBase) dictionaryBaseBean;

        if (expressionGraph != null) {
            Map<String, String> expressionGraphCopy = Maps.newHashMapWithExpectedSize(this.getExpressionGraph().size());
            for (Map.Entry expressionGraphEntry : getExpressionGraph().entrySet()) {
                expressionGraphCopy.put(expressionGraphEntry.getKey().toString(),
                        expressionGraphEntry.getValue().toString());
            }

            uifDictionaryBeanBaseCopy.setExpressionGraph(expressionGraphCopy);
        }

        if (refreshExpressionGraph != null) {
            Map<String, String> refreshExpressionGraphCopy = Maps.newHashMapWithExpectedSize(
                    this.getRefreshExpressionGraph().size());
            for (Map.Entry refreshExpressionGraphEntry : getRefreshExpressionGraph().entrySet()) {
                refreshExpressionGraphCopy.put(refreshExpressionGraphEntry.getKey().toString(),
                        refreshExpressionGraphEntry.getValue().toString());
            }

            uifDictionaryBeanBaseCopy.setRefreshExpressionGraph(refreshExpressionGraphCopy);
        }

        if (propertyExpressions != null) {
            Map<String, String> propertyExpressionsCopy = Maps.newHashMapWithExpectedSize(
                    this.getPropertyExpressions().size());
            for (Map.Entry propertyExpressionsEntry : getPropertyExpressions().entrySet()) {
                propertyExpressionsCopy.put(propertyExpressionsEntry.getKey().toString(),
                        propertyExpressionsEntry.getValue().toString());
            }

            uifDictionaryBeanBaseCopy.setPropertyExpressions(propertyExpressionsCopy);
        }
    }
}
