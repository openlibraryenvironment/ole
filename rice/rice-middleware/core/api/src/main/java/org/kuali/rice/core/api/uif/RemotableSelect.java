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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A select control type.
 */
@XmlRootElement(name = RemotableSelect.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RemotableSelect.Constants.TYPE_NAME, propOrder = {
        RemotableSelect.Elements.KEY_LABELS,
        RemotableSelect.Elements.GROUPS,
        RemotableSelect.Elements.SIZE,
        RemotableSelect.Elements.MULTIPLE,
        RemotableSelect.Elements.REFRESH_ON_CHANGE,
		CoreConstants.CommonElements.FUTURE_ELEMENTS })
public final class RemotableSelect extends RemotableAbstractControl implements RemotableSelectContract {

    @XmlElement(name = Elements.KEY_LABELS, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> keyLabels;

    @XmlElementWrapper(name = Elements.GROUPS, required = true)
    @XmlElement(name = Elements.GROUP, required = false)
    private final List<RemotableSelectGroup> groups;

    @XmlElement(name = Elements.SIZE, required = false)
    private final Integer size;

    @XmlElement(name = Elements.MULTIPLE, required = false)
    private final boolean multiple;

    @XmlElement(name = Elements.REFRESH_ON_CHANGE, required = false)
    private final boolean refreshOnChange;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Should only be invoked by JAXB.
     */
    @SuppressWarnings("unused")
    private RemotableSelect() {
        size = null;
        keyLabels = null;
        groups = null;
        multiple = false;
        refreshOnChange = false;
    }

    private RemotableSelect(Builder b) {
        size = b.size;
        keyLabels = b.keyLabels;

        final List<RemotableSelectGroup> temp = new ArrayList<RemotableSelectGroup>();
        for (RemotableSelectGroup.Builder attr : b.groups) {
            temp.add(attr.build());
        }
        this.groups = Collections.unmodifiableList(temp);

        multiple = b.multiple;
        refreshOnChange = b.refreshOnChange;
    }

    @Override
    public Map<String, String> getKeyLabels() {
        return keyLabels;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public List<RemotableSelectGroup> getGroups() {
        return groups;
    }

    @Override
    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public boolean isRefreshOnChange() {
        return refreshOnChange;
    }

    public static final class Builder extends RemotableAbstractControl.Builder implements RemotableSelectContract {
        private Integer size;
        private Map<String, String> keyLabels = Collections.emptyMap();
        private List<RemotableSelectGroup.Builder> groups = Collections.emptyList();
        private boolean multiple;
        private boolean refreshOnChange;

        private Builder(Map<String, String> keyLabels) {
            setKeyLabels(keyLabels);
        }

        private Builder(List<RemotableSelectGroup.Builder> groups) {
            setGroups(groups);
        }

        public static Builder create(Map<String, String> keyLabels) {
            return new Builder(keyLabels);
        }

        public static Builder create(List<RemotableSelectGroup.Builder> groups) {
            return new Builder(groups);
        }

        @Override
        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            if (size != null && size < 1) {
                throw new IllegalArgumentException("size was < 1");
            }

            this.size = size;
        }

        @Override
        public Map<String, String> getKeyLabels() {
            return keyLabels;
        }

        public void setKeyLabels(Map<String, String> keyLabels) {
            if (keyLabels == null) {
                throw new IllegalArgumentException("keyLabels was null");
            }

            this.keyLabels = Collections.unmodifiableMap(new LinkedHashMap<String, String>(keyLabels));
        }

        @Override
        public List<RemotableSelectGroup.Builder> getGroups() {
            return groups;
        }

        public void setGroups(List<RemotableSelectGroup.Builder> groups) {
            if (groups == null) {
                throw new IllegalArgumentException("groups was null");
            }

            this.groups = Collections.unmodifiableList(new ArrayList<RemotableSelectGroup.Builder>(groups));
        }

        @Override
        public boolean isMultiple() {
            return multiple;
        }

        public void setMultiple(boolean multiple) {
            this.multiple = multiple;
        }

        @Override
        public boolean isRefreshOnChange() {
            return refreshOnChange;
        }

        public void setRefreshOnChange(boolean refreshOnChange) {
            this.refreshOnChange = refreshOnChange;
        }

        @Override
        public RemotableSelect build() {
            if (keyLabels.isEmpty() && groups.isEmpty()) {
                throw new IllegalStateException("the keyLabels or groups must be set to a non-empty collection");
            }

            return new RemotableSelect(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static final class Constants {
        static final String TYPE_NAME = "SelectType";
        final static String ROOT_ELEMENT_NAME = "select";
    }

    static final class Elements {
        static final String SIZE = "size";
        static final String KEY_LABELS = "keyLabels";
        static final String GROUPS = "groups";
        static final String GROUP = "group";
        static final String MULTIPLE = "multiple";
        static final String REFRESH_ON_CHANGE = "refreshOnChange";
    }
}
