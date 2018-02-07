/**
* SyncEclipseSettings.groovy
*
* A groovy script which copies certain Eclipse .settings files from the rice-development-tools project
* to all Rice modules.
*
* This should be executed relative to the root of the Rice project.  To do this, execute the following command:
*
* groovy config/ide/eclipse/SyncEclipseSettings.groovy
*/

filesToCopy = [ 'development-tools/.settings/org.eclipse.jdt.ui.prefs', 'development-tools/.settings/org.eclipse.jdt.core.prefs' ]
settingsDirectory = new File('.settings')
assert settingsDirectory.exists(), 'Settings directory does not exist!'
assert settingsDirectory.isDirectory(), 'Settings directory must be a directory!'
 
ant = new AntBuilder()

def rootDir = new File('.')
rootDir.eachDirRecurse {
   checkForAndInstallSettings(it)
}

def checkForAndInstallSettings(directory) {
   if (new File(directory, ".project").exists()) {
	   settingsPath = new File(directory, ".settings").absolutePath
	   println 'Copying settings files to ' + settingsPath
	   filesToCopy.each {
		   ant.copy(file:it, todir:settingsPath)
	   }
   }
}
