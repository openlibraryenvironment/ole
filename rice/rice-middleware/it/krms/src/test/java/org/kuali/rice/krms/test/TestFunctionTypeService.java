/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krms.test;

import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.framework.engine.Function;
import org.kuali.rice.krms.framework.type.FunctionTypeService;

public class TestFunctionTypeService implements FunctionTypeService {

    @Override
    public Function loadFunction(FunctionDefinition functionDefinition) {
        if (functionDefinition == null) throw new IllegalArgumentException("functionDefinition must not be null");

        if ("gcd".equals(functionDefinition.getName())) {
            return new Function() {
                @Override
                public Object invoke(Object... args) {
                    Integer a = (Integer)args[0];
                    Integer b = (Integer)args[1];
                    
                    return Integer.valueOf(gcd(a,b));
                }
                
                private int gcd(int a, int b) {
                    if (b > a) { int temp=a; a=b; b=temp; } // swap
                    
                    while (b != 0) {
                        int m = a % b;
                        a = b;
                        b = m;
                    }

                    return a;
                }
            };
        }
        
        return null;
    }
}
