package library;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import utils.GenericLogModel;

public abstract class AbstractLogTreeItem extends TreeItem<GenericLogModel> {
	
	public AbstractLogTreeItem(GenericLogModel value) {
		super(value);
		
		// TODO Auto-generated constructor stub
	}

	public abstract ContextMenu getMenu(); 
	
}
