/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import logger.Logger;
import models.ConfigWrapper;

/**
 *
 * @author gauravsi
 */
public class Configurations {

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
            Logger.info("found workspace path: " + filePath);
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

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public static void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ConfigWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ConfigWrapper wrapper = (ConfigWrapper) um.unmarshal(file);

            Map<String, String> found = wrapper.getConfigs();

            // Save the file path to the registry.
            setFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public static void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ConfigWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            ConfigWrapper wrapper = new ConfigWrapper();
            Map save = new HashMap<String, String>();
            wrapper.setConfigs(save);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}
