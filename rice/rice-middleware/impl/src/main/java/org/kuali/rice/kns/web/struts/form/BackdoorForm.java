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
package org.kuali.rice.kns.web.struts.form;

import org.kuali.rice.kns.web.struts.action.BackdoorAction;

/**
 * A Struts ActionForm for the {@link BackdoorAction}.
 *
 * @see BackdoorAction
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BackdoorForm extends KualiForm {

	private static final long serialVersionUID = -2720178686804392055L;

	private String methodToCall = "";
    private String backdoorId;
    private Boolean showBackdoorLogin;
    private Boolean isAdmin;
    private String linkTarget;
    private String targetName;
    //determines whether to show the backdoor login textbox in the backdoor links page
    private String backdoorLinksBackdoorLogin;

    private String graphic="yes";

    public String getBackdoorId() {
        return backdoorId;
    }
    public void setBackdoorId(String backdoorId) {
        this.backdoorId = backdoorId;
    }
    public String getMethodToCall() {
        return methodToCall;
    }
    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }
    public Boolean getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public Boolean getShowBackdoorLogin() {
        return showBackdoorLogin;
    }
    public void setShowBackdoorLogin(Boolean showBackdoorLogin) {
        this.showBackdoorLogin = showBackdoorLogin;
    }
    public String getLinkTarget() {
        return linkTarget;
    }
    public void setLinkTarget(String linkTarget) {
        this.linkTarget = linkTarget;
    }
    /*
    public String getTargetName() {
        return targetName;
    }
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
    */
    public String getGraphic(){
    	return this.graphic;
    }
    public void setGraphic(String choice){
    	if(!org.apache.commons.lang.StringUtils.isEmpty(choice)&&choice.trim().equals("no")){
    		this.graphic="no";
    	}else{
    		this.graphic="yes";
    	}
    }
	public String getBackdoorLinksBackdoorLogin() {
		return backdoorLinksBackdoorLogin;
	}
	public void setBackdoorLinksBackdoorLogin(String backdoorLinksBackdoorLogin) {
		this.backdoorLinksBackdoorLogin = backdoorLinksBackdoorLogin;
	}
}
