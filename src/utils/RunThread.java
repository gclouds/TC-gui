/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import controller.ConsoleController;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import truechip.ListTestCase;
import logger.TCLogger;

/**
 *
 * @author gaurav
 */
public class RunThread extends Thread {
	public final static Logger log = Logger.getLogger(RunThread.class);

    String command;
    File file;
    ListTestCase listTestCase;

    public RunThread(String command, String makeFilePath, ListTestCase listTestCase) {
        this.command = command;
        this.file = new File(makeFilePath);
        this.listTestCase = listTestCase;
    }

    @Override
    public void run() {
        // super.run(); //To change body of generated methods, choose Tools | Templates.

        try {
            Process process = Runtime.getRuntime().exec(command, null, file);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
                final String output=line;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ConsoleController.appendToConsole(output);
                        //listTestCase.consoleLog.appendText("==> "+output + "\n");
                    }
                });

            }
            reader.close();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ConsoleController.appendToConsole("execution completed");
                    listTestCase.runButton.setText("Run");
                }
            });

        } catch (Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ConsoleController.appendToConsole("error: "+e.getLocalizedMessage());
                    //listTestCase.consoleLog.appendText("Exception: "+e.getLocalizedMessage() + "\n");
                    listTestCase.runButton.setText("Run");
                }
            });

        }

    }

}
