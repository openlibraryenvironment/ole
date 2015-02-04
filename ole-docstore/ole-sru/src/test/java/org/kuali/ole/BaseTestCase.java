/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.net.URL;

/**
 * User: Peri Subrahmanya
 * Date: 3/30/11
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseTestCase {

       public BaseTestCase() {
        try {
            System.getProperties().put("app.environment", "local");
           }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {

    }
}
