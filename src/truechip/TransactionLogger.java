package truechip;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import controller.MainPageController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import library.LogTreeItem;
import logger.DataList;
import logger.TCLogger;
import models.DetailedLogs;
import utils.ReadLogger;


/**
 * @author gauravsi
 *
 */
public class TransactionLogger {
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TransactionLogger.class);
	List<String> listOfKeysDetailed;
	String logFilePath;
	private TableView secondTable;
	Map<String, List> loggerMap;
	private AnchorPane mainContainer;
	Label logFileName = new Label();
	BorderPane listView = null;
	TreeView treeView = null;
	VBox rightVBox = null;
	TreeTableView<Map<String, Object>> centerListView;
	LogTreeItem rootItem;
	public TextArea consoleLog = new TextArea();
	FlowPane rightSideContent = new FlowPane();
	ComboBox targetComboBox = new ComboBox();
	Thread thread;
	MainPageController mainController;
	Button refreshButton;
	TextField searchTxt;
	
	public TransactionLogger(MainPageController mainController) {
		this.mainController = mainController;
		this.mainContainer = mainController.mainContainer;
		listView = new BorderPane();
		refreshButton= new Button();
		refreshButton.setTooltip(new Tooltip("Reload"));
		Image image = new Image("resources/refresh2.gif",15,15,true,true);
		refreshButton.setGraphic(new ImageView(image));
		refreshButton.setOnMouseClicked(this::setLoggerView);
		searchTxt= new TextField();
		searchTxt.setOnKeyReleased(this::search);
		ToolBar tool= new ToolBar(refreshButton,new Label("Search:"),searchTxt);
		listView.setTop(tool);
	}

	public BorderPane getNode() {
		return listView;
	}


	@FXML
	private void mylistclicked(MouseEvent event) {
		TreeItem selectedItem = (TreeItem) centerListView.getSelectionModel().getSelectedItem();
		ObservableList selectedIndex = centerListView.getSelectionModel().getSelectedCells();
		if (selectedIndex.size()>0) {
			showSecondaryTable((Map<String, Object>) selectedItem.getValue());
		}
	}
	public void showSecondaryTable(Map<String, Object> value){
		log.info("Adding Secondary Table data..." + value);
		secondTable.getItems().clear();
		for (Entry<String, Object> keySet : value.entrySet()) {
			if (listOfKeysDetailed.contains(keySet.getKey())) {
				DetailedLogs detailedLogs = new DetailedLogs(keySet.getKey(), (String) keySet.getValue());
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

	public TreeItem<Map<String, Object>> addChildren(LogTreeItem parent, DataList dataList) {
		// TreeItem<GenericLogModel> childTreeItem = new TreeItem<>(model);
		LogTreeItem childTreeItem = new LogTreeItem(dataList.getData(), this);
		//
		if (dataList.hasChild()) {
			for (DataList childDataList : dataList.getChild()) {
				childTreeItem.getChildren().add(addChildren(childTreeItem, childDataList));
			}
		}
		return childTreeItem;
	}



	public void refreshLogViewer(File logFile) {
		try {
			logFilePath=logFile.getAbsolutePath();
			ReadLogger logger = new ReadLogger(logFile);
			log.info("truechip.TransactionLogger.selectFile() map:");


			List<String[]> extractedData = TCLogger.extractData(logFile);

			TCLogger logData = new TCLogger(extractedData);

			List<DataList> tableData = logData.getLeftSideTableData();

//			GenericLogModel headerMapLeftSideTable = new GenericLogModel();
//			headerMapLeftSideTable.setLogMap(headerMap);
			

			rootItem = new LogTreeItem();

			for (int i = 0; i < tableData.size(); i++) {
				log.info("addding tableData: " + tableData.get(i).getData());

				DataList dataList = tableData.get(i);
				Map<String, Object> model = tableData.get(i).getData();

				TreeItem<Map<String, Object>> subItem = addChildren(rootItem,dataList);

				rootItem.getChildren().add(subItem);
			}

			centerListView = logger.getTLeftHandSideTable();
			centerListView.setRoot(rootItem);
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
			log.error(e);
		}
	}
	
	public void setLoggerView(MouseEvent event) {
		refreshLoggerView();
	}
	public void refreshLoggerView() {
		log.info("refreshing log view, file name:: "+logFilePath);
		File fileLogFile = new File(logFilePath);
		TransactionLogger newLoggerView= new TransactionLogger(mainController);
		newLoggerView.refreshLogViewer(fileLogFile);
	}
	public void search(KeyEvent event) {
		String filter=((TextField)event.getSource()).getText();
        if (filter.isEmpty()) {
        	centerListView.setRoot(rootItem);         
        }
        else {
        	LogTreeItem filteredRoot = new LogTreeItem();
            filter(rootItem, filter, filteredRoot);
            centerListView.setRoot(filteredRoot);
        }
		
		
		/*String text=((TextField)event.getSource()).getText();
		log.info("searching for text:: "+text);
		centerListView.getmode		
		log.info("searching for event:: "+event.getText());*/

	}
	
    private void filter(LogTreeItem root, String filter, LogTreeItem filteredRoot) {
        for (TreeItem<Map<String, Object>> child : root.getChildren()) {            
        	LogTreeItem filteredChild = new LogTreeItem(child.getValue(),this);
            filteredChild.setExpanded(true);
            filter((LogTreeItem)child, filter, filteredChild );
            if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
            	log.info(filteredChild.getValue() + " matches.");
                filteredRoot.getChildren().add(filteredChild);
            }
        }
    }
    
/*    private boolean isMatch(Map<String, Object> value, String filter) {
        return value.values().stream().anyMatch((Object o) -> o.toString().contains(filter));
    }*/
    
    private boolean isMatch(Map<String, Object> value, String filter) {
        return value.values().stream().anyMatch((Object o) -> o.toString().contains(filter));
    }
}
