/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truechip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Configurations;
import utils.UnzipUtility;

/**
 *
 * @author gauravsi
 */
public class ImportProject {
	public final static Logger log = Logger.getLogger(ImportProject.class);

    static String outputfolder = Configurations.getWorkSpaceFilePath().getParent();
    static BorderPane projectList = null;

    public static void build() {

    }

    public static BorderPane get() {
        log.info("truechip.ListTestCase.get()");
        if (projectList == null) {
            build();
            return projectList;

        } else {
            return projectList;
        }

    }

    public static void importZip() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");
            File logFile = chooser.showOpenDialog(new Stage());
            //unZipIt(logFile.getAbsolutePath());

            String outputfolder = Configurations.getWorkSpaceFilePath().getAbsolutePath();
            String inputFilePath = logFile.getAbsolutePath();
            UnzipUtility unzipper = new UnzipUtility();
            try {
                //unzipper.unzip(inputFilePath, outputfolder);
                unzipper.unzip(inputFilePath, outputfolder, "pwd");

            } catch (Exception ex) {
                log.error(ex);
            }

        } catch (Exception e) {

        }
    }

    public static void importPwdZip() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");
            File logFile = chooser.showOpenDialog(new Stage());
            //unZipIt(logFile.getAbsolutePath());

            String outputfolder = Configurations.getWorkSpaceFilePath().getAbsolutePath();
            String inputFilePath = logFile.getAbsolutePath();
            UnzipUtility unzipper = new UnzipUtility();
            try {
                unzipper.unzip(inputFilePath, outputfolder, "pwd");

            } catch (Exception ex) {
                log.error(ex);
            }

        } catch (Exception e) {

        }
    }

    public static void unZipIt(String zipFile) {

        byte[] buffer = new byte[1024];

        try {
            log.info("truechip.ImportProject.unZipIt()" + Configurations.getWorkSpaceFilePath().getParent());
            //create output directory is not exists
            File folder = new File(Configurations.getWorkSpaceFilePath().getParent());
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis
                    = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(folder + File.separator + fileName);

                log.info("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            log.info("Done");

        } catch (IOException ex) {
            log.error(ex);
        }
    }
}
