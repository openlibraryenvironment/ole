/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.document.ContractManagerAssignmentDocument;
import org.kuali.ole.module.purap.exception.PurapConfigurationException;
import org.kuali.ole.module.purap.service.ImageService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Implementation of ImageService.
 */
public class ImageServiceImpl implements ImageService {
    private static Log LOG = LogFactory.getLog(ImageServiceImpl.class);

    private ConfigurationService configurationService;
    private ParameterService parameterService;

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * @see org.kuali.ole.module.purap.dataaccess.ImageDao#getPurchasingDirectorImage(java.lang.String, java.lang.String, java.lang.String)
     */
    public String getPurchasingDirectorImage(String key, String campusCode, String location) {
        LOG.debug("getPurchasingDirectorImage() started");

        String prefix = parameterService.getParameterValueAsString(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapConstants.PURCHASING_DIRECTOR_IMAGE_PREFIX);
        String extension = "." + parameterService.getParameterValueAsString(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapConstants.PURCHASING_DIRECTOR_IMAGE_EXTENSION);
        return getFile(prefix, campusCode, key, extension, location);
    }

    /**
     * @see org.kuali.ole.module.purap.dataaccess.ImageDao#getContractManagerImage(java.lang.String, java.lang.Integer, java.lang.String)
     */
    public String getContractManagerImage(String key, Integer contractManagerId, String location) {
        LOG.debug("getContractManagerImage() started");

        NumberFormat formatter = new DecimalFormat("00");
        String cm = formatter.format(contractManagerId);

        String prefix = parameterService.getParameterValueAsString(ContractManagerAssignmentDocument.class, PurapConstants.CONTRACT_MANAGER_IMAGE_PREFIX);
        String extension = "." + parameterService.getParameterValueAsString(ContractManagerAssignmentDocument.class, PurapConstants.CONTRACT_MANAGER_IMAGE_EXTENSION);
        return getFile(prefix, cm, key, extension, location);
    }

    /**
     * @see org.kuali.ole.module.purap.dataaccess.ImageDao#getLogo(java.lang.String, java.lang.String, java.lang.String)
     */
    public String getLogo(String key, String campusCode, String location) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getLogo() started. key is " + key + ". campusCode is " + campusCode);
        }

        String prefix = parameterService.getParameterValueAsString(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapConstants.LOGO_IMAGE_PREFIX);
        String extension = "." + parameterService.getParameterValueAsString(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapConstants.LOGO_IMAGE_EXTENSION);
        return getFile(prefix, campusCode, key, extension, location);
    }

    /**
     * Copy a file from a web location to the local system.
     *
     * @param prefix   - Prefix for the file name
     * @param fileKey  - File key for file
     * @param key      - Unique key for the file
     * @param location - location of file
     * @return - location to copied file
     */
    protected String getFile(String prefix, String fileKey, String key, String extension, String location) {
        LOG.debug("getFile() started");

        // try retrieving file URL from parameter
        String urlpath = parameterService.getParameterValueAsString(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapConstants.PDF_IMAGE_LOCATION_URL);
        // if parameter value is empty, then try retrieving it from property
        if (StringUtils.isEmpty(urlpath)) {
            urlpath = configurationService.getPropertyValueAsString(OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY);
        }

        if (urlpath == null) {
            throw new PurapConfigurationException("Application Setting " + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + " is missing");
        }
        if (location == null) {
            throw new PurapConfigurationException("Valid location to store temp image files was null");
        }

        String completeUrl = urlpath + prefix + "_" + fileKey.toLowerCase() + extension;
        if (LOG.isDebugEnabled()) {
            LOG.debug("getFile() URL = " + completeUrl);
        }
        return completeUrl;
    }

}
