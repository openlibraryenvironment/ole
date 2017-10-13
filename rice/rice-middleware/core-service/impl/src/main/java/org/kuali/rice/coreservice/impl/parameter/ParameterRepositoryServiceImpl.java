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
package org.kuali.rice.coreservice.impl.parameter;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.api.parameter.ParameterQueryResults;
import org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.util.Truth;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ParameterRepositoryServiceImpl implements ParameterRepositoryService {
    private static final String SUB_PARAM_SEPARATOR = "=";

    private BusinessObjectService businessObjectService;
    private CriteriaLookupService criteriaLookupService;

    @Override 
    public Parameter createParameter(Parameter parameter) {
        if (parameter == null) {
            throw new RiceIllegalArgumentException("parameter is null");
        }

        final ParameterKey key = ParameterKey.create(parameter.getApplicationId(), parameter.getNamespaceCode(), parameter.getComponentCode(), parameter.getName());
        final Parameter existing = getParameter(key);
        if (existing != null && existing.getApplicationId().equals(parameter.getApplicationId())) {
            throw new RiceIllegalStateException("the parameter to create already exists: " + parameter);
        }

        return ParameterBo.to(businessObjectService.save(ParameterBo.from(parameter)));
    } 

    @Override
    public Parameter updateParameter(Parameter parameter) {
        if (parameter == null) {
            throw new RiceIllegalArgumentException("parameter is null");
        }

        final ParameterKey key = ParameterKey.create(parameter.getApplicationId(), parameter.getNamespaceCode(), parameter.getComponentCode(), parameter.getName());
        final Parameter existing = getParameter(key);
        if (existing == null) {
            throw new RiceIllegalStateException("the parameter does not exist: " + parameter);
        }

        final Parameter toUpdate;
        if (!existing.getApplicationId().equals(parameter.getApplicationId())) {
            final Parameter.Builder builder = Parameter.Builder.create(parameter);
            builder.setApplicationId(existing.getApplicationId());
            toUpdate = builder.build();
        } else {
            toUpdate = parameter;
        }

        return ParameterBo.to(businessObjectService.save(ParameterBo.from(toUpdate)));
    }

    @Override
    public Parameter getParameter(ParameterKey key) {
        return ParameterBo.to(getParameterBo(key));
    }

    /**
     * A private method for fetching the ParameterBo.  This method exists and is used elsewhere because of an OJB
     * bug highlighted in this jira issue: https://jira.kuali.org/browse/KULRICE-6800
     */
    private ParameterBo getParameterBo(ParameterKey key) {
        if (key == null) {
            throw new RiceIllegalArgumentException("key is null");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", key.getName());
        map.put("applicationId", key.getApplicationId());
        map.put("namespaceCode", key.getNamespaceCode());
        map.put("componentCode", key.getComponentCode());
        ParameterBo bo =  businessObjectService.findByPrimaryKey(ParameterBo.class, Collections.unmodifiableMap(map));

        if (bo == null & !KRADConstants.DEFAULT_PARAMETER_APPLICATION_ID.equals(key.getApplicationId())) {
            map.put("applicationId", KRADConstants.DEFAULT_PARAMETER_APPLICATION_ID);
            bo = businessObjectService.findByPrimaryKey(ParameterBo.class, Collections.unmodifiableMap(map));
        }
        return bo;
    }

    @Override
    public String getParameterValueAsString(ParameterKey key) {
        final ParameterBo p =  getParameterBo(key);
        return p != null ? p.getValue() : null;
    }

    @Override
    public Boolean getParameterValueAsBoolean(ParameterKey key) {
        final ParameterBo p =  getParameterBo(key);
        final String value =  p != null ? p.getValue() : null;
        return Truth.strToBooleanIgnoreCase(value);
    }

    @Override
    public Collection<String> getParameterValuesAsString(ParameterKey key) {
        return splitOn(getParameterValueAsString(key), ";");
    }

    @Override
    public String getSubParameterValueAsString(ParameterKey key, String subParameterName) {
        if (StringUtils.isBlank(subParameterName)) {
            throw new RiceIllegalArgumentException("subParameterName is blank");
        }

        Collection<String> values = getParameterValuesAsString(key);
        return getSubParameter(values, subParameterName);
    }

    @Override
    public Collection<String> getSubParameterValuesAsString(ParameterKey key, String subParameterName) {
       return splitOn(getSubParameterValueAsString(key, subParameterName), ",");
    }

    private String getSubParameter(Collection<String> values, String subParameterName) {
        for (String value : values) {
            if (subParameterName.equals(StringUtils.substringBefore(value, SUB_PARAM_SEPARATOR))) {
                return StringUtils.trimToNull(StringUtils.substringAfter(value, SUB_PARAM_SEPARATOR));
            }
        }
        return null;
    }

    private Collection<String> splitOn(String strValues, String delim) {
        if (StringUtils.isEmpty(delim)) {
            throw new RiceIllegalArgumentException("delim is empty");
        }

        if (strValues == null || StringUtils.isBlank(strValues)) {
            return Collections.emptyList();
        }

        final Collection<String> values = new ArrayList<String>();
        for (String value : strValues.split(delim)) {
            values.add(value.trim());
        }

        return Collections.unmodifiableCollection(values);
    }

    @Override
	public ParameterQueryResults findParameters(QueryByCriteria queryByCriteria) {
        if (queryByCriteria == null) {
            throw new RiceIllegalArgumentException("queryByCriteria is null");
        }

        GenericQueryResults<ParameterBo> results = criteriaLookupService.lookup(ParameterBo.class, queryByCriteria);

        ParameterQueryResults.Builder builder = ParameterQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Parameter> ims = new ArrayList<Parameter>();
        for (ParameterBo bo : results.getResults()) {
            ims.add(ParameterBo.to(bo));
        }

        builder.setResults(ims);
        return builder.build();
	}

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }
}
