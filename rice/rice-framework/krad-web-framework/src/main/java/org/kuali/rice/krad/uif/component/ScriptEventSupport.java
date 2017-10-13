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
package org.kuali.rice.krad.uif.component;

/**
 * Declares methods for retrieving the event script code
 *
 * <p>
 * The code returned by the get*Script methods will be wrapped in the
 * appropriate event registration code, therefore only the body needs to be
 * returned
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ScriptEventSupport {

	/**
	 * Script that should be executed when the component's onLoad event is fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnLoadScript();

    /**
     * Script that should be executed when the component's onLoad event is fired
     *
     * @return String JavaScript code
     */
    public void setOnLoadScript(String onLoadScript);

	/**
	 * Script to be run when the document ready event is triggered
	 *
	 * @return the onDocumentReadyScript
	 */
	public String getOnDocumentReadyScript();

    /**
     * Setter for the components onDocumentReady script
     *
     * @param onDocumentReadyScript
     */
    public void setOnDocumentReadyScript(String onDocumentReadyScript);

	/**
	 * Script that should be executed when the component's onUnload event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnUnloadScript();

    /**
     * Setter for the components onUnload script
     *
     * @param onUnloadScript
     */
    public void setOnUnloadScript(String onUnloadScript);

	/**
	 * Script that should be executed when the component's onClose event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnCloseScript();

    /**
     * Setter for the components onClose script
     *
     * @param onCloseScript
     */
    public void setOnCloseScript(String onCloseScript);

	/**
	 * Script that should be executed when the component's onBlur event is fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnBlurScript();

    /**
     * Script that should be executed when the component's onBlur event is fired
     *
     * @return String JavaScript code
     */
    public void setOnBlurScript(String onBlurScript);

	/**
	 * Script that should be executed when the component's onChange event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnChangeScript();

    /**
     * Setter for the components onChange script
     *
     * @param onChangeScript
     */
    public void setOnChangeScript(String onChangeScript);

	/**
	 * Script that should be executed when the component's onClick event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnClickScript();

    /**
     * Setter for the components onClick script
     *
     * @param onClickScript
     */
    public void setOnClickScript(String onClickScript);

	/**
	 * Script that should be executed when the component's onDblClick event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnDblClickScript();

    /**
     * Setter for the components onDblClick script
     *
     * @param onDblClickScript
     */
    public void setOnDblClickScript(String onDblClickScript);

	/**
	 * Script that should be executed when the component's onFocus event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnFocusScript();

    /**
     * Setter for the components onFocus script
     *
     * @param onFocusScript
     */
    public void setOnFocusScript(String onFocusScript);

	/**
	 * Script that should be executed when the component's onSubmit event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnSubmitScript();

    /**
     * Setter for the components onSubmit script
     *
     * @param onSubmitScript
     */
    public void setOnSubmitScript(String onSubmitScript);

	/**
	 * Script that should be executed when the component's onKeyPress event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnKeyPressScript();

    /**
     * Setter for the components onKeyPress script
     *
     * @param onKeyPressScript
     */
    public void setOnKeyPressScript(String onKeyPressScript);

	/**
	 * Script that should be executed when the component's onKeyUp event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnKeyUpScript();

    /**
     * Setter for the components onKeyUp script
     *
     * @param onKeyUpScript
     */
    public void setOnKeyUpScript(String onKeyUpScript);

	/**
	 * Script that should be executed when the component's onKeyDown event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnKeyDownScript();

    /**
     * Setter for the components onKeyDown script
     *
     * @param onKeyDownScript
     */
    public void setOnKeyDownScript(String onKeyDownScript);

	/**
	 * Script that should be executed when the component's onMouseOver event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnMouseOverScript();

    /**
     * Setter for the components onMouseOver script
     *
     * @param onMouseOverScript
     */
    public void setOnMouseOverScript(String onMouseOverScript);

	/**
	 * Script that should be executed when the component's onMouseOut event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnMouseOutScript();

    /**
     * Setter for the components onMouseOut script
     *
     * @param onMouseOutScript
     */
    public void setOnMouseOutScript(String onMouseOutScript);

	/**
	 * Script that should be executed when the component's onMouseUp event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnMouseUpScript();

    /**
     * Setter for the components onMouseUp script
     *
     * @param onMouseUpScript
     */
    public void setOnMouseUpScript(String onMouseUpScript);

	/**
	 * Script that should be executed when the component's onMouseDown event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnMouseDownScript();

    /**
     * Setter for the components onMouseDown script
     *
     * @param onMouseDownScript
     */
    public void setOnMouseDownScript(String onMouseDownScript);

	/**
	 * Script that should be executed when the component's onMouseMove event is
	 * fired
	 *
	 * @return String JavaScript code
	 */
	public String getOnMouseMoveScript();

    /**
     * Setter for the components onMouseMove script
     *
     * @param onMouseMoveScript
     */
    public void setOnMouseMoveScript(String onMouseMoveScript);

}
