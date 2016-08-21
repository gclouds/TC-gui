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
public class DetailedLogs {
    private SimpleStringProperty requestCol= new SimpleStringProperty();
    private SimpleStringProperty completionCol= new SimpleStringProperty();

    public DetailedLogs(String requestCol, String completionCol) {
        this.requestCol.set(requestCol);
        this.completionCol.set(completionCol);
    }

    public SimpleStringProperty getRequestCol() {
        return requestCol;
    }

    public void setRequestCol(SimpleStringProperty requestCol) {
        this.requestCol = requestCol;
    }

    public SimpleStringProperty getCompletionCol() {
        return completionCol;
    }

    public void setCompletionCol(SimpleStringProperty completionCol) {
        this.completionCol = completionCol;
    }

    

}
