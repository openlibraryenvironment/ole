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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.identity.Id;

/**
 * The resolution of a qualified role (role name + data) to a List of recipient
 * {@link Id}s.  In addition to the Ids of the recipients, a ResolvedQualifiedRole 
 * can also contain a label for the role (to be displayed on the Route Log) and
 * an annotation (to be displayed on the generated {@link ActionRequestValue}).
 * 
 * @see Id
 * @see ActionRequestValue
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ResolvedQualifiedRole implements Serializable {

	private static final long serialVersionUID = 5397269690550273869L;
	
	private List<Id> recipients = new ArrayList<Id>();
    private String qualifiedRoleLabel;
    private String annotation;

    public ResolvedQualifiedRole() {
    }

    public ResolvedQualifiedRole(String qualifiedRoleLabel, List<Id> recipients) {
        this(qualifiedRoleLabel, recipients, "");
    }
    
    public ResolvedQualifiedRole(String qualifiedRoleLabel, List<Id> recipients, String annotation) {
        this.qualifiedRoleLabel = qualifiedRoleLabel;
        this.recipients = recipients;
        this.annotation = annotation;
    }
    
    public List<Id> getRecipients() {
        return recipients;
    }
    public void setRecipients(List<Id> users) {
        this.recipients = users;
    }
    public String getQualifiedRoleLabel() {
        return qualifiedRoleLabel;
    }
    public void setQualifiedRoleLabel(String qualifiedRoleLabel) {
        this.qualifiedRoleLabel = qualifiedRoleLabel;
    }
    public String getAnnotation() {
        return annotation;
    }
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    
}
