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
package org.kuali.rice.krms.impl.type;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kns.datadictionary.validation.AttributeValidatingTypeServiceBase;
import org.kuali.rice.krad.service.DataDictionaryRemoteFieldService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.framework.type.RemotableAttributeOwner;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;

import javax.jws.WebParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link KrmsTypeServiceBase} is an abstract class providing default implementation and hooks for
 * provisioning and validating the custom attributes of a krms type.  Is should probably be mentioned that the
 * default validation methods don't actually check anything, they just return empty error lists.
 */
public abstract class KrmsTypeServiceBase extends AttributeValidatingTypeServiceBase implements RemotableAttributeOwner {

    /**
     * <p>get the {@link RemotableAttributeField}s for the custom attributes of this krms type.  This implementation
     * will (by default) return any attributes mapped to the type via
     * {@link org.kuali.rice.krms.impl.repository.KrmsTypeAttributeBo}. If there is is a component name defined on the
     * related {@link org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionBo} then that will be used to generate
     * the {@link RemotableAttributeField}.  If not, then a simple text input will be produced.</p>
     *
     * <p>An extending class can also override the
     * {@link #translateTypeAttribute(org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute,
     * org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition)}
     * method which is called from here, and within it hand create the RemotableAttributeField for a certain attribute.
     * </p>
     *
     * <p>Also handy for extenders to know, this method delegates to {@link #getTypeAttributeDefinitions(String)} and
     * then pulls out the {@link RemotableAttributeField}s from the returned {@link TypeAttributeDefinition}s</p>
     *
     * @param krmsTypeId the people flow type identifier.  Must not be null or blank.
     * @return
     * @throws RiceIllegalArgumentException
     */
    @Override
    public List<RemotableAttributeField> getAttributeFields(@WebParam(name = "krmsTypeId") String krmsTypeId) throws RiceIllegalArgumentException {
        List<TypeAttributeDefinition> typeAttributeDefinitions = getTypeAttributeDefinitions(krmsTypeId);

        if (CollectionUtils.isEmpty(typeAttributeDefinitions)) {
            return Collections.emptyList();
        } else {
            List<RemotableAttributeField> fields =
                    new ArrayList<RemotableAttributeField>(typeAttributeDefinitions.size());

            for (TypeAttributeDefinition typeAttributeDefinition : typeAttributeDefinitions) {
                fields.add(typeAttributeDefinition.getField());
            }
            return fields;
        }
    }

    /**
     * Gets an ordered List of {@link TypeAttributeDefinition}s for the attributes on the KRMS type specified by the
     * given krmsTypeId.
     * @param krmsTypeId the ID of the KRMS Type whose attributes we are getting.
     * @return a List of type-agnostic {@link TypeAttributeDefinition}s
     * @see AttributeValidatingTypeServiceBase
     */
    @Override
    protected List<TypeAttributeDefinition> getTypeAttributeDefinitions(String krmsTypeId) {

        if (StringUtils.isBlank(krmsTypeId)) {
            throw new RiceIllegalArgumentException("krmsTypeId must be non-null and non-blank");
        }

        List<TypeAttributeDefinition> results = new ArrayList<TypeAttributeDefinition>();

        // keep track of how to sort these
        final Map<String, Integer> sortCodeMap = new HashMap<String, Integer>();

        KrmsTypeDefinition krmsType =
                KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService().getTypeById(krmsTypeId);

        if (krmsType == null) {
            throw new RiceIllegalArgumentException("krmsTypeId must be a valid id of a KRMS type");
        } else {
            // translate attributes

            List<KrmsTypeAttribute> typeAttributes = krmsType.getAttributes();

            List<RemotableAttributeField> typeAttributeFields = new ArrayList<RemotableAttributeField>(10);
            if (!CollectionUtils.isEmpty(typeAttributes)) {
                // translate the attribute and store the sort code in our map
                for (KrmsTypeAttribute typeAttribute : typeAttributes) {

                    KrmsTypeRepositoryService typeRepositoryService = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();

                    KrmsAttributeDefinition attributeDefinition =
                            typeRepositoryService.getAttributeDefinitionById(typeAttribute.getAttributeDefinitionId());

                    RemotableAttributeField attributeField = translateTypeAttribute(typeAttribute, attributeDefinition);

                    if (typeAttribute.getSequenceNumber() == null) {
                        throw new IllegalStateException(typeAttribute.toString() + " has a null sequenceNumber");
                    } else {
                        sortCodeMap.put(attributeField.getName(), typeAttribute.getSequenceNumber());
                    }

                    TypeAttributeDefinition typeAttributeDefinition =
                            new TypeAttributeDefinition(attributeField, attributeDefinition.getName(), attributeDefinition.getComponentName(), attributeDefinition.getLabel(), null);

                    results.add(typeAttributeDefinition);
                }
            }
        }

        sortFields(results, sortCodeMap);

        return results;
    }


