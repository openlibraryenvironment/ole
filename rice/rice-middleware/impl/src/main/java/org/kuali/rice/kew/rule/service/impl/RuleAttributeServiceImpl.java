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
package org.kuali.rice.kew.rule.service.impl;

import org.jdom.Element;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.dao.RuleAttributeDAO;
import org.kuali.rice.kew.rule.service.RuleAttributeService;
import org.kuali.rice.kew.xml.RuleAttributeXmlParser;
import org.kuali.rice.kew.xml.export.RuleAttributeXmlExporter;

import javax.xml.namespace.QName;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RuleAttributeServiceImpl implements RuleAttributeService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleAttributeServiceImpl.class);

    private static final String RULE_ATTRIBUTE_NAME_REQUIRED = "rule.attribute.name.required";
    private static final String RULE_ATTRIBUTE_CLASS_REQUIRED = "rule.attribute.className.required";

    private static final String XML_FILE_NOT_FOUND = "general.error.filenotfound";
    private static final String XML_PARSE_ERROR = "general.error.parsexml";

    private RuleAttributeDAO ruleAttributeDAO;

    public void save(RuleAttribute ruleAttribute) {
        validate(ruleAttribute);
        getRuleAttributeDAO().save(ruleAttribute);
    }

    public void delete(String ruleAttributeId) {
        getRuleAttributeDAO().delete(ruleAttributeId);
    }

    public List<RuleAttribute> findByRuleAttribute(RuleAttribute ruleAttribute) {
        return getRuleAttributeDAO().findByRuleAttribute(ruleAttribute);
    }

    public RuleAttribute findByRuleAttributeId(String ruleAttributeId) {
        return getRuleAttributeDAO().findByRuleAttributeId(ruleAttributeId);
    }

    public List<RuleAttribute> findAll() {
        return getRuleAttributeDAO().getAllRuleAttributes();
    }

    public RuleAttribute findByName(String name) {
    	return getRuleAttributeDAO().findByName(name);
    }

    public RuleAttributeDAO getRuleAttributeDAO() {
        return ruleAttributeDAO;
    }

    public void setRuleAttributeDAO(RuleAttributeDAO ruleAttributeDAO) {
        this.ruleAttributeDAO = ruleAttributeDAO;
    }

    private void validate(RuleAttribute ruleAttribute) {
        LOG.debug("validating ruleAttribute");
        Collection errors = new ArrayList();
        if (ruleAttribute.getName() == null || ruleAttribute.getName().trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("Please enter a rule attribute name.", RULE_ATTRIBUTE_NAME_REQUIRED));
            LOG.error("Rule attribute name is missing");
        } else {
        	ruleAttribute.setName(ruleAttribute.getName().trim());
            if (ruleAttribute.getId() == null) {
                RuleAttribute nameInUse = findByName(ruleAttribute.getName());
                if (nameInUse != null) {
                    errors.add(new WorkflowServiceErrorImpl("Rule attribute name already in use", "routetemplate.ruleattribute.name.duplicate"));
                    LOG.error("Rule attribute name already in use");
                }
            }
        }
        if (ruleAttribute.getResourceDescriptor() == null || ruleAttribute.getResourceDescriptor().trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("Please enter a rule attribute class name.", RULE_ATTRIBUTE_CLASS_REQUIRED));
            LOG.error("Rule attribute class name is missing");
        } else {
        	ruleAttribute.setResourceDescriptor(ruleAttribute.getResourceDescriptor().trim());
        }

        LOG.debug("end validating ruleAttribute");
        if (!errors.isEmpty()) {
            throw new WorkflowServiceErrorException("RuleAttribute Validation Error", errors);
        }
    }

    @Override
    public Object loadRuleAttributeService(RuleAttribute attribute) {
        return loadRuleAttributeService(attribute, null);
    }

    @Override
    public Object loadRuleAttributeService(RuleAttribute attribute, String defaultApplicationId) {
        Object attributeService = null;
        // first check if the class name is a valid and available java class
        String attributeName = attribute.getResourceDescriptor();
        ObjectDefinition attributeObjectDefinition = getAttributeObjectDefinition(attribute, defaultApplicationId);
        attributeService = GlobalResourceLoader.getObject(attributeObjectDefinition);
        if (attributeService == null) {
            // if we can't find a class, try a service
            attributeService = GlobalResourceLoader.getService(QName.valueOf(attributeName));
        }
        return attributeService;
    }

    protected ObjectDefinition getAttributeObjectDefinition(RuleAttribute ruleAttribute, String defaultApplicationId) {
        if (ruleAttribute.getApplicationId() == null && defaultApplicationId != null) {
            return new ObjectDefinition(ruleAttribute.getResourceDescriptor(), defaultApplicationId);
        } else {
            return new ObjectDefinition(ruleAttribute.getResourceDescriptor(), ruleAttribute.getApplicationId());
        }
    }

    public void loadXml(InputStream inputStream, String principalId) {
        RuleAttributeXmlParser parser = new RuleAttributeXmlParser();
        try {
            parser.parseRuleAttributes(inputStream);
        } catch(FileNotFoundException e) {
            throw new WorkflowServiceErrorException("XML file not found", new WorkflowServiceErrorImpl("Rule Attribute XML file not found", XML_FILE_NOT_FOUND) );
    	} catch (Exception e) { //any other exception
            LOG.error("Error loading xml file", e);
            throw new WorkflowServiceErrorException("Error loading xml file", new WorkflowServiceErrorImpl("Error loading xml file", XML_PARSE_ERROR));
        }
    }

    public Element export(ExportDataSet dataSet) {
        RuleAttributeXmlExporter exporter = new RuleAttributeXmlExporter();
        return exporter.export(dataSet);
    }
    
	@Override
	public boolean supportPrettyPrint() {
		return true;
	}

	public List<RuleAttribute> findByClassName(String className) {
		return this.ruleAttributeDAO.findByClassName(className);
	}
}
