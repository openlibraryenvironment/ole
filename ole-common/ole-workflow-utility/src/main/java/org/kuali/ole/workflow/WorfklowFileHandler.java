package org.kuali.ole.workflow;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * User: peris
 * Date: 12/16/12
 * Time: 8:17 AM
 */
public class WorfklowFileHandler {

    private String workflowXMLSrcDir;
    private String workflowXMLDestDir;
    private File destDir;
    private String tmpDir;

    /**
     * This method is the main method that copies the xml files from the srouce
     * directory to the destination directory. This takes the relative path from
     * WEB-INF/classes directory and hence the soruce should be present under the
     * directory. The destination can be anywhere in the file system and has to be
     * an absolute path.
     */
    public void execute() {
        File xmlWorkflowSourceDir = null;
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        try {
            File file = null;
            if (resource != null) {
                file = new File(resource.toURI());
                if (null != file && file.isDirectory()) {
                    xmlWorkflowSourceDir = getXMLWorkflowSourceDirRelativeToPath(file);
                    iterateDir(xmlWorkflowSourceDir);
                }
            } else {
                Enumeration<URL> resources = null;
                resources = ClassLoader.getSystemClassLoader().getResources("");
                File sourceDirctory = new File(resources.nextElement().getFile(), getWorkflowXMLSrcDir());
                iterateDir(sourceDirctory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file - Name of the folder
     * @return Folder - Since the source is relative to WEB-INF/classes, thie method
     *         returns the folder that would contain the xml files or sub-folders with the xml files
     *         after traversing to the directory specified in the workflowXMLSrcDir.
     */
    private File getXMLWorkflowSourceDirRelativeToPath(File file) {
        String dir = getWorkflowXMLSrcDir();
        StringTokenizer stringTokenizer = new StringTokenizer(dir, "/");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            file = getFolder(file, token);
        }

        return file;
    }

    private File getFolder(File file, String token) {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file1 = files[i];
            if (file1.getName().equals(token)) {
                return file1;
            }
        }
        return null;
    }

    /**
     * @param sourceDirctory
     * @throws IOException This method iterates through the folder to copy any xml files found in the path.
     *                     The assumption here would be the xml files are valid workflow xml files.
     */
    private void iterateDir(File sourceDirctory) throws IOException {
        File[] files = sourceDirctory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
//            if (FilenameUtils.getExtension(file.getName()).contains("xml")) {
//                copyFileToDestDir(file);
//            } else if (file.isDirectory()) {
//                iterateDir(file);
//            }
            copyFileToDestDir(file);
        }
    }

    private void copyFileToDestDir(File inputFile) throws IOException {
//        InputStream fileInputStream = new FileInputStream(inputFile);
//        File tempFile = new File(getTempDir(), inputFile.getName());
//        IOUtils.copy(fileInputStream, new FileOutputStream(tempFile));
//        FileUtils.copyFileToDirectory(tempFile, getDestDir());

        if (inputFile.isDirectory()) {
            FileUtils.copyDirectoryToDirectory(inputFile, getDestDir());
        } else if (inputFile.isFile() && FilenameUtils.getExtension(inputFile.getName()).contains("xml")) {
            FileUtils.copyFileToDirectory(inputFile, getDestDir());
        }
    }

    public File getDestDir() {
        if (null == destDir) {
            destDir = new File(workflowXMLDestDir);
        }
        return destDir;
    }

    public String getTempDir() {
        if (null == tmpDir) {
            tmpDir = System.getProperty("java.io.tmpdir");
        }
        return tmpDir;
    }

    public String getWorkflowXMLSrcDir() {
        return workflowXMLSrcDir;
    }

    public void setWorkflowXMLSrcDir(String workflowXMLSrcDir) {
        this.workflowXMLSrcDir = workflowXMLSrcDir;
    }

    public String getWorkflowXMLDestDir() {
        return workflowXMLDestDir;
    }

    public void setWorkflowXMLDestDir(String workflowXMLDestDir) {
        this.workflowXMLDestDir = workflowXMLDestDir;
    }
}
