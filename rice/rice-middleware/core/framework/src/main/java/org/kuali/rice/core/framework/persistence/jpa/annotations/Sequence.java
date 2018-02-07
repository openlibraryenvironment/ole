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
package org.kuali.rice.core.framework.persistence.jpa.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mimicing auto-incrementing fields.  Currently, only one auto-
 * incrementing field is allowed per entity, but could be modified in the future
 * if required (just add a @Sequences annotation that help an array of @Sequence
 * annotations). This is used in conjunction with the Kuali sequence managers rather
 * than the standard JPA generated value annotations. The reason is that Kuali 
 * uses a sequence like algorithm for all supported databases (mysql, oracle) 
 * rather than identity or auto-incrementing fields.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Sequence {
	String name();
	String property();
}