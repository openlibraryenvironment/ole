package org.kuali.ole.describe.bo.marc.structuralfields;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.krad.OleComponent;
import org.kuali.ole.krad.OleComponentUtils;
import org.kuali.ole.krad.OleFilteredCopyGroup;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.springframework.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 4/18/14
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleControlFieldGroup extends FieldGroup implements OleComponent {

    private static final long serialVersionUID = -6929025512918369521L;
    private static final Logger LOG = Logger
            .getLogger(OleControlFieldGroup.class);

    private String displayFieldProperty;
    private String fieldLinkId;
    private String controlFieldGroupId;

    @Override
	public String getFilterModelProperty() {
        return ((OleFilteredCopyGroup) getGroup()).getFilterModelProperty();
	}

	public void setFilterModelProperty(String filterModelProperty) {
        ((OleFilteredCopyGroup) getGroup()).setFilterModelProperty(filterModelProperty);
	}

	@Override
    public void performInitialization(View view, Object form) {
        super.performInitialization(view, form);

        boolean editable = "true".equals(((EditorForm) form).getEditable());
        boolean displayField = "true".equals(ObjectPropertyUtils.getPropertyValue(form,
                displayFieldProperty));
        LOG.info("editable " + editable + " displayField " + displayField);

        List<? extends Component> items = getItems();
        LOG.info("initialize " + displayFieldProperty + " "
                + fieldLinkId + " " + controlFieldGroupId);
        if (StringUtils.isEmpty(displayFieldProperty)) {
            LOG.info("skip iterator");
        } else {

            Iterator<? extends Component> itemIterator = items.iterator();
            while (itemIterator.hasNext()) {
                Component component = itemIterator.next();
                if (component == null) {
                    continue;
                }

                LOG.info("checking component " + component.getId());

                // @{editable eq 'true' and displayField008 eq 'false'}
                if (fieldLinkId != null
                        && fieldLinkId.equals(component.getId())
                        && (!editable || displayField)) {
                    LOG.info("Omitting " + fieldLinkId + " from lifecycle");
                    itemIterator.remove();
                }

                // @{editable eq 'true' and displayField008 eq 'true'}
                if (controlFieldGroupId != null
                        && controlFieldGroupId.equals(component.getId())
                        && (!editable || !displayField)) {
                    LOG.info("Omitting " + controlFieldGroupId
                            + " from lifecycle");
                    itemIterator.remove();
                }
            }
        }
    }

    public String getDisplayFieldProperty() {
        return displayFieldProperty;
    }

    public void setDisplayFieldProperty(String displayFieldProperty) {
        this.displayFieldProperty = displayFieldProperty;
    }

    public String getFieldLinkId() {
        return fieldLinkId;
    }

    public void setFieldLinkId(String fieldLinkId) {
        this.fieldLinkId = fieldLinkId;
    }

    public String getControlFieldGroupId() {
        return controlFieldGroupId;
    }

    public void setControlFieldGroupId(String controlFieldGroupId) {
        this.controlFieldGroupId = controlFieldGroupId;
    }

    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        OleControlFieldGroup copy = (OleControlFieldGroup) component;
        copy.displayFieldProperty = displayFieldProperty;
        copy.fieldLinkId = fieldLinkId;
        copy.controlFieldGroupId = controlFieldGroupId;
    }

}
