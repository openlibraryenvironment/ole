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
package edu.sampleu.travel.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.rule.GenericWorkflowAttribute;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;

public class DestinationRuleAttribute extends GenericWorkflowAttribute {

	private static final String DEST_LABEL = "Destination";
	private static final String DEST_FIELD_KEY = "destination";

    private final List<Row> rows = new ArrayList<Row>();

	private String destination;

    public DestinationRuleAttribute() {
        super("destination");
    }

    public DestinationRuleAttribute(String destination) {
        super("destination");
        this.destination = destination;
    }

    /*
	public boolean isMatch(DocumentContent docContent, List<RuleExtension> ruleExtensions) {
		try {
			boolean foundDestRule = false;
			for (Iterator extensionsIterator = ruleExtensions.iterator(); extensionsIterator.hasNext();) {
	            RuleExtension extension = (RuleExtension) extensionsIterator.next();
	            if (extension.getRuleTemplateAttribute().getRuleAttribute().getClassName().equals(getClass().getName())) {
	                for (Iterator valuesIterator = extension.getExtensionValues().iterator(); valuesIterator.hasNext();) {
	                    RuleExtensionValue extensionValue = (RuleExtensionValue) valuesIterator.next();
	                    String key = extensionValue.getKey();
	                    String value = extensionValue.getValue();
	                    if (key.equals(DEST_FIELD_KEY)) {
	                    	destination = value;
	                    	foundDestRule = true;
	                    }
	                }
	            }
	        }
			if (! foundDestRule) {
				return false;
			}

			Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
					new InputSource(new BufferedReader(new StringReader(docContent.getDocContent())))).getDocumentElement();
			XPath xpath = XPathFactory.newInstance().newXPath();
			String docContentDest = xpath.evaluate("//destination", element);
			return destination.equals(docContentDest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/

	public List<Row> getRuleRows() {
		return getRows();
	}

	public List<Row> getRoutingDataRows() {
		return getRows();
	}

    private List<Row> getRows() {
        log.info("Returning rows: " + rows);
        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field(DEST_LABEL, "", Field.TEXT, false, DEST_FIELD_KEY, "", false, false, null, null));
        List<Row> rows = new ArrayList<Row>();
        rows.add(new Row(fields));
        return rows;
    }

    /* setter for edoclite field */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<String, String>();
        props.put("destination", destination);
        return props;
    }

	public List<RemotableAttributeError> validateRoutingData(Map paramMap) {
		return validateInputMap(paramMap);
	}

	public List<RemotableAttributeError> validateRuleData(Map paramMap) {
		return validateInputMap(paramMap);
	}

    private List<RemotableAttributeError> validateInputMap(Map paramMap) {
    	List errors = new ArrayList();
    	this.destination = (String) paramMap.get(DEST_FIELD_KEY);
    	if (StringUtils.isBlank(destination)  && required) {
    		errors.add(RemotableAttributeError.Builder.create(DEST_FIELD_KEY, "Destination is required.").build());
    	}
    	return errors;
    }
}
