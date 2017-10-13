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
package org.kuali.rice.kew.engine.node;

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;

/**
 * A simple {@link Node} implementation which is executed and returns a {@link SimpleResult}
 * indicating whether or not the node has completed.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface SimpleNode extends Node {

	public SimpleResult process(RouteContext context, RouteHelper helper) throws Exception;
	
}
