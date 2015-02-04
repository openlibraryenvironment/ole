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
package org.kuali.ole.docstore.process;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.TokenXMLPairExpressionIterator;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class BulkIngestDocStoreRequestBuilder.
 *
 * @author Rajesh Chowdary K
 * @version 0.8
 * @created Aug 1, 2012
 */
public class BulkIngestDocStoreRequestBuilder
        extends RouteBuilder {

    private static Logger log = LoggerFactory.getLogger(BulkIngestDocStoreRequestBuilder.class);
    private String folder = null;
    private String user = null;
    private String action = null;
    private String category;
    private String type;
    private String format;
    private String target;

    /**
     * Constructor.
     *
     * @param folder
     * @param user
     * @param action
     * @param context
     */
    public BulkIngestDocStoreRequestBuilder(String folder, String user, String action, CamelContext context,
                                            String category, String type, String format, String target) {
        super(context);
        this.folder = folder;
        this.user = user;
        this.action = action;
        this.category = category;
        this.type = type;
        this.format = format;
        this.target = target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        log.debug("Loading Bulk Ingest DocStore Request Builder Process @" + folder);
        if (DocCategory.WORK.isEqualTo(category) && DocType.BIB.isEqualTo(type) && DocFormat.MARC.isEqualTo(format)) {
            from("file:" + folder + "?noop=false&move=.done")
                    .split(new TokenXMLPairExpressionIterator("<record>", "</record>", "<collection>"))
                    .process(new BulkIngestDocumentReqProcessor(user, action, category, type, format, target));
        } else if (DocCategory.WORK.isEqualTo(category) && DocType.BIB.isEqualTo(type) && DocFormat.DUBLIN_CORE
                .isEqualTo(format)) {
            from("file:" + folder + "?noop=false&move=.done")
                    .split(new TokenXMLPairExpressionIterator("<dublin_core>", "</dublin_core>", "<>"))
                    .process(new BulkIngestDocumentReqProcessor(user, action, category, type, format, target));
        } else if (DocCategory.WORK.isEqualTo(category) && DocType.BIB.isEqualTo(type) && DocFormat.DUBLIN_UNQUALIFIED
                .isEqualTo(format)) {
            from("file:" + folder + "?noop=false&move=.done")
                    .split(new TokenXMLPairExpressionIterator("<record>", "</record>", "<ListRecords>"))
                    .process(new BulkIngestDocumentReqProcessor(user, action, category, type, format, target));
        } else {
            log.error("Un Supported Document Category/Type/Format : " + category + "/" + type + "/" + format);
            throw new Exception("Un Supported Document Category/Type/Format : " + category + "/" + type + "/" + format);
        }
        log.info("Loaded Bulk Ingest DocStore Request Builder Process @" + folder);
    }

}
