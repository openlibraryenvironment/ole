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
package org.kuali.rice.krad.util;

import org.junit.Test;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiPercent;
import org.kuali.rice.core.web.format.Formatter;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the KualiDecimal methods.
 */
public class KualiPercentTest {
    private static final int OPERAND_VALUE = 25;

    @Test public void testToKualiDecimal() throws Exception {
    	KualiPercent percent1 = new KualiPercent(50);
    	KualiDecimal percentAsDecimal1 = percent1.toKualiDecimal();
    	assertEquals(0.50, percentAsDecimal1.doubleValue(), 0);
    	
    	KualiPercent percent2 = new KualiPercent(new BigDecimal(25.2));
    	KualiDecimal percentAsDecimal2 = percent2.toKualiDecimal();
    	// should round down to 0.25
    	assertEquals(0.25, percentAsDecimal2.doubleValue(), 0);
    	
    	KualiPercent percent3 = new KualiPercent(new BigDecimal(25.7));
    	KualiDecimal percentAsDecimal3 = percent3.toKualiDecimal();
    	// should round up to 0.26
    	assertEquals(0.26, percentAsDecimal3.doubleValue(), 0);
    }

    @Test 
    public void testFormat() throws Exception {
        Formatter testFormatter = Formatter.getFormatter(KualiPercent.class, null);
                
        KualiDecimal decimal1 = new KualiDecimal(52);
        KualiDecimal decimal2 = new KualiDecimal(32.3); 

        String percent1 = (String)testFormatter.format(decimal1);
        String percent2 = (String)testFormatter.format(decimal2);
        
        assertEquals("52 percent", percent1);
        assertEquals("32.3 percent", percent2);
    
    }    

}
