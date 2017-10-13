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

import edu.sampleu.bookstore.bo.Book;
import edu.sampleu.bookstore.bo.BookOrder;
import edu.sampleu.bookstore.document.BookOrderDocument;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * SampleAction (based on BookOrderAction) class file for BookOrder maintenance Object
 * Actions prior to submit and post-Submit processes are handled.
 */

public class SampleAction extends KualiTransactionalDocumentActionBase {

    private int id = 1;

    public ActionForward addBookOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        BookOrderForm form = (BookOrderForm) actionForm;
        BookOrderDocument document = form.getBookOrderDocument();

        BookOrder newBookEntry = form.getNewBookOrder();
        document.addBookOrder(newBookEntry);

        for (BookOrder entry : document.getBookOrders()) {
            if (entry.getBookId() != null) {
                Book book = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Book.class,
                        entry.getBookId());

                entry.setUnitPrice(book.getPrice());
                Double totalPrice = 0.0d;
                if (book.getPrice() != null && entry.getQuantity() != null) {
                    totalPrice = book.getPrice().doubleValue() * entry.getQuantity().intValue();
                    if (entry.getDiscount() != null && entry.getDiscount().doubleValue() > 0) {
                        totalPrice = totalPrice - (totalPrice * entry.getDiscount().doubleValue() / 100);
                    }
                }

                entry.setTotalPrice(new KualiDecimal(totalPrice));
            }
        }

        // clear the used book order entry
        form.setNewBookOrder(new BookOrder());

        return mapping.findForward("basic");
    }

    public ActionForward deleteBookOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        BookOrderForm form = (BookOrderForm) actionForm;
        BookOrderDocument document = form.getBookOrderDocument();

        int deleteIndex = getLineToDelete(request);
        document.removeBookOrder(deleteIndex);

        return mapping.findForward("basic");
    }

    @Override
    protected void doProcessingAfterPost(KualiForm actionForm, HttpServletRequest request) {
        super.doProcessingAfterPost(actionForm, request);
        BookOrderForm form = (BookOrderForm) actionForm;
        BookOrderDocument document = form.getBookOrderDocument();
        for (BookOrder entry : document.getBookOrders()) {
            if (entry.getBookId() != null) {
                Book book = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Book.class,
                        entry.getBookId());
                entry.setUnitPrice(book.getPrice());
                Double totalPrice = 0.0d;
                if (book.getPrice() != null && entry.getQuantity() != null) {
                    totalPrice = book.getPrice().doubleValue() * entry.getQuantity().intValue();
                    if (entry.getDiscount() != null && entry.getDiscount().doubleValue() > 0) {
                        totalPrice = totalPrice - (totalPrice * entry.getDiscount().doubleValue() / 100);
                    }
                }

                entry.setTotalPrice(new KualiDecimal(totalPrice));
                entry.setBook(book);
            }
        }
    }

}
