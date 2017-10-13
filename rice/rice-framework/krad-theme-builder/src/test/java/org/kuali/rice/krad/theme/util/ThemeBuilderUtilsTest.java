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
package org.kuali.rice.krad.theme.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test cases for {@link ThemeBuilderUtils}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ThemeBuilderUtilsTest {

    /**
     * Test the path between two files is correctly determined
     */
    @Test
    public void testCalculatePathToFile() {
        File fromFile = new File("/basedir/subDir1/foo.css");
        File toFile = new File("/basedir/subDir2/foo.css");

        String path = ThemeBuilderUtils.calculatePathToFile(fromFile, toFile);

        Assert.assertEquals("Path incorrect for up one dir", "../subDir2/", path);

        fromFile = new File("/basedir/subDir1/foo.css");
        toFile = new File("/basedir/subDir1/foo.css");

        path = ThemeBuilderUtils.calculatePathToFile(fromFile, toFile);

        Assert.assertEquals("Path incorrect for same dir", "", path);

        fromFile = new File("/basedir/dir1/subDir1/foo.css");
        toFile = new File("/basedir/dir2/subDir2/foo.css");

        path = ThemeBuilderUtils.calculatePathToFile(fromFile, toFile);

        Assert.assertEquals("Path incorrect for up two dir", "../../dir2/subDir2/", path);

        fromFile = new File("/basedir/subDir1/foo.css");
        toFile = new File("/basedir/subDir1/moreDir/foo.css");

        path = ThemeBuilderUtils.calculatePathToFile(fromFile, toFile);

        Assert.assertEquals("Path incorrect for sub dir", "moreDir/", path);
    }

    /**
     * Tests the addition of a string to a string array
     */
    @Test
    public void testAddStringToArray() {
        String[] array1 = new String[] {"a1", "a2"};
        String stringToAdd = "b1";

        String[] combinedArray = ThemeBuilderUtils.addToArray(array1, stringToAdd);

        Assert.assertEquals("Combined array length is not correct", 3, combinedArray.length);

        Assert.assertEquals("a1", combinedArray[0]);
        Assert.assertEquals("a2", combinedArray[1]);
        Assert.assertEquals("b1", combinedArray[2]);
    }

    /**
     * Tests the addition of string arrays
     */
    @Test
    public void testAddArrayToArray() {
        String[] array1 = new String[] {"a1", "a2"};
        String[] array2 = new String[] {"b1", "b2", "b3"};

        String[] combinedArray = ThemeBuilderUtils.addToArray(array1, array2);

        Assert.assertEquals("Combined array length is not correct", 5, combinedArray.length);

        Assert.assertEquals("a1", combinedArray[0]);
        Assert.assertEquals("a2", combinedArray[1]);
        Assert.assertEquals("b1", combinedArray[2]);
        Assert.assertEquals("b2", combinedArray[3]);
        Assert.assertEquals("b3", combinedArray[4]);

        array2 = null;

        combinedArray = ThemeBuilderUtils.addToArray(array1, array2);

        Assert.assertEquals("Combined array length is not correct", 2, combinedArray.length);

        Assert.assertEquals("a1", combinedArray[0]);
        Assert.assertEquals("a2", combinedArray[1]);

        array1 = null;
        array2 = new String[] {"b1", "b2", "b3"};

        combinedArray = ThemeBuilderUtils.addToArray(array1, array2);

        Assert.assertEquals("Combined array length is not correct", 3, combinedArray.length);

        Assert.assertEquals("b1", combinedArray[0]);
        Assert.assertEquals("b2", combinedArray[1]);
        Assert.assertEquals("b3", combinedArray[2]);
    }
}
