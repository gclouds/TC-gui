/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author gauravsi
 */
public class ConsoleController implements Initializable {
	public final static Logger log = Logger.getLogger(ConsoleController.class);

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
        log.info("ConsoleController Main Page...");
        consoleLog.setEditable(false);
        mainPane.setCenter(consoleLog);
    }

    public static void appendToConsole(String logMessage) {
        getConsoleLog().appendText(logMessage + "\n");
    }
}
