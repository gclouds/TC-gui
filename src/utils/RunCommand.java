/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import javafx.scene.control.TextArea;
import truechip.ListTestCase;
import logger.TCLogger;

/**
 *
 * @author gauravsi
 */
public class RunCommand {
	public final static Logger log = Logger.getLogger(RunCommand.class);
    public static Thread asyncExecute(String command, File file, ListTestCase listTestCase) {

            RunThread mainThread = new RunThread(command, command, listTestCase);
            Thread t1 = new Thread(mainThread);
            t1.start();
            return t1;
    }

    public static String executeCommand(String command, File file,TextArea consoleLog) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command,null, file);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
                consoleLog.appendText(line + "\n");
            }

        } catch (Exception e) {
            log.error(e);
        }

        return output.toString();

    }

    public static void exeCommand(String cmd, File file, TextArea consoleLog) {
        Runtime rt = Runtime.getRuntime();
        RunCommand rte = new RunCommand();
        StreamWrapper error, output;

        try {
            Process proc = rt.exec(cmd, null, file);
            error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
            output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
            int exitVal = 0;

            error.start();
            output.start();
            error.join(3000);
            output.join(3000);
            exitVal = proc.waitFor();
            consoleLog.appendText("\n Output: " + output.message + "\nError: " + error.message);

            log.info("Output: " + output.message + "\nError: " + error.message);
        } catch (IOException e) {
        	log.error(e);
            consoleLog.appendText("\n" + e.getMessage());
        } catch (InterruptedException e) {
        	log.error(e);
        }
    }

    private class StreamWrapper extends Thread {

        InputStream is = null;
        String type = null;
        String message = null;

        public String getMessage() {
            return message;
        }

        StreamWrapper(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);//.append("\n");

                }
                message = buffer.toString();
            } catch (IOException ioe) {
            	log.error(ioe);
            }
        }
    }

    public StreamWrapper getStreamWrapper(InputStream is, String type) {
        return new StreamWrapper(is, type);
    }

}
