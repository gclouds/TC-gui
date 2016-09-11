package logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import constants.Matcher;
import utils.StringComparator;

public class SuperLoggger {

	List<String[]> list;

	Map<String, Object> fullData;

	public SuperLoggger(List<String[]> list) {
		this.list = list;
		fullData = new HashMap<String, Object>();
	}

	public Map<String, Object> generateTableData() throws Exception {
		List<DataList> listDataList = new ArrayList<>();
		boolean reset = false;
		DataList dataList = null;
		for (String[] lineArray : list) {
			System.out.print("checking line: ");
			System.out.println(Arrays.toString( lineArray));
			String currentString = lineArray[0].trim();
			if (StringUtils.containsIgnoreCase("row", currentString)) {
				listDataList = new ArrayList<>();

				Map<String, String> arrayList = toArrayList(lineArray);
				fullData.put("header", arrayList);
				System.out.println("end in row check");
				continue;
			}
			if (reset) {
				dataList = new DataList(generateDataMap(lineArray));
				listDataList.add(dataList);
				dataList.setCurrentString(currentString);
				reset = false;
				System.out.println("end in first reset");
				continue;
			}
			if(lineArray.length==1){
				reset=true;
				System.out.println("end in Arrays.toString(lineArray).contains)"+lineArray.length);
				continue;
			}
			if (!reset) {
				dataList=logic(dataList, lineArray, currentString);
				System.out.println("end in !reset");

			}

		}
		fullData.put("data", listDataList);
		return fullData;
	}


	private DataList logic(DataList mainDataList, String[] currentData, String currentString) throws Exception {
		Matcher findMatch = StringComparator.findMatch(mainDataList.getCurrentString(), currentString);
		switch (findMatch) {
		case EXACT_MATCH:
			return mainDataList;
		case NO_MATCH:
			if (mainDataList.hasParent()) {
				return logic(mainDataList.getParent(), currentData, currentString);
			}
			return mainDataList;
		case PREFIX_MATCH:
			DataList currentDataList = new DataList(generateDataMap(currentData));
			mainDataList.getChild().add(currentDataList);
			currentDataList.setParent(mainDataList);
			return currentDataList;
		}
		
		return mainDataList;
	}

	Map<String,String> toArrayList(String[] stringArray) {
		
		Map<String,String> output = new HashMap<>();
		//List<String> output = new ArrayList<>();
		for (int i=0;i<stringArray.length;i++) {
			output.put(Integer.toString(i), stringArray[i].trim());
			//output.add(string);
		}
		return output;
	}

	Map<String,String> generateDataMap(String[] stringArray) throws Exception{
		Map<String,String> output = new HashMap<>();
		Map<String,String> header =  (Map<String, String>) fullData.get("header");
		System.out.println("size of header: "+header.size());
		System.out.println("size of data row: "+stringArray.length);
		for(int i=0;i<stringArray.length;i++){
			output.put(header.get(Integer.toString(i)), stringArray[i].trim());
		}
		return output;
	}

}
