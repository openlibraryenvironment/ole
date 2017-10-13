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
package org.kuali.rice.kew.exception;

import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.engine.RouteContext;


/**
 * Thrown from the engine when a problem is encountered.  Wraps a {@link RouteContext}
 * which contains information about the state of the engine at the time the
 * problem was encountered.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RouteManagerException extends WorkflowRuntimeException {

    private static final long serialVersionUID = -7957245610622538745L;

    private RouteContext routeContext;
    
    public RouteManagerException(String message) {
        super(message);
    }
    
    public RouteManagerException(String message, RouteContext routeContext) {
        super(message);
        this.routeContext = routeContext;
    }
    
    public RouteManagerException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public RouteManagerException(String message, Throwable throwable, RouteContext routeContext) {
        super(message, throwable);
        this.routeContext = routeContext;
    }
    
    public RouteManagerException(Throwable throwable, RouteContext routeContext) {
        super(throwable);
        this.routeContext = routeContext;
    }
        
    public RouteContext getRouteContext() {
        return routeContext;
    }

    public void setRouteContext(RouteContext routeContext) {
        this.routeContext = routeContext;
    }
    
}
