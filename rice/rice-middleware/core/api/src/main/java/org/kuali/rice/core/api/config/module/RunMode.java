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
package org.kuali.rice.core.api.config.module;

/**
 * Defines different modes of operation for a module of Kuali Rice.  It is up to the implementation of the individual
 * modules to specify which run modes they support.  Additionally speaking, while each run mode has a general meaning,
 * individual modules will specify what each of the allowable run modes means for the configuration of the module.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public enum RunMode {
    LOCAL, EMBEDDED, REMOTE, THIN
}
