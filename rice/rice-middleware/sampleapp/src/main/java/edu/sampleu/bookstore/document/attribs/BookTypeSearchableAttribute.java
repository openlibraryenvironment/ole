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
package edu.sampleu.bookstore.document.attribs;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableDatepicker;
import org.kuali.rice.core.api.uif.RemotableQuickFinder;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * SearchableAttribute on book price
 *  <businessObject class="edu.sampleu.bookstore.bo.Book">
        <title>book1</title>
        <typeCode>SCI-FI</typeCode>
        <isbn/>
        <publisher>asdasdf</publisher>
        <publicationDate class="sql-date">2011-12-21</publicationDate>
        <price>
          <value>12.00</value>
        </price>
        <rating>
          <value>2</value>
        </rating>
        <authors>
          <edu.sampleu.bookstore.bo.Author>
            <authorName>abcd</authorName>
            <email>email@email.com</email>
            <phoneNbr>123-123-1234</phoneNbr>
            <active>true</active>
            <addresses/>
            <books/>
            <newCollectionRecord>false</newCollectionRecord>
            <extension class="edu.sampleu.bookstore.bo.Account">
              <bankName>banksy</bankName>
              <accountNumber>1234</accountNumber>
 */
public class BookTypeSearchableAttribute extends XPathSearchableAttribute {
    public BookTypeSearchableAttribute() {
        super("book_type", KewApiConstants.SearchableAttributeConstants.DATA_TYPE_STRING, "//newMaintainableObject/businessObject/typeCode/text()", "Book Author Account");
    }

    protected String getDefaultBaseLookupURL() {
        // default in DD: <property name="baseLookupUrl" value="@{#ConfigProperties['application.url']}/kr-krad/lookup"/>
        return ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.APPLICATION_URL_KEY) + "/kr-krad/lookup";
    }

    @Override
    protected RemotableAttributeField.Builder decorateRemotableAttributeField(RemotableAttributeField.Builder raf) {
        RemotableAttributeField.Builder rafb = super.decorateRemotableAttributeField(raf);
        RemotableQuickFinder.Builder widget = RemotableQuickFinder.Builder.create(
                getDefaultBaseLookupURL(),
                edu.sampleu.bookstore.bo.BookType.class.getName());
        rafb.getWidgets().add(widget);
        return rafb;
    }
}