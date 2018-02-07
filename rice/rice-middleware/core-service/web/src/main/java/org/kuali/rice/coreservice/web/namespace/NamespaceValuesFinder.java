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
package org.kuali.rice.coreservice.web.namespace;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.namespace.Namespace;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NamespaceValuesFinder extends KeyValuesBase {

    @Override
	public List<KeyValue> getKeyValues() {

        // get a list of all Namespaces
        List<Namespace> namespaces = CoreServiceApiServiceLocator.getNamespaceService().findAllNamespaces();
        // copy the list of codes before sorting, since we can't modify the results from this method
        namespaces = namespaces == null ? new ArrayList<Namespace>(0) : new ArrayList<Namespace>( namespaces );

        // sort using comparator.
        Collections.sort(namespaces, NamespaceComparator.INSTANCE);

        // create a new list (code, descriptive-name)
        List<KeyValue> labels = new ArrayList<KeyValue>( namespaces.size() );
        labels.add(new ConcreteKeyValue("", ""));
        for ( Namespace namespace : namespaces ) {
            labels.add( new ConcreteKeyValue(namespace.getCode(), namespace.getCode() + " - " + namespace.getName() ) );
        }
        return labels;
    }

    private static class NamespaceComparator implements Comparator<Namespace> {
        public static final Comparator<Namespace> INSTANCE = new NamespaceComparator();

        @Override
        public int compare(Namespace o1, Namespace o2) {
            return o1.getCode().compareTo( o2.getCode() );
        }
    }
}
