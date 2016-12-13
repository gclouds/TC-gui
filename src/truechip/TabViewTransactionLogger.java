package truechip;

import java.io.File;
import java.util.Iterator;

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
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TabViewTransactionLogger.class);

	MainPageController mainController;
	private File logFile;
	private TabPane rootTab;
	Button refreshButton;


	public TabViewTransactionLogger(MainPageController mainController) {
		this.mainController = mainController;
		rootTab = new TabPane();
		TabPaneDetacher.create().makeTabsDetachable(rootTab);
		refreshButton = new Button();
		
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
			log.error(e);
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
	public void refreshLoggerViewAll() {
		Iterator<Tab> iterator = rootTab.getTabs().iterator();
		while(iterator.hasNext()){
			Tab selectedItem = iterator.next();
			String currentfilePath = selectedItem.getTooltip().getText();
			File fileLogFile = new File(currentfilePath);
			TransactionLogger newLoggerView= new TransactionLogger(mainController);
			selectedItem.setContent(newLoggerView.getNode());
			newLoggerView.refreshLogViewer(fileLogFile);
		}
		
	}

	public TabPane getRootTab() {
		mainController.getToolbarMain().getItems().clear();
		Button browseButton = new Button("Browse");
		browseButton.setOnMouseClicked(this::selectFile);
		mainController.getToolbarMain().getItems().addAll(new Label("Logger Viewer"), browseButton);
		return rootTab;
	}
	public void setLoggerView(MouseEvent event) {
		refreshLoggerView();
	}

}
