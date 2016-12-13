/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TestDataTable {

    private TableView<TestDataModel> table = new TableView<TestDataModel>();
    public ObservableList<TestDataModel> data = null;
    final HBox hb = new HBox();

    public VBox getTestData(List<TestDataModel> testdata) {

        data =FXCollections.observableArrayList(testdata);
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("Argument");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<TestDataModel, String>("arguName"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<TestDataModel, String>>() {
            @Override
            public void handle(CellEditEvent<TestDataModel, String> t) {
                ((TestDataModel) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setArguName(t.getNewValue());
            }
        }
        );

        TableColumn lastNameCol = new TableColumn("Value");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<TestDataModel, String>("arguValue"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<TestDataModel, String>>() {
            @Override
            public void handle(CellEditEvent<TestDataModel, String> t) {
                ((TestDataModel) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setArguValue(t.getNewValue());
            }
        }
        );

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol);

        final TextField addEmail = new TextField();
        addEmail.setPromptText("Target");

        final Button addButton = new Button("Run");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        hb.getChildren().addAll(addEmail, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        return vbox;
    }

}
