package org.kuali.ole.select.service.transmission;

import com.jcraft.jsch.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.kuali.ole.select.service.OleTransmissionService;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.businessobject.VendorTransmissionFormatDetail;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 11/7/11
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleTransmissionServiceImpl implements OleTransmissionService {
    private static Log LOG = LogFactory.getLog(OleTransmissionServiceImpl.class);

    public void doSFTPUpload(VendorTransmissionFormatDetail vendorTransmissionFormatDetail, String fileLocation, String preferredFileName) {
        LOG.trace("************************************doSFTPUpload() started************************************");

        JSch jsch = new JSch();
        Session session = null;
        String sftpUsername = vendorTransmissionFormatDetail.getVendorEDIConnectionUserName();
        String sftpHostname = vendorTransmissionFormatDetail.getVendorEDIConnectionAddress();
        String sftpPassword = vendorTransmissionFormatDetail.getVendorEDIConnectionPassword();
        VendorDetail vendorDetail = vendorTransmissionFormatDetail.getVendorDetail();
        try {
            session = jsch.getSession(sftpUsername, sftpHostname, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftpPassword);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            String remoteDir = vendorDetail.getVendorName().toString();
            try {
                sftpChannel.cd(ORDER_RECORDS);
            } catch (SftpException e) {
                sftpChannel.mkdir(ORDER_RECORDS);
                sftpChannel.cd(ORDER_RECORDS);
            }
            try {
                sftpChannel.cd(ORDERS_TO_BE_PROCESSED_BY_VENDOR_FOLDER);
            } catch (SftpException e) {
                sftpChannel.mkdir(ORDERS_TO_BE_PROCESSED_BY_VENDOR_FOLDER);
                sftpChannel.cd(ORDERS_TO_BE_PROCESSED_BY_VENDOR_FOLDER);
            }
            try {
                sftpChannel.cd(remoteDir);
            } catch (SftpException e) {
                sftpChannel.mkdir(remoteDir);
                sftpChannel.cd(remoteDir);
            }
            sftpChannel.put(new FileInputStream(fileLocation), preferredFileName);
            sftpChannel.exit();
        } catch (Exception e) {
            LOG.error("Exception performing SFTP upload of " + preferredFileName + " to " + sftpHostname, e);
            throw new RuntimeException(e);
        } finally {
            session.disconnect();
        }

        LOG.trace("************************************doSFTPUpload() completed************************************");
    }

    public void doFTPUpload(VendorTransmissionFormatDetail vendorTransmissionFormatDetail, String fileLocation, String preferredFileName) {
        LOG.trace("************************************doFTPUpload() started************************************");
        FTPClient ftpClient = new FTPClient();
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        String ftpUsername = vendorTransmissionFormatDetail.getVendorEDIConnectionUserName();
        String ftpHostname = vendorTransmissionFormatDetail.getVendorEDIConnectionAddress();
        String ftpPassword = vendorTransmissionFormatDetail.getVendorEDIConnectionPassword();
        VendorDetail vendorDetail = vendorTransmissionFormatDetail.getVendorDetail();
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("File Location in FTP Server================>" + fileLocation);
                LOG.debug("File source=================================>" + fileLocation);
                LOG.debug("FileName====================================>" + preferredFileName);
            }

            String remoteDir = vendorDetail.getVendorAliases().toString();
            ftpClient.mkd(remoteDir);
            ftpClient.cwd(remoteDir);
            inputStream = new FileInputStream(fileLocation);
            ftpClient.storeFile(preferredFileName, inputStream);

            ftpClient.logout();
            inputStream.close();
        } catch (Exception e) {
            LOG.error("Exception performing SFTP upload of " + preferredFileName + " to " + ftpHostname, e);
            throw new RuntimeException(e);
        }
        LOG.trace("************************************doFTPUpload() completed************************************");
    }


    @Override
    public void doSFTPUpload(String sftpHostname, String sftpUsername, String sftpPassword, String file, String fileName) {
        /*
       Left blank as OLE providing custom implementation
        */
    }

    @Override
    public void doFTPUpload(String ftpHostname, String ftpUsername, String ftpPassword, String file, String fileName) {
        /*
       Left blank as OLE providing custom implementation
        */
    }
}
