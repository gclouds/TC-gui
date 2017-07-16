/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import library.LicenseUtill;
import utils.CheckLicense;
import utils.Configurations;
import utils.RootLogger;

/**
 *
 * @author gauravsi
 */
public class AppMain extends Application {
	public final static Logger log = Logger.getLogger(Application.class);
	public static LicenseUtill licenseObejct;
	static{
		//RootLogger rootLogger = new RootLogger(true, true);
	}
    static AppMain appmain=null;
    @Override
    public void start(Stage stage) throws Exception {
        appmain=this;
        //Configurations.removePrefrences();
        if (Configurations.getWorkSpaceFilePath() == null) {

            final Label labelSelectedDirectory = new Label("Please select workspace");

            Button btnOpenDirectoryChooser = new Button();
            btnOpenDirectoryChooser.setText("Browse");
            btnOpenDirectoryChooser.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    log.info("opening workspace dialog....");
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File selectedDirectory
                            = directoryChooser.showDialog(stage);

                    if (!selectedDirectory.isDirectory()) {
                        log.info("false: " + selectedDirectory.getAbsolutePath());
                        labelSelectedDirectory.setText("No Directory selected");
                    } else {
                        Configurations.setFilePath(selectedDirectory);
                        log.info("true: " + selectedDirectory.getAbsolutePath());

                        try {
                            start(stage);
                        } catch (Exception ex) {
                            log.info(ex);
                        }
                    }
                }
            });
            VBox vBox = new VBox();
            vBox.getChildren().addAll(
                    labelSelectedDirectory,
                    btnOpenDirectoryChooser);

            StackPane root1 = new StackPane();
            root1.getChildren().add(vBox);

            Scene scene1 = new Scene(root1, 300, 250);

            stage.setTitle("TrueChip- GUI");
            stage.setScene(scene1);
            stage.show();

        } else {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/MainPage.fxml"));

            Scene scene = new Scene(root);
            String externalForm = getClass().getClassLoader().getResource("styles/caspian.css").toExternalForm();
            //scene.getStylesheets().add(externalForm);
            stage.setTitle("TrueChip- GUI");
            stage.getIcons().add(new Image("resources/true-chip-logo.png"));
            stage.setScene(scene);
            //stage.setFullScreen(true);
            stage.show();

        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	boolean isConsoleAppender=true;
    	boolean isLicensed=true;
    	if(args.length==1 && args[0].equalsIgnoreCase("show")){
    		isConsoleAppender=true;
    	}
        try {
        	RootLogger rootLogger = new RootLogger(isConsoleAppender, true);
        	licenseObejct=CheckLicense.getLicenseUtill();
        	if(CheckLicense.checkLic(licenseObejct)){
        		launch(args);
        	}else{
        		//System.out.println("No valid license found on this machine!!!");
        		//System.exit(1);
        		launch(args);
        	}
        	//launch(args);
            
        } catch (Throwable t) {
        	System.out.println("No valid license found on this machine or something went wrong!!!");
        	log.error(t);
        }
    }

    public static void restart(Stage stage) throws Exception{
        stage.close();
        appmain.start(stage);
        
    }

}
