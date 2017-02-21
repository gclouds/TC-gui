/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truechip;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import constants.SystemConstants;
import controller.ConsoleController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.ProjectData;
import utils.Configurations;
import utils.FileReader;
import utils.RunThread;

/**
 *
 * @author gauravsi
 */
public class ListTestCase {
	public final static Logger log = Logger.getLogger(ListTestCase.class);

    private AnchorPane mainContainer;
    BorderPane listView = null;
    TreeView treeView = null;
    String outputfolder = Configurations.getWorkSpaceFilePath().getAbsolutePath();
    String makeFilePath = "";
    String projectPath = null;
    String tcName = "";
    VBox rightVBox = null;
    ListView centerListView = null;
    GridPane rightGrid = null;
    public TextArea consoleLog = new TextArea();
    public Button runButton = new Button("Run");
    FlowPane rightSideContent = new FlowPane();
    ComboBox<String> targetComboBox = new ComboBox<String>();
    Thread thread;

    Map<String, ProjectData> projects;

    public ListTestCase(AnchorPane mainContainer) {
        this.mainContainer = mainContainer;
        rightGrid = new GridPane();
        build();
    }

    public void build() {

        projects = new HashMap<String, ProjectData>();

        // Create the TreeView
        treeView = new TreeView();

        TreeItem rootItem = new TreeItem("Projects");
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        File file = new File(outputfolder);
        
        String[] names = file.list();

        for (String name : names) {
            String projectLocation = outputfolder + File.separator + name;
            if (new File(projectLocation).isDirectory()) {
                ProjectData projectdata = new ProjectData(projectLocation);
                TreeItem project = new TreeItem(name);
                rootItem.getChildren().add(project);
                try {
                    for (Map.Entry<String, String> keySet : projectdata.getTestcases().entrySet()) {
                        project.getChildren().add(new TreeItem(keySet.getKey()));
                    }
                } catch (Exception e) {
                }
                projects.put(name, projectdata);

            }
        }
        file = null;
        treeView.setOnMouseClicked(this::projectListClick);

        centerListView = new ListView<String>();
        centerListView.setOnMouseClicked(this::tcListClick);
        VBox centerVbox = new VBox();
        centerVbox.getChildren().add(new Label("Test Case List"));
        centerVbox.getChildren().add(centerListView);

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Remove");

        item1.setOnAction(this::removeProject);
        contextMenu.getItems().addAll(item1);
        treeView.setContextMenu(contextMenu);

        listView = new BorderPane();
        listView.setTop(new Label("Test Case List"));
        listView.setLeft(treeView);
        listView.setCenter(centerListView);
        listView.setRight(new Label("Please select a TestCase List"));

        targetComboBox.setOnAction((event) -> {
            String item = targetComboBox.getSelectionModel().getSelectedItem().toString();
            if (!item.equalsIgnoreCase("regress")) {
                for (Object nextCheckBox : centerListView.getItems()) {
                    try {
                        CheckBox checkItem = (CheckBox) nextCheckBox;
                        checkItem.setSelected(false);
                    } catch (ClassCastException e) {
                    }
                }
            }
        });

    }

    public BorderPane get() {
        log.info("truechip.ListTestCase.get()");
        if (listView == null) {
            build();
            return listView;
        } else {
            return listView;
        }

    }

    public List<String> getAllTestCases(String folderName) {
        String pathToSim = outputfolder + File.separator + folderName + File.separator + "sim";
        return null;
    }

    @FXML
    private void projectListClick(MouseEvent event) {
        centerListView.getItems().clear();
        tcName = null;
        TreeView name = (TreeView) event.getSource();

        TreeItem item = (TreeItem) name.getSelectionModel().getSelectedItem();
        if (item != null && item.isLeaf()) {
            buidCenterTc((String) item.getParent().getValue(), item.getValue().toString());

            rightGrid.getChildren().clear();
            BuildRightGrid((String) item.getParent().getValue());
            ScrollPane sp = new ScrollPane();
            sp.setContent(rightGrid);
            VBox.setVgrow(sp, Priority.ALWAYS);

            //rightSideContent.getChildren().add(sp);
            listView = new BorderPane();
            listView.setTop(new Label("Test Case List"));
            listView.setLeft(treeView);
            listView.setCenter(centerListView);
            listView.setRight(sp);

            addToMainContainer(listView);

        } else if (item != null) {
            projectPath = outputfolder + File.separator + item.getValue();
        }
    }

