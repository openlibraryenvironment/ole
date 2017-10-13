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
package org.kuali.rice.kew.engine;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kew.engine.OrchestrationConfig.EngineCapability;
import org.kuali.rice.kew.engine.simulation.SimulationEngine;

import static org.junit.Assert.*;

/**
 * Unit tests for the WorkflowEngineFactoryImpl class. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkflowEngineFactoryImplTest {

    private WorkflowEngineFactory factory;
    
    @Before
    public void setup() {
        factory = new WorkflowEngineFactoryImpl();
    }
    
    @Test
    public void standardEngineCreate() {
        OrchestrationConfig config = new OrchestrationConfig(EngineCapability.STANDARD);
        WorkflowEngine workflowEngine = factory.newEngine(config);
        
        assertNotNull(workflowEngine);
        assertFalse(workflowEngine instanceof BlanketApproveEngine);
        assertFalse(workflowEngine instanceof SimulationEngine);
        assertTrue(workflowEngine instanceof StandardWorkflowEngine);
    }
    
    @Test
    public void blanketApproveEngineCreate() {
        OrchestrationConfig config = new OrchestrationConfig(EngineCapability.BLANKET_APPROVAL);
        WorkflowEngine workflowEngine = factory.newEngine(config);
        
        assertNotNull(workflowEngine);
        assertTrue(workflowEngine instanceof BlanketApproveEngine);
        assertFalse(workflowEngine instanceof SimulationEngine);
    }
    
    @Test
    public void simulationEngineCreate() {
        OrchestrationConfig config = new OrchestrationConfig(EngineCapability.SIMULATION);
        WorkflowEngine workflowEngine = factory.newEngine(config);
        
        assertNotNull(workflowEngine);
        assertFalse(workflowEngine instanceof BlanketApproveEngine);
        assertTrue(workflowEngine instanceof SimulationEngine);
    }
    
    @Test
    public void standardEngineRunPostProcessLogic() {
        OrchestrationConfig config = new OrchestrationConfig(EngineCapability.STANDARD, true);
        WorkflowEngine workflowEngine = factory.newEngine(config);
        assertTrue(((StandardWorkflowEngine)workflowEngine).isRunPostProcessorLogic());
    }
    
    @Test
    public void standardEngineDoNotRunPostProcessLogic() {
        OrchestrationConfig config = new OrchestrationConfig(EngineCapability.STANDARD, false);
        WorkflowEngine workflowEngine = factory.newEngine(config);
        assertFalse(((StandardWorkflowEngine)workflowEngine).isRunPostProcessorLogic());
    }
    
}
