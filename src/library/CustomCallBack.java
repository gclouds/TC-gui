package library;

import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import utils.GenericLogModel;

public class CustomCallBack implements Callback<TreeTableView<GenericLogModel>, TreeTableRow<GenericLogModel>> {

	@Override
	public TreeTableRow<GenericLogModel> call(TreeTableView<GenericLogModel> param) {
		return new LogTreeTableRow();
	}

}
