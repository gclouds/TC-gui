/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import static utils.FileReader.releaseResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import library.CustomCallBack;
import library.LogTreeItem;
import logger.Logger;
import models.DetailedLogs;
import sun.net.www.protocol.mailto.MailToURLConnection;
import logger.Logger;

/**
 *
 * @author gauravsi
 */
public class ReadLogger {
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReadLogger.class);

	List<String> listOfKeysDetailed;

	File logFile;
	List<String> fileLine;

	public ReadLogger(File logFile) throws IOException {
		this.logFile = logFile;
		fileLine = FileUtils.readLines(logFile);
		listOfKeysDetailed= new ArrayList<>();
	}

	public TreeTableView<GenericLogModel> getTLeftHandSideTable() throws Exception {

		try {
			TreeTableView<GenericLogModel> outputTableview = new TreeTableView();
			String exceptionString = "";
			boolean isException = false;
			TreeTableColumn<GenericLogModel, String> firstCollumn = new TreeTableColumn<>("*");
			firstCollumn.setPrefWidth(30);
			firstCollumn.setResizable(false);
			//firstCollumn.setCellFactory(param-> param.getValue().getValue().getValue("*"));
			firstCollumn.setCellValueFactory(new Callback<CellDataFeatures<GenericLogModel, String>, ObservableValue<String>>() {
				@Override 
				public ObservableValue<String> call(CellDataFeatures<GenericLogModel, String> p) {
					return new SimpleStringProperty("");
				}
			  });
			//outputTableview.getColumns().add(firstCollumn);
			outputTableview.setRowFactory(new CustomCallBack()); 
			
			for (String next : fileLine) {
				log.info("at line: " + next);
				String firstCheck = next.substring(0, 3);
				if (firstCheck.equalsIgnoreCase("#.C")) {
					//log.info("firstCheck: "+firstCheck);
					String result = next.replace("#.C", "").replace(".#", "");
					String[] splitCols = result.split("\\|");

					if (splitCols.length > 1) {
						for (int i = 0; i < splitCols.length; i++) {
							setCollumns(splitCols[i], outputTableview);
						}
					} else {
						setCollumns(result, outputTableview);
					}
					break;
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Not a suitable format type of Transaction logger");
					alert.setContentText("Please try a valid Transaction logger");
					alert.showAndWait();
					break;
				}
			}
			if (isException) {
				throw new Exception(exceptionString);
			}
			return outputTableview;
		} catch (Exception e) {
			log.error(e);
			throw new Exception("not a valid type of log file" + logFile.getAbsolutePath());
		}

	}

	public TableView getDetailedTable() {
		String exceptionString = "Invalid data: No Lines starts with '#.R'";
		TableView outputTableview = new TableView();
		try {
			boolean isException = false;
			for (String next : fileLine) {
				log.info("at line: " + next);
				String firstCheck = next.substring(0, 3);
				if (firstCheck.equalsIgnoreCase("#.R")) {
					exceptionString=firstCheck;
					//log.info("firstCheck: "+firstCheck);
					String[] splitedFirst = next.split(".#.");
					if (splitedFirst.length == 2) {
						String cleanedHead = splitedFirst[0].replace("#.R", "");
						String result = cleanedHead.substring(cleanedHead.indexOf("[") + 1, cleanedHead.indexOf("]"));
						String[] split = result.split(">");
						if (split.length == 2) {
							//TreeTableColumn<GenericLogModel, String> subtableCol = new TreeTableColumn<>(subCol);
							//subtableCol.setCellValueFactory(param -> param.getValue().getValue().getValue(subCol.trim()));
							TableColumn mainCol = new TableColumn(split[0]);
							String[] subCols = split[1].replace("(", "").replace(")", "").split(",");
							TableColumn<DetailedLogs, String> name = new TableColumn<>(subCols[0]);
							name.setCellValueFactory(param -> param.getValue().getRequestCol());
							TableColumn<DetailedLogs, String> value = new TableColumn<>(subCols[1]);
							value.setCellValueFactory(param -> param.getValue().getCompletionCol());
							mainCol.getColumns().addAll(name, value);
							outputTableview.getColumns().add(mainCol);
						} else {
							exceptionString = "split.length == 2:" + (split.length == 2);
							isException = true;
						}
						String detailedKeyString = splitedFirst[1].replace(".#", "");
						String[] detailedKeysArray = detailedKeyString.split("\\|");
						for(String key:detailedKeysArray){
							listOfKeysDetailed.add(key.trim());
						}
					}
					isException = false;
					break;
				} else if (firstCheck.equalsIgnoreCase("#.R")) {

				} else {
					exceptionString = "firstCheck.equalsIgnoreCase(\"#.C\"): " + firstCheck;
					log.info(exceptionString);
					isException = true;
				}
			}
			if (isException) {
				log.info("Errors in line"+exceptionString);
				outputTableview=null;
			}
		} catch (Exception e) {
			log.info("Errors in line"+exceptionString);
			log.info(e);
			outputTableview=null;
		}
		return outputTableview;
	}

	public Map<String, List> getMap() {
		List<String> list = new ArrayList<>();
		Map<String, List> output = new HashMap<String, List>();
		log.info("utils.ReadLogger.getMap()" + logFile.getAbsolutePath());
		BufferedReader br = null;
		try {
			br = new BufferedReader(new java.io.FileReader(logFile));
			log.info("  reading for logfile: " + logFile.getAbsolutePath());

			String line = br.readLine();
			boolean isRead = false;
			int timeIndex = 0;

			while (line != null) {
				try {
					//log.info("  reading for targets>>> " + line);
					if (line.length() > 0) {
						if (line.contains(" Time ")) {
							line = line.substring(2);
							List<String> asList = Arrays.asList(line.split("\\|"));
							timeIndex = getTimeIndex(asList);
							output.put("time", asList);

							log.info("timeIndex:: " + timeIndex + " at line: " + line);
							isRead = true;
							line = br.readLine();
							continue;
						}
						if (isRead) {
							if (line.contains("|")) {
								line = line.substring(1);
								List<String> asList = Arrays.asList(line.split("\\|"));
								output.put(asList.get(timeIndex).trim(), asList);
								log.info(timeIndex + "key: " + asList.get(timeIndex) + ", value: " + asList);
							}
							//log.info("Adding targtes == " + line);
						}
					}
				} catch (NullPointerException e) {
					log.info("NullPointerException: " + line);
					//e.printStackTrace();
				}
				line = br.readLine();
			}
			releaseResource(logFile, br);
		} catch (IOException e) {
			log.error(e);
			releaseResource(logFile, br);
		}

		return output;
	}

	private int getTimeIndex(List<String> list) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			String next = (String) iterator.next();
			if (next.contains("Time")) {
				return list.indexOf(next);
			}
		}
		return 0;
	}

	private void setCollumns(String colString, TreeTableView table) {

		TreeTableColumn<GenericLogModel, String> mainCol;

		//TableColumn mainCol;
		String[] split = colString.split(">");
		if (split.length > 1) {
			log.info("adding main collumn: " + split[0]);
			mainCol = new TreeTableColumn(split[0]);
			mainCol.setCellValueFactory(param -> param.getValue().getValue().getValue(split[0]));
			String[] subCols = split[1].replace("(", "").replace(")", "").split(",");
			for (String subCol : subCols) {
				log.info("Adding sub collumn: '" + subCol.trim() + "'");
				TreeTableColumn<GenericLogModel, String> subtableCol = new TreeTableColumn<>(subCol);
				subtableCol.setCellValueFactory(param -> param.getValue().getValue().getValue(subCol.trim()));
				mainCol.getColumns().add(subtableCol);
			}
			table.getColumns().add(mainCol);
		} else {
			mainCol = new TreeTableColumn(colString);
			mainCol.setCellValueFactory(param -> param.getValue().getValue().getValue(colString.trim()));
			table.getColumns().add(mainCol);
		}

	}

	public List<String> getListOfKeysDetailed() {
		return listOfKeysDetailed;
	}

	public void setListOfKeysDetailed(List<String> listOfKeysDetailed) {
		this.listOfKeysDetailed = listOfKeysDetailed;
	}


}
