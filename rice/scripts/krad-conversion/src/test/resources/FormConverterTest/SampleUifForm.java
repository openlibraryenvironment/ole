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
package edu.sampleu.bookstore.document.web;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.KRADConstants;
import javax.servlet.http.HttpServletRequest;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.web.form.UifFormBase;

// Generated Imports

import edu.sampleu.bookstore.bo.BookOrder;
import edu.sampleu.bookstore.document.BookOrderDocument;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

/**
 * SampleUifForm is a generated placeholder form
 * TODO: produce proper UifFormBase to replace this generated placeholder
 *
 * @author rice
 */
public class SampleUifForm extends TransactionalDocumentFormBase {

    public SampleUifForm() {
        super();
        setViewTypeName(ViewType.DEFAULT);
    }



}
