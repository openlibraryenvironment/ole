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
package org.kuali.rice.krad.lookup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * A {@link Collection} that may be truncated
 *
 * @param <T> The type that the collection stores internally
 */
public class CollectionIncomplete<T> implements List<T>, RandomAccess, Serializable {

    private static final long serialVersionUID = 8683452581122892189L;
	private final List<T> list;
    private Long actualSizeIfTruncated;


    /**
     * Collection that may be incomplete/truncated
     *
     * @param collection
     * @param actualSizeIfTruncated the actual collection size before truncation.  Zero if the collection is not truncated.
     */
    public CollectionIncomplete(Collection<T> collection, Long actualSizeIfTruncated) {
        super();
        this.list = new ArrayList<T>(collection);
        this.actualSizeIfTruncated = actualSizeIfTruncated;
    }

    /**
     * @see java.util.List#add(int, Object)
     */
    public void add(int arg0, T arg1) {
        list.add(arg0, arg1);
    }

    /**
     * @see java.util.List#add(Object)
     */
    public boolean add(T arg0) {
        return list.add(arg0);
    }

    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int arg0, Collection<? extends T> arg1) {
        return list.addAll(arg0, arg1);
    }

    /**
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends T> arg0) {
        return list.addAll(arg0);
    }

    /**
     * @see java.util.List#clear()
     */
    public void clear() {
        list.clear();
    }

    /**
     * @see java.util.List#contains(Object)
     */
    public boolean contains(Object arg0) {
        return list.contains(arg0);
    }

    /**
     *  @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> arg0) {
        return list.containsAll(arg0);
    }

    /**
     * @see java.util.List#equals(Object)
     */
    public boolean equals(Object arg0) {
        return list.equals(arg0);
    }

    /**
     * @see java.util.List#get(int)
     */
    public T get(int arg0) {
        return list.get(arg0);
    }

    /**
     * @see java.util.List#hashCode()
     */
    public int hashCode() {
        return list.hashCode();
    }

    /**
     * @see java.util.List#indexOf(Object)
     */
    public int indexOf(Object arg0) {
        return list.indexOf(arg0);
    }

    /**
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * @see java.util.List#iterator()
     */
    public Iterator<T> iterator() {
        return list.iterator();
    }

    /**
     * @see java.util.List#lastIndexOf(Object)
     */
    public int lastIndexOf(Object arg0) {
        return list.lastIndexOf(arg0);
    }

    /**
     * @see java.util.List#listIterator()
     */
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    /**
     * @see java.util.List#listIterator(int)
     */
    public ListIterator listIterator(int arg0) {
        return list.listIterator(arg0);
    }

    /**
     * @see java.util.List#remove(int)
     */
    public T remove(int arg0) {
        return list.remove(arg0);
    }

    /**
     * @see java.util.List#remove(Object)
     */
    public boolean remove(Object arg0) {
        return list.remove(arg0);
    }

    /**
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> arg0) {
        return list.removeAll(arg0);
    }

    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> arg0) {
        return list.retainAll(arg0);
    }

    /**
     * @see java.util.List#set(int, Object)
     */
    public T set(int arg0, T arg1) {
        return list.set(arg0, arg1);
    }

    /**
     * @see java.util.List#size()
     */
    public int size() {
        return list.size();
    }

    /**
     * @see java.util.List#subList(int, int)
     */
    public List<T> subList(int arg0, int arg1) {
        return list.subList(arg0, arg1);
    }

    /**
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return list.toArray();
    }

    /**
     * @see java.util.List#toArray(Object[])
     */
    public <T> T[] toArray(T[] arg0) {
        return list.toArray(arg0);
    }

    /**
     * @see java.util.List#toString()
     */
    public String toString() {
        return list.toString();
    }

    /**
     * Get the actual collection size if the collection was truncated
     *
     * <p>
     * For non-truncated collection the <code>getActualSizeIfTruncated</code> is zero.
     * </p>
     *
     * @return Returns the actualSizeIfTruncated.
     */
    public Long getActualSizeIfTruncated() {
        return actualSizeIfTruncated;
    }

    /**
     * Set the actual collection size if the collection was truncated
     *
     * @param actualSizeIfTruncated The actualSizeIfTruncated to set.
     */
    public void setActualSizeIfTruncated(Long actualSizeIfTruncated) {
        this.actualSizeIfTruncated = actualSizeIfTruncated;
    }
}
