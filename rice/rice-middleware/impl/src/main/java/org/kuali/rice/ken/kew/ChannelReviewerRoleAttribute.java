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
package org.kuali.rice.ken.kew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalName;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.GenericRoleAttribute;
import org.kuali.rice.kew.rule.QualifiedRoleName;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.workgroup.GroupNameId;


/**
 * KEW RoleAttribute implementation that is responsible for encapsulating a list
 * of users and groups which are reviewers for a Notification Channel.
 * This implementation relies on the default XML form implemented by GenericRoleAttribute
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ChannelReviewerRoleAttribute extends GenericRoleAttribute {
    private static final Logger LOG = Logger.getLogger(ChannelReviewerRoleAttribute.class);
    private static final List<RoleName> SUPPORTED_ROLES;
    
    static {
        RoleName reviewerRole = new RoleName(ChannelReviewerRoleAttribute.class.getName(), "reviewers", "Reviewers");
        List<RoleName> tmp = new ArrayList<RoleName>(1);
        tmp.add(reviewerRole);
        SUPPORTED_ROLES = Collections.unmodifiableList(tmp);
    }

    /**
     * Constructs a ChannelReviewerRoleAttribute.java.
     */
    public ChannelReviewerRoleAttribute() {
        super("channelReviewers");
        LOG.info("CHANNEL REVIEWER ROLE ATTRIBUTE CONSTRUCTOR");
    }

    /**
     * @see org.kuali.rice.kew.rule.GenericRoleAttribute#isMatch(org.kuali.rice.kew.routeheader.DocumentContent, java.util.List)
     */
    @Override
    public boolean isMatch(DocumentContent docContent, List<RuleExtension> ruleExtensions) {
        LOG.info("CHANNEL REVIEWER ROLE ATTRIBUTE IS MATCH");
        return super.isMatch(docContent, ruleExtensions);
    }

    /**
     * @see org.kuali.rice.kew.rule.GenericWorkflowAttribute#getProperties()
     */
    @Override
    public Map<String, String> getProperties() {
        LOG.info("CHANNEL REVIEWER ROLE ATTRIBUTE GETPROPERTIES");
        // intentionally unimplemented...not intending on using this attribute client-side
        return null;
    }

    /**
     * @see org.kuali.rice.kew.rule.RoleAttribute#getRoleNames()
     */
    public List<RoleName> getRoleNames() {
        LOG.info("CHANNEL REVIEWER ROLE ATTRIBUTE CALLED ROLENAMES");
        return SUPPORTED_ROLES;
    }
    
    /**
     * @see org.kuali.rice.kew.rule.GenericRoleAttribute#getQualifiedRoleNames(java.lang.String, org.kuali.rice.kew.routeheader.DocumentContent)
     */
    @Override
    public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
        List<String> qrn = new ArrayList<String>(1);
        qrn.add(roleName);
        return qrn;
    }

    /**
     * This looks at the reviewers list passed through from KEN and then resolves the individuals that need to actually approve 
     * the message.
     * @see org.kuali.rice.kew.rule.GenericRoleAttribute#resolveRecipients(org.kuali.rice.kew.engine.RouteContext, org.kuali.rice.kew.rule.QualifiedRoleName)
     */
    @Override
    protected List<Id> resolveRecipients(RouteContext routeContext, QualifiedRoleName qualifiedRoleName) {
        LOG.info("CHANNEL REVIEWER ROLE ATTRIBUTE CALLED");
        List<Id> ids = new ArrayList<Id>();

        LOG.info("DOC CONTENT:" + routeContext.getDocumentContent().getDocContent());
        LOG.info("ATTR CONTENT:" + routeContext.getDocumentContent().getAttributeContent());
        DocumentContent dc = routeContext.getDocumentContent();
        List<Map<String, String>> attrs;
        try {
            attrs = content.parseContent(dc.getAttributeContent());
        } catch (XPathExpressionException xpee) {
            throw new WorkflowRuntimeException("Error parsing ChannelReviewer role attribute content", xpee);
        }
        
        if (attrs.size() > 0) {
            Map<String, String> values = attrs.get(0);
            if (values != null) {
                // iterate through all "fields" and accumulate a list of users and groups
                for (Map.Entry<String, String> entry: values.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    LOG.info("Entry: " + name + "=" + value);
                    Id id;
                    if (name.startsWith("user")) {
                        LOG.info("Adding user: " + value);
                        id = new PrincipalName(value);
                        ids.add(id);
                    } else if (name.startsWith("group")) {
                        LOG.info("Adding group: " + value);
                        id = new GroupNameId(value);
                        ids.add(id);
                    } else {
                        LOG.error("Invalid attribute value: " + name + "=" + value);
                    }
                }
            }
        } else {
            LOG.debug("No attribute content found for ChannelReviewerRoleAttribute");
        }
        
        LOG.info("Returning ids: " + ids.size());
        return ids;
    }
}
