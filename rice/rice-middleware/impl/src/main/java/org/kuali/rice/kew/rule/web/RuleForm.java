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
package org.kuali.rice.kew.rule.web;

import org.kuali.rice.kns.web.struts.form.KualiForm;

/**
 * This class is the action form for the Rule Document.
 */
public class RuleForm extends KualiForm {
    private static final long serialVersionUID = 1L;

    private String documentTypeName;
    private String ruleTemplateName;

    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    public String getRuleTemplateName() {
        return this.ruleTemplateName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public void setRuleTemplateName(String ruleTemplateName) {
        this.ruleTemplateName = ruleTemplateName;
    }

    public void clearSearchableAttributeProperties() {
        this.documentTypeName = "";
        this.ruleTemplateName = "";
    }

}
