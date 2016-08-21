/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truechip;

import controller.MainPageController;
import java.io.File;
import java.util.List;
import java.util.Map;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.Logger;
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

    Button broseButton;
    private TableView detailedLogReport;
    Map<String, List> loggerMap;
    private AnchorPane mainContainer;
    Label logFileName = new Label();
    BorderPane listView = null;
    TreeView treeView = null;
    String outputfolder = Configurations.getWorkSpaceFilePath().getAbsolutePath();
    String makeFilePath = "";
    String projectPath = null;
    String tcName = "";
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

        broseButton = new Button("browse log file");

        broseButton.setOnMouseClicked(this::selectFile);
        mainController.toolbarMain.getItems().clear();
        mainController.toolbarMain.getItems().addAll(new Label("Logger viewer"), broseButton, logFileName);
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
            File logFile = chooser.showOpenDialog(new Stage());

            ReadLogger logger = new ReadLogger(logFile.getAbsolutePath());
            Logger.info("truechip.TransactionLogger.selectFile() map:");
            
            detailedLogReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

//            loggerMap = logger.getMap();
//
//            Logger.info("truechip.TransactionLogger.selectFile() map:" + loggerMap.size());
//            Map<String, List> treeMap = new TreeMap<String, List>(loggerMap);
//            treeMap.remove("time");
//            Iterator<String> keySet = treeMap.keySet().iterator();
//            while (keySet.hasNext()) {
//                centerListView.getItems().add(keySet.next());
//            }
            //listView.setLeft(centerListView);
            LogModel log = new LogModel(logFile.getAbsolutePath());
            final TreeItem<GenericLogModel> treeItem = new TreeItem<>(log.getRootModel());
            List<GenericLogModel> listModels = log.getListModels();
            for (int i = 0; i < listModels.size(); i++) {
                System.out.println("adding child to centerlistview: " + listModels.get(i).getLogMap().get("Time"));
                TreeItem<GenericLogModel> childTreeItem = addChildren(listModels.get(i));
                treeItem.getChildren().add(childTreeItem);
            }
            TreeTableView<GenericLogModel> tLeftHandSideTable = logger.getTLeftHandSideTable();
            tLeftHandSideTable.setRoot(treeItem);
            centerListView = tLeftHandSideTable;
            centerListView.setRoot(treeItem);
            centerListView.setShowRoot(false);
            centerListView.setOnMouseClicked(this::mylistclicked);

            logFileName.setText(logFile.getAbsolutePath());

            detailedLogReport=logger.getDetailedTable();
            detailedLogReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            centerListView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
            listView.setCenter(centerListView);

            listView.setRight(detailedLogReport);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void mylistclicked(MouseEvent event) {
        TreeItem selectedItems = (TreeItem) centerListView.getSelectionModel().getSelectedItem();
        GenericLogModel value =(GenericLogModel) selectedItems.getValue();
        System.out.println("Adding detailed table row.........." + value.getLogMap());
        detailedLogReport.getItems().clear();

        for (Map.Entry<String, String> keySet:value.getLogMap().entrySet()) {
            DetailedLogs detailedLogs = new DetailedLogs(keySet.getKey(), keySet.getValue());
            detailedLogReport.getItems().add(detailedLogs);
        }
        System.out.println("Added detailed table row.........." + selectedItems);
        detailedLogReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    private void tcListClick(MouseEvent event) {
        ListView testCaseSelected = (ListView) event.getSource();
        if (testCaseSelected != null) {
            tcName = testCaseSelected.getSelectionModel().getSelectedItem().toString();
            System.out.println("truechip.ListTestCase.tcListClick():" + tcName);
        }
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
    
    public TreeItem<GenericLogModel> addChildren(GenericLogModel model){
        TreeItem<GenericLogModel> childTreeItem= new TreeItem<>(model);
        if(model.isHasChild()){
            for(GenericLogModel child:model.getChild()){
                childTreeItem.getChildren().add(addChildren(child));
            }
        }
        return childTreeItem;
    }

}
