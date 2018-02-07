<#--

    Copyright 2005-2014 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<#-- creates the script event registration code for the events
supported and configured on the component -->

<#macro eventScript component>

    <#local eventHandlers>
        <#if component.onLoadScript?has_content>
          jQuery('#' + '${component.id}').load(function(e) {
            ${component.onLoadScript}
          });
        </#if>

        <#if component.onDocumentReadyScript?has_content>
          jQuery(document).ready(function(e) {
            ${component.onDocumentReadyScript}
          });
        </#if>

        <#if component.onUnloadScript?has_content>
          jQuery('#' + '${component.id}').unload(function(e) {
            ${component.onUnloadScript}
          });
        </#if>

        <#if component.onBlurScript?has_content>
          jQuery('#' + '${component.id}').blur(function(e) {
            ${component.onBlurScript}
          });
        </#if>

        <#if component.onChangeScript?has_content>
          jQuery('#' + '${component.id}').change(function(e) {
            ${component.onChangeScript}
          });
        </#if>

        <#if component.onClickScript?has_content>
          jQuery('#' + '${component.id}').click(function(e) {
            ${component.onClickScript}
          });
        </#if>

        <#if component.onDblClickScript?has_content>
          jQuery('#' + '${component.id}').dblclick(function(e) {
            ${component.onDblClickScript}
          });
        </#if>

        <#if component.onFocusScript?has_content>
          jQuery('#' + '${component.id}').focus(function(e) {
            ${component.onFocusScript}
          });
        </#if>

        <#if component.onKeyPressScript?has_content>
          jQuery('#' + '${component.id}').keypress(function(e) {
            ${component.onKeyPressScript}
          });
        </#if>

        <#if component.onKeyUpScript?has_content>
          jQuery('#' + '${component.id}').keyup(function(e) {
            ${component.onKeyUpScript}
          });
        </#if>

        <#if component.onKeyDownScript?has_content>
          jQuery('#' + '${component.id}').keydown(function(e) {
            ${component.onKeyDownScript}
          });
        </#if>

        <#if component.onMouseOverScript?has_content>
          jQuery('#' + '${component.id}').mouseover(function(e) {
            ${component.onMouseOverScript}
          });
        </#if>

        <#if component.onMouseOutScript?has_content>
          jQuery('#' + '${component.id}').mouseout(function(e) {
            ${component.onMouseOutScript}
          });
        </#if>

        <#if component.onMouseUpScript?has_content>
          jQuery('#' + '${component.id}').mouseup(function(e) {
            ${component.onMouseUpScript}
          });
        </#if>

        <#if component.onMouseDownScript?has_content>
          jQuery('#' + '${component.id}').mousedown(function(e) {
            ${component.onMouseDownScript}
          });
        </#if>

        <#if component.onMouseMoveScript?has_content>
          jQuery('#' + '${component.id}').mousemove(function(e) {
            ${component.onMouseMoveScript}
          });
        </#if>
    </#local>

    <#local eventHandlers="${eventHandlers?trim}"/>
    <#if eventHandlers?has_content>
        <@krad.script component=component value="${eventHandlers}" />
    </#if>

</#macro>