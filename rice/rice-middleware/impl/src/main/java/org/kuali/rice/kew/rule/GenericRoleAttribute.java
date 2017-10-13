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
package org.kuali.rice.kew.rule;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;


/**
 * Generic base class that implements common functionality to simplify implementing
 * a RoleAttribute.  This includes a standard qualified role name String format and simplified
 * template methods, as well as a generic attribute content model.
 * 
 * <p>Control flow:</p>
 * 
 * <ol>
 *   <li>{@link #getQualifiedRoleNames(String, DocumentContent)}
 *     <ol>
 *       <li>{@link #generateQualifiedRoleNames(String, DocumentContent)}
 *         <ol>
 *           <li>{@link #getRoleNameQualifiers(String, DocumentContent)}</li>
 *         </ol>
 *       </li>
 *     </ol>
 *   </li>
 *   <li>{@link #resolveQualifiedRole(RouteContext, String, String)}
 *     <ol>
 *       <li>{@link #resolveQualifiedRole(RouteContext, QualifiedRoleName)}
 *         <ol>
 *           <li>{@link #resolveRecipients(RouteContext, QualifiedRoleName)}</li>
 *           <li>{@link #getLabelForQualifiedRoleName(QualifiedRoleName)}</li>
 *         </ol>
 *       </li>
 *     </ol>
 *   </li>
 * </ol> 
 *     
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class GenericRoleAttribute extends GenericWorkflowAttribute implements RoleAttribute {
    public GenericRoleAttribute() {
        super(null);
    }

    public GenericRoleAttribute(String uniqueName) {
        super(uniqueName);
    }

    public boolean isMatch(DocumentContent docContent, List<RuleExtension> ruleExtensions) {
        // FIXME: ok, this MUST return true, as it IS evaluated (don't know why)
        // maybe investigate only calling isMatch on rule attributes as it doesn't seem that
        // matching is relevant for role attributes...is it?
        // throw new UnsupportedOperationException("Role attributes do not implement isMatch");
        return true;
    }

    public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
        List<QualifiedRoleName> qualifiedRoleNames = generateQualifiedRoleNames(roleName, documentContent);
        if (qualifiedRoleNames == null) {
            return null;
        }
        List<String> q = new ArrayList<String>(qualifiedRoleNames.size());
        for (QualifiedRoleName qrn: qualifiedRoleNames) {
            q.add(qrn.encode());
        }
        return q;
    }

    /**
     * Template method responsible for producing a list of QualifiedRoleName objects.  Default implementation
     * calls {@link #getRoleNameQualifiers(String, DocumentContent)}
     */
    protected List<QualifiedRoleName> generateQualifiedRoleNames(String roleName, DocumentContent documentContent) {
        List<String> qualifiers = getRoleNameQualifiers(roleName, documentContent);
        if (qualifiers == null) {
            qualifiers = new ArrayList<String>(0);
        }
        List<QualifiedRoleName> qualifiedRoleNames = new ArrayList<QualifiedRoleName>(qualifiers.size());
        for (String qualifier: qualifiers) {
            qualifiedRoleNames.add(new QualifiedRoleName(roleName, qualifier));
        }
        return qualifiedRoleNames;
    }

    /**
     * Template method responsible for producing qualifiers for a role name
     */
    protected List<String> getRoleNameQualifiers(String roleName, DocumentContent documentContent) {
        return null;
    }

    public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRoleName) {
        QualifiedRoleName qrn = QualifiedRoleName.parse(qualifiedRoleName);
        return resolveQualifiedRole(routeContext, qrn);
    }

    /**
     * Template method that delegates to {@link #resolveRecipients(RouteContext, QualifiedRoleName)} and
     * {@link #getLabelForQualifiedRoleName(QualifiedRoleName)
     */
    protected ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, QualifiedRoleName qualifiedRoleName) {
        List<Id> recipients = resolveRecipients(routeContext, qualifiedRoleName);
        ResolvedQualifiedRole rqr = new ResolvedQualifiedRole(getLabelForQualifiedRoleName(qualifiedRoleName),
                                                              recipients
                                                              ); // default to no annotation...
        return rqr;
    }

    protected String getLabelForQualifiedRoleName(QualifiedRoleName qualifiedRoleName) {
        // do what everybody else does and just use the base role name
        return qualifiedRoleName.getBaseRoleName();
    }

    /**
     * Template method for subclasses to implement
     */
    protected List<Id> resolveRecipients(RouteContext routeContext, QualifiedRoleName qualifiedRoleName) {
        return null;
    }
}
