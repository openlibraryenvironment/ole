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
package org.kuali.rice.core.api.util.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node of the Tree<T, K> class. The Node<T, K> is also a container, and
 * can be thought of as instrumentation to determine the location of the type T
 * in the Tree<T ,K>.
 */
public class Node<T, K> implements Serializable {
    private static final long serialVersionUID = -2650587832812603561L;

    private T data;
    private List<Node<T, K>> children;

    private K nodeLabel;
    private String nodeType;

    /**
     * Default constructor.
     */
    public Node() {
        super();

        children = new ArrayList<Node<T, K>>();
    }

    /**
     * Convenience constructor to create a Node<T, K> with an instance of T.
     *
     * @param data an instance of T.
     */
    public Node(T data) {
        this();
        setData(data);
    }
    
    /**
     * Convenience constructor to create a Node<T, K> with an instance of T and K.
     *
     * @param data an instance of T.
     */
    public Node(T data, K label) {
        this();
        setData(data);
        setNodeLabel(label);
    }

    /**
     * Return the children of Node<T, K>. The Tree<T> is represented by a single
     * root Node<T, K> whose children are represented by a List<Node<T, K>>. Each of
     * these Node<T, K> elements in the List can have children. The getChildren()
     * method will return the children of a Node<T, K>.
     *
     * @return the children of Node<T, K>
     */
    public List<Node<T, K>> getChildren() {
        if (this.children == null) {
            return new ArrayList<Node<T, K>>();
        }
        return this.children;
    }

    /**
     * Sets the children of a Node<T, K> object. See docs for getChildren() for
     * more information.
     *
     * @param children the List<Node<T, K>> to set.
     */
    public void setChildren(List<Node<T, K>> children) {
        this.children = children;
    }

    /**
     * Returns the number of immediate children of this Node<T, K>.
     *
     * @return the number of immediate children.
     */
    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    /**
     * Adds a child to the list of children for this Node<T, K>. The addition of
     * the first child will create a new List<Node<T, K>>.
     *
     * @param child a Node<T, K> object to set.
     */
    public void addChild(Node<T, K> child) {
        if (children == null) {
            children = new ArrayList<Node<T, K>>();
        }
        children.add(child);
    }

    /**
     * Inserts a Node<T, K> at the specified position in the child list. Will
     * throw an ArrayIndexOutOfBoundsException if the index does not exist.
     *
     * @param index the position to insert at.
     * @param child the Node<T, K> object to insert.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void insertChildAt(int index, Node<T, K> child) throws IndexOutOfBoundsException {
        if (index == getNumberOfChildren()) {
            // this is really an append
            addChild(child);
            return;
        } else {
            children.get(index); //just to throw the exception, and stop here
            children.add(index, child);
        }
    }

    /**
     * Remove the Node<T, K> element at index index of the List<Node<T, K>>.
     *
     * @param index the index of the element to delete.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    /**
     * Data object contained in the node (a leaf)
     *
     * @return Object
     */
    public T getData() {
        return this.data;
    }

    /**
     * Setter for the nodes data
     *
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Object containing the data for labeling the node (can be simple String)
     *
     * @return K
     */
    public K getNodeLabel() {
        return nodeLabel;
    }

    /**
     * Setter for the nodes label data
     *
     * @param nodeLabel
     */
    public void setNodeLabel(K nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    /**
     * Indicates what type of node is being represented, used to give
     * a functional label for the node that can also be used for presentation
     * purposes
     *
     * @return String node type
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Setter for the node type String
     *
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String toString() {
        if (getData() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("{").append(getData().toString()).append(",[");
            int i = 0;
            for (Node<T, K> e : getChildren()) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(e.getData().toString());
                i++;
            }
            sb.append("]").append("}");
            return sb.toString();
        } else {
            return super.toString();
        }
    }
}

