/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.pciLogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import logger.Logger;
import models.DetailedLogs;
import models.PCI_Request_LogModel;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author gauravsi
 */
public class PCI_LogUtil {

    File logFile;
    private TableView table = new TableView();
    private TableView detailedLogReport;

    public TableView getDetailedLogReport() {
        return detailedLogReport;
    }

    public void setDetailedLogReport(TableView detailedLogReport) {
        this.detailedLogReport = detailedLogReport;
    }

  

    private List<String[]> requestLine;
    private List<String[]> completionLine;
    PCI_Request_LogModel model ;


    public PCI_LogUtil(File logFile) {
        this.logFile = logFile;
        getProcessedLog();
        model = new PCI_Request_LogModel(requestLine,completionLine);
        model.setTypeOFValue(ValuesConstants.DECIMAL);
        
    }

    public void showLog(GridPane mainAnchorPane, Double margin) {
        Logger.info("PCI_LogUtil>>showLog started: "+model.size);

        table.setEditable(true);

        table.setOnMouseClicked(this::mylistclicked);
                
        TableColumn request = new TableColumn("Request");
        TableColumn completion = new TableColumn("Completion");
        
        TableColumn requestTl = new TableColumn("Request TL");
        TableColumn requestDir = new TableColumn("Direction");
        TableColumn requestTime = new TableColumn("Time");
        TableColumn completiontl = new TableColumn("Completion TL");
        TableColumn completionDir = new TableColumn("Direction");
        TableColumn completionTime = new TableColumn("Time");
        
        requestTl.setCellValueFactory(new PropertyValueFactory<PCI_Model,String>("requestTl"));
        requestDir.setCellValueFactory(new PropertyValueFactory<PCI_Model,String>("requestDir"));
        requestTime.setCellValueFactory(new PropertyValueFactory<PCI_Model,String>("requestTime"));
        completiontl.setCellValueFactory(new PropertyValueFactory<PCI_Model,String>("completiontl"));
        completionDir.setCellValueFactory(new PropertyValueFactory<PCI_Model,String>("completionDir"));
        completionTime.setCellValueFactory(new PropertyValueFactory<PCI_Model,String>("completionTime"));
        
        
        request.getColumns().addAll(requestTl,requestDir,requestTime);
        completion.getColumns().addAll(completiontl,completionDir,completionTime);

        request.setCellValueFactory(new PropertyValueFactory<PCI_Request_LogModel,String>("Direction"));
        completion.setCellValueFactory(new PropertyValueFactory<PCI_Request_LogModel,String>("TypeofTransaction"));
        
        table.getColumns().addAll(request, completion);
        
        ObservableList<PCI_Model> data = table.getItems();
        
        Logger.info("PCI_LogUtil>>showLog adding model: "+model.size);

        for(int i=1;i<model.size;i++){
            Logger.info(">>adding items: "+i);
            data.add(new PCI_Model(model, i));
        }
        
        table.setItems(data);
        
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mainAnchorPane.add(table, 0, 1);
    }

    public void getProcessedLog() {
        BufferedReader br ;
        try {
            List<String> readLines = FileUtils.readLines(logFile);

            br = new BufferedReader(new FileReader(logFile));

            requestLine = new ArrayList<String[]>();
            completionLine = new ArrayList<String[]>();

            String line = br.readLine();

            while (line != null) {
                String[] lineSplit = line.split("&");
                requestLine.add(lineSplit[0].split("\\|"));
                completionLine.add(lineSplit[1].split("\\|"));
                Logger.info("line size busffer array: "+lineSplit.length+">"+requestLine.size()+">"+completionLine.size());
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
    
    @FXML
    private void mylistclicked(MouseEvent event) {
        PCI_Model selectedItems = (PCI_Model) table.getSelectionModel().getSelectedItem();
        Logger.info("Adding detailed table row.........."+selectedItems.getRequestTl());
        
        ObservableList allData = detailedLogReport.getItems();
        allData.clear();
        for(int i =PCI_Constants_Req.Type.getIndex();i<selectedItems.requestLine.length;i++){
            String requestCol=PCI_Model.catchNull(requestLine.get(1), i)+": "+PCI_Model.catchNull(selectedItems.requestLine, i);
            String completionCol=PCI_Model.catchNull(completionLine.get(1), i)+": "+PCI_Model.catchNull(selectedItems.completionLine, i);
            DetailedLogs detailedLogs= new DetailedLogs(requestCol, completionCol);
            allData.add(detailedLogs);
        }
        detailedLogReport.setItems(allData);
        detailedLogReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
}
