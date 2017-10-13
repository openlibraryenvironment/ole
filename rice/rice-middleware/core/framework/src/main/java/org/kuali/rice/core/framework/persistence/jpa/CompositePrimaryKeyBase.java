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
package org.kuali.rice.core.framework.persistence.jpa;

import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public abstract class CompositePrimaryKeyBase implements Serializable {

    public boolean equals(Object o) {
        Class thisClass = (this.getClass());
        Class otherClass = thisClass.cast(o).getClass();

        if (o == this) return true;
        if (!(thisClass.isInstance(o))) return false;
        if (o == null) return false;
        
        Method[] methods = thisClass.getMethods();
        for (Method method : methods) {
            if (isGetter(method)) {
                try {
                     if (method.invoke(this) != null) {
                        if (method.getReturnType().isInstance(Object.class)                      
                            && !method.invoke(this).equals(method.invoke(o))) {
                             return false;
                         } else if (method.invoke(this) != method.invoke(o)) {
                             return false;
                         }
                    } else {
                        return false;
                    }
                }
                catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    return false;
                }
                catch (SecurityException ex) {
                    ex.printStackTrace();
                    return false;
                }
                catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                    return false;
                }
                catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Class thisClass = (this.getClass());
        
        Method[] methods = thisClass.getMethods();
        
        //need to sort methods so we can be sure they are ordered correctly every time.
        Arrays.sort(methods, new Comparator() {
            public int compare(Object a, Object b) {
                return ((Method)a).getName().compareTo(((Method)b).getName());
            }
        });
        for (Method method : methods) {
            if (isGetter(method)) {
                try {
                    hashCodeBuilder.append(method.invoke(this));
                }
                catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
                catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                } 
            } 
        }
        
        return hashCodeBuilder.toHashCode();
    }
    
    protected static boolean isGetter(Method method){
        if(!(method.getName().startsWith("get") 
                || method.getName().startsWith("is"))) {
            return false;
        }
        if(method.getParameterTypes().length != 0) {
            return false;  
        }
        if(void.class.equals(method.getReturnType())) { 
            return false;
        }
        
        return true;
      }
    
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	
    	try {
			for (Field field : this.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				
				if (field.isAnnotationPresent(Id.class)) {
					s.append(field.getName());
					s.append(": ");
					s.append(field.get(this));
					s.append(", ");
				}
			}
			
			return s.substring(0, s.length() - 2).toString();
		} catch (Exception e) {
			return s.toString();
		}
    }
    
}