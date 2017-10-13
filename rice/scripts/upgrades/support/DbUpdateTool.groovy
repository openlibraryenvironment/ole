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
import javax.swing.filechooser.FileNameExtensionFilter
import java.sql.Connection
import java.sql.DriverManager

/**
 * Database update tool that supports multiple pluggable actions.
 *
 * To run:
 * <pre>
 * java -classpath /path/to/mysql-connector-java-5.1.6.jar:/path/to/groovy-all-1.8.1.jar groovy.ui.GroovyMain support/DbUpdateTool.groovy jdbc:mysql://10.0.0.3/schema user pass <command> <args>
 * </pre>
 *
 * (could not find a way to use -jar flag with additional classpath for jdbc drivers)
 */

abstract class DbCommand {
    /**
     * If we had metadata that identified db version, this would be the place to check
     * whether the command applied
     */
    def boolean isValid(Connection c) { true }
    def abstract help()
    def abstract perform(Connection c, List<String> args)
}

/**
 * A map of all the implemented commands
 */
COMMANDS = [:]

/* get the path of this file */
def File getScriptPath() {
    def url = this.class.protectionDomain.codeSource.location
    try {
        new File(url.toURI())
    } catch(URISyntaxException e) {
        new File(url.getPath())
    }
}

/* iterate over scripts that match a specified suffix, calling specified closure */
def void enumerate_scripts(File dir, String pattern, Closure c = { -> }) {
    dir.listFiles([accept: { file, filename -> filename.endsWith(pattern) }] as FilenameFilter).each {
        def class_name = (it.name =~ /(.*)${pattern}$/)[0][1]
        c.call(class_name)
    }
}

/* loads command classes for all "*Command.groovy" files in the specified dir */
def Map loadCommands(File dir) {
    def commands = [:]
    enumerate_scripts(dir, "Command.groovy", {
      name ->
        println "Loading command " + name
        commands[name] = Class.forName(name + "Command", true, this.class.classLoader)
    })
    commands
}

/* create a new command */
def DbCommand createCommand(String name) {
    COMMANDS[name].newInstance()
}

/* try to load jdbc drivers */
def loadDrivers() {
    ["com.mysql.jdbc.Driver", "oracle.jdbc.OracleDriver"].each {
        try {
            Class.forName(it)
        } catch (Exception e) {
            println e
        }
    }
}

def findLibDirs(script_dir, list) {
    script_dir.eachDir {
        if (it.name == "scripts") {
            list << it
        } else {
            findLibDirs(it, list)
        }
    }
    list
}

// start up two levels from script location
def script_dir = getScriptPath().parentFile.parentFile
def lib_dirs = findLibDirs(script_dir, [])

// add 'lib' dir to classloader
// this.class.classLoader.rootLoader not available in this version of Groovy (< 1.8.2 ?)...
//this.class.classLoader.rootLoader.addURL(lib_dir.toURL())
lib_dirs.each {
    this.class.classLoader.addURL(it.toURL())
}
COMMANDS = [:]
/* load all the commands */
lib_dirs.each {
    COMMANDS += loadCommands(it)
}

if (args.length < 4) {
    println 'usage: groovy DbUpdateTool.groovy <jdbc url> <username> <pass> <command> <args>'
    println()
    println 'example: java -classpath /path/to/mysql-connector-java-5.1.6.jar:/path/to/groovy-all-1.8.1.jar groovy.ui.GroovyMain support/DbUpdateTool.groovy jdbc:mysql://10.0.0.3/schema user pass <command> <args>'
    println()
    println 'Commands'
    println()
    // no command specified? - print help for all commands
    if (COMMANDS.isEmpty()) {
        println "no commands defined in lib dirs:"
        println "\t" + lib_dirs
    } else {
        COMMANDS.each { key, value ->
            printf("%-40.40s %s\n", key, createCommand(key).help())
        }
    }
    return
}


def url = args[0]
def user = args[1]
def pass = args[2]
def command_name = args[3]
def command_args = args.length > 4 ? args[4..-1] : []

// load jdbc drivers and get a connection to the db
loadDrivers()
def con = DriverManager.getConnection(url, user, pass)

def command = createCommand(command_name)
if (!command.isValid(con)) {
    println "${command_name} is not valid for this database"
} else {
    command.perform(con, args as List<String>)
}