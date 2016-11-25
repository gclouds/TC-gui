/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxapplication2.pciLogUtil.PCI_LogUtil;
import logger.Logger;
import models.DetailedLogs;

/**
 *
 * @author gauravsi
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ListView<String> myProductList;
    @FXML
    private TableView detailedLogReport;
    
    @FXML
    TableColumn requestColDetails;
    
    @FXML
    TableColumn completionColDetails;

    @FXML
    private Label label;

    @FXML
    public TableView logResultTable;

    @FXML
    public GridPane mainGridPane;
    
    @FXML
    public AnchorPane mainAnchorPane;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Logger.info("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myProductList.setOnMouseClicked(this::mylistclicked);
        String[] listOfProduct = {"GUI PCI", "XYZ", "ABC"};
        ObservableList<String> observableArrayList = FXCollections.observableArrayList(listOfProduct);
        myProductList.setItems(observableArrayList);
        
        AnchorPane.setBottomAnchor(mainGridPane, 0.0);
        AnchorPane.setRightAnchor(mainGridPane, 0.0);
        AnchorPane.setLeftAnchor(mainGridPane, 0.0);
        AnchorPane.setTopAnchor(mainGridPane, 0.0);
        
        requestColDetails.setCellValueFactory(new PropertyValueFactory<DetailedLogs,String>("requestCol"));
        completionColDetails.setCellValueFactory(new PropertyValueFactory<DetailedLogs,String>("completionCol"));
        // TODO
    }

    @FXML
    private void mylistclicked(MouseEvent event) {
        String selectedItem = myProductList.getSelectionModel().getSelectedItem();
        if (selectedItem.equalsIgnoreCase("GUI PCI")) {
            Button selectFileButton = new Button("Browse Log File");
            selectFileButton.setOnAction(this::selectFile);
            mainGridPane.add(selectFileButton, 0, 0);
            mainGridPane.setVisible(true);
        }else{
            mainGridPane.setVisible(false);
        }
        Logger.info("hi.........." + selectedItem);
    }

    @FXML
    private void selectFile(ActionEvent event) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");
            File logFile = chooser.showOpenDialog(new Stage());

            PCI_LogUtil log = new PCI_LogUtil(logFile);
            log.setDetailedLogReport(detailedLogReport);
            log.showLog(mainGridPane, 20.0);

        } catch (Exception e) {

        }

    }

}
