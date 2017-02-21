/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truechip;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import constants.SystemConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.TestDataModel;
import utils.Configurations;
import utils.FIleFilter;
import utils.FileReader;
import utils.RunCommand;

/**
 *
 * @author gauravsi
 */
public class RegressionTestCase {
	public final static Logger log = Logger.getLogger(RegressionTestCase.class);

    private AnchorPane mainContainer;
    BorderPane listView = null;
    TreeView treeView = null;
    String outputfolder = Configurations.getWorkSpaceFilePath().getAbsolutePath();
    String makeFilePath = "";
    String tcName = "";
    VBox rightVBox = null;
    ListView centerListView = null;
    public TextArea consoleLog = new TextArea();
    FlowPane rightSideContent = new FlowPane();
    ComboBox targetComboBox = null;

    public RegressionTestCase(AnchorPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void build() {
        log.info("truechip.ListTestCase.build()");
        //Logger.info("truechip.ListTestCase.build()");
        // Create the TreeView
        treeView = new TreeView();

        TreeItem rootItem = new TreeItem("Projects");
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);

        File file = new File(outputfolder);
        log.info("truechip.ListTestCase.build() path searching: " + file.getAbsolutePath());
        String[] names = file.list();
        log.info("truechip.ListTestCase.build() path size: " + names.length);

        for (String name : names) {
            log.info(outputfolder + File.separator + name);

            if (new File(outputfolder + File.separator + name).isDirectory()) {
                log.info(name);
                TreeItem project = new TreeItem(name);
                rootItem.getChildren().add(project);
                project.getChildren().clear();

                try {
                    Iterator<String> allTestCases = getAllTestCases(name).iterator();
                    while (allTestCases.hasNext()) {
                        String next = allTestCases.next();
                        project.getChildren().add(new TreeItem(next));
                    }
                } catch (Exception e) {

                }

            }
        }

        treeView.setOnMouseClicked(this::mylistclick);

        centerListView = new ListView<String>();
        centerListView.setOnMouseClicked(this::tcListClick);
        VBox centerVbox = new VBox();
        centerVbox.getChildren().add(new Label("Test Case List"));
        centerVbox.getChildren().add(centerListView);

        rightVBox = new VBox();
        rightVBox.setPadding(new Insets(10));
        rightVBox.setSpacing(8);

        rightSideContent.setPadding(new Insets(5, 0, 5, 0));
        rightSideContent.setVgap(4);
        rightSideContent.setHgap(4);
        rightSideContent.setPrefWrapLength(170);
        //listView.setRight(rightSideContent);

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Run Test Case (Default)");
        MenuItem item2 = new MenuItem("Run Test Case (Detailed)");
        MenuItem item3 = new MenuItem("Run a regressioin");
        item1.setOnAction(this::runATest);
        item2.setOnAction(this::runATestDetailed);
        contextMenu.getItems().addAll(item1, item2, item3);
        centerListView.setContextMenu(contextMenu);

        listView = new BorderPane();
        listView.setTop(new Label("Test Case List"));
        listView.setLeft(treeView);
        listView.setCenter(centerListView);
        listView.setRight(new Label("please select a TestCase List"));

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
        List output = new ArrayList<String>();
        String fullPath = outputfolder + File.separator + folderName + File.separator + "sim";
        log.info("truechip.ListTestCase.getAllTestCases()" + fullPath);
        File file = new File(fullPath);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("text only", "txt");
        File[] listFiles = file.listFiles(new FIleFilter("txt"));
        for (File names : listFiles) {
            output.add(names.getName());
            log.info("truechip.ListTestCase.getAllTestCases():" + names.getName());
        }

        return output;

    }

