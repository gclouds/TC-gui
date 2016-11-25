/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truechip;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.MainPageController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
//import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import library.LogTreeItem;
import logger.DataList;
import logger.Logger;
import models.DetailedLogs;
import models.ProjectData;
import utils.Configurations;
import utils.GenericLogModel;
import utils.ReadLogger;

/**
 *
 * @author gauravsi
 */
public class TransactionLogger {
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TransactionLogger.class);
	List<String> listOfKeysDetailed;
	File logFile;
	private TableView secondTable;
	Map<String, List> loggerMap;
	private AnchorPane mainContainer;
	Label logFileName = new Label();
	BorderPane listView = null;
	TreeView treeView = null;
	VBox rightVBox = null;
	TreeTableView centerListView;
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
		listView = new BorderPane();
		
	}

	public BorderPane getNode() {
		return listView;
	}


	@FXML
	private void mylistclicked(MouseEvent event) {
		TreeItem selectedItem = (TreeItem) centerListView.getSelectionModel().getSelectedItem();
		ObservableList selectedIndex = centerListView.getSelectionModel().getSelectedCells();
		if (selectedIndex.size()>0) {
			showSecondaryTable((GenericLogModel)selectedItem.getValue());
		}
	}
	public void showSecondaryTable(GenericLogModel value){
		log.info("Adding Secondary Table data..." + value.getLogMap());
		secondTable.getItems().clear();
		for (Map.Entry<String, String> keySet : value.getLogMap().entrySet()) {
			if (listOfKeysDetailed.contains(keySet.getKey())) {
				DetailedLogs detailedLogs = new DetailedLogs(keySet.getKey(), keySet.getValue());
				secondTable.getItems().add(detailedLogs);
			}
		}
		log.info("Added Secondary Table data!");
		secondTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	public void refreshMainContainer() {
		AnchorPane.setBottomAnchor(listView, 0.0);
		AnchorPane.setTopAnchor(listView, 0.0);
		AnchorPane.setLeftAnchor(listView, 0.0);
		AnchorPane.setRightAnchor(listView, 0.0);
	}
	public void refreshMainContainer(Node node) {
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
	}
	public void addToMainContainer(BorderPane listView) {
		AnchorPane.setBottomAnchor(listView, 0.0);
		AnchorPane.setTopAnchor(listView, 0.0);
		AnchorPane.setLeftAnchor(listView, 0.0);
		AnchorPane.setRightAnchor(listView, 0.0);
	}

	public TreeItem<GenericLogModel> addChildren(GenericLogModel model, DataList dataList) {
		// TreeItem<GenericLogModel> childTreeItem = new TreeItem<>(model);
		TreeItem<GenericLogModel> childTreeItem = new LogTreeItem(model, this);
		//
		if (dataList.hasChild()) {
			for (DataList childDataList : dataList.getChild()) {
				GenericLogModel chilModel = new GenericLogModel();
				chilModel.setLogMap(childDataList.getData());
				childTreeItem.getChildren().add(addChildren(chilModel, childDataList));
			}
		}
		return childTreeItem;
	}



	public void refreshLogViewer(File logFile) {
		try {
			ReadLogger logger = new ReadLogger(logFile);
			log.info("truechip.TransactionLogger.selectFile() map:");


			List<String[]> extractedData = Logger.extractData(logFile);

			Logger logData = new Logger(extractedData);

			HashMap<String, String> headerMap = logData.getHeaderMap();

			List<DataList> leftSideTableData = logData.getLeftSideTableData();

			GenericLogModel headerMapLeftSideTable = new GenericLogModel();
			headerMapLeftSideTable.setLogMap(headerMap);

			final TreeItem<GenericLogModel> treeItem = new TreeItem<>(headerMapLeftSideTable);

			for (int i = 0; i < leftSideTableData.size(); i++) {
				log.info("leftSideTableData: " + leftSideTableData.get(i).getData());

				DataList dataList = leftSideTableData.get(i);
				GenericLogModel model = new GenericLogModel();
				model.setLogMap(leftSideTableData.get(i).getData());

				TreeItem<GenericLogModel> childTreeItem = addChildren(model, dataList);

				treeItem.getChildren().add(childTreeItem);
			}

			TreeTableView<GenericLogModel> tLeftHandSideTable = logger.getTLeftHandSideTable();
			tLeftHandSideTable.setRoot(treeItem);
			centerListView = tLeftHandSideTable;
			centerListView.setRoot(treeItem);
			centerListView.setShowRoot(false);
			centerListView.setOnMouseClicked(this::mylistclicked);
			centerListView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

			logFileName.setText(logFile.getAbsolutePath());

			
			// setting second table
			secondTable = logger.getDetailedTable();
			
			 final StackPane sp1 = new StackPane();
			 sp1.getChildren().add(centerListView);
			 final StackPane sp2 = new StackPane();
			 SplitPane sp = new SplitPane();
			 sp.getItems().addAll(sp1, sp2);
			if(secondTable !=null){
				listOfKeysDetailed = logger.getListOfKeysDetailed();
				secondTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				 sp2.getChildren().add(secondTable);
				sp.setDividerPositions(0.7f, 0.3f);
			}else{
				sp.setDividerPositions(1f);

			}
			
			listView.setCenter(sp);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
