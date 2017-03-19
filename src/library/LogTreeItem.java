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
import logger.TCLogger;
import logger.SuperLoggger;
import numbersystem.Convertor;
import truechip.TransactionLogger;
import utils.GenericLogModel;
import utils.NumberSystemUtils;

public class LogTreeItem extends TreeItem<Map<String, Object>>{
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogTreeItem.class);

	ContextMenu contextMenu=new ContextMenu();
	TransactionLogger root;
	
	MenuItem toHexMenu = new MenuItem("to HEX");
	MenuItem toDecMenu = new MenuItem("to DEC");
	MenuItem toOctMenu = new MenuItem("to OCT");
	MenuItem toBinMenu = new MenuItem("to BIN");


	public LogTreeItem(Map<String, Object> value,TransactionLogger transactionLogger) {
		super(value);
		contextMenu.getItems().addAll(toBinMenu,toDecMenu,toHexMenu,toOctMenu);
		this.root=transactionLogger;
		
	}
	
	public LogTreeItem(){
		super(null);
	}


	@SuppressWarnings("restriction")
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

	public void getConvertedLogModel(Map<String, Object> logModel,ToValue toValue){
		for(Entry<String, Object> keyset:logModel.entrySet()){
			String columnHeader = keyset.getKey();
			String cellvalue = (String) keyset.getValue();
			log.info("converting value for:: "+keyset.getKey()+":"+cellvalue);
			String[] splitedCellValue = cellvalue.split("'");
			try {
				cellvalue=getConvertedValue(splitedCellValue[1].charAt(0)+"",splitedCellValue[0], splitedCellValue[1].substring(1),toValue);
				logModel.put(columnHeader, cellvalue);
				log.info("converted value for:: "+keyset.getKey()+":"+cellvalue);
			} catch (Exception e) {
				log.error("error:: "+cellvalue+", "+toValue,e);
			}
		}
		Map<String, Object> clonedMap = (Map<String, Object>) ((HashMap<String, Object>) logModel).clone();
		setValue(null);
		setValue(clonedMap);
		root.showSecondaryTable(logModel);
	}
	
	public static String getConvertedValue(String columnHeader,String prefix, String value,ToValue toValue) throws Exception {
		value=value.toUpperCase();
		log.info("==>>getConvertedValue");
		log.info("header:: "+columnHeader+",prefix:: "+prefix+", value:: "+value+", toValue:: "+toValue);

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
		log.info("<<==getConvertedValue, returned:: "+value);
		return value;
	}

}