    @FXML
    private void mylistclick(MouseEvent event) {
        centerListView.getItems().clear();
        tcName = null;
        TreeView name = (TreeView) event.getSource();
        //TreeItem name = (TreeItem)event.getSource();
        TreeItem item = (TreeItem) name.getSelectionModel().getSelectedItem();
        if (item.isLeaf()) {
            String fullPath = outputfolder + File.separator + item.getParent().getValue() + File.separator + "sim" + File.separator + item.getValue();
            makeFilePath = outputfolder + File.separator + item.getParent().getValue() + SystemConstants.defaultMakeFilePath;
            File testcaseFile = new File(fullPath);
            File makeFile = new File(makeFilePath);
            log.info("truechip.ListTestCase.mylistclick()" + fullPath);
            List<String> readLines = FileReader.readLines(fullPath);
            centerListView.getItems().clear();
            centerListView.getItems().addAll(readLines);
            //adding targets
            List<String> readMakeFileTargets = FileReader.readMakeFileTargets(makeFilePath);
            log.info("size of targets: " + readMakeFileTargets);
            targetComboBox = new ComboBox();
            Button runButton = new Button("Run");
            runButton.setOnAction(this::runATestDetailed);
            Label targetLabel = new Label("Targets: ");
            for (String line : readMakeFileTargets) {
                log.info("adding targets...");
                targetComboBox.getItems().add(line);

            }
            targetComboBox.setValue(targetComboBox.getItems().get(0));
            HBox targetHBox = new HBox();
            targetHBox.getChildren().addAll(targetLabel, targetComboBox, runButton);
            targetHBox.setSpacing(10);
            rightVBox.getChildren().add(targetHBox);

            List<String> readMakeFileArg = FileReader.readMakeFileArg(makeFilePath);
            List<TestDataModel> rowDataList = new ArrayList<TestDataModel>();
            log.info("adding test data...." + readMakeFileArg.size());
            Iterator<String> iterator = readMakeFileArg.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                //Logger.info(line.split("\\?=")[0].trim() + " data...." + line.split("\\?=")[1].trim());
                Label label1 = new Label(line.split("\\?=")[0].trim());
                TextField textField = new TextField();
                textField.setId(line.split("\\?=")[0].trim());
                //Logger.info("Argumtents: "+textField.getId());
                //textField.setText(line.split("\\?=")[1].trim());
                HBox hb = new HBox();
                hb.getChildren().addAll(label1, textField);
                hb.setSpacing(10);
                rightVBox.getChildren().add(hb);
            }

            log.info("adding test rowDataList...." + rowDataList.size());
            ScrollPane sp = new ScrollPane();
            sp.setContent(rightVBox);
            VBox.setVgrow(sp, Priority.ALWAYS);
            //rightSideContent.getChildren().add(sp);
            listView = new BorderPane();
            listView.setTop(new Label("Test Case List"));
            listView.setLeft(treeView);
            listView.setCenter(centerListView);
            listView.setRight(sp);
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(listView);
            AnchorPane.setBottomAnchor(listView, 0.0);
            AnchorPane.setTopAnchor(listView, 0.0);
            AnchorPane.setLeftAnchor(listView, 0.0);
            AnchorPane.setRightAnchor(listView, 0.0);
            showPopUp();

        } else {

        }

    }

    @FXML
    private void tcListClick(MouseEvent event) {
        ListView listView = (ListView) event.getSource();
        tcName = listView.getSelectionModel().getSelectedItem().toString();
        log.info("truechip.ListTestCase.tcListClick():" + tcName);

    }

    @FXML
    private void runATest(ActionEvent event) {
        log.info("truechip.ListTestCase.runATest()");
        runTest(true);

    }

    @FXML
    private void runATestDetailed(ActionEvent event) {
        if (tcName != null) {
            log.info("truechip.ListTestCase.runATestDetailed(): " + tcName);
            runTest(false);
        } else {
            consoleLog.appendText("\nplease select a test case!!! ");

        }

    }

    private void showPopUp() {
        consoleLog.setDisable(false);
        listView.setBottom(consoleLog);
    }

    private void runTest(boolean isDefaultRun) {
        File makeFile = new File(makeFilePath);
        String parent = makeFile.getParent();
        File makeFileDir = new File(parent);

        //String command = makeFilePath + " TESTNAME=" + tcName;
        String command = "make " + targetComboBox.getSelectionModel().getSelectedItem().toString() + " TESTNAME=" + tcName;

        if (isDefaultRun) {
        } else {

            List<String> readMakeFileArg = FileReader.readMakeFileArg(makeFilePath);
            Iterator<String> iterator = readMakeFileArg.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                log.info(line.split("\\?=")[0].trim() + " data...." + line.split("\\?=")[1].trim());
                TextField lookup = (TextField) rightVBox.lookup("#" + line.split("\\?=")[0].trim());
                //Logger.info(">>>>>>>>>>>>>>>>>>>>>>.." + lookup.getText().trim().equalsIgnoreCase(""));
                if (!lookup.getText().trim().equalsIgnoreCase("")) {
                    // command = command + " " + line.split("\\?=")[0].trim() + "=" + lookup.getText();
                    command = command + " " + lookup.getId() + "=" + lookup.getText();

                }
            }
        }
        consoleLog.appendText("\nrunning command: " + command);
        try {
            log.info("dir: " + makeFileDir.getAbsolutePath());
            RunCommand.exeCommand(command, makeFileDir,consoleLog);

        } catch (Exception e) {
            consoleLog.appendText("\n" + e.getLocalizedMessage());
        }

//        RunCommand.exeCommand("cd " + outputfolder);
//        RunCommand.exeCommand("ipconfig");
//        RunCommand.exeCommand("dir");
        log.info("truechip.ListTestCase.runATestDetailed()");

    }
}
