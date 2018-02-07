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
package org.kuali.rice.coreservice.impl.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.component.Component;
import org.kuali.rice.coreservice.api.component.ComponentService;
import org.kuali.rice.coreservice.api.namespace.Namespace;
import org.kuali.rice.coreservice.api.namespace.NamespaceService;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.api.parameter.ParameterQueryResults;
import org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.component.ComponentEbo;
import org.kuali.rice.coreservice.framework.namespace.NamespaceEbo;
import org.kuali.rice.coreservice.framework.parameter.ParameterEbo;
import org.kuali.rice.coreservice.framework.parameter.ParameterTypeEbo;
import org.kuali.rice.coreservice.impl.component.ComponentBo;
import org.kuali.rice.coreservice.impl.namespace.NamespaceBo;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.coreservice.impl.parameter.ParameterTypeBo;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.impl.RemoteModuleServiceBase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.like;

public class CoreServiceRemoteModuleService extends RemoteModuleServiceBase {

    private static final org.apache.log4j.Logger LOG =
            org.apache.log4j.Logger.getLogger(CoreServiceRemoteModuleService.class);

    private static final String CODE = "code";
    private static final String NAMESPACE_CODE = "namespaceCode";
    private static final String COMPONENT_CODE = "componentCode";
    private static final String NAME = "name";
    private static final String APPLICATION_ID = "applicationId";

    @Override
    public <T extends ExternalizableBusinessObject> T getExternalizableBusinessObject(Class<T> businessObjectClass,
            Map<String, Object> fieldValues) {
        T result = null;
        if (NamespaceEbo.class.isAssignableFrom(businessObjectClass)) {
            if(isNonBlankValueForKey(fieldValues, CODE)) {
                Namespace namespace = getNamespaceService().getNamespace((String)fieldValues.get(CODE));
                result = (T) NamespaceBo.from(namespace);
            }
        }
        if (ParameterEbo.class.isAssignableFrom(businessObjectClass)) {
            if(isNonBlankValueForKey(fieldValues, APPLICATION_ID) && isNonBlankValueForKey(fieldValues, NAMESPACE_CODE)
                    && isNonBlankValueForKey(fieldValues, COMPONENT_CODE) && isNonBlankValueForKey(fieldValues, NAME)) {
                ParameterKey key = ParameterKey.create((String)fieldValues.get(APPLICATION_ID),(String)fieldValues.get(NAMESPACE_CODE),
                        (String)fieldValues.get(COMPONENT_CODE),(String)fieldValues.get(NAME));
                Parameter parameter = getParameterRepositoryService().getParameter(key);
                result = (T) ParameterBo.from(parameter);
            }
        }
        if (ComponentEbo.class.isAssignableFrom(businessObjectClass)) {
            if(isNonBlankValueForKey(fieldValues, CODE) && isNonBlankValueForKey(fieldValues, NAMESPACE_CODE)){
                Component component = getComponentService().getComponentByCode((String)fieldValues.get(NAMESPACE_CODE), (String)fieldValues.get(CODE));
                result = (T) ComponentBo.from(component);
            }
        }
        if (ParameterTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if(isNonBlankValueForKey(fieldValues, CODE)){
                ParameterTypeBo parameterType = getBusinessObjectService().findBySinglePrimaryKey(ParameterTypeBo.class,(String)fieldValues.get(CODE));
                result = (T) parameterType;
            }
        }
        return result;
    }

