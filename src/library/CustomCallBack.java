package library;

import java.util.Map;

import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import utils.GenericLogModel;

public class CustomCallBack implements Callback<TreeTableView<Map<String, Object>>, TreeTableRow<Map<String, Object>>> {

	@Override
	public TreeTableRow<Map<String, Object>> call(TreeTableView<Map<String, Object>> param) {
		return new LogTreeTableRow();
	}

}
