package library;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import constants.SystemConstants;
import constants.ToValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import logger.Logger;
import logger.SuperLoggger;
import numbersystem.Convertor;
import truechip.TransactionLogger;
import utils.GenericLogModel;
import utils.NumberSystemUtils;

public class LogTreeItem extends TreeItem<GenericLogModel>{
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogTreeItem.class);

	ContextMenu contextMenu=new ContextMenu();
	TransactionLogger root;
	
	MenuItem toHexMenu = new MenuItem("to HEX");
	MenuItem toDecMenu = new MenuItem("to DEC");
	MenuItem toOctMenu = new MenuItem("to OCT");
	MenuItem toBinMenu = new MenuItem("to BIN");


	public LogTreeItem(GenericLogModel value,TransactionLogger transactionLogger) {
		super(value);
		contextMenu.getItems().addAll(toBinMenu,toDecMenu,toHexMenu,toOctMenu);
		this.root=transactionLogger;
		
	}


	public ContextMenu getContextMenu() {
		toHexMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				log.info(event.getSource().getClass());
				getConvertedLogModel(getValue(),ToValue.TO_HEX);
				}
		});
		toDecMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getConvertedLogModel(getValue(),ToValue.TO_DEC);			}
		});
		toOctMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getConvertedLogModel(getValue(),ToValue.TO_OCT);
			}
		});
		toBinMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getConvertedLogModel(getValue(),ToValue.TO_BIN);
			}
		});
		return contextMenu;
	}


	public void setContextMenu(ContextMenu contextMenu) {
		this.contextMenu = contextMenu;
	}

	public void getConvertedLogModel(GenericLogModel logModel,ToValue toValue){
		for(Entry<String, String> keyset:logModel.getLogMap().entrySet()){
			String columnHeader = keyset.getKey();
			String cellvalue = keyset.getValue();
			log.info("converting value for:: "+keyset.getKey()+":"+cellvalue);
			String[] splitedCellValue = cellvalue.split("'");
			if(splitedCellValue.length==2){
				try {
					cellvalue=getConvertedValue(splitedCellValue[1].charAt(0)+"",splitedCellValue[0], splitedCellValue[1].substring(1),toValue);
					logModel.getLogMap().put(columnHeader, cellvalue);
					log.info("converted value for:: "+keyset.getKey()+":"+cellvalue);
				} catch (Exception e) {
					log.error(e);
				}
				
			}
		}
		HashMap<String, String> logMap = (HashMap<String, String>)logModel.getLogMap().clone();
		logModel.setLogMap(logMap);
		setValue(null);
		setValue(logModel);
		root.showSecondaryTable(logModel);
	}
	
	public static String getConvertedValue(String columnHeader,String prefix, String value,ToValue toValue) throws Exception {
		if (columnHeader.contains("d")) {
			Convertor convertor = NumberSystemUtils.getDecConvertor();
			value=prefix+toValue.getPrefix()+toValue.getConvertedValue(convertor, value);
		}else if(columnHeader.contains("h")){
			Convertor convertor = NumberSystemUtils.getHexConvertor();
			log.info(toValue.name()+" classname::::::::::::::: "+convertor.hexBin.toBase2(value));
			value=prefix+toValue.getPrefix()+toValue.getConvertedValue(convertor, value);
		}else if(columnHeader.contains("o")){
			Convertor convertor = NumberSystemUtils.getOctConvertor();
			value=prefix+toValue.getPrefix()+toValue.getConvertedValue(convertor, value);
		}else if(columnHeader.contains("b")){
			Convertor convertor = NumberSystemUtils.getBinConvertor();
			value=prefix+toValue.getPrefix()+toValue.getConvertedValue(convertor, value);
		}
		return value;
	}

}
