/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;

import constants.ToValue;

/**
 *
 * @author gauravsi
 */
public class Configurations {
	public final static Logger log = Logger.getLogger(Configurations.class);

	private static ToValue globalValueType=ToValue.TO_DEC; 
    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public static void removePrefrences() {
        Preferences prefs = Preferences.userNodeForPackage(Configurations.class);
        prefs.remove("filePath");

    }

    public static File getWorkSpaceFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Configurations.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            log.info("found workspace path: " + filePath);
            //return filePath;
            File file = new File(filePath);
            if(file.exists()){
                return new File(filePath);
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public static void setFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Configurations.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
        } else {
            prefs.remove("filePath");

        }
    }

}
