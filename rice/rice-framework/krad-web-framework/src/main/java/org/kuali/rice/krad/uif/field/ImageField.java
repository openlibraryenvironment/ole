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
package org.kuali.rice.krad.uif.field;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Header;
import org.kuali.rice.krad.uif.element.Image;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.view.View;

import java.util.List;

/**
 * Field that wraps an image content element.
 *
 * <p>
 * Puts a <code>&lt;DIV&gt;</code> tag around an image element. This allows for labeling, styling, etc.
 * </p>
 *
 * @see org.kuali.rice.krad.uif.element.Image
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "imageField-bean", parent = "Uif-ImageField")
public class ImageField extends FieldBase {
    private static final long serialVersionUID = -7994212503770623408L;

    private Image image;

    public ImageField() {
        super();
    }

    /**
     * PerformFinalize override - calls super, corrects the field's Label for attribute to point to this field's
     * content
     *
     * @param view the view
     * @param model the model
     * @param parent the parent component
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        //determine what id to use for the for attribute of the label, if present
        if (this.getFieldLabel() != null && this.getImage() != null && StringUtils.isNotBlank(this.getImage().getId())) {
            this.getFieldLabel().setLabelForComponentId(this.getImage().getId());
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(image);

        return components;
    }

    /**
     * Retrieves the {@link Image} element wrapped by this field
     *
     * @return the Image element representing the HTML IMG element
     */
    @BeanTagAttribute(name="image",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Image getImage() {
        return image;
    }

    /**
     * Sets the Image to be wrapped by this field
     *
     * @param image the Image element to be wrapped by this field
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Retrieves the URL the image wrapped by this field
     *
     * @see org.kuali.rice.krad.uif.element.Image#getSource()
     * @return the URL for the image
     */
    @BeanTagAttribute(name="source")
    public String getSource() {
        return image.getSource();
    }

    /**
     * Sets the source URL for the Image associated with this field
     *
     * @param source URL for the image
     */
    public void setSource(String source) {
        image.setSource(source);
    }

    /**
     * Provides alternate information for the image element
     *
     * <p>The altText property specifies an alternate text for an image. It is displayed by the browser
     * if the image cannot be displayed.  This is especially important for accessibility, because screen
     * readers can't understand images, but rather will read aloud the alternative text assigned to them.
     * </p>
     *
     * @see org.kuali.rice.krad.uif.element.Image#getAltText()
     * @return alternative information about this image
     */
    @BeanTagAttribute(name="altText")
    public String getAltText() {
        return image.getAltText();
    }

    /**
     * Sets the alternate text attribute of the image assosiated with this field
     *
     * @param altText the alternative information about the image
     */
    public void setAltText(String altText) {
        image.setAltText(altText);
    }

    /**
     * Gets the height of the image
     *
     * @return height
     */
    @BeanTagAttribute(name="height")
    public String getHeight() {
        return image.getHeight();
    }

    /**
     * Sets the height of the image
     *
     * @param height
     */
    public void setHeight(String height) {
        image.setHeight(height);
    }

    /**
     * Sets the width of the image
     *
     * @param width
     */
    public void setWidth(String width) {
        if (image != null) {
            image.setWidth(width);
        }
    }

    /**
     * Gets the width of the image
     *
     * @return width
     */
    @BeanTagAttribute(name="width")
    public String getWidth() {
        return image.getWidth();
    }

    /**
     * Gets the caption header text
     *
     * @return captionHeaderText
     */
    @BeanTagAttribute(name="captionHeaderText")
    public String getCaptionHeaderText() {
        return image.getCaptionHeaderText();
    }

    /**
     * Sets the caption header text
     *
     * @param captionHeaderText
     */
    public void setCaptionHeaderText(String captionHeaderText) {
        image.setCaptionHeaderText(captionHeaderText);
    }

    /**
     * Gets the caption header
     *
     * @return captionHeader
     */
    @BeanTagAttribute(name="captionHeader",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Header getCaptionHeader() {
        return image.getCaptionHeader();
    }

    /**
     * Sets the caption header
     *
     * @param captionHeader
     */
    public void setCaptionHeader(Header captionHeader) {
        image.setCaptionHeader(captionHeader);
    }

    /**
     * Gets the cutline text
     *
     * @return cutlineText
     */
    @BeanTagAttribute(name="cutlineText")
    public String getCutlineText() {
        return image.getCutlineText();
    }

    /**
     * Sets the cutline text
     *
     * @param cutlineText
     */
    public void setCutlineText(String cutlineText) {
        image.setCutlineText(cutlineText);
    }

    /**
     * Gets the cutline
     *
     * @return cutline
     */
    @BeanTagAttribute(name="cutline",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Message getCutline() {
        return image.getCutlineMessage();
    }

    /**
     * Sets the cutline
     *
     * @param cutline
     */
    public void setCutline(Message cutline) {
        image.setCutlineMessage(cutline);
    }

    /**
     * Gets boolen of whether the caption header is above the image
     *
     * @return captionHeaderAboveImage
     */
    @BeanTagAttribute(name="captionHeaderAboveImage")
    public boolean isCaptionHeaderAboveImage() {
        return image.isCaptionHeaderPlacementAboveImage();
    }

    /**
     * Sets boolen of whether the caption header is above the image
     *
     * @param captionHeaderAboveImage
     */
    public void setCaptionHeaderAboveImage(boolean captionHeaderAboveImage) {
        image.setCaptionHeaderPlacementAboveImage(captionHeaderAboveImage);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        ImageField imageFieldCopy = (ImageField) component;

        if (this.image != null) {
            imageFieldCopy.setImage((Image) this.image.copy());
        }
    }
}
