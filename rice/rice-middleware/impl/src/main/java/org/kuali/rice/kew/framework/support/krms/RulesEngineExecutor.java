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
package org.kuali.rice.kew.framework.support.krms;

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.krms.api.engine.Engine;
import org.kuali.rice.krms.api.engine.EngineResults;

/**
 * Serves as an interface to execution of a rules engine during execution of the workflow engine.  Applications that
 * wish to integrate with KRMS from the workflow engine should implement an executor and make it available to the
 * workflow engine in one of two ways:
 *
 * <ol>
 *     <li>Register an extension with KEW (by ingesting a file containing a &lt;ruleAttribute&gt; mapping a name to the
 *     implementation class for the RulesEngineExecutor.  This name can then be referenced within the route node definition.</li>
 *     <li>Simply reference the fully-qualified class name of the RulesEngineExecutor implementation class inside of
 *     the route node definition</li>
 * </ol>
 *
 * <p>In the first case, the route node definition would look similar to the following:</p>
 *
 * <pre>
 * {@code
 * <requests name="MyRulesBasedNode">
 *   <rulesEngine executor="MyRulesEngineExecutor"/>
 * </requests>
 * }
 * </pre>
 *
 * <p>The above assumes that an extension/rule attribute has been defined with the name of "MyRulesEngineExecutor".</p>
 *
 * <p>Alternatively, the fully-qualified class name can be specified directly as follows:</p>
 *
 * <pre>
 * {@code
 * <requests name="MyRulesBasedNode">
 *   <rulesEngine executorClass="MyRulesEngineExecutor"/>
 * </requests>
 * }
 * </pre>
 *
 * <p>TODO - this interface should really be part of the framework module, but depends on RouteContext which is currently
 * part of the impl module.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RulesEngineExecutor {

    EngineResults execute(RouteContext routeContext, Engine engine);

}
