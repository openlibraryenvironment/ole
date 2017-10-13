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
package org.kuali.rice.krad.web.bind;

import org.kuali.rice.krad.uif.view.ViewModel;
import org.springframework.beans.BeanWrapper;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * This is a description of what this class does - swgibson don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifBeanPropertyBindingResult extends BeanPropertyBindingResult {
    private static final long serialVersionUID = -3740046436620585003L;

    public UifBeanPropertyBindingResult(Object target, String objectName,  boolean autoGrowNestedPaths, int autoGrowCollectionLimit) {
        super(target, objectName, autoGrowNestedPaths, autoGrowCollectionLimit);
    }

    /**
     * Create a new {@link BeanWrapper} for the underlying target object.
     * @see #getTarget()
     */
    @Override
    protected UifViewBeanWrapper createBeanWrapper() {
        Assert.state(super.getTarget() != null, "Cannot access properties on null bean instance '" + getObjectName() + "'!");
        Assert.state(super.getTarget() instanceof ViewModel, "Object must be instance of ViewModel to use Uif Bean Wrapper");

        return new UifViewBeanWrapper((ViewModel) super.getTarget());
    }
}
