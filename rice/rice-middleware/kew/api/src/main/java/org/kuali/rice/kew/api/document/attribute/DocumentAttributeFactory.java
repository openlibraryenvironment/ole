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
package org.kuali.rice.kew.api.document.attribute;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 * A factory that helps with creation of new {@link DocumentAttribute} instances as well as translation to concrete
 * instances from a {@link DocumentAttributeContract}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentAttributeFactory {

    /**
     * Constructs a document attribute containing character data from the given attribute name and {@link String} value.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code String}
     *
     * @return a constructed {@code DocumentAttributeString} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeString createStringAttribute(String name, String value) {
        DocumentAttributeString.Builder builder = DocumentAttributeString.Builder.create(name);
        builder.setValue(value);
        return builder.build();
    }

    /**
     * Constructs a document attribute containing date/time data from the given attribute name and {@link DateTime}
     * object.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code DateTime}
     *
     * @return a constructed {@code DocumentAttributeDateTime} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeDateTime createDateTimeAttribute(String name, DateTime value) {
        DocumentAttributeDateTime.Builder builder = DocumentAttributeDateTime.Builder.create(name);
        builder.setValue(value);
        return builder.build();
    }

    /**
     * Constructs a document attribute containing date/time data from the given attribute name and {@link Date} object.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code Date}
     *
     * @return a constructed {@code DocumentAttributeDateTime} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeDateTime createDateTimeAttribute(String name, Date value) {
        return createDateTimeAttribute(name, new DateTime(value));
    }

    /**
     * Constructs a document attribute containing date/time data from the given attribute name and a numeric long
     * representing the number of milliseconds from 1970-01-01T00:00:00Z in the default time zone.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param instant the instant value represented as milliseconds from 1970-01-01T00:00:00Z
     *
     * @return a constructed {@code DocumentAttributeDateTime} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeDateTime createDateTimeAttribute(String name, long instant) {
        return createDateTimeAttribute(name, new DateTime(instant));
    }

    /**
     * Constructs a document attribute containing date/time data from the given attribute name and {@link Calendar}
     * object.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code Calendar}
     *
     * @return a constructed {@code DocumentAttributeDateTime} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeDateTime createDateTimeAttribute(String name, Calendar value) {
        return createDateTimeAttribute(name, new DateTime(value));
    }

    /**
     * Constructs a document attribute containing real number data from the given attribute name and {@link BigDecimal}
     * object.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code BigDecimal}
     *
     * @return a constructed {@code DocumentAttributeDecimal} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeDecimal createDecimalAttribute(String name, BigDecimal value) {
        DocumentAttributeDecimal.Builder builder = DocumentAttributeDecimal.Builder.create(name);
        builder.setValue(value);
        return builder.build();
    }

    /**
     * Constructs a document attribute containing real number data from the given attribute name and {@link Number}
     * object.  The given number is first translated to a {@code BigDecimal} using {@link Number#doubleValue()}.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code Number}
     *
     * @return a constructed {@code DocumentAttributeDecimal} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeDecimal createDecimalAttribute(String name, Number value) {
        return createDecimalAttribute(name, BigDecimal.valueOf(value.doubleValue()));
    }

    /**
     * Constructs a document attribute containing integer number data from the given attribute name and
     * {@link BigInteger} object.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code BigInteger}
     *
     * @return a constructed {@code DocumentAttributeInteger} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeInteger createIntegerAttribute(String name, BigInteger value) {
        DocumentAttributeInteger.Builder builder = DocumentAttributeInteger.Builder.create(name);
        builder.setValue(value);
        return builder.build();
    }

    /**
     * Constructs a document attribute containing integer number data from the given attribute name and {@link Number}
     * object.  The given number is first translated to a {@code BigInteger} using {@link Number#longValue()}.
     *
     * @param name the name of the attribute to construct, must not be a null or blank value
     * @param value the value of the attribute as a {@code Number}
     *
     * @return a constructed {@code DocumentAttributeInteger} representing the document attribute
     *
     * @throws IllegalArgumentException if name is a null or blank value
     */
    public static DocumentAttributeInteger createIntegerAttribute(String name, Number value) {
        return createIntegerAttribute(name, BigInteger.valueOf(value.longValue()));
    }

    /**
     * Loads the given {@link DocumentAttributeContract} into the appropriate builder instance based on the type of the
     * given contract implementation.
     *
     * @param contract the contract to load into a builder
     * @return an implementation of {@link DocumentAttribute.AbstractBuilder} which handles instance of the given
     * contract
     *
     * @throws IllegalArgumentException if the given contract is null
     * @throws IllegalArgumentException if a builder implementation could not be determined into which to load the given
     * contract implementation
     */
    public static DocumentAttribute.AbstractBuilder<?> loadContractIntoBuilder(DocumentAttributeContract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("contract was null");
        }
        if (contract instanceof DocumentAttributeString) {
            DocumentAttributeString attribute = (DocumentAttributeString)contract;
            DocumentAttributeString.Builder builder = DocumentAttributeString.Builder.create(attribute.getName());
            builder.setValue(attribute.getValue());
            return builder;
        } else if (contract instanceof DocumentAttributeDateTime) {
            DocumentAttributeDateTime attribute = (DocumentAttributeDateTime)contract;
            DocumentAttributeDateTime.Builder builder = DocumentAttributeDateTime.Builder.create(attribute.getName());
            builder.setValue(attribute.getValue());
            return builder;
        } else if (contract instanceof DocumentAttributeInteger) {
            DocumentAttributeInteger attribute = (DocumentAttributeInteger)contract;
            DocumentAttributeInteger.Builder builder = DocumentAttributeInteger.Builder.create(attribute.getName());
            builder.setValue(attribute.getValue());
            return builder;
        } else if (contract instanceof DocumentAttributeDecimal) {
            DocumentAttributeDecimal attribute = (DocumentAttributeDecimal)contract;
            DocumentAttributeDecimal.Builder builder = DocumentAttributeDecimal.Builder.create(attribute.getName());
            builder.setValue(attribute.getValue());
            return builder;
        }
        throw new IllegalArgumentException("Given document attribute class could not be converted: " + contract.getClass());
    }


}
