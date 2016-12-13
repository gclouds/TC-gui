/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.util.List;
import java.util.Map;

import constants.SystemConstants;
import utils.Configurations;
import utils.FileReader;

/**
 *
 * @author gauravsi
 */
public class ProjectData {

    String outputfolder = Configurations.getWorkSpaceFilePath().getAbsolutePath();

    String makeFilePath;
    List<String> listTargets;
    List<String> listSwitches;
    Map<String, String> testcases;
    String projectLocation;

    public ProjectData(String projectLocation) {
        this.projectLocation = projectLocation;
        this.makeFilePath=projectLocation+ File.separator + "sim";
        testcases = FileReader.getAllTestCaseFileNames(makeFilePath);
        listTargets =FileReader.readMakeFileTargets(projectLocation+SystemConstants.defaultMakeFilePath);
        listSwitches = FileReader.readMakeFileArg(projectLocation+SystemConstants.defaultMakeFilePath) ;
    }

    public String getOutputfolder() {
        return outputfolder;
    }

    public String getMakeFilePath() {
        return makeFilePath;
    }

    public List<String> getListTargets() {
        return listTargets;
    }

    public List<String> getListSwitches() {
        return listSwitches;
    }

    public Map<String, String> getTestcases() {
        return testcases;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

 

}
