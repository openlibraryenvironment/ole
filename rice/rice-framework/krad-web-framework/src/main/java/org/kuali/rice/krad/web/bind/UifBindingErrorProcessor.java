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

import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.web.format.FormatException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.beans.PropertyAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;

/**
 * This is a description of what this class does - pctsh don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class UifBindingErrorProcessor extends DefaultBindingErrorProcessor {

	public void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult) {
		// Create field error with the exceptions's code, e.g. "typeMismatch".
		super.processPropertyAccessException(ex, bindingResult);
		Object rejectedValue = ex.getValue();
		if (!(rejectedValue == null || rejectedValue.equals(""))) {
			if (ex.getCause() instanceof FormatException) {
				GlobalVariables.getMessageMap().putError(ex.getPropertyName(), ((FormatException)ex.getCause()).getErrorKey(),
						new String[] {rejectedValue.toString()});
			}else{
				GlobalVariables.getMessageMap().putError(ex.getPropertyName(), RiceKeyConstants.ERROR_CUSTOM,
						new String[] {"Invalid format"});
			}
		}
	}

}