    @Override
    public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsList(
            Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        if(NamespaceEbo.class.isAssignableFrom(businessObjectClass)) {

            //
            // sticks and bubblegum query
            //

            List<Namespace> namespaces = getNamespaceService().findAllNamespaces();

            List<NamespaceBo> results = new ArrayList<NamespaceBo>(namespaces.size());

            for (Namespace namespace : namespaces) {
                NamespaceBo namespaceBo = NamespaceBo.from(namespace);
                boolean fieldsMatch = true;
                for (Map.Entry<String, Object> fieldValue : fieldValues.entrySet()) {
                    if (!fieldMatches(namespaceBo, fieldValue)) {
                        fieldsMatch = false;
                        break;
                    }
                }
                if (fieldsMatch) { results.add(namespaceBo); }
            }
            return (List<T>)results;
        }
        if(ParameterEbo.class.isAssignableFrom(businessObjectClass)) {
            QueryByCriteria queryCriteria = QueryByCriteria.Builder.fromPredicates(like("name", "*"));
            ParameterQueryResults parameterResults = getParameterRepositoryService().findParameters(
                    queryCriteria);

            List<Parameter> parameters = parameterResults.getResults();
            List<ParameterBo> results =  new ArrayList<ParameterBo>(parameters.size());

            for (Parameter parameter : parameters) {
                ParameterBo parameterBo = ParameterBo.from(parameter);
                boolean fieldsMatch = true;
                for (Map.Entry<String, Object> fieldValue : fieldValues.entrySet()) {
                    if (!fieldMatches(parameterBo, fieldValue)) {
                        fieldsMatch = false;
                        break;
                    }
                }
                if (fieldsMatch) { results.add(parameterBo); }
            }
            return (List<T>)results;
        }

        return Collections.emptyList();
    }

    private boolean fieldMatches(Object ebo, Map.Entry<String,Object> fieldValue) {
        try {
            return ObjectUtils.equals(fieldValue.getValue(), BeanUtils.getProperty(ebo, fieldValue.getKey()));
        } catch (IllegalAccessException e) {
            LOG.warn("querying " + ebo.getClass().getName() + " for an inaccessible field called '" + fieldValue.getKey() + "'" );
        } catch (InvocationTargetException e) {
            LOG.warn("exception querying " + ebo.getClass().getName() + " for a field called '" + fieldValue.getKey() + "'" );
        } catch (NoSuchMethodException e) {
            LOG.warn("querying " + ebo.getClass().getName() + " for an invalid field called '" + fieldValue.getKey() + "'" );
        }
        return false;
    }

    @Override
    public boolean isExternalizableBusinessObjectLookupable(Class boClass) {
        if(NamespaceEbo.class.isAssignableFrom(boClass)) { return true; }
        else if(ParameterEbo.class.isAssignableFrom(boClass)) { return true; }
        else if(ComponentEbo.class.isAssignableFrom(boClass)) { return true; }
        else if(ParameterTypeEbo.class.isAssignableFrom(boClass)) { return true; }
        return false;
    }

    @Override
    public boolean isExternalizableBusinessObjectInquirable(Class boClass) {
        if(NamespaceEbo.class.isAssignableFrom(boClass)) { return true; }
        else if(ParameterEbo.class.isAssignableFrom(boClass)) { return true; }
        else if(ComponentEbo.class.isAssignableFrom(boClass)) { return true; }
        else if(ParameterTypeEbo.class.isAssignableFrom(boClass)) { return true; }
        return false;
    }

    // Lazy init holder class, see Effective Java item 71
    private static class NamespaceServiceHolder {
        static final NamespaceService namespaceService = CoreServiceApiServiceLocator.getNamespaceService();
    }

    private NamespaceService getNamespaceService() {
        return NamespaceServiceHolder.namespaceService;
    }
    private static class ParameterRepositoryServiceHolder {
        static final ParameterRepositoryService parameterRepositoryService = CoreServiceApiServiceLocator.getParameterRepositoryService();
    }

    private ParameterRepositoryService getParameterRepositoryService() {
        return ParameterRepositoryServiceHolder.parameterRepositoryService;
    }
    private static class ComponentServiceHolder {
        static final ComponentService componentService = CoreServiceApiServiceLocator.getComponentService();
    }

    private ComponentService getComponentService() {
        return ComponentServiceHolder.componentService;
    }

    private static class BusinessObjectServiceHolder {
        static final BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    }

    private BusinessObjectService getBusinessObjectService() {
        return BusinessObjectServiceHolder.businessObjectService;
    }

}
