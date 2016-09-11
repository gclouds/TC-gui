package utils;

import java.awt.Font;

import controller.MainPageController;

public class MainWindowsUtils {
    public static MainPageController mainController;

    
    
    public static void setLeftHandErrorMsg(String message){
    	mainController.leftStatusLabel.setText(message);
    }
    
    public static void setRightHandErrorMsg(String message){
    	
    	mainController.rightStatusLabel.setText(message);
    }

	public static MainPageController getMainController() {
		return mainController;
	}

	public static void setMainController(MainPageController mainController) {
		MainWindowsUtils.mainController = mainController;
	}
}
