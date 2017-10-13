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
package org.kuali.rice.kew.lookupable;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;
import org.kuali.rice.kew.api.KewApiConstants;


/**
 * A ColumnDecorator for columns in a Lookup which adds an extra non-breaking space if the column
 * is an empty String.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupColumnDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		if (columnValue == null) {
			return KewApiConstants.HTML_NON_BREAKING_SPACE;
		}
		if (columnValue instanceof String && StringUtils.isEmpty(((String)columnValue).trim())) {
			return KewApiConstants.HTML_NON_BREAKING_SPACE;
		}
		return columnValue;
	}

}
