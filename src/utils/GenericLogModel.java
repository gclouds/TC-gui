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

/**
 *
 * @author gauravsi
 */
public class GenericLogModel {
    Map<String, String> logMap=new HashMap<>();
    
    public Map<String, String> getLogMap() {
        return logMap;
    }

    public void setLogMap(Map<String, String> logMap) {
        this.logMap = logMap;
    }
    
    public SimpleStringProperty getValue(String key){
        System.out.println("get map value: '"+key+"': '"+getLogMap().get(key)+"'");
        return new SimpleStringProperty(getLogMap().get(key));
    }

}
