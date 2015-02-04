package org.kuali.ole.deliver.inquiry;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.inquiry.InquiryViewAuthorizerBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 9/5/12
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronInquiryViewAuthorizer extends InquiryViewAuthorizerBase {
    private List<ParameterBo> parametersList;
    private Map<String, String> labelMap = new HashMap<String, String>();
    private boolean isListPopulated;
    private boolean isTitleSet;

    public void populateParametersList() {
        isListPopulated = true;
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(OLEConstants.NAMESPACE_CODE, OLEConstants.OlePatron.PATRON_NAMESPACE);
        criteriaMap.put(OLEConstants.COMPONENT_CODE, OLEConstants.OlePatron.PATRON);
        parametersList = (List<ParameterBo>) businessObjectService.findMatching(ParameterBo.class, criteriaMap);
        for (int i = 0; i < parametersList.size(); i++) {
            labelMap.put(parametersList.get(i).getName(), parametersList.get(i).getValue());
        }
    }


    @Override
    public boolean canViewField(View view, ViewModel model, Field field, String propertyName, Person user) {
        // check view authz flag is set
        if (!isTitleSet && view.getHeader().getHeaderText().contains(OLEConstants.OlePatron.PATRON)) {
            if (!isListPopulated) {
                populateParametersList();
            }
            view.getHeader().setHeaderText(view.getHeader().getHeaderText().replace(OLEConstants.OlePatron.PATRON, labelMap.get(OLEConstants.OlePatron.PATRON)));
            isTitleSet = true;
        }
        if (!field.getComponentSecurity().isViewAuthz()) {
            if (!isListPopulated) {
                populateParametersList();
            }
            if (labelMap.containsKey(field.getLabel())) {
                field.setLabel(labelMap.get(field.getLabel()));
            }
            return true;
        }

        return isAuthorizedByTemplate(view, field, model, KimConstants.PermissionTemplateNames.VIEW_FIELD, user, null,
                null, false);
    }
}
