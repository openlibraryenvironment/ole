/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.module.purap.transmission.service.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.transmission.service.TransmissionService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * This class is for performing operations on SFTP Server
 */
public class TransmissionServiceImpl implements TransmissionService {
    private static Log LOG = LogFactory.getLog(TransmissionServiceImpl.class);
    protected OlePurapService olePurapService;

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    @Override
    public void doSFTPUpload(String sftpHostname, String sftpUsername, String sftpPassword, String file, String fileName) {
        LOG.trace("************************************doSFTPUpload() started************************************");

        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(sftpUsername, sftpHostname, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftpPassword);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.mkdir("");
            sftpChannel.put(new FileInputStream(file), fileName);
            sftpChannel.exit();
            session.disconnect();
        } catch (Exception e) {
            LOG.error("Exception performing SFTP upload of " + file + " to " + sftpHostname, e);
            throw new RuntimeException(e);
        }

        LOG.trace("************************************doSFTPUpload() completed************************************");
    }

    /**
     * This method is to perform file upload
     *
     * @param ftpHostname
     * @param ftpUsername
     * @param ftpPassword
     * @param file
     * @param fileName
     */
    @Override
    public void doFTPUpload(String ftpHostname, String ftpUsername, String ftpPassword, String file, String fileName) {
        LOG.trace("************************************doFTPUpload() started************************************");
        FTPClient ftpClient = new FTPClient();
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            ftpClient.connect(ftpHostname);
            ftpClient.login(ftpUsername, ftpPassword);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                LOG.debug("Connected to FTP server.");
            } else {
                LOG.debug("FTP server refused connection.");
            }

            // upload
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String fileLocation = getFileLocation();
            if (LOG.isDebugEnabled()) {
                LOG.debug("File Location in FTP Server================>" + fileLocation);
                LOG.debug("File source=================================>" + file);
                LOG.debug("FileName====================================>" + fileName);
            }
            ftpClient.mkd(fileLocation);
            ftpClient.cwd(fileLocation);
            inputStream = new FileInputStream(file);
            ftpClient.storeFile(fileName, inputStream);

            ftpClient.logout();
            inputStream.close();
        } catch (Exception e) {
            LOG.error("Exception performing SFTP upload of " + file + " to " + ftpHostname, e);
            throw new RuntimeException(e);
        }
        LOG.trace("************************************doFTPUpload() completed************************************");
    }

    /**
     * This method is to get the directory in the Server
     *
     * @return
     */
    public String getFileLocation() {
        String fileLocation = getOlePurapService().getParameter(OLEConstants.VENDOR_DIRECTORY);
        return fileLocation;
    }
}
