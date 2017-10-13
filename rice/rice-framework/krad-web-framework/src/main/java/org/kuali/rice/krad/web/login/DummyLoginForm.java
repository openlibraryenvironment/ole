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
package org.kuali.rice.krad.web.login;


import org.kuali.rice.krad.uif.component.RequestParameter;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Basic form for Dummy Login
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DummyLoginForm extends UifFormBase {
    private static final long serialVersionUID =  -7525378097732816418L;

    //InputFields
    protected String login_user;
    protected String login_pw;
    @RequestParameter
    protected String login_message;

    public DummyLoginForm() {
        super();
        login_message = "";
    }

    /**
     * Below are basic getters and setters for this data object  *
     */

    public String getLogin_user() {
        return login_user;
    }

    public void setLogin_user(String login_user) {
        this.login_user = login_user;
    }

    public String getLogin_pw() {
        return login_pw;
    }

    public void setLogin_pw(String login_pw) {
        this.login_pw = login_pw;
    }

    public String getLogin_message() {
        return login_message;
    }

    public void setLogin_message(String login_message) {
        this.login_message = login_message;
    }
}
