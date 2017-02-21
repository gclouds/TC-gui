/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import constants.SystemConstants;
import constants.ToValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import truechip.ImportProject;
import truechip.ListTestCase;
import truechip.TabViewTransactionLogger;
import utils.Configurations;
import utils.MainWindowsUtils;

/**
 *
 * @author gauravsi
 */
public class MainPageController implements Initializable {
	public final static Logger log = Logger.getLogger(MainPageController.class);

	@FXML
	public Menu file_menu;
	
	@FXML
	public Menu file_units;

	@FXML
	public MenuItem file_testcaseList;
	@FXML
	public MenuItem file_runTestCase;
	@FXML
	public MenuItem file_runRegression;
	@FXML
	public MenuItem file_transactionLogger;
	@FXML
	public MenuItem file_importProject;
	@FXML
	public MenuItem file_importProjectPwd;
	@FXML
	public MenuItem file_exit;

	@FXML
	public MenuItem file_units_hex;
	@FXML
	public MenuItem file_units_dec;
	@FXML
	public MenuItem file_units_oct;
	@FXML
	public MenuItem file_units_bin;

	@FXML
	public AnchorPane mainContainer;

	@FXML
	public Label leftStatusLabel;

	@FXML
	public Label rightStatusLabel;

	@FXML
	public MenuItem workspacename;

	@FXML
	public ToolBar toolbarMain;

	public static ListTestCase listTestCase;
	TabViewTransactionLogger logViewer;

	private BorderPane mainView;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		log.info("initialize Main Page...");
		workspacename.setText(Configurations.getWorkSpaceFilePath().getAbsolutePath());
		
		mainView = new BorderPane();
		mainView.setTop(toolbarMain);
		listTestCase = new ListTestCase(mainContainer);
		listTestCase.build();

		logViewer = new TabViewTransactionLogger(this);
		// logViewer.build();

		BorderPane get = listTestCase.get();
		mainView.setCenter(get);
		addMainContainer(mainView);
		initToolBar();
		MainWindowsUtils.setMainController(this);
	}

	@FXML
	private void fileMenuSelected(ActionEvent event) {
		log.info(event);
		Object source = event.getTarget();
		MenuItem menuItem = (MenuItem) source;
		String id = menuItem.getId();
		log.info("controller.MainPageController.fileMenuSelected():" + id);

		switch (id) {
		case "file_testcaseList":
			BorderPane get = listTestCase.get();
			mainView.setCenter(get);
			initToolBar();
			break;
		case "file_runTestCase":
			listTestCase.get();
			break;
		case "file_runRegression":
			listTestCase.get();
			break;
		case "file_transactionLogger":
			mainView.setCenter(logViewer.getRootTab());
			break;
		case "file_importProject":
			doFileImport(null);
			break;
		case "file_exit":
			Stage stage = (Stage) mainContainer.getScene().getWindow();
			stage.close();
			System.exit(0);
			break;
		case "file_changeWorkSpace":
			log.info("controller.MainPageController.fileMenuSelected()workspacename");
			break;

		}
	}

	@FXML
	private void unitsMenuSelected(ActionEvent event) {
		log.info(event);
		Object source = event.getTarget();
		MenuItem menuItem = (MenuItem) source;
		String id = menuItem.getId();
		log.info("controller.MainPageController.fileMenuSelected():" + id);
		switch (id) {//
		case "file_units_hex":
			log.info("file_units_hex");
			SystemConstants.toValue = ToValue.TO_HEX;
			break;
		case "file_units_dec":
			log.info("file_units_dec");
			SystemConstants.toValue = ToValue.TO_DEC;
			break;
		case "file_units_oct":
			log.info("file_units_oct");
			SystemConstants.toValue = ToValue.TO_OCT;
			break;
		case "file_units_bin":
			log.info("file_units_bin");
			SystemConstants.toValue = ToValue.TO_BIN;
			break;
		}
		logViewer.refreshLoggerViewAll();
	}

	@FXML
	private void menu_workspace(ActionEvent event) {
		try {
			Configurations.removePrefrences();
			Stage stage = (Stage) mainContainer.getScene().getWindow();
			AppMain.restart(stage);

		} catch (Exception e) {
		}
	}

	public void addMainContainer(Node container) {
		mainContainer.getChildren().clear();
		mainContainer.getChildren().add(container);
		AnchorPane.setBottomAnchor(container, 0.0);
		AnchorPane.setTopAnchor(container, 0.0);
		AnchorPane.setLeftAnchor(container, 0.0);
		AnchorPane.setRightAnchor(container, 0.0);
	}

	public void refreshProject(MouseEvent event) {
		listTestCase.build();
		addMainContainer(listTestCase.get());
	}

	public void doFileImport(MouseEvent event) {
		leftStatusLabel.setText("processing...");
		ImportProject.importZip();
		leftStatusLabel.setText("Done");
	}

	private void initToolBar() {
		getToolbarMain().getItems().clear();
		Button changeWorkSpacebutton = new Button();
		changeWorkSpacebutton.getStyleClass().add("button-toolbar");
		changeWorkSpacebutton.setTooltip(new Tooltip("Change WorkSpace"));
		changeWorkSpacebutton.setGraphic(new ImageView(new Image("resources/change.jpg", 25, 25, false, false)));
		changeWorkSpacebutton.setOnAction(this::menu_workspace);

		Button removeProjectButton = new Button();
		removeProjectButton.getStyleClass().add("button-toolbar");
		removeProjectButton.setTooltip(new Tooltip("Remove Porject"));
		removeProjectButton.setGraphic(new ImageView(new Image("resources/remove.png", 25, 25, false, false)));
		removeProjectButton.setOnAction(listTestCase::removeProject);

		Button importProjectbutton = new Button();
		importProjectbutton.getStyleClass().add("button-toolbar");
		importProjectbutton.setTooltip(new Tooltip("Import Porject"));
		importProjectbutton.setGraphic(new ImageView(new Image("resources/import.png", 25, 25, false, false)));
		importProjectbutton.setOnMouseClicked(this::doFileImport);

		Button refreshProjectbutton = new Button();
		refreshProjectbutton.getStyleClass().add("button-toolbar");
		// refreshProjectbutton.setPadding(new Insets(0, 0, 0, 0));
		refreshProjectbutton.setTooltip(new Tooltip("Refresh Porject"));
		refreshProjectbutton.setGraphic(new ImageView(new Image("resources/refresh.png", 25, 25, false, false)));
		refreshProjectbutton.setOnMouseClicked(this::refreshProject);

		getToolbarMain().getItems().addAll(importProjectbutton, removeProjectButton, changeWorkSpacebutton,
				refreshProjectbutton);
	}

	public ToolBar getToolbarMain() {
		return toolbarMain;
	}


}
