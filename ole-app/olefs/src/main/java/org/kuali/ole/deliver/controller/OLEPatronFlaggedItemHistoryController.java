package org.kuali.ole.deliver.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEFlaggedItems;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronNotes;
import org.kuali.ole.deliver.form.OLEPatronFlaggedItemHistoryForm;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 5/6/14
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/olePatronFlaggedItemHistoryController")
public class OLEPatronFlaggedItemHistoryController extends UifControllerBase {

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEPatronFlaggedItemHistoryForm();
    }


    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        super.start(form, result, request, response);
        OLEPatronFlaggedItemHistoryForm olePatronFlaggedItemHistoryForm=(OLEPatronFlaggedItemHistoryForm)form;
        if(StringUtils.isNotEmpty(request.getParameter("itemBarcode"))){
           olePatronFlaggedItemHistoryForm.setItemBarcode(request.getParameter("itemBarcode"));
        }
        if (StringUtils.isNotEmpty((olePatronFlaggedItemHistoryForm.getItemBarcode()))) {
            olePatronFlaggedItemHistoryForm.setItemsList(getItemFlaggedHistoryUsingBarcode(olePatronFlaggedItemHistoryForm.getItemBarcode()));
        } else {
            olePatronFlaggedItemHistoryForm.setItemsList(new ArrayList<OLEFlaggedItems>());
        }
        return getUIFModelAndView(olePatronFlaggedItemHistoryForm);
    }

    private List<OLEFlaggedItems> getItemFlaggedHistoryUsingBarcode(String itemBarcode){
        List<OLEFlaggedItems> items=new ArrayList<OLEFlaggedItems>();
        List<OlePatronNotes>  olePatronNotes =(List<OlePatronNotes>)KRADServiceLocator.getBusinessObjectService().findAll(OlePatronNotes.class);
        String checkInTextType1="Item: " + itemBarcode + " checked in";
        String checkInTextType2="Item : "+itemBarcode+" checked in";
        if (olePatronNotes != null) {
            for (OlePatronNotes notes : olePatronNotes) {
                if (notes.getPatronNoteText() != null && (notes.getPatronNoteText().toUpperCase().contains(checkInTextType1.toUpperCase()) ||notes.getPatronNoteText().toUpperCase().contains(checkInTextType2.toUpperCase()) )) {
                    OLEFlaggedItems oleFlaggedItems = new OLEFlaggedItems();
                    oleFlaggedItems.setPatronId(notes.getOlePatronId());
                    oleFlaggedItems.setPatronNote(notes.getPatronNoteText());
                    if(notes.getPatronNoteText().toUpperCase().contains(checkInTextType2.toUpperCase())){
                       oleFlaggedItems.setPatronFlagType(OLEConstants.FLAG_TYP_ITM_DAMAGED);
                    }
                    if(notes.getPatronNoteText().toUpperCase().contains(checkInTextType1.toUpperCase())){
                        oleFlaggedItems.setPatronFlagType(OLEConstants.FLAG_TYP_ITM_MISSING);
                    }
                    String name = "";
                    if (notes.getOlePatron() != null) {
                        oleFlaggedItems.setPatronBarcode(notes.getOlePatron().getBarcode());
                    }
                    if(notes.getOlePatronId()!=null){
                        Map<String, String> patronId = new HashMap<String, String>();
                        patronId.put(OLEConstants.OlePatron.PAY_BILL_PATRON_ID, notes.getOlePatronId());
                        OLEPatronEntityViewBo document = (OLEPatronEntityViewBo) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEPatronEntityViewBo.class, patronId);
                        if (document != null) {
                            if (document.getFirstName() != null && !document.getFirstName().equalsIgnoreCase("")) {
                                name = name + document.getFirstName();
                            }
                            if (document.getLastName() != null && !document.getLastName().equalsIgnoreCase("")) {
                                name = name + " " + document.getLastName();
                            }
                        }
                    }
                    oleFlaggedItems.setPatronName(name);
                    items.add(oleFlaggedItems);
                }
            }
        }
        return items;
    }

}
