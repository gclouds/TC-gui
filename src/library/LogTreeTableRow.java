package library;

import java.util.Map;

import javafx.scene.control.TreeTableRow;

public class LogTreeTableRow extends TreeTableRow<Map<String, Object>> {

	@Override
	protected void updateItem(Map<String, Object> item, boolean empty) {
		
		if (empty) {
        } else {
        	setContextMenu(((LogTreeItem)getTreeItem()).getContextMenu());
        }
		super.updateItem(item, empty);
	}
}
