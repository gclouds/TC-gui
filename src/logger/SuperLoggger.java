package logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import constants.Matcher;
import constants.SystemConstants;
import numbersystem.Convertor;
import utils.NumberSystemUtils;
import utils.StringComparator;

public class SuperLoggger {
	public final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);

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
			Logger.info("checking line: "+lineArray);
			String currentString = lineArray[0].trim();
			if (StringUtils.containsIgnoreCase("row", currentString)) {
				listDataList = new ArrayList<>();

				Map<String, String> arrayList = toArrayList(lineArray);
				fullData.put("header", arrayList);
				Logger.info("end in row check");
				continue;
			}
			if (reset) {
				dataList = new DataList(generateDataMap(lineArray));
				listDataList.add(dataList);
				dataList.setCurrentString(currentString);
				reset = false;
				Logger.info("end in first reset");
				continue;
			}
			if (lineArray.length == 1) {
				reset = true;
				Logger.info("end in Arrays.toString(lineArray).contains)" + lineArray.length);
				continue;
			}
			if (!reset) {
				dataList = logic(dataList, lineArray, currentString);
				Logger.info("end in !reset");

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

	Map<String, String> toArrayList(String[] stringArray) {

		Map<String, String> output = new HashMap<>();
		// List<String> output = new ArrayList<>();
		for (int i = 0; i < stringArray.length; i++) {
			output.put(Integer.toString(i), stringArray[i].trim());
			// output.add(string);
		}
		return output;
	}

	HashMap<String, String> generateDataMap(String[] stringArray) throws Exception {
		HashMap<String, String> output = new HashMap<>();
		Map<String, String> header = (HashMap<String, String>) fullData.get("header");
		Logger.info("size of header: " + header.size());
		Logger.info("size of data row: " + stringArray.length);
		for (int i = 0; i < stringArray.length; i++) {
			String key = header.get(Integer.toString(i));
			Object keyUnit = getKeyUnit(key);
			String value = stringArray[i].trim();

			if (keyUnit instanceof String) {
				if (StringComparator.validString(value))
					output.put((String) keyUnit, value);
				else
					output.put((String) keyUnit, "-");
			} else if (keyUnit instanceof Map) {
				Map<String, String> keyUnitMap = (Map<String, String>) keyUnit;
				if (StringComparator.validString(value)) {
					output.put(keyUnitMap.get("name"),
							keyUnitMap.get("size") + getValue(keyUnitMap.get("unit"), value));
				} else {
					output.put(keyUnitMap.get("name"), "-");
				}

			} else {
				output.put(key.trim(), value);
			}
		}
		return output;
	}

	public static String getValue(String columnHeader, String value) throws Exception {
		if (columnHeader.equalsIgnoreCase("d")) {
			Convertor convertor = NumberSystemUtils.getDecConvertor();
			value = SystemConstants.toValue.getPrefix() + SystemConstants.toValue.getConvertedValue(convertor, value);
		} else if (columnHeader.equalsIgnoreCase("h")) {
			Convertor convertor = NumberSystemUtils.getHexConvertor();
			value = SystemConstants.toValue.getPrefix() + SystemConstants.toValue.getConvertedValue(convertor, value);
		} else if (columnHeader.equalsIgnoreCase("o")) {
			Convertor convertor = NumberSystemUtils.getOctConvertor();
			value = SystemConstants.toValue.getPrefix() + SystemConstants.toValue.getConvertedValue(convertor, value);
		} else if (columnHeader.equalsIgnoreCase("b")) {
			Convertor convertor = NumberSystemUtils.getBinConvertor();
			value = SystemConstants.toValue.getPrefix() + SystemConstants.toValue.getConvertedValue(convertor, value);
		}
		return value;
	}

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void info(Object msg) {
		logger.info(msg);
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void error(Object msg) {
		logger.error(msg);
	}

	private static Object getKeyUnit(String key) {
		if (key.contains("{") && key.contains("}")) {
			String str = key.substring(key.indexOf("{") + 1, key.indexOf("}"));
			if (str.contains("'")) {
				Map<String, String> map = new HashMap();
				map.put("size", str.split("'")[0]);
				map.put("unit", str.split("'")[1]);
				map.put("name", key.substring(0, key.indexOf("{")));
				return map;
			}
			Logger.info("condition: " + str.contains("'"));
			Logger.info("unit: >> " + str);
		}
		return key.trim();
	}
}
