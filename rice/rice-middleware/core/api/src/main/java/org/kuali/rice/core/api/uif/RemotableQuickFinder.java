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
package org.kuali.rice.core.api.uif;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A quick finder widget that can be used by a TextInput, HiddenInput, Select, or MultiSelect control types.
 */
@XmlRootElement(name = RemotableQuickFinder.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RemotableQuickFinder.Constants.TYPE_NAME, propOrder = {
		RemotableQuickFinder.Elements.BASE_LOOKUP_URL,
        RemotableQuickFinder.Elements.DATA_OBJECT_CLASS,
        RemotableQuickFinder.Elements.LOOKUP_PARAMETERS,
        RemotableQuickFinder.Elements.FIELD_CONVERSIONS,
		CoreConstants.CommonElements.FUTURE_ELEMENTS })
public final class RemotableQuickFinder extends RemotableAbstractWidget {

    @XmlElement(name = Elements.BASE_LOOKUP_URL, required = false)
    private final String baseLookupUrl;

    @XmlElement(name = Elements.DATA_OBJECT_CLASS, required = true)
    private final String dataObjectClass;

    @XmlElement(name = Elements.LOOKUP_PARAMETERS, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> lookupParameters;

    @XmlElement(name = Elements.FIELD_CONVERSIONS, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> fieldConversions;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Should only be invoked by JAXB.
     */
    @SuppressWarnings("unused")
    private RemotableQuickFinder() {
        baseLookupUrl = null;
        dataObjectClass = null;
        lookupParameters = null;
        fieldConversions = null;
    }

    private RemotableQuickFinder(Builder b) {
        baseLookupUrl = b.baseLookupUrl;
        dataObjectClass = b.dataObjectClass;
        lookupParameters = b.lookupParameters;
        fieldConversions = b.fieldConversions;
    }

    public String getBaseLookupUrl() {
        return baseLookupUrl;
    }

    public String getDataObjectClass() {
        return dataObjectClass;
    }

    public Map<String, String> getLookupParameters() {
        return Collections.unmodifiableMap(lookupParameters);
    }

    public Map<String, String> getFieldConversions() {
        return Collections.unmodifiableMap(fieldConversions);
    }

    public static final class Builder extends RemotableAbstractWidget.Builder {
        private String baseLookupUrl;
        private String dataObjectClass;
        private Map<String, String> lookupParameters = Collections.emptyMap();
        private Map<String, String> fieldConversions = Collections.emptyMap();

        private Builder(String baseLookupUrl, String dataObjectClass) {
            setBaseLookupUrl(baseLookupUrl);
            setDataObjectClass(dataObjectClass);
        }

        public static Builder create(String baseLookupUrl, String dataObjectClass) {
            return new Builder(baseLookupUrl, dataObjectClass);
        }

        public String getBaseLookupUrl() {
            return baseLookupUrl;
        }

        public void setBaseLookupUrl(String baseLookupUrl) {
            /*if (StringUtils.isBlank(baseLookupUrl)) {
                throw new IllegalArgumentException("baseLookupUrl is blank");
            }*/

            this.baseLookupUrl = baseLookupUrl;
        }

        public String getDataObjectClass() {
            return dataObjectClass;
        }

        public void setDataObjectClass(String dataObjectClass) {
            if (StringUtils.isBlank(dataObjectClass)) {
                throw new IllegalArgumentException("dataObjectClass is blank");
            }

            this.dataObjectClass = dataObjectClass;
        }

        public Map<String, String> getLookupParameters() {
            return Collections.unmodifiableMap(lookupParameters);
        }

        public void setLookupParameters(Map<String, String> lookupParameters) {
            if (lookupParameters != null) {
                this.lookupParameters = new HashMap<String, String>(lookupParameters);
            }
        }

        public Map<String, String> getFieldConversions() {
            return Collections.unmodifiableMap(fieldConversions);
        }

        public void setFieldConversions(Map<String, String> fieldConversions) {
            if (fieldConversions != null) {
                this.fieldConversions = new HashMap<String, String>(fieldConversions);
            }
        }

        @Override
        public RemotableQuickFinder build() {
            return new RemotableQuickFinder(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static final class Constants {
        static final String TYPE_NAME = "QuickFinderType";
        final static String ROOT_ELEMENT_NAME = "quickFinder";
    }

    static final class Elements {
        static final String BASE_LOOKUP_URL = "baseLookupUrl";
        static final String DATA_OBJECT_CLASS = "dataObjectClass";
        static final String LOOKUP_PARAMETERS = "lookupParameters";
        static final String FIELD_CONVERSIONS = "fieldConversions";
    }
}
