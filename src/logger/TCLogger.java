/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import utils.FileReader;

/**
 *
 * @author gauravsi
 */
public class TCLogger extends SuperLoggger {
	public final static Logger logger = Logger.getLogger(TCLogger.class);

	private HashMap<String, String> headerMap;
	private List<DataList> leftSideTable;
	
    public TCLogger(List<String[]> list) throws Exception {
		super(list);
		
		Map<String, Object> generateTableData = super.generateTableData();
		
		headerMap = (HashMap<String, String>) generateTableData.get("header");
		leftSideTable = (List<DataList>) generateTableData.get("data");
		
	}
    

	


	public HashMap<String, String> getHeaderMap() {
		return headerMap;
	}


	public void setHeaderMap(HashMap<String, String> headerMap) {
		this.headerMap = headerMap;
	}


	public List<DataList> getLeftSideTableData() {
		return leftSideTable;
	}


	public void setLeftSideTableData(List<DataList> leftSideTable) {
		this.leftSideTable = leftSideTable;
	}
	
	
	public static List<String[]> extractData(File logFile){
		List<String[]> output= new ArrayList<>();
		List<String> readLinesForLogger = FileReader.readLinesForLogger(logFile);
		
		Iterator<String> iterator = readLinesForLogger.iterator();
		
		while(iterator.hasNext()){
			String line = iterator.next();
			if(line.startsWith("|") && line.endsWith("|")){
				String correctData = line.substring(line.indexOf("|")+1, line.lastIndexOf("|"));
				String[] split = correctData.split("\\|");
				output.add(split);
				//print(split);
			}
		}
		return output;
	}
	
	private static void print(String[] toPrint){
		
		for(String str:toPrint){
			logger.info(str);
		}
		
	}
}
