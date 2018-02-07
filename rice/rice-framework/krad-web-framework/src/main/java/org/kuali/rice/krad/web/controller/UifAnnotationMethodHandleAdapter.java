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
package org.kuali.rice.krad.web.controller;

import org.kuali.rice.krad.web.bind.UifServletRequestDataBinder;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import javax.servlet.http.HttpServletRequest;

/**
 * Overloaded in order to hook in the UIF Binder classes
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifAnnotationMethodHandleAdapter extends AnnotationMethodHandlerAdapter {

    @Override
    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object target,
            String objectName) throws Exception {
        if (target != null) {
            // only override for UifFormBase models so that non KRAD spring MVC
            // can be used in same dispatcher servlet.
            if (target instanceof UifFormBase) {
                return new UifServletRequestDataBinder(target, objectName);
            }
        }

        return super.createBinder(request, target, objectName);
    }
}
