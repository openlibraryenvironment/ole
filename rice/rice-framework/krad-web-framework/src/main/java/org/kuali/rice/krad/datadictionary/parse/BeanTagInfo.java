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
package org.kuali.rice.krad.datadictionary.parse;

/**
 * Data storage class for information about a custom tag for a single bean object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BeanTagInfo {
    private Class<?> beanClass;
    private String parent;
    private String tag;
    private boolean defaultTag;

    /**
     * Constructor initalizing global values
     */
    public BeanTagInfo() {
        beanClass = null;
        parent = null;
    }

    /**
     * Sets the class represented by the defined tag.
     *
     * @param beanClass - The class to be stored.
     */
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Retrieves the class represented by the tag.
     * The bean class stored is the data object defined for the Spring Bean
     *
     * @return The class being defined by the tag.
     */
    public Class<?> getBeanClass() {
        return beanClass;
    }

    /**
     * Sets the default parent for the associated tag.
     *
     * @param parent - The name of the default parent bean for the tag.
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * Retrieves the bean parent stored for the represented tag.
     * The default parent bean to be used when the custom tag is used.
     *
     * @return The default parent bean value.
     */
    public String getParent() {
        return this.parent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isDefaultTag() {
        return defaultTag;
    }

    public void setDefaultTag(boolean defaultTag) {
        this.defaultTag = defaultTag;
    }
}
