package truechip;

import java.io.File;

import controller.MainPageController;
import de.jensd.shichimifx.utils.TabPaneDetacher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Configurations;

public class TabViewTransactionLogger {

	MainPageController mainController;
	private File logFile;
	private TabPane rootTab;
	Button refreshButton;


	public TabViewTransactionLogger(MainPageController mainController) {
		this.mainController = mainController;
		rootTab = new TabPane();
		TabPaneDetacher.create().makeTabsDetachable(rootTab);
		refreshButton = new Button();
		refreshButton.setTooltip(new Tooltip("Refresh"));
		Image image = new Image("resources/refresh2.gif",15,15,true,true);
		BackgroundImage bgI= new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background background = new Background(bgI);
		refreshButton.setGraphic(new ImageView(image));
		//refreshButton.setBackground(background);
	}
	
	
	@FXML
	private void selectFile(MouseEvent event) {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setInitialDirectory(Configurations.getWorkSpaceFilePath());
			chooser.setTitle("Open File");
			logFile = chooser.showOpenDialog(new Stage());
			createNewTab(logFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void createNewTab(File logFile) {
		TransactionLogger newLoggerView= new TransactionLogger(mainController);
		Tab newLogTab= new Tab();
		newLogTab.setText(logFile.getName());
		Tooltip tip= new Tooltip(logFile.getAbsolutePath());
		newLogTab.setTooltip(tip);
		newLogTab.setContent(newLoggerView.getNode());
		newLoggerView.refreshLogViewer(logFile);
		rootTab.getTabs().add(newLogTab);
		rootTab.selectionModelProperty().get().select(newLogTab);
	}


	public void refreshLoggerView() {
		Tab selectedItem = rootTab.getSelectionModel().getSelectedItem();
		String currentfilePath = selectedItem.getTooltip().getText();
		File fileLogFile = new File(currentfilePath);
		TransactionLogger newLoggerView= new TransactionLogger(mainController);
		selectedItem.setContent(newLoggerView.getNode());
		newLoggerView.refreshLogViewer(fileLogFile);
	}


	public TabPane getRootTab() {
		mainController.getToolbarMain().getItems().clear();
		Button browseButton = new Button("browse log file");
		browseButton.setOnMouseClicked(this::selectFile);
		refreshButton.setOnMouseClicked(this::setLoggerView);
		mainController.getToolbarMain().getItems().addAll(new Label("Logger Viewer"), browseButton,refreshButton);
		return rootTab;
	}
	public void setLoggerView(MouseEvent event) {
		refreshLoggerView();
	}

}
