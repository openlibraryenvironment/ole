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
package edu.sampleu.krms.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.uif.RemotableAbstractWidget;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableAttributeLookupSettings;
import org.kuali.rice.core.api.uif.RemotableQuickFinder;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.krad.uif.util.LookupInquiryUtils;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;
import org.kuali.rice.krms.impl.type.AgendaTypeServiceBase;
import org.kuali.rice.location.api.campus.Campus;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;
import org.kuali.rice.location.impl.campus.CampusBo;
import org.springframework.beans.factory.annotation.Required;

import javax.jws.WebParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Sample AgendaTypeService that creates a RemotableAttributeField for specifying the campus
 */
public class CampusAgendaTypeService extends AgendaTypeServiceBase {

    private static final String CAMPUS_FIELD_NAME = "Campus";

    private ConfigurationService configurationService;

    @Override
    public RemotableAttributeField translateTypeAttribute(KrmsTypeAttribute inputAttribute,
            KrmsAttributeDefinition attributeDefinition) {

        if (CAMPUS_FIELD_NAME.equals(attributeDefinition.getName())) {
            return createCampusField();
        } else {
            return super.translateTypeAttribute(inputAttribute,
                attributeDefinition);
        }
    }

    @Required
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    private RemotableAttributeField createCampusField() {

        String campusBoClassName = CampusBo.class.getName();

        String baseLookupUrl = LookupInquiryUtils.getBaseLookupUrl();

        RemotableQuickFinder.Builder quickFinderBuilder =
                RemotableQuickFinder.Builder.create(baseLookupUrl, campusBoClassName);

        quickFinderBuilder.setLookupParameters(Collections.singletonMap("Campus", "code"));
        quickFinderBuilder.setFieldConversions(Collections.singletonMap("code","Campus"));

        RemotableTextInput.Builder controlBuilder = RemotableTextInput.Builder.create();
        controlBuilder.setSize(30);
        controlBuilder = RemotableTextInput.Builder.create();
        controlBuilder.setSize(Integer.valueOf(40));
        controlBuilder.setWatermark("campus code");

        RemotableAttributeLookupSettings.Builder lookupSettingsBuilder = RemotableAttributeLookupSettings.Builder.create();
        lookupSettingsBuilder.setCaseSensitive(Boolean.TRUE);
        lookupSettingsBuilder.setInCriteria(true);
        lookupSettingsBuilder.setInResults(true);
        lookupSettingsBuilder.setRanged(false);

        RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create(CAMPUS_FIELD_NAME);
        builder.setAttributeLookupSettings(lookupSettingsBuilder);
        builder.setRequired(true);
        builder.setDataType(DataType.STRING);
        builder.setControl(controlBuilder);
        builder.setLongLabel("Campus");
        builder.setShortLabel("Campus");
        builder.setMinLength(Integer.valueOf(1));
        builder.setMaxLength(Integer.valueOf(40));
        builder.setWidgets(Collections.<RemotableAbstractWidget.Builder>singletonList(quickFinderBuilder));

        return builder.build();
    }

    @Override
    public List<RemotableAttributeError> validateAttributes(@WebParam(name = "krmsTypeId") String krmsTypeId,
            @WebParam(name = "attributes") @XmlJavaTypeAdapter(
                    value = MapStringStringAdapter.class) Map<String, String> attributes) throws RiceIllegalArgumentException {

        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>(super.validateAttributes(krmsTypeId, attributes));

        RemotableAttributeError.Builder campusErrorBuilder = RemotableAttributeError.Builder.create(CAMPUS_FIELD_NAME);

        String campusValue = attributes.get(CAMPUS_FIELD_NAME);

        if (StringUtils.isEmpty(campusValue)) {
            campusErrorBuilder.addErrors(configurationService.getPropertyValueAsString("error.agenda.invalidAttributeValue"));
        } else {
            Campus campus = LocationApiServiceLocator.getCampusService().getCampus(campusValue);

            if (campus == null) {
                campusErrorBuilder.addErrors(configurationService.getPropertyValueAsString("error.agenda.invalidAttributeValue"));
            }
        }

        if (campusErrorBuilder.getErrors().size() > 0) {
            errors.add(campusErrorBuilder.build());
        }

        return errors;
    }
}
