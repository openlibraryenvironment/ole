package org.kuali.ole.deliver;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.krad.OleComponent;
import org.kuali.ole.krad.OleComponentUtils;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 4/29/14
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleGroup extends Group {
    private static final Logger LOG = Logger
            .getLogger(OleGroup.class);

    private static String ALTER_DUE_DATE_ID = "AlterDueDateSection-HorizontalBoxSection";

    private static String RENEWAL_ID = "RenewalDueDateSection-HorizontalBoxSection";

    private static String PROXY_PATRON_ID = "RealPatronSection-HorizontalBoxSection";

    private static String DAMAGED_ITEM_ID = "DamagedItem-HorizontalBoxSection";

    private static String MISSING_PIECE_ID = "MissingPiece-HorizontalBoxSection";

    private static String CLAIMS_RETURN_ID = "ClaimsReturn-HorizontalBoxSection";

    private static String OVERRIDE_LOGIN = "OverRideLogInSectionLink";

    private static String PATRON_NOTE_ID = "PatronUserNote-HorizontalBoxSection";

    private static String CONFIRM_CIRULATION_LOCATION = "ConfirmCirculationLocationChange";

    private static String MISSING_PIECE_RECORD_NOTE = "OLELoanView-RecordNote-MissingPiece-MessageBox";

    private static String DAMAGED_RECORD_NOTE = "OLELoanView-RecordNote-Damaged-MessageBox";

    private static String LOAN_CLAIMS_OPTIONS = "loanClaimsOption";

    private static String INVALID_BARCODE_ID = "InvalidBarcode-MessageBox";

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Sets the bindByNamePrefix if blank on any InputField and
     * FieldGroup instances within the items List</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object form) {

        super.performInitialization(view, form);

//        boolean alterDueDate = ((OleLoanForm) form).isAlterDueDateFlag();
//        boolean renewDueDateFlag = ((OleLoanForm) form).isRenewDueDateFlag();
//        boolean realPatronFlag =  ((OleLoanForm) form).isRealPatronFlag();
//        boolean missingPieceDialog = ((OleLoanForm) form).isMissingPieceDialog();
//        boolean damagedItemDialog =  ((OleLoanForm) form).isDamagedItemDialog();
//        boolean claimsReturnFlag = ((OleLoanForm) form).isClaimsReturnFlag();
//        boolean overrideFlag = ((OleLoanForm) form).isOverrideFlag();
//        boolean patronNoteFlag = ((OleLoanForm) form).isPatronNoteFlag();
//        boolean changeLocationFlag = ((OleLoanForm) form).isChangeLocationFlag();
//        boolean displayMissingPieceNotePopup = ((OleLoanForm) form).isDisplayMissingPieceNotePopup();
//        boolean displayDamagedRecordNotePopup = ((OleLoanForm) form).isDisplayDamagedRecordNotePopup();
//        boolean claimsFlag = ((OleLoanForm) form).isClaimsFlag();
//        LOG.info("alterDueDate " + alterDueDate);
//        List<? extends Component> items = getItems();
//        Iterator<? extends Component> itemIterator = items.iterator();
//        while (itemIterator.hasNext()) {
//            Component component = itemIterator.next();
//            if (component == null) {
//                continue;
//            }
//
//            LOG.info("checking component " + component.getId());
//
//
//            if (ALTER_DUE_DATE_ID.equals(component.getId())
//                    && !alterDueDate) {
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(RENEWAL_ID.equals(component.getId())
//                    && !renewDueDateFlag){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(PROXY_PATRON_ID.equals(component.getId())
//                    && !realPatronFlag){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(DAMAGED_ITEM_ID.equals(component.getId())
//                    && !damagedItemDialog){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(MISSING_PIECE_ID.equals(component.getId())
//                    && !missingPieceDialog){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(CLAIMS_RETURN_ID.equals(component.getId())
//                    && !claimsReturnFlag){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(OVERRIDE_LOGIN.equals(component.getId())
//                    && !overrideFlag){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(PATRON_NOTE_ID.equals(component.getId())
//                    && !patronNoteFlag){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(CONFIRM_CIRULATION_LOCATION.equals(component.getId())
//                    && !changeLocationFlag){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(MISSING_PIECE_RECORD_NOTE.equals(component.getId())
//                    && !displayMissingPieceNotePopup){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(DAMAGED_RECORD_NOTE.equals(component.getId())
//                    && !displayDamagedRecordNotePopup){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }else if(LOAN_CLAIMS_OPTIONS.equals(component.getId())
//                    && !claimsFlag){
//                LOG.info("Omitting "+component.getId()+" from lifecycle");
//                itemIterator.remove();
//            }
//        }
    }

	/**
	 * Filters {@link OleComponent} instances based on
	 * {@link OleComponent#getFilterModelProperty()}, if the current model is
	 * available.
	 */
	@Override
	protected <T> void copyProperties(T component) {
		List<? extends Component> srcitems = getItems();
		
		// prevent super() from copying items
		setItems(null);
		super.copyProperties(component);
		setItems(srcitems);
		
		Group groupCopy = (Group) component;
		groupCopy.setItems(OleComponentUtils.filterItems(srcitems));
	}

}