    protected void sortFields(List<TypeAttributeDefinition> results,
            final Map<String, Integer> sortCodeMap) {// sort the results
        Collections.sort(results, new Comparator<TypeAttributeDefinition>() {
            @Override
            public int compare(TypeAttributeDefinition o1, TypeAttributeDefinition o2) {
                if (o1 == o2 || o1.equals(o2))
                    return 0;
                // we assume that each has a sort code based on our previous check
                Integer o1SortCode = sortCodeMap.get(o1.getName());
                Integer o2SortCode = sortCodeMap.get(o2.getName());

                if (o1SortCode.compareTo(o2SortCode) != 0) {
                    return o1SortCode.compareTo(o2SortCode);
                } else {
                    // if sort codes are the same, we still would like a consistent order
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });
    }

    @Override
    public List<RemotableAttributeError> validateAttributes(@WebParam(name = "krmsTypeId") String krmsTypeId,
            @WebParam(name = "attributes") @XmlJavaTypeAdapter(
                    value = MapStringStringAdapter.class) Map<String, String> attributes) throws RiceIllegalArgumentException {
        return super.validateAttributes(krmsTypeId, attributes);
    }

    @Override
    public List<RemotableAttributeError> validateAttributesAgainstExisting(
            @WebParam(name = "krmsTypeId") String krmsTypeId, @WebParam(name = "newAttributes") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> newAttributes,
            @WebParam(name = "oldAttributes") @XmlJavaTypeAdapter(
                    value = MapStringStringAdapter.class) Map<String, String> oldAttributes) throws RiceIllegalArgumentException {
        return validateAttributes(krmsTypeId, newAttributes);
    }

    /**
     * Translate a {@link org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute} into a {@link org.kuali.rice.core.api.uif.RemotableAttributeField}.
     * Override this method to provide custom translation of certain attributes.
     * @param inputAttribute the {@link org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute} to translate
     * @param attributeDefinition the {@link KrmsAttributeDefinition} for the given inputAttribute
     * @return a {@link org.kuali.rice.core.api.uif.RemotableAttributeField} for the given inputAttribute
     */
    public RemotableAttributeField translateTypeAttribute(KrmsTypeAttribute inputAttribute,
            KrmsAttributeDefinition attributeDefinition) {

        if (StringUtils.isEmpty(attributeDefinition.getComponentName())) {
            RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create(attributeDefinition.getName());

            RemotableTextInput.Builder controlBuilder = RemotableTextInput.Builder.create();
            controlBuilder.setSize(80);

            controlBuilder.setWatermark(attributeDefinition.getDescription());

            builder.setShortLabel(attributeDefinition.getLabel());
            builder.setLongLabel(attributeDefinition.getLabel());
            builder.setName(attributeDefinition.getName());

//            builder.setHelpSummary("helpSummary: " + attributeDefinition.getDescription());
//            builder.setHelpDescription("helpDescription: " + attributeDefinition.getDescription());

            builder.setControl(controlBuilder);
            builder.setMaxLength(400);

            return builder.build();
        } else {
            return getDataDictionaryRemoteFieldService().buildRemotableFieldFromAttributeDefinition(
                    attributeDefinition.getComponentName(),
                    attributeDefinition.getName());
        }
    }

    public DataDictionaryRemoteFieldService getDataDictionaryRemoteFieldService() {
        return KRADServiceLocatorWeb.getDataDictionaryRemoteFieldService();
    }

    @Override
    protected List<RemotableAttributeError> validateNonDataDictionaryAttribute(RemotableAttributeField attr, String key,
            String value) {
        return Collections.emptyList();
    }
}
