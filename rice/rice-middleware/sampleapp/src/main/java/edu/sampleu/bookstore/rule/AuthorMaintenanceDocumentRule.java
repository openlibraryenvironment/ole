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
package edu.sampleu.bookstore.rule;

import edu.sampleu.bookstore.bo.Address;
import edu.sampleu.bookstore.bo.Author;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.List;

/*
 * Business Rule for Author Document that follows prior to submit action.
 * Checks that Author Document has at least one address and no two address should be of same type. 
 */

public class AuthorMaintenanceDocumentRule extends MaintenanceDocumentRuleBase {
	private static final String AUTHOR_ENTRIES_PROPERTY_PATH = KRADConstants.DOCUMENT_PROPERTY_NAME
			+ ".authorEntries";
	private static final String NO_ADDRESS_TYPE_ERROR_KEY = RiceKeyConstants.ERROR_CUSTOM;
	private static final String ERROR_MESSAGE_NO_ADDTYPE_FOUND = "You must add atleast one address for Author.";
	private static final String SAME_ADDRESS_TYPE_ERROR_KEY = RiceKeyConstants.ERROR_CUSTOM;
	private static final String ERROR_MESSAGE_SAME_ADDTYPR_FOUND = "You must not repeat address type for addresses of Author.";

	@Override
	protected boolean processGlobalRouteDocumentBusinessRules(
			MaintenanceDocument document) {
		// TODO Auto-generated method stub
		System.out.println("****Inside Global*****");
		// cast the document to a Author Document
		Author author = (Author)document.getDocumentDataObject();
		System.out.println("Inside Global Aothor Got is : " + author);
		System.out.println("****Inside Global*****");
		// get the list of book order entries off of the book order document
		List<Address> addressEntries = author.getAddresses();
		System.out.println("Inside Global Aothor address is : " + addressEntries);
		
		// make sure that the list is not empty
		if (addressEntries == null || addressEntries.isEmpty()) {
			System.out.println("--Got addressEntries Null--");
			GlobalVariables.getMessageMap().putError(
					AUTHOR_ENTRIES_PROPERTY_PATH,NO_ADDRESS_TYPE_ERROR_KEY,
					ERROR_MESSAGE_NO_ADDTYPE_FOUND);
			return false;
		} else {
			System.out.println("--Got addressEntries Obj--");
			System.out.println("Inside Global Aothor address Size is : " + addressEntries.size());
			Address tempAddress = null;
			for(Address address : addressEntries){
				tempAddress = address;
				for(Address address2 : addressEntries){
					if(!tempAddress.equals(address2)){
						if(tempAddress.getAddressType()!= null && tempAddress.getAddressType() != null && tempAddress.getAddressType().getType().equals(address2.getAddressType().getType())){
							System.out.println("Got Same Address Typr");
							GlobalVariables.getMessageMap().putError(
									AUTHOR_ENTRIES_PROPERTY_PATH,SAME_ADDRESS_TYPE_ERROR_KEY,
									ERROR_MESSAGE_SAME_ADDTYPR_FOUND);
							return false;
						}
					}else {
						System.out.println("Got Same Address Object");
					} 
				}
			}
			
		}
		
		
		
		return super.processGlobalRouteDocumentBusinessRules(document);
	}

//	@Override
//	protected boolean processCustomRouteDocumentBusinessRules(Document document) {
//		System.out.println("****Inside Custom*****");
//		// cast the document to a Author Document
//		AuthorDocument author = (AuthorDocument) document;
//
//		// get the list of book order entries off of the book order document
//		List<Address> addressEntries = author.getAuthorAddEntries();
//
//		// make sure that the list is not empty
//		if (addressEntries == null && addressEntries.isEmpty()) {
//			System.out.println("Got address Null");
//			GlobalVariables.getMessageMap().putError(
//					AUTHOR_ENTRIES_PROPERTY_PATH, SAME_ADDRESS_TYPE_ERROR_KEY,
//					ERROR_MESSAGE);
//			return false;
//		}
//
//		return super.processCustomRouteDocumentBusinessRules(document);
//	}

}
