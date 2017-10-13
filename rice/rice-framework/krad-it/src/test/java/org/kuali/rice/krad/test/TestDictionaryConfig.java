/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for triggering the loading of a test data dictionary instance
 *
 * <p>
 * When enabled on a @{link KRADTestCase} a data dictionary instance will be created and populated with the
 * files listed in the 'testDataDictionary' bean. Additional files for the test can be given by configuring
 * the annotation property dataDictionaryFiles
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TestDictionaryConfig {
    // namespace the dictionary beans should be associated with
    String namespaceCode();

    // list of dictionary files (separated by a comma) that will be loaded in the test dictionary
    String dataDictionaryFiles() default "";
}
