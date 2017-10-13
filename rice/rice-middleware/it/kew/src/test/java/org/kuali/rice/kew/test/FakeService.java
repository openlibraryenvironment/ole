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

import java.util.List;

import org.kuali.rice.kew.test.FakeServiceImpl.Invocation;

/**
 * Interface for a fake service
 * TODO: consider merging with {@link TestServiceInterface}/{@link GenericTestService} in KSB module 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see FakeServiceImpl
 * @see TestServiceInterface
 */
public interface FakeService {
    /**
     * @return the invocations made up to this point
     */
    public List<Invocation> getInvocations();

    /**
     * An arbitrary method
     * @param arg1 doesn't matter
     * @param arg2 doesn't matter
     */
    public void method1(Object arg1, Object arg2);

    /**
     * An arbitrary method
     * @param arg1 doesn't matter
     * @return a {@link java.util.Date} instance
     */
    public Object method2(Object arg1);
}
