/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
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
import logger.Logger;
import utils.Configurations;
import utils.RootLogger;

/**
 *
 * @author gauravsi
 */
public class AppMain extends Application {
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
                    Logger.info("opening workspace dialog....");
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File selectedDirectory
                            = directoryChooser.showDialog(stage);

                    if (!selectedDirectory.isDirectory()) {
                        Logger.info("false: " + selectedDirectory.getAbsolutePath());
                        labelSelectedDirectory.setText("No Directory selected");
                    } else {
                        Configurations.setFilePath(selectedDirectory);
                        Logger.info("true: " + selectedDirectory.getAbsolutePath());

                        try {
                            start(stage);
                        } catch (Exception ex) {
                            Logger.info(ex);
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
    	boolean isConsoleAppender=false;
    	if(args.length==1 && args[0].equalsIgnoreCase("show")){
    		isConsoleAppender=true;
    	}
        try {
        	RootLogger rootLogger = new RootLogger(isConsoleAppender, true);
            launch(args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void restart(Stage stage) throws Exception{
        stage.close();
        appmain.start(stage);
        
    }

}
