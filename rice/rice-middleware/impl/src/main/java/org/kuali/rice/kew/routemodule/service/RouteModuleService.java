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
package org.kuali.rice.kew.routemodule.service;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.routemodule.RouteModule;



/**
 * A service for locating Route Modules.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RouteModuleService {
    
    public RouteModule findRouteModule(RouteNode node) throws ResourceUnavailableException;
    
    public RouteModule findRouteModule(ActionRequestValue actionRequest) throws ResourceUnavailableException;
    
}
