/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import javafx.scene.control.TreeItem;

/**
 *
 * @author gauravsi
 */
public class LogModel {
	public final static Logger log = Logger.getLogger(LogModel.class);

	List<String> lines;
	Map<String, Object> logMap;
	Map<String, String> header;
	HashMap<String, String> rootLogMap;
	GenericLogModel rootModel;
	List<GenericLogModel> listModels;
	TreeItem<LogModel> root;

	public LogModel(File file) {
		this.lines = FileReader.readLinesForLogger(file);
		log.info("utils.LogModel.<init>() size of lines: " + lines.size());
		root = new TreeItem<LogModel>();
		setLogMap();
	}

	public List<GenericLogModel> getListModels() {
		return listModels;
	}

	private void setListModels(List<GenericLogModel> listModels) {
		this.listModels = listModels;
	}

	public GenericLogModel getRootModel() {
		rootModel = new GenericLogModel();
		rootModel.setLogMap(rootLogMap);
		return rootModel;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public TreeItem<LogModel> getRoot() {
		return root;
	}

	public LogModel() {
	}

	public Map getLogMap() {
		return logMap;
	}

	public void setLogMap() {
		Iterator<String> iterator = lines.iterator();
		List<List<List<String>>> listout = new ArrayList<>();
		List<List<String>> listFirstNode = null;
		while (iterator.hasNext()) {
			String next = iterator.next();
			next = next.substring(1, next.lastIndexOf("|"));
			if (next.startsWith("Row")) {
				header = new HashMap<String, String>();
				rootLogMap = new HashMap();
				next = next.substring(next.indexOf("|") + 1);
				log.info("lock hogaya: " + next);

				List<String> list = new ArrayList<>();
				String[] split = next.split("\\|");
				for (int i = 0; i < split.length; i++) {
					list.add(split[i]);
				}

				for (int i = 0; i < list.size(); i++) {
					header.put(i + "", list.get(i).trim());
					rootLogMap.put(list.get(i).trim(), "-");
				}
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+header);
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+rootLogMap);
			} else {
				// Logger.info("next: " + next);
				try {
					List<String> lineArray = new ArrayList<>();
					String[] split = next.split("\\|");
					for (int i = 0; i < split.length; i++) {
						lineArray.add(split[i]);
					}
					if (!next.contains("|")) {
						if (listFirstNode != null) {
							listout.add(listFirstNode);
							listFirstNode = new ArrayList<>();
						} else {
							listFirstNode = new ArrayList<>();
						}
					} else {
						listFirstNode.add(lineArray);
					}
				} catch (Exception e) {
					log.info("next-error: " + next);
					log.info("listout: " + listout.size());
					// e.printStackTrace();
				}
			}
		}
		log.info("listout: " + listout.size());
		buildLogModel(listout);
	}

	/**
	 * @param listout
	 */
	private void buildLogModel(List<List<List<String>>> listout) {/*
		GenericLogModel secondChildModel = new GenericLogModel();

		List<GenericLogModel> listModels = new ArrayList<>();
		for (int i = 0; i < listout.size(); i++) {

			List<List<String>> rowLog = listout.get(i);
			Logger.info("rowLog: " + rowLog);
			Logger.info("header: " + header);

			GenericLogModel model = new GenericLogModel();
			Map<String, String> rootMap = model.getLogMap();

			for (int j = 0; j < rowLog.size(); j++) {
				List<String> collumn = rowLog.get(j);
				String firstCol = collumn.get(0).trim();
				collumn.remove(0);
				Logger.info("firstCol:" + firstCol);
				int firstId = 1;
				// collumn = collumn.subList(1, collumn.size() - 1);
				if (firstCol.equalsIgnoreCase("1")) {
					for (int k = 0; k < collumn.size(); k++) {
						rootMap.put(header.get("" + k), collumn.get(k).trim());
						// Logger.info("key: " + header.get("" + k) + ",
						// value: " + collumn.get(k).trim());
					}
				} else if (firstCol.equalsIgnoreCase("0")) {
					for (int k = 0; k < collumn.size(); k++) {
						rootMap.put(header.get("" + k), collumn.get(k).trim());
						// Logger.info("key: " + header.get("" + k) + ",
						// value: " + collumn.get(k).trim());
					}
					model.setHasChild(false);
				} else if (firstCol.contains("1.1")) {
					model.setHasChild(true);
					GenericLogModel childModel = new GenericLogModel();
					Map<String, String> childMap = childModel.getLogMap();
					for (int k = 0; k < collumn.size(); k++) {
						childMap.put(header.get("" + k), collumn.get(k).trim());
						// Logger.info("key: " + header.get("" + k) + ",
						// value: " + collumn.get(k).trim());
						firstId++;
					}
					model.addChild(childModel);
				} else if (firstCol.contains("1.2")) {
					model.setHasChild(true);
					if (firstCol.equalsIgnoreCase("1.2")) {
						Map<String, String> childMap = secondChildModel.getLogMap();
						for (int k = 0; k < collumn.size(); k++) {
							childMap.put(header.get("" + k), collumn.get(k).trim());
							// Logger.info("key: " + header.get("" + k) +
							// ", value: " + collumn.get(k).trim());
							firstId++;
						}
						model.addChild(secondChildModel);
					} else if (firstCol.contains("1.2.")) {
						Logger.info(">>>>>>>>>>>>> 1.2.  " + firstCol);
						GenericLogModel childChildModel = new GenericLogModel();
						Map<String, String> childMap = childChildModel.getLogMap();
						for (int k = 0; k < collumn.size(); k++) {
							childMap.put(header.get("" + k), collumn.get(k).trim());
							// Logger.info("key: " + header.get("" + k) +
							// ", value: " + collumn.get(k).trim());
							firstId++;
						}
						secondChildModel.addChild(childChildModel);
						secondChildModel.setHasChild(true);
					}
				}
			}
			secondChildModel = new GenericLogModel();
			listModels.add(model);
		}
		setListModels(listModels);
	*/}

	private void buildLogModel1(List<List<List<String>>> listout) {/*
		GenericLogModel secondChildModel = new GenericLogModel();

		List<GenericLogModel> listModels = new ArrayList<>();
		for (int i = 0; i < listout.size(); i++) {

			List<List<String>> rowLog = listout.get(i);
			Logger.info("rowLog: " + rowLog);
			Logger.info("header: " + header);

			GenericLogModel model = new GenericLogModel();
			Map<String, String> rootMap = model.getLogMap();

			for (int j = 0; j < rowLog.size(); j++) {
				List<String> collumn = rowLog.get(j);
				String firstCol = collumn.get(0).trim();
				collumn.remove(0);
				Logger.info("firstCol:" + firstCol);
				int firstId = 1;
				// collumn = collumn.subList(1, collumn.size() - 1);
				if (firstCol.equalsIgnoreCase("1")) {
					for (int k = 0; k < collumn.size(); k++) {
						rootMap.put(header.get("" + k), collumn.get(k).trim());
						// Logger.info("key: " + header.get("" + k) + ",
						// value: " + collumn.get(k).trim());
					}
				} else if (firstCol.equalsIgnoreCase("0")) {
					for (int k = 0; k < collumn.size(); k++) {
						rootMap.put(header.get("" + k), collumn.get(k).trim());
						// Logger.info("key: " + header.get("" + k) + ",
						// value: " + collumn.get(k).trim());
					}
					model.setHasChild(false);
				} else if (firstCol.contains("1.1")) {
					model.setHasChild(true);
					GenericLogModel childModel = new GenericLogModel();
					Map<String, String> childMap = childModel.getLogMap();
					for (int k = 0; k < collumn.size(); k++) {
						childMap.put(header.get("" + k), collumn.get(k).trim());
						// Logger.info("key: " + header.get("" + k) + ",
						// value: " + collumn.get(k).trim());
						firstId++;
					}
					model.addChild(childModel);
				} else if (firstCol.contains("1.2")) {
					model.setHasChild(true);
					if (firstCol.equalsIgnoreCase("1.2")) {
						Map<String, String> childMap = secondChildModel.getLogMap();
						for (int k = 0; k < collumn.size(); k++) {
							childMap.put(header.get("" + k), collumn.get(k).trim());
							// Logger.info("key: " + header.get("" + k) +
							// ", value: " + collumn.get(k).trim());
							firstId++;
						}
						model.addChild(secondChildModel);
					} else if (firstCol.contains("1.2.")) {
						Logger.info(">>>>>>>>>>>>> 1.2.  " + firstCol);
						GenericLogModel childChildModel = new GenericLogModel();
						Map<String, String> childMap = childChildModel.getLogMap();
						for (int k = 0; k < collumn.size(); k++) {
							childMap.put(header.get("" + k), collumn.get(k).trim());
							// Logger.info("key: " + header.get("" + k) +
							// ", value: " + collumn.get(k).trim());
							firstId++;
						}
						secondChildModel.addChild(childChildModel);
						secondChildModel.setHasChild(true);
					}
				}
			}
			secondChildModel = new GenericLogModel();
			listModels.add(model);
		}
		setListModels(listModels);
	*/}

}
