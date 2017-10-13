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
package org.kuali.rice.krad.keyvalues;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KeyValuesService;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is a Generic ValuesFinder that builds the list of KeyValuePairs it returns
 * in getKeyValues() based on a BO along with a keyAttributeName and labelAttributeName
 * that are specified.
 */
@Transactional
public class PersistableBusinessObjectValuesFinder <T extends PersistableBusinessObject> extends KeyValuesBase {

    private static final Log LOG = LogFactory.getLog(PersistableBusinessObjectValuesFinder.class);

    private Class<T> businessObjectClass;
    private String keyAttributeName;
    private String labelAttributeName;
    private boolean includeKeyInDescription = false;
    private boolean includeBlankRow = false;

    /**
     * Build the list of KeyValues using the key (keyAttributeName) and
     * label (labelAttributeName) of the list of all business objects found
     * for the BO class specified.
     *
     * @see org.kuali.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
	public List<KeyValue> getKeyValues() {
    	List<KeyValue> labels = new ArrayList<KeyValue>();

    	try {
    	    KeyValuesService boService = KRADServiceLocator.getKeyValuesService();
            Collection<T> objects = boService.findAll(businessObjectClass);
            if(includeBlankRow) {
            	labels.add(new ConcreteKeyValue("", ""));
            }
            for (T object : objects) {
            	Object key = PropertyUtils.getProperty(object, keyAttributeName);
            	String label = (String)PropertyUtils.getProperty(object, labelAttributeName);
            	if (includeKeyInDescription) {
            	    label = key + " - " + label;
            	}
            	labels.add(new ConcreteKeyValue(key.toString(), label));
    	    }
    	} catch (IllegalAccessException e) {
            LOG.debug(e.getMessage(), e);
            LOG.error(e.getMessage());
            throw new RuntimeException("IllegalAccessException occurred while trying to build keyValues List. dataObjectClass: " + businessObjectClass + "; keyAttributeName: " + keyAttributeName + "; labelAttributeName: " + labelAttributeName + "; includeKeyInDescription: " + includeKeyInDescription, e);
    	} catch (InvocationTargetException e) {
            LOG.debug(e.getMessage(), e);
            LOG.error(e.getMessage());
            throw new RuntimeException("InvocationTargetException occurred while trying to build keyValues List. dataObjectClass: " + businessObjectClass + "; keyAttributeName: " + keyAttributeName + "; labelAttributeName: " + labelAttributeName + "; includeKeyInDescription: " + includeKeyInDescription, e);
    	} catch (NoSuchMethodException e) {
            LOG.debug(e.getMessage(), e);
            LOG.error(e.getMessage());
            throw new RuntimeException("NoSuchMethodException occurred while trying to build keyValues List. dataObjectClass: " + businessObjectClass + "; keyAttributeName: " + keyAttributeName + "; labelAttributeName: " + labelAttributeName + "; includeKeyInDescription: " + includeKeyInDescription, e);
    	}

        return labels;
    }

    /**
     * @return the dataObjectClass
     */
    public Class<T> getBusinessObjectClass() {
        return this.businessObjectClass;
    }

    /**
     * @param businessObjectClass the dataObjectClass to set
     */
    public void setBusinessObjectClass(Class<T> businessObjectClass) {
        this.businessObjectClass = businessObjectClass;
    }

    /**
     * @return the includeKeyInDescription
     */
    public boolean isIncludeKeyInDescription() {
        return this.includeKeyInDescription;
    }

    /**
     * @param includeKeyInDescription the includeKeyInDescription to set
     */
    public void setIncludeKeyInDescription(boolean includeKeyInDescription) {
        this.includeKeyInDescription = includeKeyInDescription;
    }

    /**
     * @return the keyAttributeName
     */
    public String getKeyAttributeName() {
        return this.keyAttributeName;
    }

    /**
     * @param keyAttributeName the keyAttributeName to set
     */
    public void setKeyAttributeName(String keyAttributeName) {
        this.keyAttributeName = keyAttributeName;
    }

    /**
     * @return the labelAttributeName
     */
    public String getLabelAttributeName() {
        return this.labelAttributeName;
    }

    /**
     * @param labelAttributeName the labelAttributeName to set
     */
    public void setLabelAttributeName(String labelAttributeName) {
        this.labelAttributeName = labelAttributeName;
    }

	/**
	 * @return the includeBlankRow
	 */
	public boolean isIncludeBlankRow() {
		return this.includeBlankRow;
	}

	/**
	 * @param includeBlankRow the includeBlankRow to set
	 */
	public void setIncludeBlankRow(boolean includeBlankRow) {
		this.includeBlankRow = includeBlankRow;
	}

}
