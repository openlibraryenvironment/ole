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
package org.kuali.ole.documenthandler;

import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;

/**
 * Class to WorkInstanceOleMLContentHandler.
 *
 * @author Rajesh Chowdary K
 * @created Feb 24, 2012
 */
public class WorkInstanceOleMLContentHandler {
    private InstanceOlemlRecordProcessor olemlProcessor = new InstanceOlemlRecordProcessor();

    /**
     * Method to alter the pre ingest operations for the given Instance OleML Content Manipulations.
     * Manipulates content to insert hodlings Identifier and Instance Identifier
     *
     * @param document
     * @param fileNodeUUID
     * @param parentNode
     * @throws RepositoryException
     */
    public void doInstanceOleMLContentManipulations(RequestDocument document, final String fileNodeUUID,
                                                    final Node parentNode) throws RepositoryException {
        if (document.getContent().getContentObject() instanceof OleHoldings) { // holdings
            ((OleHoldings) document.getContent().getContentObject()).setHoldingsIdentifier(fileNodeUUID);
            document.getContent()
                    .setContent(olemlProcessor.toXML((OleHoldings) document.getContent().getContentObject()));
        } else if (document.getContent().getContentObject() instanceof SourceHoldings) { // source holdings.
            ((SourceHoldings) document.getContent().getContentObject()).setHoldingsIdentifier(fileNodeUUID);
            document.getContent()
                    .setContent(olemlProcessor.toXML((SourceHoldings) document.getContent().getContentObject()));
        } else if (document.getContent().getContentObject() instanceof Item) { // item
            ((Item) document.getContent().getContentObject()).setItemIdentifier(fileNodeUUID);
            document.getContent().setContent(olemlProcessor.toXML((Item) document.getContent().getContentObject()));
        } else if (document.getContent().getContentObject() instanceof Instance) { // instance
            Instance inst = ((Instance) document.getContent().getContentObject());
            inst.setInstanceIdentifier(parentNode.getIdentifier());
            InstanceCollection instanceCollection = new InstanceCollection();
            ArrayList<Instance> oleinsts = new ArrayList<Instance>();
            oleinsts.add(inst);
            instanceCollection.setInstance((oleinsts));
            document.getContent().setContent(olemlProcessor.toXML(instanceCollection));
        }
    }
}
