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
package org.kuali.rice.kew.rule.xmlrouting;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.xml.xstream.XStreamSafeSearchFunction;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;
import java.util.List;
import java.util.Map;

/**
 * A function resolver for XPath functions provided by KEW.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkflowFunctionResolver implements XPathFunctionResolver {
	
	private List<RuleExtension> ruleExtensions;
	private Node rootNode;
	private XPath xpath;


	public XPathFunction resolveFunction(QName fname, int arity) {
		if (fname == null) {
			throw new NullPointerException("The function name cannot be null.");
		}
		if (fname.equals(new QName("http://nothingfornowwf.com", "ruledata", "wf"))) {
			if (ruleExtensions == null) {
				throw new IllegalArgumentException("There are no rule extensions.");
			}
			return new XPathFunction() {
				public Object evaluate(List args) {
					if (args.size() == 1) {
						String name = (String) args.get(0);
						for (RuleExtension ruleExtension : ruleExtensions) {
						    for (Map.Entry<String, String> entry : ruleExtension.getExtensionValuesMap().entrySet()) {
						        if (entry.getKey().equals(name)) {
						            return entry.getValue();
						        }
						    }
						}
					}
					return "";
				}
			};
		} else if (fname.equals(new QName("http://nothingfornowwf.com", "xstreamsafe", "wf"))) {
			return new XStreamSafeSearchFunction(rootNode, this.getXpath());
		} else if (fname.equals(new QName("http://nothingfornowwf.com", "upper-case", "wf"))) {
			return new UpperCaseFunction();
		} else if (fname.equals(new QName("http://nothingfornowwf.com", "field", "wf"))) {
			return new XPathFunction() {
				public Object evaluate(java.util.List args) {
					if (args.size() == 1) {
						String name = (String) args.get(0);
						try {
							return field(name);
						} catch (Exception e) {
							throw new WorkflowRuntimeException("Failed to find field to validate.", e);
						}
					}
					return "";
				}
			};
		} else if (fname.equals(new QName("http://nothingfornowwf.com", "empty", "wf"))) {
			return new XPathFunction() {
				public Object evaluate(java.util.List args) {
					return empty(args.get(0));
				}
			};
		} else {
			return null;
		}
	}

	public String field(String fieldName) throws Exception {
	    return xpath.evaluate("//edlContent/data/version[@current='true']/field[@name='" + fieldName + "']/value", rootNode);
	}
	    
	    
	public boolean empty(Object object) {
	    if (object instanceof String) {
	    	return StringUtils.isBlank((String)object);
	    }
	    return object == null;
	}

	public List<RuleExtension> getRuleExtensions() {
        return this.ruleExtensions;
    }

    public void setRuleExtensions(List<RuleExtension> ruleExtensions) {
        this.ruleExtensions = ruleExtensions;
    }

    public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}
	

	public XPath getXpath() {
		return xpath;
	}

	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}
}
