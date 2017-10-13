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
package org.kuali.rice.coreservice.framework.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public final class ParameterConstants {
    public static final String ALL_COMPONENT = "All";
    public static final String DOCUMENT_COMPONENT = "Document";
    public static final String LOOKUP_COMPONENT = "Lookup";
    public static final String BATCH_COMPONENT = "Batch";

    @Retention(RetentionPolicy.RUNTIME)
    @Target( { ElementType.TYPE })
    public static @interface NAMESPACE {
        String namespace();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target( { ElementType.TYPE })
    public static @interface COMPONENT {
        String component();
    }

	private ParameterConstants() {
		throw new UnsupportedOperationException("do not call");
	}
}
