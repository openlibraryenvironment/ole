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

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = RemotableAttributeLookupSettings.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RemotableAttributeLookupSettings.Constants.TYPE_NAME, propOrder = {
        RemotableAttributeLookupSettings.Elements.IN_CRITERIA,
        RemotableAttributeLookupSettings.Elements.IN_RESULTS,
        RemotableAttributeLookupSettings.Elements.RANGED,
        RemotableAttributeLookupSettings.Elements.LOWER_BOUND_INCLUSIVE,
        RemotableAttributeLookupSettings.Elements.UPPER_BOUND_INCLUSIVE,
        RemotableAttributeLookupSettings.Elements.CASE_SENSITIVE,
        RemotableAttributeLookupSettings.Elements.LOWER_LABEL,
        RemotableAttributeLookupSettings.Elements.UPPER_LABEL,
        RemotableAttributeLookupSettings.Elements.LOWER_DATEPICKER,
        RemotableAttributeLookupSettings.Elements.UPPER_DATEPICKER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RemotableAttributeLookupSettings extends AbstractDataTransferObject implements AttributeLookupSettings {

    @XmlElement(name = Elements.IN_CRITERIA, required = true)
    private final boolean inCriteria;

    @XmlElement(name = Elements.IN_RESULTS, required = true)
    private final boolean inResults;

    @XmlElement(name = Elements.RANGED, required = true)
    private final boolean ranged;

    @XmlElement(name = Elements.LOWER_BOUND_INCLUSIVE, required = false)
    private final boolean lowerBoundInclusive;

    @XmlElement(name = Elements.UPPER_BOUND_INCLUSIVE, required = false)
    private final boolean upperBoundInclusive;

    @XmlElement(name = Elements.CASE_SENSITIVE, required = false)
    private final Boolean caseSensitive;

    @XmlElement(name = Elements.UPPER_LABEL, required = false)
    private final String upperLabel;

    @XmlElement(name = Elements.LOWER_LABEL, required = false)
    private final String lowerLabel;

    @XmlElement(name = Elements.UPPER_DATEPICKER, required = false)
    private final Boolean upperDatePicker;

    @XmlElement(name = Elements.LOWER_DATEPICKER, required = false)
    private final Boolean lowerDatePicker;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private RemotableAttributeLookupSettings() {
        this.inCriteria = false;
        this.inResults = false;
        this.ranged = false;
        this.lowerBoundInclusive = false;
        this.upperBoundInclusive = false;
        this.caseSensitive = null;
        this.lowerLabel = null;
        this.upperLabel = null;
        this.lowerDatePicker = null;
        this.upperDatePicker = null;
    }

    private RemotableAttributeLookupSettings(Builder builder) {
        this.inCriteria = builder.isInCriteria();
        this.inResults = builder.isInResults();
        this.ranged = builder.isRanged();
        this.lowerBoundInclusive = builder.isLowerBoundInclusive();
        this.upperBoundInclusive = builder.isUpperBoundInclusive();
        this.caseSensitive = builder.isCaseSensitive();
        this.lowerLabel = builder.getLowerLabel();
        this.upperLabel = builder.getUpperLabel();
        this.lowerDatePicker = builder.isLowerDatePicker();
        this.upperDatePicker= builder.isUpperDatePicker();
    }

    @Override
    public boolean isInCriteria() {
        return inCriteria;
    }

    @Override
    public boolean isInResults() {
        return inResults;
    }

    @Override
    public boolean isRanged() {
        return ranged;
    }

    @Override
    public boolean isLowerBoundInclusive() {
        return this.lowerBoundInclusive;
    }

    @Override
    public boolean isUpperBoundInclusive() {
        return this.upperBoundInclusive;
    }

    @Override
    public Boolean isCaseSensitive() {
        return caseSensitive;
    }

    @Override
    public String getLowerLabel() {
        return lowerLabel;
    }

    @Override
    public String getUpperLabel() {
        return upperLabel;
    }

    @Override
    public Boolean isLowerDatePicker() {
        return lowerDatePicker;
    }

    @Override
    public Boolean isUpperDatePicker() {
        return upperDatePicker;
    }

    /**
     * A builder which can be used to construct {@link RemotableAttributeLookupSettings} instances.  Enforces the constraints of the {@link AttributeLookupSettings}.
     */
    public final static class Builder implements Serializable, ModelBuilder, AttributeLookupSettings {

        private boolean inCriteria;
        private boolean inResults;
        private boolean ranged;
        private boolean lowerBoundInclusive;
        private boolean upperBoundInclusive;
        private Boolean caseSensitive;
        private String lowerLabel;
        private String upperLabel;
        private Boolean lowerDatePicker;
        private Boolean upperDatePicker;

        private Builder() {
            setInCriteria(true);
            setInResults(true);
            setRanged(false);
            setLowerLabel(null);
            setUpperLabel(null);
            setLowerDatePicker(null);
            setUpperDatePicker(null);
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(AttributeLookupSettings contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setInCriteria(contract.isInCriteria());
            builder.setInResults(contract.isInResults());
            builder.setRanged(contract.isRanged());
            builder.setLowerBoundInclusive(contract.isLowerBoundInclusive());
            builder.setUpperBoundInclusive(contract.isUpperBoundInclusive());
            builder.setCaseSensitive(contract.isCaseSensitive());
            return builder;
        }

        public RemotableAttributeLookupSettings build() {
            return new RemotableAttributeLookupSettings(this);
        }

        @Override
        public boolean isInCriteria() {
            return inCriteria;
        }

        @Override
        public boolean isInResults() {
            return inResults;
        }

        @Override
        public boolean isRanged() {
            return ranged;
        }

        @Override
        public boolean isLowerBoundInclusive() {
            return this.lowerBoundInclusive;
        }

        @Override
        public boolean isUpperBoundInclusive() {
            return this.upperBoundInclusive;
        }

        @Override
        public Boolean isCaseSensitive() {
            return caseSensitive;
        }

        @Override
        public String getLowerLabel() {
            return lowerLabel;
        }

        @Override
        public String getUpperLabel() {
            return upperLabel;
        }

        @Override
        public Boolean isLowerDatePicker() {
            return lowerDatePicker;
        }

        @Override
        public Boolean  isUpperDatePicker() {
            return upperDatePicker;
        }

        public void setInCriteria(boolean inCriteria) {
            this.inCriteria = inCriteria;
        }

        public void setInResults(boolean inResults) {
            this.inResults = inResults;
        }

        public void setRanged(boolean ranged) {
            this.ranged = ranged;
        }

        public void setLowerBoundInclusive(boolean lowerBoundInclusive) {
            this.lowerBoundInclusive = lowerBoundInclusive;
        }

        public void setUpperBoundInclusive(boolean upperBoundInclusive) {
            this.upperBoundInclusive = upperBoundInclusive;
        }

        public void setCaseSensitive(Boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }
        
        public void setLowerLabel(String s) {
            this.lowerLabel = s;
        }

        public void setUpperLabel(String s) {
            this.upperLabel = s;
        }
        
        public void setLowerDatePicker(Boolean b) {
            this.lowerDatePicker = b;
        }

        public void setUpperDatePicker(Boolean b) {
            this.upperDatePicker = b;
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "remotableAttributeLookupSettings";
        final static String TYPE_NAME = "RemotableAttributeLookupSettingsType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String IN_CRITERIA = "inCriteria";
        final static String IN_RESULTS = "inResults";
        final static String RANGED = "ranged";
        final static String LOWER_BOUND_INCLUSIVE = "lowerBoundInclusive";
        final static String UPPER_BOUND_INCLUSIVE = "upperBoundInclusive";
        final static String CASE_SENSITIVE = "caseSensitive";
        final static String LOWER_LABEL = "lowerLabel";
        final static String UPPER_LABEL = "upperLabel";
        final static String LOWER_DATEPICKER = "lowerDatePicker";
        final static String UPPER_DATEPICKER = "upperDatePicker";
    }
}