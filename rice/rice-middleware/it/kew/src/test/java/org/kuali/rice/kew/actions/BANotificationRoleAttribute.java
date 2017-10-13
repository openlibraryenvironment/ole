/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalName;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.AbstractRoleAttribute;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;

/**
 * Current state of affairs
 * 
 * jitrue -> primary recipient
 * natjohns -> delegate (type dictated by rule setup)
 * shenl -> delegate (type dictated by rule setup)
 *
 */
public class BANotificationRoleAttribute extends AbstractRoleAttribute {

    public List<RoleName> getRoleNames() {
        return Arrays.asList(new RoleName[] { new RoleName(getClass().getName(), "Notify", "Notify"), new RoleName(getClass().getName(), "Notify2", "Notify2"), new RoleName(getClass().getName(), "NotifyDelegate", "NotifyDelegate") });
    }

    public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
        List<String> qualifiedRoleNames = new ArrayList<String>();
        if (roleName.equals("Notify") || roleName.equals("Notify2")) {
            qualifiedRoleNames.add("jitrue");    
        } else throw new RuntimeException("Bad Role " + roleName);        
        return qualifiedRoleNames;
    }

    public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
        if (roleName.equals("Notify") || roleName.equals("Notify2")) {
            return new ResolvedQualifiedRole(roleName, Arrays.asList(new Id[] { new PrincipalName(qualifiedRole) }));
        } else if (roleName.equals("NotifyDelegate")) {
            List<Id> recipients = new ArrayList<Id>();
            recipients.add(new PrincipalName("natjohns"));
            recipients.add(new PrincipalName("shenl"));
            return new ResolvedQualifiedRole(roleName, recipients);
        }
        throw new RuntimeException("Bad Role " + roleName);
    }

}
