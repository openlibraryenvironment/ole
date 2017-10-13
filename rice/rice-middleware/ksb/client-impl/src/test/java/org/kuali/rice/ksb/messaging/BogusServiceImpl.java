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
package org.kuali.rice.ksb.messaging;

import org.apache.cxf.transport.http.HTTPException;

import java.io.IOException;
import java.net.URL;

/**
 * implementation of BogusService that is instrumented to let us know if the service method was invoked
 */
public class BogusServiceImpl implements BogusService {

    private boolean wasCalled = false;

    /**
     * lets us know if doSomething() was called.
     * @return
     */
    public boolean getWasCalled() {
        return wasCalled;
    }

    /**
     * This method will fail in a way that looks to BusClientFailureProxy like a dead web service endpoint.
     * @return
     * @throws IOException
     */
    @Override
    public String doSomething() throws IOException {
        wasCalled = true;
        throw new HTTPException(404, "Baddy bad", new URL("http://foo.com/"));
    }
}
