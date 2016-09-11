/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truechip;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import controller.MainPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.DataList;
import logger.Logger;
import logger.SuperLoggger;
import models.DetailedLogs;
import models.ProjectData;
import utils.Configurations;
import utils.GenericLogModel;
import utils.LogModel;
import utils.ReadLogger;

/**
 *
 * @author gauravsi
 */
public class TransactionLogger {

	List<String> listOfKeysDetailed;
	File logFile;
	Button browseButton;
	Button refreshButton = new Button("refresh");
	private TableView detailedLogReport;
	Map<String, List> loggerMap;
	private AnchorPane mainContainer;
	Label logFileName = new Label();
	BorderPane listView = null;
	TreeView treeView = null;
	VBox rightVBox = null;
	TreeTableView centerListView;
	GridPane rightGrid = null;
	public TextArea consoleLog = new TextArea();
	public Button runButton = new Button("Run");
	FlowPane rightSideContent = new FlowPane();
	ComboBox targetComboBox = new ComboBox();
	Thread thread;
	MainPageController mainController;
	Map<String, ProjectData> projects;

	public TransactionLogger(MainPageController mainController) {
		this.mainController = mainController;
		this.mainContainer = mainController.mainContainer;
		rightGrid = new GridPane();

		detailedLogReport = new TableView();
		TableColumn request = new TableColumn("Key");
		request.setCellValueFactory(new PropertyValueFactory<DetailedLogs, String>("requestCol"));

		TableColumn completion = new TableColumn("Value");
		completion.setCellValueFactory(new PropertyValueFactory<DetailedLogs, String>("completionCol"));
		detailedLogReport.getColumns().addAll(request, completion);
		
	}

	public void build() {
		listView = new BorderPane();
		listView.setCenter(new TableView());
		listView.setRight(new TableView());

		browseButton = new Button("browse log file");

		browseButton.setOnMouseClicked(this::selectFile);
		mainController.toolbarMain.getItems().clear();
		refreshButton.setVisible(false);
		refreshButton.setOnMouseClicked(this::setLoggerView);
		mainController.toolbarMain.getItems().addAll(new Label("Logger Viewer"), browseButton,logFileName,refreshButton);
	}

	public BorderPane get() {
		if (listView == null) {
			System.out.println("truechip.TransactionLogger.get()");
			build();
			return listView;
		} else {
			return listView;
		}

	}

	@FXML
	private void selectFile(MouseEvent event) {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Open File");
			logFile = chooser.showOpenDialog(new Stage());
			refreshLogViewer(logFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void mylistclicked(MouseEvent event) {
		TreeItem selectedItems = (TreeItem) centerListView.getSelectionModel().getSelectedItem();
		GenericLogModel value = (GenericLogModel) selectedItems.getValue();
		System.out.println("Adding detailed table row.........." + value.getLogMap());
		detailedLogReport.getItems().clear();

		for (Map.Entry<String, String> keySet : value.getLogMap().entrySet()) {
			if (listOfKeysDetailed.contains(keySet.getKey())) {
				DetailedLogs detailedLogs = new DetailedLogs(keySet.getKey(), keySet.getValue());
				detailedLogReport.getItems().add(detailedLogs);
			}

		}
		System.out.println("Added detailed table row.........." + selectedItems);
		detailedLogReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void refreshMainContainer() {
		build();
		mainContainer.getChildren().clear();
		mainContainer.getChildren().add(get());
		AnchorPane.setBottomAnchor(get(), 0.0);
		AnchorPane.setTopAnchor(get(), 0.0);
		AnchorPane.setLeftAnchor(get(), 0.0);
		AnchorPane.setRightAnchor(get(), 0.0);
	}

	public void addToMainContainer(BorderPane listView) {
		mainContainer.getChildren().clear();
		mainContainer.getChildren().add(listView);
		AnchorPane.setBottomAnchor(get(), 0.0);
		AnchorPane.setTopAnchor(get(), 0.0);
		AnchorPane.setLeftAnchor(get(), 0.0);
		AnchorPane.setRightAnchor(get(), 0.0);
	}

	public TreeItem<GenericLogModel> addChildren(GenericLogModel model, DataList dataList) {
		TreeItem<GenericLogModel> childTreeItem = new TreeItem<>(model);
		if (dataList.hasChild()) {
			for (DataList childDataList : dataList.getChild()) {
				GenericLogModel chilModel= new GenericLogModel();
				chilModel.setLogMap(childDataList.getData());
				childTreeItem.getChildren().add(addChildren(chilModel,childDataList));
			}
		}
		return childTreeItem;
	}
	
	private void setLoggerView(MouseEvent event){
		refreshLogViewer(logFile);
	}
	
	private void refreshLogViewer(File logFile){
		try{
			ReadLogger logger = new ReadLogger(logFile);
			Logger.info("truechip.TransactionLogger.selectFile() map:");

			// setting detailed table
			detailedLogReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			detailedLogReport = logger.getDetailedTable();
			listOfKeysDetailed = logger.getListOfKeysDetailed();
			detailedLogReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			//LogModel log = new LogModel(logFile);
			//final TreeItem<GenericLogModel> treeItem = new TreeItem<>(log.getRootModel());
			//List<GenericLogModel> listModels = log.getListModels();
			
			List<String[]> extractedData = Logger.extractData(logFile);
			
			Logger logData=new Logger(extractedData);
			
			Map<String, String> headerMap = logData.getHeaderMap();
			
			List<DataList> leftSideTableData = logData.getLeftSideTableData();
			
			GenericLogModel headerMapLeftSideTable= new GenericLogModel();
			headerMapLeftSideTable.setLogMap(headerMap);
			
			
			final TreeItem<GenericLogModel> treeItem = new TreeItem<>(headerMapLeftSideTable);

			for (int i = 0; i < leftSideTableData.size(); i++) {
				System.out.println("leftSideTableData: " + leftSideTableData.get(i).getData());
				
				DataList dataList = leftSideTableData.get(i);
				GenericLogModel model= new GenericLogModel();
				model.setLogMap(leftSideTableData.get(i).getData());

				TreeItem<GenericLogModel> childTreeItem = addChildren(model,dataList );
				
				treeItem.getChildren().add(childTreeItem);
			}

			TreeTableView<GenericLogModel> tLeftHandSideTable = logger.getTLeftHandSideTable();
			tLeftHandSideTable.setRoot(treeItem);
			centerListView = tLeftHandSideTable;
			centerListView.setRoot(treeItem);
			centerListView.setShowRoot(false);
			centerListView.setOnMouseClicked(this::mylistclicked);

			logFileName.setText(logFile.getAbsolutePath());

			centerListView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
			listView.setCenter(centerListView);

			listView.setRight(detailedLogReport);
			refreshButton.setVisible(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
