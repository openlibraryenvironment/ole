///*
// * Copyright 2011 The Kuali Foundation.
// *
// * Licensed under the Educational Community License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.opensource.org/licenses/ecl2.php
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.kuali.ole;
//
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by IntelliJ IDEA.
// * User: peris
// * Date: 5/18/11
// * Time: 10:54 AM
// * To change this template use File | Settings | File Templates.
// */
//public class CategoryInfoObject {
//    private String category;
//    private String type;
//    private List<String> formats;
//    private Map<String, String> directoryPath;
//    private Map<String, List<File>> files;
//
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public List<String> getFormats() {
//        return formats;
//    }
//
//    public void setFormats(List<String> formats) {
//        this.formats = formats;
//    }
//
//    public Map<String, String> getDirectoryPath() {
//        return directoryPath;
//    }
//
//    public void setDirectoryPath(Map<String, String> directoryPath) {
//        this.directoryPath = directoryPath;
//    }
//
//    public String getDirectoryPathForFormat(String format) {
//        return directoryPath.get(format);
//    }
//
//    public Map<String, List<File>> getFiles() {
//        return files;
//    }
//
//    public void setFiles(Map<String, List<File>> files) {
//        this.files = files;
//    }
//
//    public List<File> getFiles(String format) {
//        if (null != files) {
//            return files.get(format);
//        }
//        return null;
//    }
//}
