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
package org.kuali.rice.kew.engine.node.hierarchyrouting;

import java.util.List;

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;


/**
 * HierarchyProvider is responsible for exposing the hierarchy that the HierarchyRoutingNode
 * climbs/descends.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface HierarchyProvider {
    /**
     * Marker interface for objects the represent a "stop" or "node" in a hierarchy.
     * E.g. "unit"
     */
    public interface Stop { }

    /**
     * Initializer for the hierarchy provider.
     * @param nodeInstance the HierarchyRouteNode instance
     * @param context the RouteContext (nodeInstance may NOT be the HierarchyRouteNode instance; e.g. when transitioning into)
     */
    public void init(RouteNodeInstance nodeInstance, RouteContext context);
    
    /**
     * Find all leaf stops in the xml and convert them into a list of Stop objects
     * @param context the RouteContext
     * @return List Stop objects
     */
    public List<Stop> getLeafStops(RouteContext context);

    /**
     * @param nodeInstance the node instance
     * @return whether stop state is associated with the specified node instance
     */
    public boolean hasStop(RouteNodeInstance nodeInstance);

    /**
     * Returns the Stop at the specified routeNodeInstance, or null if the node instance
     * is not associated with a Stop
     * @param nodeInstance the node instance to check
     * @return the Stop at the route node instance
     */
    public Stop getStop(RouteNodeInstance nodeInstance);

    /**
     * Set any state necessary on the request node instance for a given stop.  E.g. for chart/org routing
     * set the org and chart in the node state
     * @param requestNodeInstance the request node instance
     * @param stop the stop for the request node
     */
    public void setStop(RouteNodeInstance requestNodeInstance, Stop stop);

    /**
     * @param stop the stop
     * @return a a string that can be used to uniquely identify the stop.  E.g. for chart/org routing,
     * the chart and org
     */
    public String getStopIdentifier(Stop stop);

    /**
     * @param stopId the stop identifier
     * @return the Stop by stop identifier
     */
    public Stop getStopByIdentifier(String stopId);
    

    /**
     * @param stop a stop
     * @return the parent stop of the specified stop
     */
    public Stop getParent(Stop stop);

    /**
     * @param stop the stop
     * @return whether the given stop is the root stop, i.e. has no parents
     */
    public boolean isRoot(Stop stop);
    
    /**
     * @param a one stop
     * @param b another stop
     * @return whether stops are equivalent
     */
    public boolean equals(Stop a, Stop b);

    /**
     * Configures the single request node definition/prototype used for all node instances
     * @param hiearchyNodeInstance the hierarchy node instance
     * @param node the request node definition/prototype
     */
    public void configureRequestNode(RouteNodeInstance hiearchyNodeInstance, RouteNode node);
}
