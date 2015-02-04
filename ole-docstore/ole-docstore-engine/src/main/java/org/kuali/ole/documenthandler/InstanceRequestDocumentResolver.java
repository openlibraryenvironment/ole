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

import org.apache.solr.common.SolrDocument;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class to InstanceRequestDocumentResolver.
 *
 * @author Rajesh Chowdary K
 * @created Feb 22, 2012
 */
public class InstanceRequestDocumentResolver {
    private static Logger logger = LoggerFactory.getLogger(InstanceRequestDocumentResolver.class);

    private InstanceOlemlRecordProcessor olemlProcessor = new InstanceOlemlRecordProcessor();

    /**
     * Method to get Parsed Holdings & Item Documents.
     *
     * @param instanceDoc  - Instance document in format OleML
     * @param linkedBibIds TODO
     * @return
     */
    public List<RequestDocument> getParsedHoldingsNItemDocuments(RequestDocument instanceDoc,
                                                                 List<String> linkedBibIds) {
        List<RequestDocument> parsedItemNHoldingsDocuments = new ArrayList<RequestDocument>();
        if (instanceDoc != null && DocCategory.WORK.isEqualTo(instanceDoc.getCategory())
                && DocType.INSTANCE.isEqualTo(instanceDoc.getType()) && DocFormat.OLEML
                .isEqualTo(instanceDoc.getFormat())) {
            String docContent = instanceDoc.getContent().getContent();
            InstanceCollection instanceCollection = olemlProcessor.fromXML(docContent);
            instanceDoc.getContent().setContentObject(instanceCollection);
            // XML conversion
            if (instanceCollection.getInstance() != null && instanceCollection.getInstance().size() > 0) {
                Instance instance = instanceCollection.getInstance().get(0);
                resolveLinkingWithBib(instance);

                if (instance.getResourceIdentifier().size() == 1) {
                    if (instance.getResourceIdentifier().get(0) == null || ""
                            .equals(instance.getResourceIdentifier().get(0))) {
                        instance.getResourceIdentifier().remove(0);
                    }
                }
                if (linkedBibIds != null && linkedBibIds.size() > 0) {
                    for (String likedBibId : linkedBibIds) {
                        instance.getResourceIdentifier().add(likedBibId);
                    }
                }
                parsedItemNHoldingsDocuments.add(generateInstanceDocument(instance));

                OleHoldings oleHolding = instance.getOleHoldings();
                RequestDocument rdHol = (RequestDocument) instanceDoc.clone();
                Content content = new Content();
                content.setContent(olemlProcessor.toXML(oleHolding));
                content.setContentObject(oleHolding);
                rdHol.setContent(content);
                if (oleHolding.getExtension() != null) {
                    rdHol.setAdditionalAttributes(getFirstAdditionalAttributes(oleHolding.getExtension()));
                }
                parsedItemNHoldingsDocuments.add(rdHol);

                SourceHoldings sourceHoldings = instance.getSourceHoldings();
                RequestDocument rdSrcHol = (RequestDocument) instanceDoc.clone();
                Content sourceHolContent = new Content();
                sourceHolContent.setContent(olemlProcessor.toXML(sourceHoldings));
                sourceHolContent.setContentObject(sourceHoldings);
                rdSrcHol.setContent(sourceHolContent);
                if (sourceHoldings != null && sourceHoldings.getExtension() != null) {
                    rdSrcHol.setAdditionalAttributes(getFirstAdditionalAttributes(sourceHoldings.getExtension()));
                }
                parsedItemNHoldingsDocuments.add(rdSrcHol);

                for (Item oleItem : instance.getItems().getItem()) {
                    RequestDocument rdItm = (RequestDocument) instanceDoc.clone();
                    Content itemContent = new Content();
                    itemContent.setContent(olemlProcessor.toXML(oleItem));
                    itemContent.setContentObject(oleItem);
                    rdItm.setContent(itemContent);
                    if (oleItem != null && oleItem.getExtension() != null) {
                        rdItm.setAdditionalAttributes(getFirstAdditionalAttributes(oleItem.getExtension()));
                    }
                    parsedItemNHoldingsDocuments.add(rdItm);
                }
            }
        }
        return parsedItemNHoldingsDocuments;
    }

    private void resolveLinkingWithBib(Instance instance) {
        instance.getResourceIdentifier().clear();
        if (ProcessParameters.BULK_INGEST_IS_LINKING_ENABLED) {
            for (FormerIdentifier frids : instance.getFormerResourceIdentifier()) {
                Identifier identifier = frids.getIdentifier();
                try {
                    if (identifier.getIdentifierValue() != null
                            && identifier.getIdentifierValue().trim().length() != 0) {
                        List<SolrDocument> solrDocs = ServiceLocator.getIndexerService()
                                .getSolrDocument("SystemControlNumber",
                                        "\"" + identifier
                                                .getIdentifierValue()
                                                + "\"");
                        if (solrDocs != null && solrDocs.size() > 0) {
                            for (SolrDocument solrDoc : solrDocs) {
                                if (checkApplicability(identifier.getIdentifierValue(),
                                        solrDoc.getFieldValue("SystemControlNumber"))) {
                                    instance.getResourceIdentifier().add(solrDoc.getFieldValue("id").toString());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("Exception (ignored) while linking instance with bib: ", e);
                }
            }
        }
    }

    private boolean checkApplicability(Object value, Object fieldValue) {
        if (fieldValue instanceof Collection) {
            for (Object object : (Collection) fieldValue) {
                if (object.equals(value)) {
                    return true;
                }
            }
            return false;
        } else {
            return value.equals(fieldValue);
        }
    }

    private RequestDocument generateInstanceDocument(Instance instance) {
        InstanceCollection instanceCollection = new InstanceCollection();
        List<Instance> instances = new ArrayList<Instance>();
        instanceCollection.setInstance(instances);
        Instance inst = new Instance();
        instances.add(inst);
        inst.setInstanceIdentifier(instance.getInstanceIdentifier());
        inst.setResourceIdentifier(instance.getResourceIdentifier());
        inst.setFormerResourceIdentifier(instance.getFormerResourceIdentifier());
        inst.setExtension(instance.getExtension());
        String cont = olemlProcessor.toXML(instanceCollection);
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        requestDocument.setContent(new Content());
        requestDocument.getContent().setContent(cont);
        requestDocument.getContent().setContentObject(inst);
        return requestDocument;
    }

    /**
     * Method to get Additional Attributes
     *
     * @param extension
     * @return
     */
    private AdditionalAttributes getFirstAdditionalAttributes(Extension extension) {
        if (extension != null && extension.getContent() != null) {
            for (Object obj : extension.getContent()) {
                if (obj instanceof AdditionalAttributes) {
                    return (AdditionalAttributes) obj;
                }
            }
        }
        return null;
    }
}
