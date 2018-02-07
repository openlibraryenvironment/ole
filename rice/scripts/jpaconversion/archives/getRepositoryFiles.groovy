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
/* Configurable Fields */
def ojbMappingPattern = ~/.*OJB.*repository.*xml/
def projHome = '/java/projects/play/rice-1.1.0'

/* End User Configurable Fields */
def sourceDirectories = []
def repositories = []

getRespositoryFiles(projHome, ojbMappingPattern, repositories, sourceDirectories)

println 'Found '+repositories.size().toString()+' OJB mapping files:'
repositories.each {println it}
println 'Found the files in the following '+sourceDirectories.size().toString()+' Source Directories:'
sourceDirectories.each {println it}

def getRespositoryFiles(String projHome, ojbMappingPattern, ArrayList repositories, ArrayList sourceDirectories){
    repositories.clear()
    sourceDirectories.clear()

    // local helpers
    def addRepository = { File f -> 
            repositories.add( f.getPath() );
            sourceDirectories.add( f.getParent() )
            }

    def dir = new File(projHome)

    println 'directoryName='+dir.getPath()
    println 'ojbMappingPattern='+ojbMappingPattern

    dir.eachFileMatch(ojbMappingPattern, addRepository)
    dir.eachDirRecurse { File myFile ->
        myFile.eachFileMatch(ojbMappingPattern, addRepository)
        }

}



