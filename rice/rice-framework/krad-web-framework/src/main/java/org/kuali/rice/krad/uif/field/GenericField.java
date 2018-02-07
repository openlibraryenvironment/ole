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
package org.kuali.rice.krad.uif.field;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;

/**
 * Field whose output is produced by invoking a method or template and has no
 * properties (generic)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "genericField-bean", parent = "Uif-CustomTemplateField"),
        @BeanTag(name = "customTemplateField-bean", parent = "Uif-CustomTemplateField")})
public class GenericField extends FieldBase {
    private static final long serialVersionUID = -7756170771822430903L;

    public GenericField() {
        super();
    }

}
