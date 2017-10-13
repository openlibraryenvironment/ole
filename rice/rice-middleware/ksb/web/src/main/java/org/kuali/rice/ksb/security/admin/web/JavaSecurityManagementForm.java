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
package org.kuali.rice.ksb.security.admin.web;

import java.security.KeyStoreException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.kuali.rice.ksb.service.KSBServiceLocator;

/**
 * Struts action form for the {@link JavaSecurityManagementAction} 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class JavaSecurityManagementForm extends ActionForm {

    private static final long serialVersionUID = -46462912979586142L;

    private String alias;
    private String password;
    private String passwordVerify;
    
    /**
     * This method is used to check for completeness of the form as well as verification of the desired password
     */
    public ActionErrors validateGenerateClientKeystore(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        // check that all data is filled in
        if (StringUtils.isBlank(getAlias())) {
            errors.add("property", new ActionMessage("Alias must have a valid value.",false));
        }
        if (StringUtils.isBlank(getPassword()) || StringUtils.isBlank(getPasswordVerify()) ) {
            errors.add("property", new ActionMessage("Password must have a valid value in both fields.",false));
        }
        if (errors.isEmpty()) {
            // if password and passwordVerify are not equal error out
            if (!StringUtils.equals(getPassword(), getPasswordVerify())) {
                errors.add("property", new ActionMessage("Passwords do not match.",false));
            }
        }
        if (errors.isEmpty()) {
            try {
                if (KSBServiceLocator.getJavaSecurityManagementService().isAliasInKeystore(getAlias())) {
                    errors.add("property", new ActionMessage("Alias '" + getAlias() + "' already exists in keystore.",false));
                }
            } catch (KeyStoreException e) {
                errors.add("property", new ActionMessage("Could not check keystore file for alias '" + getAlias(),false));
            }
        }
        return errors;
    }

    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        if (alias == null) {
            this.alias = null;
        } else {
            this.alias = alias.trim();
        }
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordVerify() {
        return this.passwordVerify;
    }
    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }

}
