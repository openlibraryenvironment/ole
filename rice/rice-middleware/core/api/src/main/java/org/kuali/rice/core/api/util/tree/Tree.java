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
 * Represents a Tree of Objects of generic type T. The Tree is represented as
 * a single rootElement which points to a List<Node<T>> of children. There is
 * no restriction on the number of children that a particular node may have.
 * This Tree provides a method to serialize the Tree into a List by doing a
 * pre-order traversal. It has several methods to allow easy updates of Nodes
 * in the Tree.
 */
public class Tree<T, K> implements Serializable {
    private static final long serialVersionUID = -8335139832978783512L;

    private Node<T, K> rootElement;

    /**
     * Default constructor.
     */
    public Tree() {
        super();
    }

    /**
     * Return the root Node of the tree.
     *
     * @return the root element.
     */
    public Node<T, K> getRootElement() {
        return this.rootElement;
    }

    /**
     * Set the root Element for the tree.
     *
     * @param rootElement the root element to set.
     */
    public void setRootElement(Node<T, K> rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * Returns the Tree<T> as a List of Node<T, K> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     *
     * @return a List<Node<T, K>>.
     */
    public List<Node<T, K>> toList() {
        List<Node<T, K>> list = new ArrayList<Node<T, K>>();
        walk(rootElement, list);
        return list;
    }

    /**
     * Returns a String representation of the Tree. The elements are generated
     * from a pre-order traversal of the Tree.
     *
     * @return the String representation of the Tree.
     */
    public String toString() {
        return toList().toString();
    }

    /**
     * Walks the Tree in pre-order style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference
     * as it recurses down the tree.
     *
     * @param element the starting element.
     * @param list    the output of the walk.
     */
    private void walk(Node<T, K> element, List<Node<T, K>> list) {
        list.add(element);
        for (Node<T, K> data : element.getChildren()) {
            walk(data, list);
        }
    }
}

