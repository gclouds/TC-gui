/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logger.Logger;

/**
 *
 * @author gauravsi
 */
public class GenericLogModel {
	HashMap<String, String> logMap=new HashMap<>();
    
    public HashMap<String, String> getLogMap() {
        return logMap;
    }

    public void setLogMap(HashMap<String, String> logMap) {
        this.logMap = logMap;
    }
    
    public StringProperty getValue(String key){
        Logger.info("get map value: '"+key+"': '"+getLogMap().get(key)+"'");
        return new SimpleStringProperty(getLogMap().get(key));
    }

}
