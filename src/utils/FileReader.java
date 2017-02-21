/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author gauravsi
 */
public class FileReader {
	public final static Logger log = Logger.getLogger(FileReader.class);

    public static List<String> readLines(String pathname) {
        File file = new File(pathname);

        BufferedReader br = null;
        try {
            List<String> readLines = FileUtils.readLines(file);
            br = new BufferedReader(new java.io.FileReader(file));

            String line = br.readLine();
            log.info("line size readLines array: " + readLines.size());

            while (line != null) {
                line = br.readLine();
            }
            releaseResource(file, br);
            return readLines;
        } catch (IOException e) {
            log.error(e);
            releaseResource(file, br);
            return null;
        }
    }
    
        public static List<String> readLinesForLogger(File file) {

        BufferedReader br = null;
        try {
            List<String> readLines = new ArrayList<>();
            br = new BufferedReader(new java.io.FileReader(file));

            String line = br.readLine();
            log.info("line size readLines array: " + readLines.size());

            while (line != null) {
                if(line.length()>5 && line.startsWith("|")){
                    readLines.add(line);
                    //Logger.info("adding lines: "+line);
                }
                line = br.readLine();
            }
            releaseResource(file, br);
            return readLines;
        } catch (IOException e) {
            log.error(e);
            releaseResource(file, br);
            return null;
        }
    }

    public static List<String> readMakeFileTargets(String pathname) {
        File file = new File(pathname);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new java.io.FileReader(file));
            log.info("  reading for targets>>> " + file.getAbsolutePath());

            String line = br.readLine();
            boolean isRead = false;
            while (line != null) {
                try {
                    //Logger.info("  reading for targets>>> " + line);

                    if (line.length() > 0) {
                        if (line.contains("-target-start")) {
                            log.info("found #@@@-target-start");
                            isRead = true;
                            line = br.readLine();
                            continue;
                        }
                        log.info("isRead  >> " + isRead + " == " + line);
                        if (isRead) {
                            line = line.substring(1);
                            log.info("Adding targtes == " + line);
                            return Arrays.asList(line.trim().split(" "));
                        }
                        if (line.contains("-target-end")) {
                            log.info("found #@@@-target-end");
                            isRead = false;
                            break;
                        }
                    }
                } catch (NullPointerException e) {
                    log.info("NullPointerException: " + line);
                    //e.printStackTrace();
                }
                line = br.readLine();
            }
            releaseResource(file, br);
            return new ArrayList<>();
        } catch (IOException e) {
        	log.error(e);
            releaseResource(file, br);
            return new ArrayList<>();
        }
    }

    public static List<String> readMakeFileArg(String pathname) {
        File file = new File(pathname);
        BufferedReader br = null;
        try {
            //List<String> readLines = FileUtils.readLines(file);
            List<String> readLines = new ArrayList<>();
            br = new BufferedReader(new java.io.FileReader(file));
            log.info("  reading for switiches >>> " + file.getAbsolutePath());

            String line = br.readLine();
            boolean isRead = false;
            while (line != null) {

                line = br.readLine();

                try {
                    if (line.length() > 0) {
                        if (line.contains("#@@@-switiches-start")) {
                            log.info("found #@@@-switiches-start");
                            isRead = true;
                            continue;
                        }
                        if (isRead && line.contains("?=")) {
                            log.info("adding switiches >> " + line);
                            readLines.add(line);
                        }
                        if (line.contains("#@@@-swithches-end")) {
                            log.info("found @@@-swithches-end");
                            isRead = false;
                            break;
                        }

                    }
                } catch (NullPointerException e) {
                    log.info("NullPointerException: " + line);
                    //e.printStackTrace();
                }
            }
            log.info(file.getAbsoluteFile() + " line size readMakeFileArg array: " + readLines.size());
            releaseResource(file, br);
            return readLines;
        } catch (IOException e) {
        	log.error(e);
            releaseResource(file, br);
            return null;
        }
    }

    public static Map<String, String> getAllTestCaseFileNames(String pathToSim) {
        Map<String, String> output = new HashMap<String, String>();
        File file = new File(pathToSim);
        File[] listFiles = file.listFiles(new FIleFilter("txt"));
        for (File name : listFiles) {
            output.put(name.getName(), name.getAbsolutePath());
            log.info("truechip.ListTestCase.getAllTestCases():" + name.getName());
        }
        file = null;
        releaseResource(file, null);
        return output;
    }

    public static boolean deleteProject(String pathToProject) throws IOException {
        System.gc();
        File fileToBeDeleted = new File(pathToProject);
        log.info("deleting: " + fileToBeDeleted.getAbsolutePath());
        FileUtils.deleteDirectory(fileToBeDeleted);
        releaseResource(fileToBeDeleted, null);
        return true;
    }

    public static void releaseResource(File file, BufferedReader br) {
        file = null;
        try {
            br.close();
        } catch (NullPointerException e) {
        } catch (IOException e) {
            br = null;
        }
        System.gc();
    }

    public static boolean overideRegressList(List<String> list, String pathToRegressFile) {
        // Convert the string to a
        // byte array.
        log.info("writng to file: " + pathToRegressFile);
        String s = "";
        for (String str : list) {
            s = s + str + "\n";
        }
        byte data[] = s.getBytes();
        Path p = Paths.get(pathToRegressFile);
        try {
            FileWriter writer = new FileWriter(pathToRegressFile);
            writer.write("");
            writer.append(s);
            writer.flush();
            writer.close();

        } catch (IOException x) {
            System.err.println(x);
        } finally {

        }
        return true;
    }
}
