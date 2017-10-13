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
package org.kuali.rice.krms.impl.repository.language;

import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinitionContract;

/**
 * This class translates requirement components and statements into 
 * natural language.
 */
public interface NaturalLanguageTranslator {
	/**
	 * Translates a requirement component for a specific natural language 
	 * usuage type (context) into natural language.
	 * 
	 * @param proposition Proposition to be translated
	 * @param nlUsageTypeKey Natural language usage type key (context)
	 * @return Natural language requirement translation
	 * @throws org.kuali.student.r2.common.exceptions.DoesNotExistException Requirement component id does not exists
	 * @throws org.kuali.student.r2.common.exceptions.OperationFailedException
	 */
	public String translateProposition(PropositionDefinitionContract proposition, String nlUsageTypeKey) throws RiceIllegalStateException;

	/**
	 * Translates a requirement component for a specific natural language
	 * usuage type (context) and language locale (e.g. 'en' for English,
	 * 'de' for German) into natural language.
	 *
	 * @param proposition Proposition to be translated
	 * @param nlUsageTypeKey Natural language usage type key (context)
	 * @param language Translation language
	 * @return
	 * @throws org.kuali.student.r2.common.exceptions.DoesNotExistException
	 * @throws org.kuali.student.r2.common.exceptions.OperationFailedException
	 */
	public String translateProposition(PropositionDefinitionContract proposition, String nlUsageTypeKey, String language) throws RiceIllegalStateException;


}
