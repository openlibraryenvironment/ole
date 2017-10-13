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
package org.kuali.rice.krad.uif.element;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ErrorReport;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Content element that renders a HTML <code>&lt;IMG&gt;</code> tag
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "image-bean", parent = "Uif-Image"),
        @BeanTag(name = "helpImage-bean", parent = "Uif-HelpImage"),
        @BeanTag(name = "quickLookupImage-bean", parent = "Uif-QuickLookupImage"),
        @BeanTag(name = "directInquiryImage-bean", parent = "Uif-DirectInquiryImage")})
public class Image extends ContentElementBase {
    private static final long serialVersionUID = -3911849875276940507L;

    private String source;
    private String altText;
    private String height;
    private String width;

    private boolean captionHeaderPlacementAboveImage;

    private String captionHeaderText;
    private Header captionHeader;

    private String cutlineText;
    private Message cutlineMessage;

    public Image() {
        super();

        altText = "";
    }

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>Initializes the cutline message and caption header components if necessary</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        if ((StringUtils.isNotBlank(captionHeaderText) || (getPropertyExpression("captionHeaderText") != null)) && (
                captionHeader == null)) {
            captionHeader = ComponentFactory.getImageCaptionHeader();
            view.assignComponentIds(captionHeader);
        }

        if ((StringUtils.isNotBlank(cutlineText) || (getPropertyExpression("cutlineText") != null)) && (cutlineMessage
                == null)) {
            cutlineMessage = ComponentFactory.getImageCutlineMessage();
            view.assignComponentIds(cutlineMessage);
        }
    }

    /**
     * Performs the following steps
     *
     * <ul>
     * <li>Set the caption header text on the caption header</li>
     * <li>Set the cutline text on the cutline message</li>
     * </ul>
     *
     * @see Component#performFinalize(org.kuali.rice.krad.uif.view.View, java.lang.Object,
     * org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (StringUtils.isNotBlank(captionHeaderText)) {
            captionHeader.setHeaderText(captionHeaderText);
        }

        if (StringUtils.isNotBlank(cutlineText)) {
            cutlineMessage.setMessageText(cutlineText);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(captionHeader);
        components.add(cutlineMessage);

        return components;
    }

    /**
     * returns the URL of this image
     *
     * @return the URL of this image.
     */
    @BeanTagAttribute(name="source")
    public String getSource() {
        return this.source;
    }

    /**
     * Sets the URL of this image
     *
     * @param source the URL of this image
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Provides alternate information for the image element
     *
     * <p>The altText property specifies an alternate text for an image. It is displayed by the browser
     * if the image cannot be displayed.  This is especially important for accessibility, because screen
     * readers can't understand images, but rather will read aloud the alternative text assigned to them.
     * <br>
     * Some best practices:
     * <ul>
     * <li>spacer images, bullets, and icons should have the altText set to null or the empty string. This
     * will prevent screen readers from announcing it.</li>
     * <li>Make the altText message as short and succinct as possible</li>
     * <li>Describe the content of the image and nothing more</li>
     * </ul>
     * </p>
     *
     * @return alternative information about this image
     */
    @BeanTagAttribute(name="altText")
    public String getAltText() {
        return this.altText;
    }

    /**
     * Sets the alternate text property for this image
     *
     * @param altText the alternative information about the image
     */
    public void setAltText(String altText) {
        this.altText = altText;
    }

    /**
     * Returns the height style attribute of this image
     *
     * <p>
     * The default unit of measure is pixels.<br>
     * It is good practice to specify both the height and width attributes for an image.
     * If these attributes are set, the space required for the image is reserved when the page is loaded.
     * However, without these attributes, the browser does not know the size of the image. The effect will
     * be that the page layout will change while the images load.
     * </p>
     *
     * @return the height style attribute of this image
     */
    @BeanTagAttribute(name="height")
    public String getHeight() {
        return this.height;
    }

    /**
     * Sets the height style attribute of the image.
     *
     * @param height the height of the image
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * Returns the width style attribute of the image
     *
     * <p>
     * The default unit of measure is pixels.<br>
     * It is good practice to specify both the height and width attributes for an image.
     * If these attributes are set, the space required for the image is reserved when the page is loaded.
     * However, without these attributes, the browser does not know the size of the image. The effect will
     * be that the page layout will change while the images load.
     * <p>
     *
     * @return the width of this image
     */
    @BeanTagAttribute(name="width")
    public String getWidth() {
        return width;
    }

    /**
     * Sets the width style attribute of the image
     *
     * @param width the width of this image
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Retrieves the caption text for this image
     *
     * <p>
     * The caption text is a headline for the picture. It may be displayed either above or below the picture.
     * </p>
     *
     * @return the caption
     */
    @BeanTagAttribute(name="captionHeaderText")
    public String getCaptionHeaderText() {
        return captionHeaderText;
    }

    /**
     * Sets the text displayed as of the caption for the picture
     *
     * @param captionHeaderText the caption text
     */
    public void setCaptionHeaderText(String captionHeaderText) {
        this.captionHeaderText = captionHeaderText;
    }

    /**
     * Retrieves the {@link Header} component used to display the caption for this image
     *
     * @return Header component which wraps the caption text.
     */
    @BeanTagAttribute(name="captionHeader",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Header getCaptionHeader() {
        return captionHeader;
    }

    /**
     * Sets the Header used to display the caption for this image
     *
     * @param captionHeader header component which wraps the caption text
     */
    public void setCaptionHeader(Header captionHeader) {
        this.captionHeader = captionHeader;
    }

    /**
     * Retrieves the cutline text for this image
     *
     * <p>
     * The cutline text give more detailed information about the picture. Generally it describes
     * the who, what, where, when of this image.
     * </p>
     *
     * @return the cutline text.
     */
    @BeanTagAttribute(name="cutlineText")
    public String getCutlineText() {
        return cutlineText;
    }

    /**
     * Sets the cutline text that describes this image
     *
     * @param cutlineText the cutline text that describes this image
     */
    public void setCutlineText(String cutlineText) {
        this.cutlineText = cutlineText;
    }

    /**
     * Gets the {@link Message} component used to display the cutline.
     *
     * <p>
     * Wrapping the cutline text with a Message component allows styling of the cutline text.
     * </p>
     *
     * @return Message component wrapping the cutline
     */
    @BeanTagAttribute(name="cutlineMessage",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Message getCutlineMessage() {
        return cutlineMessage;
    }

    /**
     * Sets the Message component used to display the cutline for this image
     *
     * @param cutlineMessage Message
     */
    public void setCutlineMessage(Message cutlineMessage) {
        this.cutlineMessage = cutlineMessage;
    }

    /**
     * Specifies whether the image caption is to be displayed above or below the image
     *
     * @return true if the caption is to be displayed above the image. false if displayed below the image.
     */
    @BeanTagAttribute(name="captionHeaderPlacmentAboveImage")
    public boolean isCaptionHeaderPlacementAboveImage() {
        return captionHeaderPlacementAboveImage;
    }

    /**
     * Sets whether the image caption is to be displayed above or below the image
     *
     * @param captionHeaderPlacementAboveImage true displays above image, false displays below image
     */
    public void setCaptionHeaderPlacementAboveImage(boolean captionHeaderPlacementAboveImage) {
        this.captionHeaderPlacementAboveImage = captionHeaderPlacementAboveImage;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer){
        tracer.addBean(this);

        // Checks that a source is set
        if(getSource()==null){
            if(!Validator.checkExpressions(this, "source")){
                String currentValues [] = {"source ="+getSource()};
                tracer.createError("Source must be set",currentValues);
            }
        }

        // Checks that alt text is set
        if(getAltText().compareTo("")==0){
            if(Validator.checkExpressions(this, "altText")){
                String currentValues [] = {"altText ="+getAltText()};
                tracer.createWarning("Alt text should be set, violates accessibility standards if not set",currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Image imageCopy = (Image) component;
        imageCopy.setAltText(this.altText);

        if (this.captionHeader != null) {
            imageCopy.setCaptionHeader((Header)this.captionHeader.copy());
        }

        imageCopy.setCaptionHeaderPlacementAboveImage(this.captionHeaderPlacementAboveImage);
        imageCopy.setCaptionHeaderText(this.captionHeaderText);

        if (this.cutlineMessage != null) {
            imageCopy.setCutlineMessage((Message)this.cutlineMessage.copy());
        }

        imageCopy.setCutlineText(this.cutlineText);
        imageCopy.setHeight(this.height);
        imageCopy.setSource(this.source);
        imageCopy.setWidth(this.width);
    }
}
