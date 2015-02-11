package org.kuali.ole.krad;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.PageGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Utility class for supporting low-level KRAD optimizations.
 */
public final class OleComponentUtils {

	private static final Logger LOG = Logger.getLogger(OleComponentUtils.class);

	private static ThreadLocal<Format> CURRENCY_FORMAT = new ThreadLocal<Format>() {
		@Override
		protected Format initialValue() {
			return NumberFormat.getCurrencyInstance();
		}
	};

	private static ThreadLocal<Format> DATE_FORMAT = new ThreadLocal<Format>() {
		@Override
		protected Format initialValue() {
			return DateFormat.getDateInstance();
		}
	};

	/**
	 * Gets the KRAD model for the active request.
	 * 
	 * @return KRAD model
	 */
	public static Object getModel() {
		RequestAttributes attr = RequestContextHolder.getRequestAttributes();
		if (attr == null) {
			return null;
		} else {
			return attr.getAttribute(UifConstants.REQUEST_FORM,
					RequestAttributes.SCOPE_REQUEST);
		}
	}

	/**
	 * Filters items for a group that also implements OleComponentUtils.
	 */
	public static List<? extends Component> filterItems(
			List<? extends Component> srcitems) {
		if (srcitems == null) {
			return null;
		}

		Object model = getModel();
		if (model == null) {
			return srcitems;
		} else {
			LOG.debug("Filtering based on " + model);
		}

		List<Component> items = new ArrayList<Component>();
		filterloop: for (Component itemComponent : srcitems) {

			// Filter out OleComponent instances matching true
			if (itemComponent instanceof OleComponent) {
				OleComponent oleComp = (OleComponent) itemComponent;
				String filterProp = oleComp.getFilterModelProperty();
				Object filterVal = filterProp == null ? null
						: ObjectPropertyUtils.getPropertyValue(model,
								filterProp);
				if (filterVal instanceof String)
					filterVal = Boolean.parseBoolean((String) filterVal);
				if (filterVal == null && filterProp != null)
					filterVal = Boolean.TRUE;
				if (Boolean.TRUE.equals(filterVal)) {
					LOG.debug("Omitting " + itemComponent.getClass() + " "
							+ itemComponent.getId() + ", " + filterProp
							+ " is true");
					continue filterloop;
				} else {
					LOG.debug("Keeping " + itemComponent.getClass() + " "
							+ itemComponent.getId() + ", " + filterProp
							+ " is true");
				}
			}

			items.add((Component) itemComponent.copy());
		}

		return items;
	}

	/**
	 * Filters items for a view to remove non-current pages.
	 */
	public static List<? extends Group> filterCurrentPage(String currentPageId,
			List<? extends Group> srcitems) {
		if (srcitems == null) {
			return null;
		}

		Object model = getModel();
		if (model == null || !(model instanceof UifFormBase))
			return srcitems;

		if (StringUtils.isEmpty(currentPageId))
			return srcitems;

		LOG.debug("Filtering view pages based on " + model + ", pageId = "
				+ currentPageId);

		List<Group> items = new ArrayList<Group>();
		for (Group group : srcitems) {
			String compId = group.getId();

			// Filter out OleComponent instances matching true
			if ((group instanceof PageGroup) && !currentPageId.equals(compId)) {
				LOG.debug("Omitting " + compId + ", not current page");
				continue;
			} else
				LOG.debug("Keeping " + compId
						+ ", current page or not a PageGroup");

			items.add((Group) group.copy());
		}

		return items;
	}

	/**
	 * Convenience method for converting {@link Number} to currency string
	 * representation with minimal overhead.
	 * 
	 * <p>
	 * Intended for use in KRAD SpEL expressions, for example:
	 * </p>
	 * 
	 * <pre>
	 * @{T(org.kuali.ole.krad.OleComponentUtils).formatAsCurrency(anAmount)}
	 * </pre>
	 * 
	 * @param amount
	 *            Currency amount
	 * @return formatted currency string
	 */
	public static String formatAsCurrency(Number amount) {
		return amount == null ? "" : CURRENCY_FORMAT.get().format(amount);
	}

	/**
	 * Convenience method for converting {@link Date} to a string
	 * representation with minimal overhead.
	 * 
	 * <p>
	 * Intended for use in KRAD SpEL expressions, for example:
	 * </p>
	 * 
	 * <pre>
	 * @{T(org.kuali.ole.krad.OleComponentUtils).formatDate(aDate)}
	 * </pre>
	 * 
	 * @param date date
	 * @return formatted date string
	 */
	public static String formatDate(Date date) {
		return date == null ? "" : DATE_FORMAT.get().format(date);
	}

	private OleComponentUtils() {
	}

}

