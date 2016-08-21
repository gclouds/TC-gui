/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author gauravsi
 */
public class TestDataModel {
    
        private final SimpleStringProperty arguName;
        private final SimpleStringProperty arguValue;
 
        public TestDataModel(String arguName, String arguValue) {
            this.arguName = new SimpleStringProperty(arguName);
            this.arguValue = new SimpleStringProperty(arguValue);
        }

    public SimpleStringProperty getArguName() {
        return arguName;
    }

    public SimpleStringProperty getArguValue() {
        return arguValue;
    }

    public void setArguName(String arguName) {
        this.arguName.set(arguName);
    }

    public void setArguValue(String arguValue) {
        this.arguValue.set(arguValue);
    }
     
}
