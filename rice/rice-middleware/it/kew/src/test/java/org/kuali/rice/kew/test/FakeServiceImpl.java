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
package org.kuali.rice.kew.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A generic, reusable fake service that records invocations, for testing purposes
 * TODO: consider merging with {@link TestServiceInterface}/{@link GenericTestService} in KSB module 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see FakeService
 * @see GenericTestService
 */
public class FakeServiceImpl implements FakeService {
    /**
     * Record of a method invocation
     */
    public static class Invocation {
        public long timestamp;
        public String methodName;
        public List<Object> arguments = new ArrayList<Object>();
        public Object returnValue;
        public Invocation(String methodName) {
            this.timestamp = Calendar.getInstance().getTimeInMillis();
            this.methodName = methodName;
        }
    }

    private List<Invocation> invocations = Collections.synchronizedList(new ArrayList<Invocation>(0));

    /**
     * @return the invocations made up to this point
     */
    public List<Invocation> getInvocations() {
        return invocations;
    }

    /**
     * An arbitrary method
     * @param arg1 doesn't matter
     * @param arg2 doesn't matter
     */
    public void method1(Object arg1, Object arg2) {
        Invocation i = new Invocation("method1");
        i.arguments.add(arg1);
        i.arguments.add(arg2);
        invocations.add(i);
    }

    /**
     * An arbitrary method
     * @param arg1 doesn't matter
     * @return a {@link java.util.Date} instance
     */
    public Object method2(Object arg1) {
        Invocation i = new Invocation("method2");
        i.arguments.add(arg1);
        i.returnValue = new Date();
        invocations.add(i);
        return i.returnValue;
    }
}
