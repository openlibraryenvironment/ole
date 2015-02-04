/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.PreBag;

/**
 * Class for Utility operations on File Compression.
 *
 * @author Rajesh Chowdary K
 * @created May 24, 2012
 */
public class CompressUtils {

    private static BagFactory bagFactory = new BagFactory();
    private static final int BUFFER_SIZE = 1024;
    private static final String DATA_DIR = "data/";
    private static final Form FILENAME_NORMALIZATION_FORM = Form.NFC;

    /**
     * Method to zip all files in a given directory.
     *
     * @param sourceDir
     * @return
     * @throws IOException
     */
    public File createZipFile(File sourceDir) throws IOException {
        File zipFile = File.createTempFile("tmp", ".zip");
        String path = sourceDir.getAbsolutePath();
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFile));
        ArrayList<File> fileList = getAllFilesList(sourceDir);
        for (File file : fileList) {
            ZipEntry ze = new ZipEntry(file.getAbsolutePath().substring(path.length() + 1));
            zip.putNextEntry(ze);
            FileInputStream fis = new FileInputStream(file);
            IOUtils.copy(fis, zip);
            fis.close();
            zip.closeEntry();
        }
        zip.close();
        return zipFile;
    }

    /**
     * Method to create a zipped bag file from a given source directory.
     *
     * @param sourceDir
     * @return
     * @throws IOException
     */
    public File createZippedBagFile(File sourceDir) throws IOException {
        File tempDir = File.createTempFile("tmp", ".dir");
        FileUtils.deleteQuietly(tempDir);
        File bagDir = new File(tempDir, "bag_dir");
        bagDir.mkdirs();
        FileUtils.copyDirectory(sourceDir, bagDir);
        PreBag preBag;
        synchronized (bagFactory) {
            preBag = bagFactory.createPreBag(bagDir);
        }
        preBag.makeBagInPlace(BagFactory.Version.V0_96, false);
        File zipFile = createZipFile(tempDir);
        FileUtils.deleteQuietly(tempDir);
        return zipFile;
    }

    /**
     * Method to extract a given zipped bag file to a given output directory or to a temp directory if toDir is null.
     *
     * @param bagFilePath
     * @param toDir
     * @return
     * @throws IOException
     */
    public File extractZippedBagFile(String bagFilePath, String toDir) throws IOException {
        File bagFile = new File(bagFilePath);
        File extractDir = null;
        if (toDir != null && toDir.trim().length() != 0)
            extractDir = new File(toDir);
        else
            extractDir = File.createTempFile("tmp", ".ext");
        FileUtils.deleteQuietly(extractDir);
        extractDir.mkdirs();

        byte[] buffer = new byte[BUFFER_SIZE];
        ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(bagFile)));
        ZipEntry next;
        while ((next = zip.getNextEntry()) != null) {
            String name = next.getName().replace('\\', '/').replaceFirst("[^/]*/", "");
            if (name.startsWith(DATA_DIR)) {
                File localFile = new File(extractDir, Normalizer.normalize(name.substring(DATA_DIR.length()), FILENAME_NORMALIZATION_FORM));
                if (next.isDirectory()) {
                    if (!localFile.exists() && !localFile.mkdir())
                        throw new IOException("error creating local directories in output directory");
                } else {
                    File parent = localFile.getParentFile();
                    if (!parent.exists() && !parent.mkdirs())
                        throw new IOException("error creating local directories in output directory");
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
                    int bytesRead;
                    while ((bytesRead = zip.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    bos.close();
                }
            }
            else {
                File localFile = new File(extractDir, name);
                if (next.isDirectory()) {
                    if (!localFile.exists() && !localFile.mkdir())
                        throw new IOException("error creating local directories in output directory");
                } else {
                    File parent = localFile.getParentFile();
                    if (!parent.exists() && !parent.mkdirs())
                        throw new IOException("error creating local directories in output directory");
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
                    int bytesRead;
                    while ((bytesRead = zip.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    bos.close();
                }
            }
        }
        zip.close();
        return extractDir;
    }

    /**
     * Method to get All Files List in a given directory.
     *
     * @param directory
     * @return
     */
    public ArrayList<File> getAllFilesList(File directory) {
        ArrayList<File> fileList = new ArrayList<File>();
        if (directory.isFile())
            fileList.add(directory);
        else if (directory.isDirectory())
            for (File innerFile : directory.listFiles())
                fileList.addAll(getAllFilesList(innerFile));
        return fileList;
    }

    public void deleteFiles(List<File> files) {
        try {
            for (File file : files) {
                try {
                    file.delete();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

}
