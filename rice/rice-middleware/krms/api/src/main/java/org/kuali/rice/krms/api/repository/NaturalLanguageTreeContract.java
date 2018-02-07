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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.rice.krms.api.repository;

import java.util.List;

/**
 * Natural Language representation of a proposition
 * 
 * @see NaturalLanguageTree
 */
public interface NaturalLanguageTreeContract {

    /**
     * Returns the natural language representation for this node in the tree
     * 
     * @return the natural language representation for this node in the tree
     */
    String getNaturalLanguage();
    
    /**
     * Returns the natural language for children of this node
     * 
     * @return the natural language for children of this node
     */
    List<? extends NaturalLanguageTreeContract> getChildren();

    
}
