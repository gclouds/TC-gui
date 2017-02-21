package logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataList {

	public DataList(Map<String, Object> data) {
		this.data = data;
	}

	private Map<String, Object> data;
	private String currentString;
	private List<DataList> child = new ArrayList<>();
	private DataList parent = null;

	public boolean hasParent() {
		if (parent == null)
			return false;
		else
			return true;
	}

	public boolean hasChild() {
		return !(child.isEmpty());
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public List<DataList> getChild() {
		return child;
	}

	public void addChild(DataList child) {
		this.child.add(child);
	}

	public void setChild(List<DataList> child) {
		this.child = child;
	}

	public DataList getParent() {
		return parent;
	}

	public void setParent(DataList parent) {
		this.parent = parent;
	}

	public String getCurrentString() {
		return (String) data.get("Row");
	}

	public void setCurrentString(String currentString) {
		this.currentString = currentString;
	}
}