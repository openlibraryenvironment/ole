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
 * A textarea control type.
 */
@XmlRootElement(name = RemotableTextarea.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RemotableTextarea.Constants.TYPE_NAME, propOrder = {
        RemotableTextarea.Elements.ROWS,
        RemotableTextarea.Elements.COLS,
        RemotableTextarea.Elements.WATERMARK,
		CoreConstants.CommonElements.FUTURE_ELEMENTS })
public final class RemotableTextarea extends RemotableAbstractControl implements Watermarked, RowsCols {

    @XmlElement(name = Elements.ROWS, required = false)
    private final Integer rows;

    @XmlElement(name = Elements.COLS, required = false)
    private final Integer cols;

    @XmlElement(name = Elements.WATERMARK, required = false)
    private final String watermark;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Should only be invoked by JAXB.
     */
    @SuppressWarnings("unused")
    private RemotableTextarea() {
        rows = null;
        cols = null;
        watermark = null;
    }

    private RemotableTextarea(Builder b) {
        rows = b.rows;
        cols = b.cols;
        watermark = b.watermark;
    }

    @Override
    public Integer getRows() {
        return rows;
    }

    @Override
    public Integer getCols() {
        return cols;
    }

    @Override
    public String getWatermark() {
        return watermark;
    }

    public static final class Builder extends RemotableAbstractControl.Builder implements Watermarked, RowsCols {
        private Integer rows;
        private Integer cols;
        private String watermark;

        private Builder() {
            super();
        }

        public static Builder create() {
            return new Builder();
        }

        @Override
        public Integer getRows() {
            return rows;
        }

        public void setRows(Integer rows) {
            if (rows != null && rows < 1) {
                throw new IllegalArgumentException("rows was < 1");
            }

            this.rows = rows;
        }

        @Override
        public Integer getCols() {
            return cols;
        }

        public void setCols(Integer cols) {
            if (cols != null && cols < 1) {
                throw new IllegalArgumentException("cols was < 1");
            }

            this.cols = cols;
        }

        @Override
        public String getWatermark() {
            return watermark;
        }

        public void setWatermark(String watermark) {
            this.watermark = watermark;
        }

        @Override
        public RemotableTextarea build() {
            return new RemotableTextarea(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static final class Constants {
        static final String TYPE_NAME = "TextareaType";
        final static String ROOT_ELEMENT_NAME = "textarea";
    }

    static final class Elements {
        static final String COLS = "cols";
        static final String ROWS = "rows";
        static final String WATERMARK = "watermark";
    }
}
