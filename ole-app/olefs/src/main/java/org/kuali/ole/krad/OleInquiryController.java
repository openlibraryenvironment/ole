package org.kuali.ole.krad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleProxyPatronDocument;
import org.kuali.ole.deliver.form.OlePatronDocumentInquiryForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.controller.InquiryController;
import org.kuali.rice.krad.web.form.InquiryForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/inquiry")
public class OleInquiryController extends InquiryController {

	private static final Logger LOG = Logger
			.getLogger(OleInquiryController.class);

	private static String DATA_OBJECT_CN_PARAM = "dataObjectClassName";
	private static String DATA_OBJECT_CN_ATTRIBUTE = OleInquiryController.class
			.getName() + '.' + DATA_OBJECT_CN_PARAM;

	private String getDataObjectClassName(HttpServletRequest request) {
		String rv = request.getParameter(DATA_OBJECT_CN_PARAM);
		if (rv == null)

			rv = (String) request.getSession().getAttribute(
					DATA_OBJECT_CN_ATTRIBUTE);

		else
			request.getSession().setAttribute(DATA_OBJECT_CN_ATTRIBUTE, rv);

		return rv;
	}

	@Override
	protected InquiryForm createInitialForm(HttpServletRequest request) {
		String dataObjectClassName = getDataObjectClassName(request);

		if (OlePatronDocument.class.getName().equals(dataObjectClassName))
			return new OlePatronDocumentInquiryForm();

		return super.createInitialForm(request);
	}

	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=refreshLoanSection")
	public ModelAndView refreshLoanSection(
			@ModelAttribute("KualiForm") UifFormBase uifForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if ((uifForm instanceof OlePatronDocumentInquiryForm)
				&& !((OlePatronDocumentInquiryForm) uifForm)
						.isFilterProxySection()) {

			OlePatronDocumentInquiryForm form = (OlePatronDocumentInquiryForm) uifForm;
			OlePatronDocument patronDocument = (OlePatronDocument) form
					.getDataObject();

			LoanProcessor loanProcessor = new LoanProcessor();
			OleDeliverRequestDocumentHelperServiceImpl requestService = new OleDeliverRequestDocumentHelperServiceImpl();
			List<OleDeliverRequestBo> oleDeliverRequestBoList = patronDocument
					.getOleDeliverRequestBos();
			if (oleDeliverRequestBoList.size() > 0) {
				for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {

					// OleItemSearch oleItemSearch = requestService
					// .getItemDetailsForPatron(oleDeliverRequestBoList
					// .get(i).getItemUuid());
					// if (oleItemSearch != null
					// && oleItemSearch.getItemBarCode() != null) {
					// oleDeliverRequestBoList.get(i).setTitle(
					// oleItemSearch.getTitle());
					// oleDeliverRequestBoList.get(i).setCallNumber(
					// oleItemSearch.getCallNumber());
					// }

					requestService.processItem(oleDeliverRequestBoList.get(i));
				}
			}

			try {
				patronDocument.setOleLoanDocuments(loanProcessor
						.getPatronLoanedItemBySolr(patronDocument
								.getOlePatronId()));
				patronDocument
						.setOleTemporaryCirculationHistoryRecords(loanProcessor
								.getPatronTemporaryCirculationHistoryRecords(patronDocument
										.getOlePatronId()));
				patronDocument.setOleDeliverRequestBos(loanProcessor
						.getPatronRequestRecords(patronDocument
								.getOlePatronId()));
			} catch (Exception e) {
				LOG.error("Cannot fetch the patron loaned items.", e);

			}

		}

		return super.refresh(uifForm, result, request, response);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=refreshProxyPatron")
	public ModelAndView refreshProxyPatron(
			@ModelAttribute("KualiForm") UifFormBase uifForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if ((uifForm instanceof OlePatronDocumentInquiryForm)
				&& !((OlePatronDocumentInquiryForm) uifForm)
						.isFilterProxySection()) {

			OlePatronDocumentInquiryForm form = (OlePatronDocumentInquiryForm) uifForm;
			OlePatronDocument patronDocument = (OlePatronDocument) form
					.getDataObject();

			List<OleProxyPatronDocument> oleProxyPatronDocuments = patronDocument
					.getOleProxyPatronDocuments();
			List<OleProxyPatronDocument> proxyPatronDocumentList = new ArrayList<OleProxyPatronDocument>();
			if (oleProxyPatronDocuments.size() > 0) {
				for (OleProxyPatronDocument oleProxyPatronDocument : oleProxyPatronDocuments) {
					Map<String, String> proxyMap = new HashMap<String, String>();
					proxyMap.put(OLEConstants.OlePatron.PATRON_ID,
							oleProxyPatronDocument.getProxyPatronId());
					OlePatronDocument tempDocument = (OlePatronDocument) KRADServiceLocator
							.getBusinessObjectService().findByPrimaryKey(
									OlePatronDocument.class, proxyMap);
					if (tempDocument != null
							&& tempDocument.isActiveIndicator()) {
						oleProxyPatronDocument
								.setProxyPatronBarcode(tempDocument
										.getBarcode());
						oleProxyPatronDocument
								.setProxyPatronFirstName(tempDocument
										.getEntity().getNames().get(0)
										.getFirstName());
						oleProxyPatronDocument
								.setProxyPatronLastName(tempDocument
										.getEntity().getNames().get(0)
										.getLastName());
						proxyPatronDocumentList.add(oleProxyPatronDocument);
					}
				}
				patronDocument
						.setOleProxyPatronDocuments(proxyPatronDocumentList);
			}
			OlePatronDocument olePatronDocument;
			OleProxyPatronDocument proxyPatronDocument = null;
			List<OleProxyPatronDocument> proxyPatronDocuments = new ArrayList<OleProxyPatronDocument>();
			Map proxyMap = new HashMap();
			proxyMap.put(OLEConstants.OlePatron.PROXY_PATRON_ID,
					patronDocument.getOlePatronId());
			List<OleProxyPatronDocument> oleProxyPatronDocumentList = (List<OleProxyPatronDocument>) KRADServiceLocator
					.getBusinessObjectService().findMatching(
							OleProxyPatronDocument.class, proxyMap);
			if (oleProxyPatronDocumentList.size() > 0) {
				proxyMap.remove(OLEConstants.OlePatron.PROXY_PATRON_ID);
				for (OleProxyPatronDocument oleProxyPatronDocument : oleProxyPatronDocumentList) {
					proxyMap.put(OLEConstants.OlePatron.PATRON_ID,
							oleProxyPatronDocument.getOlePatronId());
					List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator
							.getBusinessObjectService().findMatching(
									OlePatronDocument.class, proxyMap);
					if (olePatronDocumentList.size() > 0) {
						olePatronDocument = olePatronDocumentList.get(0);
						proxyPatronDocument = new OleProxyPatronDocument();
						if (olePatronDocument.isActiveIndicator()) {
							proxyPatronDocument
									.setOlePatronId(olePatronDocument
											.getOlePatronId());
							proxyPatronDocument
									.setProxyForPatronFirstName(olePatronDocument
											.getEntity().getNames().get(0)
											.getFirstName());
							proxyPatronDocument
									.setProxyForPatronLastName(olePatronDocument
											.getEntity().getNames().get(0)
											.getLastName());
							proxyPatronDocument
									.setProxyPatronActivationDate(oleProxyPatronDocument
											.getProxyPatronActivationDate());
							proxyPatronDocument
									.setProxyPatronExpirationDate(oleProxyPatronDocument
											.getProxyPatronExpirationDate());
							/*
							 * List<OleProxyPatronDocument> proxyPatronList =
							 * (List<OleProxyPatronDocument>)
							 * getBusinessObjectService
							 * ().findMatching(OleProxyPatronDocument.class,
							 * proxyMap); if (proxyPatronList.size() > 0) { for
							 * (OleProxyPatronDocument proxyPatron :
							 * proxyPatronList) { if
							 * (proxyPatron.getOlePatronId(
							 * ).equals(olePatronDocument.getOlePatronId())) {
							 * proxyPatronDocument
							 * .setProxyPatronActivationDate(proxyPatron
							 * .getProxyPatronActivationDate());
							 * proxyPatronDocument
							 * .setProxyPatronExpirationDate(proxyPatron
							 * .getProxyPatronExpirationDate()); } } }
							 */
						}
						proxyPatronDocuments.add(proxyPatronDocument);
					}
				}
				patronDocument
						.setOleProxyPatronDocumentList(proxyPatronDocuments);
			}
		}

		return super.refresh(uifForm, result, request, response);
	}

}

