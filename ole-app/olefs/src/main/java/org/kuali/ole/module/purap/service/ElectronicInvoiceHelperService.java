/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.service;

import org.kuali.ole.module.purap.businessobject.ElectronicInvoiceLoad;
import org.kuali.ole.module.purap.document.ElectronicInvoiceRejectDocument;
import org.kuali.ole.sys.batch.InitiateDirectory;

public interface ElectronicInvoiceHelperService extends InitiateDirectory {

    public ElectronicInvoiceLoad loadElectronicInvoices();

    public boolean doMatchingProcess(ElectronicInvoiceRejectDocument rejectDocument);

    public boolean createPaymentRequest(ElectronicInvoiceRejectDocument rejectDocument);

}
