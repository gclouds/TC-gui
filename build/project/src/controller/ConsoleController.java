/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logger.Logger;

/**
 *
 * @author gauravsi
 */
public class ConsoleController implements Initializable {

    @FXML
    BorderPane mainPane;

    static TextArea consoleLog = new TextArea();

    private static TextArea getConsoleLog() {
        if (consoleLog != null) {
            return consoleLog;
        } else {
            consoleLog = new TextArea();
            return consoleLog;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logger.info("ConsoleController Main Page...");
        consoleLog.setEditable(false);
        mainPane.setCenter(consoleLog);
    }

    public static void appendToConsole(String logMessage) {
        getConsoleLog().appendText(logMessage + "\n");
    }
}
