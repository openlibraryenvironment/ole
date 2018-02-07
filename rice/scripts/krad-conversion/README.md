KNS KRAD Conversion Script
---
Note: This script is currently still IN DEVELOPMENT should NOT used for converting kns applications.

Purpose:
---
To produce a non-destructive testable krad-compatible screens from existing KNS forms

Assumptions:
---
Application being modified has portalBody and portalTab tag that follows Rice convention (used for adding tab for new area)


Usage:
---
Locate the project directory and a target directory for running your conversion script.

Create a new file (project.conversion.properties) or modify the krad.conversion.properties.  Make sure to update the input
and output directories as well as the project information.

If running from Intellij, add the scripts/krad-conversion as a new module
    File > New Module > [ Select scripts/krad-conversion ]  > Build from existing module > Maven > Next
    Update maven imports

Update the project artifact settings as necessary and the input.dir and output.dir settings.  Be sure to include
a full directory path and that the output.dir contains no resources as it will be cleaned before building the new project

Run 'mvn install'

The script will generate a war overlay structure in order to maintain the dependencies and original files of the app
without requiring the need to copy in place.

Once the script is complete, run 'mvn clean package' on the target project

You can then open the project in eclipse (if so you may wish to run eclipse:eclipse) or intellij
