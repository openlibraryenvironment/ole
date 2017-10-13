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
package org.kuali.rice.kew.engine.node.hierarchyrouting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeConfigParam;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.rule.NamedRuleSelector;
import org.kuali.rice.kew.util.Utilities;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * A simple hierarchy provider that provides hierarchy based on doc content
 * <pre>
 * stop id="..." recipient="..." type="..."
 *     stop ...
 *     stop ...
 *       stop ...
 * </pre>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimpleHierarchyProvider implements HierarchyProvider {
    private static final Logger LOG = Logger.getLogger(SimpleHierarchyProvider.class);

    /**
     * Simple implementation of Stop.  Contains stop recipient type,
     * recipient string, and string id, and maintains pointers to
     * parent and children stops.
     */
    static class SimpleStop implements Stop {
        private static enum RecipientType { USER, WORKGROUP };
        public SimpleStop parent;
        public List<SimpleStop> children = new ArrayList<SimpleStop>();
        public RecipientType type;
        public String recipient;
        public String id;

        public String toString() {
            return new ToStringBuilder(this).append("id", id)
                                            .append("recipient", recipient)
                                            .append("type", type)
                                            .append("parent", parent == null ? null : parent.id)
                                            .append("children", StringUtils.join(CollectionUtils.collect(children, new Transformer() {
                                                        public Object transform(Object o) { return ((SimpleStop) o).id; }
                                                    })
                                                    , ','))
                                            .toString();
                                                    
        }
        
        public boolean equals(Object o) {
            if (!(o instanceof SimpleStop)) return false;
            return id.equals(((SimpleStop) o).id);
        }
        
        public int hashCode() {
            return ObjectUtils.hashCode(id);
        }
    }

    /**
     * The root stop
     */
    private SimpleStop root;
    /**
     * Map of Stop id-to-Stop instance
     */
    private Map<String, SimpleStop> stops = new HashMap<String, SimpleStop>();

    public void init(RouteNodeInstance nodeInstance, RouteContext context) {
        init(context.getDocumentContent().getDocument().getDocumentElement());
    }

    /**
     * This constructor can be used in tests
     * @param element the root Element of the hierarchy XML
     */
    public void init(Element element) {
        Element rootStop = findRootStop(element);
        root = parseStops(rootStop, null);
    }

    /**
     * @param e the element at which to start the search
     * @return the first stop element encountered
     * @throws RuntimeException if no stop elements were encountered 
     */
    protected Element findRootStop(Element e) {
        if ("stop".equals(e.getNodeName())) return e;
        NodeList nl = e.getElementsByTagName("stop");
        if (nl == null || nl.getLength() == 0) {
            throw new RuntimeException("No stops found");
        }
        return (Element) nl.item(0);
    }

    /**
     * Parses a hierarchy of stop elements recursively, and populates the stops Map.
     * @param e a stop element
     * @param parent the parent of the current element (if any)
     * @return the SimpleStop instance for the initial element
     */
    protected SimpleStop parseStops(Element e, SimpleStop parent) {
        LOG.debug("parsing element: " + e + " parent: " + parent);
        SimpleStop stop = parseStop(e);
        LOG.debug("parsed stop: "+ stop);
        stop.parent = parent;
        if (parent != null) {
            parent.children.add(stop);
        }
        stops.put(e.getAttribute("id"), stop);
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            org.w3c.dom.Node n = nl.item(i);
            if (!(n instanceof Element)) continue;
            parseStops((Element) n, stop);
        }
        return stop;
    }

    /**
     * Parses stop info from a stop element
     * @param e the stop element
     * @return a SimpleStop initialized with attribute/property information
     */
    protected SimpleStop parseStop(Element e) {
        SimpleStop ss = new SimpleStop();
        String recipient = e.getAttribute("recipient");
        String type = e.getAttribute("type");
        String id = e.getAttribute("id");
        if (id == null) {
            throw new RuntimeException("malformed document content, missing id attribute: " + e);
        }
        /* make optional, since ruleselector/rules can govern this ? */
        /*
        if (recipient == null) {
            throw new RuntimeException("malformed document content, missing recipient attribute: " + e);
        }
        if (type == null) {
            throw new RuntimeException("malformed document content, missing type attribute: " + e);
        }
        */
        ss.id = id;
        ss.recipient = recipient;

        if (!StringUtils.isEmpty(type)) {
            SimpleStop.RecipientType rtype = SimpleStop.RecipientType.valueOf(type.toUpperCase());
            ss.type = rtype;
        }

        return ss;
    }

    /* Returns the list of stops in the stops Map which have 0 children */
    public List<Stop> getLeafStops(RouteContext context) {
        List<Stop> leafStops = new ArrayList<Stop>();
        for (SimpleStop stop: stops.values()) {
            if (stop.children.size() == 0) {
                leafStops.add(stop);
            }
        }
        return leafStops;
    }

    /* Looks up a stop in the stops map by identifier */
    public Stop getStopByIdentifier(String stopId) {
        return stops.get(stopId);
    }

    /* Returns the identifier for the specified stop */
    public String getStopIdentifier(Stop stop) {
        return ((SimpleStop) stop).id;
    }

    public boolean hasStop(RouteNodeInstance nodeInstance) {
        return nodeInstance.getNodeState("id") != null;
    }
    
    public void setStop(RouteNodeInstance requestNodeInstance, Stop stop) {
        SimpleStop ss = (SimpleStop) stop;
        requestNodeInstance.addNodeState(new NodeState("id", getStopIdentifier(stop)));
        //requestNodeInstance.addNodeState(new NodeState(KewApiConstants.RULE_SELECTOR_NODE_STATE_KEY, "named"));
        //requestNodeInstance.addNodeState(new NodeState(KewApiConstants.RULE_NAME_NODE_STATE_KEY, "NodeInstanceRecipientRule"));
        if (ss.recipient != null) {
            requestNodeInstance.addNodeState(new NodeState("recipient", ((SimpleStop) stop).recipient));
        }
        if (ss.type != null) {
            requestNodeInstance.addNodeState(new NodeState("type", ((SimpleStop) stop).type.name().toLowerCase()));
        }
    }
    
    public boolean equals(Stop a, Stop b) {
        return ObjectUtils.equals(a, b);
    }

    public Stop getParent(Stop stop) {
        return ((SimpleStop) stop).parent;
    }

    public boolean isRoot(Stop stop) {
        return equals(stop, root);
    }

    public Stop getStop(RouteNodeInstance nodeInstance) {
        NodeState state = nodeInstance.getNodeState("id");
        if (state == null) {
            //return null;
            throw new RuntimeException();
        } else {
            LOG.warn("id Node state on nodeinstance " + nodeInstance + ": " + state);
            return stops.get(state.getValue());
        }
    }

    /* Propagates the rule selector and rule name from the hierarchy node to the request node */
    public void configureRequestNode(RouteNodeInstance hierarchyNodeInstance, RouteNode node) {
        Map<String, String> cfgMap = Utilities.getKeyValueCollectionAsMap(node.getConfigParams());
        // propagate rule selector and name from hierarchy node
        if (!cfgMap.containsKey(RouteNode.RULE_SELECTOR_CFG_KEY)) {
            Map<String, String> hierarchyCfgMap = Utilities.getKeyValueCollectionAsMap(hierarchyNodeInstance.getRouteNode().getConfigParams());
            node.getConfigParams().add(new RouteNodeConfigParam(node, RouteNode.RULE_SELECTOR_CFG_KEY, hierarchyCfgMap.get(RouteNode.RULE_SELECTOR_CFG_KEY)));
        }
        if (!cfgMap.containsKey(NamedRuleSelector.RULE_NAME_CFG_KEY)) {
            Map<String, String> hierarchyCfgMap = Utilities.getKeyValueCollectionAsMap(hierarchyNodeInstance.getRouteNode().getConfigParams());
            node.getConfigParams().add(new RouteNodeConfigParam(node, NamedRuleSelector.RULE_NAME_CFG_KEY, hierarchyCfgMap.get(NamedRuleSelector.RULE_NAME_CFG_KEY)));
        }
    }
}