    @FXML
    private void tcListClick(MouseEvent event) {
        ListView testCaseSelected = (ListView) event.getSource();
        if (testCaseSelected != null) {
            tcName = testCaseSelected.getSelectionModel().getSelectedItem().toString();
            log.info("truechip.ListTestCase.tcListClick():" + tcName);
        }
    }

    @FXML
    private void runATestDetailed(ActionEvent event) {
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
//        if (item != null && item.isLeaf() && projects.containsKey(item.getParent().getValue())) {
        boolean isRgress = (targetComboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("regress"));

        ProjectData project = projects.get(item.getParent().getValue());
        log.info("truechip.ListTestCase.runATestDetailed(): " + tcName);
        log.info("truechip.ListTestCase.runATestDetailed()" + event);
        if (runButton.getText().equalsIgnoreCase("Run")) {
            runTest(project);
        } else {
            runButton.setText("Run");
            thread.interrupt();
            thread.stop();
            consoleLog.appendText("\nCurrent exeuction state: killed");
            thread = null;
        }

    }

    private void runTest(ProjectData project) {
        log.info("inside running test...");
        String command = "make " + targetComboBox.getSelectionModel().getSelectedItem().toString();
        String selectedTarget = targetComboBox.getSelectionModel().getSelectedItem().toString();
        List<String> testcaseNames = new ArrayList<String>();
        for (Object nextCheckBox : centerListView.getItems()) {
            try {
                CheckBox checkItem = (CheckBox) nextCheckBox;
                if (checkItem.isSelected()) {
                    testcaseNames.add(checkItem.getText());
                }
            } catch (ClassCastException e) {
            }
        }
        if (testcaseNames.size() == 0) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Waring");
            alert.setHeaderText("Please select atleast one Test Case!!!");
            alert.setContentText("To select multiple TestCases,please select appropiate Target.");
            alert.showAndWait();
        } else if (testcaseNames.size() > 0) {
            Iterator<String> iterator = project.getListSwitches().iterator();
            log.info("Size of the iterator: " + project.getListSwitches().size());

            boolean isAllGood = true;
            if (testcaseNames.size() == 1) {
                command = command + " TESTNAME=" + testcaseNames.get(0);
            } else if (testcaseNames.size() > 1 && selectedTarget.equalsIgnoreCase("regress")) {
                isAllGood = FileReader.overideRegressList(testcaseNames, project.getMakeFilePath() + SystemConstants.REGRESS_FILE_NAME);
            }

            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] split = line.split("\\?=");
                try {
                    String id = split[0].trim();
                    log.info("loking for : " + id);
                    TextField lookup = (TextField) rightGrid.lookup("#" + id);
                    // Logger.info(">>>>>>>>>>>>>>>>>>>>>>.." + lookup.getText());
                    if (!lookup.getText().isEmpty()) {
                        // command = command + " " + line.split("\\?=")[0].trim() + "=" + lookup.getText();
                        command = command + " " + lookup.getId() + "=" + lookup.getText();
                    }
                } catch (Exception e) {
                	log.error(e);
                    ConsoleController.appendToConsole("error: " + e.getLocalizedMessage());
                    //consoleLog.setText("Something went worng!!!\n");
                    runButton.setText("Run");
                }
            }
            if (isAllGood) {
                try {
                    log.info("Runing for make file path: " + project.getMakeFilePath());
                    //RunCommand.exeCommand(command, makeFileDir, consoleLog);
                    runButton.setText("Kill");
                    ConsoleController.appendToConsole("running command: " + command);
                    //consoleLog.appendText("Running Command: " + command + "\n");
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/TestCaseConsole.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Console");
                    stage.setScene(new Scene(root, 450, 450));
                    stage.show();
                    stage.setOnCloseRequest(this::stopExecution);

                    RunThread mainThread = new RunThread(command, project.getMakeFilePath(), this);
                    thread = new Thread(mainThread);
                    thread.start();
                } catch (Exception e) {
                    log.error(e);
                    ConsoleController.appendToConsole("error: " + e.getLocalizedMessage());
                    runButton.setText("Run");
                }
            }
        }

    }

    public void removeProject(ActionEvent event) {
        try {
            FileReader.deleteProject(projectPath);
            refreshMainContainer();
        } catch (Exception e) {
        	log.error(e);
            refreshMainContainer();
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

    public void stopExecution(Event event) {
        runButton.setText("Run");
        thread.interrupt();
        thread.stop();
        consoleLog.appendText("\nCurrent exeuction state: killed");
        thread = null;
    }

    public void addToMainContainer(BorderPane listView) {
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(listView);
        AnchorPane.setBottomAnchor(get(), 0.0);
        AnchorPane.setTopAnchor(get(), 0.0);
        AnchorPane.setLeftAnchor(get(), 0.0);
        AnchorPane.setRightAnchor(get(), 0.0);
    }

    public void BuildRightGrid(String projectname) {
        rightGrid.setMinWidth(300.0);
        try {
            ProjectData project = projects.get(projectname);
            List<String> makeFileTargets = project.getListTargets();
            log.info("Size of targets: " + makeFileTargets.size());
            if (makeFileTargets.size() > 0) {
                runButton.setOnAction(this::runATestDetailed);
                Label targetLabel = new Label("Targets: ");
                targetComboBox.getItems().clear();
                for (String line : makeFileTargets) {
                    log.info("adding targets...");
                    targetComboBox.getItems().add(line);
                }
                targetComboBox.setValue(targetComboBox.getItems().get(0));
                rightGrid.setPadding(new Insets(5.0));
                rightGrid.setVgap(2.0);
                rightGrid.setHgap(2.0);
                int rowCount = 0;
                HBox firsRow = new HBox(8, targetLabel, targetComboBox, runButton);
                rightGrid.add(firsRow, 0, rowCount++, 2, 1);

                List<String> readMakeFileArg = project.getListSwitches();
                log.info("adding test data...." + readMakeFileArg.size());
                Iterator<String> iterator = readMakeFileArg.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String[] line = next.split("\\?=");
                    log.info("adding at row: " + rowCount);
                    try {
                        Label label1 = new Label(line[0].trim());
                        label1.setAlignment(Pos.CENTER_RIGHT);
                        TextField textField = new TextField();
                        log.info("Adding text field: " + next);
                        if (line[0].trim().equalsIgnoreCase(SystemConstants.defaultRegerssSwitchName)) {
                            textField.setText(SystemConstants.REGRESS_FILE_NAME);
                        } else if (line.length > 1) {
                            textField.setText(line[1].trim());
                        } else {
                            textField.setText("");
                        }
                        textField.setId(line[0].trim());
                        rightGrid.addRow(rowCount++, label1, textField);
                    } catch (Exception e) {
                    	log.error(e);
                    }
                }
            }
        } catch (Exception e) {
        	log.error(e);
        }
    }

    public void buidCenterTc(String projectName, String tCName) {
        ProjectData project = projects.get(projectName);
        String testCaseFilePath = project.getTestcases().get(tCName);
        String makeFilePath = project.getMakeFilePath();
        List<String> testCaseNames = FileReader.readLines(testCaseFilePath);
        centerListView.getItems().clear();

        Button checkboxSelectAll = new Button("Select All");
        checkboxSelectAll.setFont(Font.font(10));
        checkboxSelectAll.setOnAction((event) -> {
            targetComboBox.getSelectionModel().selectLast();
            for (Object nextCheckBox : centerListView.getItems()) {
                try {
                    CheckBox checkItem = (CheckBox) nextCheckBox;
                    checkItem.setSelected(true);
                } catch (ClassCastException e) {
                }
            }
        });
        Button checkboxDeSelectAll = new Button("Deselect All");
        checkboxDeSelectAll.setFont(Font.font(10));
        checkboxDeSelectAll.setOnAction((event) -> {
            for (Object nextCheckBox : centerListView.getItems()) {
                try {
                    CheckBox checkItem = (CheckBox) nextCheckBox;
                    checkItem.setSelected(false);
                } catch (ClassCastException e) {
                	
                }
            }
        });

        HBox hbox = new HBox(5.0);
        hbox.getChildren().addAll(checkboxSelectAll, checkboxDeSelectAll);
        centerListView.getItems().add(hbox);
        for (String tescase : testCaseNames) {
            CheckBox checkbox = new CheckBox(tescase);
            checkbox.setOnAction((event1) -> {
                if (!targetComboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("regress")) {
                    int count = 0;
                    for (Object nextCheckBox : centerListView.getItems()) {
                        try {
                            CheckBox checkItem = (CheckBox) nextCheckBox;
                            if (checkItem.isSelected()) {
                                count++;
                            }
                        } catch (ClassCastException e) {
                        }
                    }
                    if (count > 1) {
                        ((CheckBox) event1.getSource()).setSelected(false);
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Waring");
                        alert.setHeaderText("Cannot select more than one Test Case!!!");
                        alert.setContentText("To select multiple TestCases,please select appropiate Target.");
                        alert.showAndWait();
                    }
                }
            });
            centerListView.getItems().add(checkbox);
        }
    }

    void refreshCenterList(ActionEvent event) {

    }

    @FXML
    void selectAll(ActionEvent event) {

    }

    @FXML
    void deSelectAll(ActionEvent event) {

    }
}
