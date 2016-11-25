package library;

import javafx.scene.control.TreeTableRow;
import utils.GenericLogModel;

public class LogTreeTableRow extends TreeTableRow<GenericLogModel> {

	@Override
	protected void updateItem(GenericLogModel item, boolean empty) {
		
		if (empty) {
        } else {
        	setContextMenu(((LogTreeItem)getTreeItem()).getContextMenu());
        }
		super.updateItem(item, empty);
	}
}
