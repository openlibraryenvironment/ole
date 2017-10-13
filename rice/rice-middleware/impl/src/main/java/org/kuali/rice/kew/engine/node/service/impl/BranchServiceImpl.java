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
package org.kuali.rice.kew.engine.node.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.dao.BranchDAO;
import org.kuali.rice.kew.engine.node.service.BranchService;



public class BranchServiceImpl implements BranchService {
    private static final Logger LOG = Logger.getLogger(BranchServiceImpl.class);

    private BranchDAO branchDAO;
    
    public void save(Branch branch){
        getBranchDAO().save(branch);
    }
    
     public BranchDAO getBranchDAO() {
        return branchDAO;
    }
    public void setBranchDAO(BranchDAO branchDAO) {
        this.branchDAO = branchDAO;
    }
    
    public void deleteBranchStates(List statesToBeDeleted){
        getBranchDAO().deleteBranchStates(statesToBeDeleted);
    }

    /**
     * Walks up the Branch/scope hierarchy trying to find a variable with the specified name
     * @param branch the lowermost branch at which to start the search
     * @param name name of the variable to search for
     * @return a BranchState object in the first Branch/scope in which the variable was found
     */
    private BranchState resolveScopedVariable(Branch branch, String name) {
        Branch b = branch;
        while (b != null) {
            for (BranchState bs: b.getBranchState()) {
                LOG.debug(bs);
            }
            LOG.debug("Resolving variable: '" + name + "' in scope (branch): '" + branch.getName() + "' (" + branch.getBranchId() + ")");
            BranchState bs = b.getBranchState(name);
            if (bs != null) {
                return bs;    
            }
            b = b.getParentBranch();
        }
        return null;
    }

    public String getScopedVariableValue(Branch branch, String name) {
        BranchState bs = resolveScopedVariable(branch, name);
        if (bs != null) return bs.getValue();
        return null;
    }

    public String setScopedVariableValue(Branch branch, String name, String value) {
        LOG.debug("Setting scoped variable value: " + name + " " + value);
        BranchState bs = resolveScopedVariable(branch, name);
        String oldValue = null;
        if (bs == null) {
            LOG.debug("Defining new variable named '" + name + "' at scope '" + branch + "'");
            // create new variable at initial search scope
            bs = new BranchState();
            bs.setKey(name);
            bs.setValue(value);
            bs.setBranch(branch);
            branch.addBranchState(bs);
        } else {
            oldValue = bs.getValue();
            LOG.debug("Replacing old value of variable '" + name + "' (" + oldValue + ") at scope '" + branch + "' with new value: " + value);
            bs.setValue(value);
        }
        // now save the Branch whose state we just modified
        save(bs.getBranch());
        return oldValue;
    }
    
}
