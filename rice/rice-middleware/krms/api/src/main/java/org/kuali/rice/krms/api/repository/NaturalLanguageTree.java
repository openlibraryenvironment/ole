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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.rice.krms.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.KrmsConstants;

/**
 * Concrete model object implementation of a natural language tree immutable.
 * Instances of natural language tree can be (un)marshalled to and from XML.
 *
 * @see NaturalLanguageTreeContract
 */
@XmlRootElement(name = NaturalLanguageTree.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NaturalLanguageTree.Constants.TYPE_NAME, propOrder = {
    NaturalLanguageTree.Elements.NATURAL_LANGUAGE,
    NaturalLanguageTree.Elements.CHILDREN,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class NaturalLanguageTree implements NaturalLanguageTreeContract {

    private static final long serialVersionUID = 2783959459503209577L;
    @XmlElement(name = NaturalLanguageTree.Elements.NATURAL_LANGUAGE, required = false)
    private String naturalLanguage;
    @XmlElement(name = NaturalLanguageTree.Elements.CHILDREN, required = false)
    private List<NaturalLanguageTree> children;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<org.w3c.dom.Element> _futureElements = null;

    /**
     * This constructor should never be called. It is only present for use
     * during JAXB unmarshalling.
     */
    public NaturalLanguageTree() {
        this.naturalLanguage = null;
        this.children = null;
    }

    @Override
    public String getNaturalLanguage() {
        return naturalLanguage;
    }

    @Override
    public List<NaturalLanguageTree> getChildren() {
        return children;
    }

    /**
     * Constructs a KRMS Repository Agenda object from the given builder. This
     * constructor is private and should only ever be invoked from the builder.
     *
     * @param builder the Builder from which to construct the Agenda
     */
    private NaturalLanguageTree(Builder builder) {
        this.naturalLanguage = builder.getNaturalLanguage();
        List<NaturalLanguageTree> list = null;
        if (builder.getChildren() != null) {
            list = new ArrayList<NaturalLanguageTree>(builder.getChildren().size());
            for (NaturalLanguageTreeContract nltree : builder.getChildren()) {
                list.add(Builder.create(nltree).build());
            }
            this.children = Collections.unmodifiableList(list);
        }
    }

    /**
     * This builder is used to construct instances of KRMS Repository Agenda. It
     * enforces the constraints of the {@link NaturalLanguageTreeContract}.
     */
    public static class Builder implements NaturalLanguageTreeContract, ModelBuilder, Serializable {

        private static final long serialVersionUID = -8862851720709537839L;
        private String naturalLanguage;
        private List<? extends NaturalLanguageTreeContract> children;
 
        /**
         * Private constructor for creating a builder with all of it's required
         * attributes.
         */
        private Builder() {
        }        
        
        /**
         * Private constructor for creating a builder with all of it's required
         * attributes.
         */
        private Builder(String naturalLanguage, List<? extends NaturalLanguageTreeContract> children) {
            setNaturalLanguage(naturalLanguage);
            setChildren(children);
        }

        @Override
        public String getNaturalLanguage() {
            return naturalLanguage;
        }

        public void setNaturalLanguage(String naturalLanguage) {
            this.naturalLanguage = naturalLanguage;
        }

        @Override
        public List<? extends NaturalLanguageTreeContract> getChildren() {
            return children;
        }

        public void setChildren(List<? extends NaturalLanguageTreeContract> children) {
            this.children = children;
        }

        
        /**
         * Creates a builder by populating it with data from the given
         * {@link NaturalLanguageTreeContract}.
         *
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the
         * contract
         * @throws IllegalArgumentException if the contract is null
         */
        public static NaturalLanguageTree.Builder create() {
            NaturalLanguageTree.Builder builder = new NaturalLanguageTree.Builder();
            return builder;
        }
        /**
         * Creates a builder by populating it with data from the given
         * {@link NaturalLanguageTreeContract}.
         *
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the
         * contract
         * @throws IllegalArgumentException if the contract is null
         */
        public static NaturalLanguageTree.Builder create(NaturalLanguageTreeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract is null");
            }
            NaturalLanguageTree.Builder builder = new NaturalLanguageTree.Builder(contract.getNaturalLanguage(), contract.getChildren());

            return builder;
        }

        /**
         * Builds an instance of a Natural Language Tree based on the current state of the
         * builder.
         *
         * @return the fully-constructed Agenda
         */
        @Override
        public NaturalLanguageTree build() {
            return new NaturalLanguageTree(this);
        }
    }

    /**
     * Defines some constants used on this class.
     */
    public static class Constants {

        final static String ROOT_ELEMENT_NAME = "naturalLanguageTree";
        final static String TYPE_NAME = "NaturalLanguageTreeType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = {"_futureElements"};
        public final static String EVENT = "Event";   // key for event attribute
    }

    /**
     * A private class which exposes constants which define the XML element
     * names to use when this object is marshalled to XML.
     */
    public static class Elements {

        final static String NATURAL_LANGUAGE = "naturalLanguage";
        final static String CHILDREN = "children";
    }

    public static class Cache {

        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + NaturalLanguageTree.Constants.TYPE_NAME;
    }
}
