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

import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * A typesafe enumeration defining the various types of Nodes in the Workflow Engine.
 * This class was added to aid in unifying the node type concept across multiple
 * components within the system.  It also defines type hierarchys for the various
 * node types.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NodeType {

    private static final String SIMPLE_NAME = "simple";
    private static final String JOIN_NAME = "join";
    private static final String SPLIT_NAME = "split";
    private static final String SUB_PROCESS_NAME = "process";
    private static final String DYNAMIC_NAME = "dynamic";
    private static final String START_NAME = "start";
    private static final String REQUEST_ACTIVATION_NAME = "requestActivation";
    private static final String REQUESTS_NAME = "requests";
    private static final String ROLE_NAME = "role";

    /**
     * A Sorted List containing the NodeTypes in order sorted by largest extension depth first, i.e. in bottom-up extension order.
     */
    private static final List typeList = new ArrayList();
    private static final Comparator depthComparator = new ExtensionDepthComparator();


    // primitive node types
    public static final NodeType SIMPLE = new NodeType(SIMPLE_NAME, SimpleNode.class, null);
    public static final NodeType SPLIT = new NodeType(SPLIT_NAME, SplitNode.class, SimpleSplitNode.class);
    public static final NodeType JOIN = new NodeType(JOIN_NAME, JoinNode.class, SimpleJoinNode.class);
    public static final NodeType SUB_PROCESS = new NodeType(SUB_PROCESS_NAME, SubProcessNode.class, SimpleSubProcessNode.class);
    public static final NodeType DYNAMIC = new NodeType(DYNAMIC_NAME, DynamicNode.class, null);

    // derivative node types
    public static final NodeType REQUEST_ACTIVATION = new NodeType(SIMPLE, REQUEST_ACTIVATION_NAME, RequestActivationNode.class, RequestActivationNode.class);
    public static final NodeType START = new NodeType(REQUEST_ACTIVATION, START_NAME, InitialNode.class, InitialNode.class);
    public static final NodeType REQUESTS = new NodeType(REQUEST_ACTIVATION, REQUESTS_NAME, RequestsNode.class, RequestsNode.class);
    public static final NodeType ROLE = new NodeType(REQUESTS, ROLE_NAME, RoleNode.class, RoleNode.class);

    private NodeType extensionBase;
    private String name;
    private Class baseClass;
    private Class defaultClass;
    private int extensionDepth = 0;

    private NodeType(String name, Class baseClass, Class defaultClass) {
        this(null, name, baseClass, defaultClass);
    }

    private NodeType(NodeType extensionBase, String name, Class baseClass, Class defaultClass) {
        this.extensionBase = extensionBase;
        this.name = name;
        this.baseClass = baseClass;
        this.defaultClass = defaultClass;
        while (extensionBase != null) {
            extensionDepth++;
            extensionBase = extensionBase.getExtensionBase();
        }
        typeList.add(this);
        // keep the list sorted
        Collections.sort(typeList, depthComparator);
    }

    public NodeType getExtensionBase() {
        return extensionBase;
    }

    public Class getBaseClass() {
        return baseClass;
    }

    public Class getDefaultClass() {
        return defaultClass;
    }

    public String getName() {
        return name;
    }

    public int getExtensionDepth() {
        return extensionDepth;
    }

    public boolean isAssignableFrom(Class typeClass) {
        return getBaseClass().isAssignableFrom(typeClass);
    }

    public boolean isAssignableFrom(NodeType type) {
        return getBaseClass().isAssignableFrom(type.getBaseClass());
    }

    public boolean isInstanceOf(Object object) {
    	return ClassLoaderUtils.isInstanceOf(object, getBaseClass());
    }

    public boolean isTypeOf(Class typeClass) {
        return typeClass.isAssignableFrom(getBaseClass());
    }

    public boolean isTypeOf(NodeType type) {
        return type.getBaseClass().isAssignableFrom(getBaseClass());
    }

    public boolean isCustomNode(String className) {
        return getDefaultClass() == null || !getDefaultClass().getName().equals(className);
    }

    public boolean equals(Object object) {
        if (object instanceof NodeType) {
            return ((NodeType)object).name.equals(name);
        }
        return false;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public static NodeType fromClassName(String className) throws ResourceUnavailableException {
        //Class typeClass = SpringServiceLocator.getExtensionService().loadClass(className);
        Object typeObject = GlobalResourceLoader.getResourceLoader().getObject(new ObjectDefinition(className));
        if (typeObject == null) {
        	throw new ResourceUnavailableException("Could not locate the node with the given class name '" + className + "'.");
        }
        //Class typeClass = typeObject.getClass();
    	// depends upon the proper sorting of typeList
        for (Iterator iterator = typeList.iterator(); iterator.hasNext();) {
            NodeType type = (NodeType) iterator.next();
            //if (type.isAssignableFrom(typeClass)) {
            //if (ClassLoaderUtils.isInstanceOf(typeObject, type))
            if (type.isInstanceOf(typeObject)) {
                return type;
            }
        }
        return null;
    }

    public static NodeType fromNode(RouteNode node) throws ResourceUnavailableException {
        return fromClassName(node.getNodeType());
    }

    public static NodeType fromNodeInstance(RouteNodeInstance nodeInstance) throws ResourceUnavailableException {
        return fromNode(nodeInstance.getRouteNode());
    }

    /**
     * Sorts in descending extension-depth order.
     */
    private static class ExtensionDepthComparator implements Comparator {

        public int compare(Object object1, Object object2) {
            NodeType type1 = (NodeType)object1;
            NodeType type2 = (NodeType)object2;
            if (type1.getExtensionDepth() > type2.getExtensionDepth()) {
                return -1;
            } else if (type1.getExtensionDepth() < type2.getExtensionDepth()) {
                return 1;
            }
            return 0;
        }

    }

    public static List<NodeType> getTypeList(){
    	return typeList;
    }

}
