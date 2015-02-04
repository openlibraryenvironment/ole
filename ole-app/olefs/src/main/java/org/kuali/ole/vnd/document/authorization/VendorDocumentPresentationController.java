/*
 * Copyright 2009 The Kuali Foundation
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
package org.kuali.ole.vnd.document.authorization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.authorization.FinancialSystemMaintenanceDocumentPresentationControllerBase;
import org.kuali.ole.vnd.VendorPropertyConstants;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

public class VendorDocumentPresentationController extends FinancialSystemMaintenanceDocumentPresentationControllerBase {

    @Override
    public Set<String> getConditionallyReadOnlySectionIds(MaintenanceDocument document) {
        Set<String> conditionallyReadOnlySectionIds = super.getConditionallyReadOnlySectionIds(document);
        VendorDetail vendor = (VendorDetail)document.getNewMaintainableObject().getDataObject();

        if (!vendor.isVendorParentIndicator()) {
            // make some sections read only, e.g. supplier diversity cause they're on the header
            conditionallyReadOnlySectionIds.add(VendorPropertyConstants.VENDOR_SUPPLIER_DIVERSITIES);
        }

        return conditionallyReadOnlySectionIds;
    }

    @Override
    public Set<String> getConditionallyReadOnlyPropertyNames(MaintenanceDocument document) {
        Set<String> conditionallyReadonlyPropertyNames = super.getConditionallyReadOnlyPropertyNames(document);
        VendorDetail vendor = (VendorDetail)document.getNewMaintainableObject().getDataObject();
        VendorDocumentAuthorizer documentAuthorizer = (VendorDocumentAuthorizer) SpringContext.getBean(DocumentHelperService.class).getDocumentAuthorizer(document);
        boolean isAuthorized = documentAuthorizer.isAuthorized(document, VendorPropertyConstants.OLE_VND, VendorPropertyConstants.PERMISSION_NAME_INACTIVATE_VENDOR, GlobalVariables.getUserSession().getPerson().getPrincipalId());

        String nameSpaceCode = OLEConstants.Vendor.VENDOR_NAMESPACE;

        boolean hasPermission = KimApiServiceLocator.getPermissionService().hasPermission(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.Vendor.DEACTIVATE_VENDOR);
        boolean hasPermissionVendorFlag = KimApiServiceLocator.getPermissionService().hasPermission(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.Vendor.EDIT_VENDOR_LINKING_NUM);

        if (!isAuthorized) {
            if (!hasPermission) {
        conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_ACTIVE_INDICATOR);
        conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_INACTIVE_REASON);
        }
            if (!hasPermissionVendorFlag) {
                conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_LINKING_NUMBER);
            }
        }
        if (vendor.isVendorParentIndicator()) {
            // Vendor Parent Indicator should be readOnly if the vendor is a parent.
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_PARENT_INDICATOR);

            // For existing vendors, don't allow vendor type code to be changed if maint table indicates it shouldn't be changed
            if (ObjectUtils.isNotNull(vendor.getVendorHeaderGeneratedIdentifier()) && !vendor.getVendorHeader().getVendorType().isVendorTypeChangeAllowedIndicator()) {
                conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_TYPE_CODE);
            }
        }

        // If the vendor is not a parent, there are certain fields that should be readOnly
        else {
            // All the fields in VendorHeader should be readOnly if the vendor is not a parent.
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_TYPE_CODE);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_TAX_NUMBER);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_TAX_TYPE_CODE);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_OWNERSHIP_CODE);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_OWNERSHIP_CATEGORY_CODE);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_FEDERAL_WITHOLDING_TAX_BEGINNING_DATE);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_FEDERAL_WITHOLDING_TAX_END_DATE);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_W9_RECEIVED_INDICATOR);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_W8_BEN_RECEIVED_INDICATOR);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_DEBARRED_INDICATOR);
            conditionallyReadonlyPropertyNames.add(VendorPropertyConstants.VENDOR_FOREIGN_INDICATOR);
        }

        return conditionallyReadonlyPropertyNames;
    }

    @Override
    public Set<String> getConditionallyHiddenPropertyNames(BusinessObject businessObject) {
        Set<String> conditionallyHiddenPropertyNames = super.getConditionallyHiddenPropertyNames(businessObject);
        MaintenanceDocument document = (MaintenanceDocument) businessObject;
        VendorDetail vendor = (VendorDetail)document.getNewMaintainableObject().getDataObject();
        // If the vendor is a parent then the vendor parent name should be hidden.
        if (vendor.isVendorParentIndicator()) {
            conditionallyHiddenPropertyNames.add(VendorPropertyConstants.VENDOR_PARENT_NAME);
        }

        return conditionallyHiddenPropertyNames;
    }

    @Override
    public boolean canBlanketApprove(Document document) {

        String documentTypeName = OLEConstants.Vendor.DOCUMENT_TYPE;
        String nameSpaceCode = OLEConstants.Vendor.VENDOR_NAMESPACE;
        Map<String,String> permissionDetails = new HashMap<String, String>();
        permissionDetails.put(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME,documentTypeName);
        boolean hasPermission = false;
        /*hasPermission = KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.Vendor.BLANKET_APPROVE, permissionDetails, new HashMap<String, String>());*/
        if (!document.getDocumentHeader().getWorkflowDocument().getStatus().getCode().equalsIgnoreCase(OLEConstants.FINAL_STATUS)) {
            hasPermission = KimApiServiceLocator.getPermissionService().hasPermission(
                    GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                    OLEConstants.Vendor.BLANKET_APPROVE_VENDOR_DOCUMENT);
        }

        Map<String,String> roleQualifiers = new HashMap<String,String>();

      /*  hasPermission = KimApiServiceLocator.getPermissionService().isAuthorized(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.Vendor.BLANKET_APPROVE_VENDOR_DOCUMENT, new HashMap<String, String>());
      */
      if(!hasPermission) {
            permissionDetails.put(KimConstants.AttributeConstants.REQUIRED,"FALSE");
            permissionDetails.put(KimConstants.AttributeConstants.ACTION_DETAILS_AT_ROLE_MEMBER_LEVEL,"FALSE");
            permissionDetails.put(KimConstants.AttributeConstants.ROUTE_NODE_NAME,OLEConstants.Vendor.VENDOR_MANAGEMENT);
           /* hasPermission = KimApiServiceLocator.getResponsibilityService().hasResponsibilityByTemplate(
                    GlobalVariables.getUserSession().getPerson().getPrincipalId(),
                    OLEConstants.CoreModuleNamespaces.VENDOR, OLEConstants.Vendor.VENDOR_REVIEW,
                    new HashMap<String, String>(), permissionDetails);*/
           // hasPermission = KimApiServiceLocator.getResponsibilityService().hasResponsibility(GlobalVariables.getUserSession().getPerson().getPrincipalId(), OLEConstants.Vendor.VENDOR_REVIEW, null);

            hasPermission = KimApiServiceLocator.getResponsibilityService().hasResponsibility(GlobalVariables.getUserSession().getPerson().getPrincipalId(),OLEConstants.CoreModuleNamespaces.VENDOR, OLEConstants.Vendor.VENDOR_REVIEW_DOCUMENT, Collections.<String, String>emptyMap());


       }

       return hasPermission;
    }

  }
