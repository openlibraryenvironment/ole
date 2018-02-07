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
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;

/**
 * A text input control type.
 */
@XmlRootElement(name = RemotableTextInput.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RemotableTextInput.Constants.TYPE_NAME, propOrder = {
        RemotableTextInput.Elements.SIZE,
        RemotableTextInput.Elements.WATERMARK,
		CoreConstants.CommonElements.FUTURE_ELEMENTS })
public final class RemotableTextInput extends RemotableAbstractControl implements Sized, Watermarked {

    @XmlElement(name = Elements.SIZE, required = false)
    private final Integer size;

    @XmlElement(name = Elements.WATERMARK, required = false)
    private final String watermark;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Should only be invoked by JAXB.
     */
    @SuppressWarnings("unused")
    private RemotableTextInput() {
        size = null;
        watermark = null;
    }

    private RemotableTextInput(Builder b) {
        size = b.size;
        watermark = b.watermark;
    }
    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public String getWatermark() {
        return watermark;
    }

    public static final class Builder extends RemotableAbstractControl.Builder implements Sized, Watermarked {
        private Integer size;
        private String watermark;

        private Builder() {
            super();
        }

        public static Builder create() {
            return new Builder();
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
        public String getWatermark() {
            return watermark;
        }

        public void setWatermark(String watermark) {
            this.watermark = watermark;
        }

        @Override
        public RemotableTextInput build() {
            return new RemotableTextInput(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static final class Constants {
        static final String TYPE_NAME = "TextInputType";
        final static String ROOT_ELEMENT_NAME = "textInput";
    }

    static final class Elements {
        static final String SIZE = "size";
        static final String WATERMARK = "watermark";
    }
}
