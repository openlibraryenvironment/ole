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
package org.kuali.rice.kew.api.action;

/**
 * Specifies a set of Action codes.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ActionSetContract {

    public boolean hasAction(String actionCode);
    public boolean addAction(String actionCode);
    public boolean removeAction(String actionCode);
    public boolean hasApprove();
    public boolean hasComplete();
    public boolean hasAcknowledge();
    public boolean hasFyi();
    public boolean hasDisapprove();
    public boolean hasCancel();
    public boolean hasRouted();
    public boolean addApprove();
    public boolean addComplete();
    public boolean addAcknowledge();
    public boolean addFyi();
    public boolean addDisapprove();    
    public boolean addCancel();
    public boolean addRouted();
    
}
