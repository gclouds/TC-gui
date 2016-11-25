/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.pciLogUtil;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import logger.Logger;
import models.PCI_Request_LogModel;

public class PCI_Model {

    private SimpleStringProperty requestTl;
    private SimpleStringProperty requestDir;
    private SimpleStringProperty requestTime;
    private SimpleStringProperty completiontl;
    private SimpleStringProperty completionDir;
    private SimpleStringProperty completionTime;
    
    public String[] requestLine;
    public String[] completionLine;

    public PCI_Model(PCI_Request_LogModel logModel,int index) {
        Logger.info("PCI_Model:constructor()"+index);
        requestLine=logModel.getRequestLine(index);
        completionLine=logModel.getCompletionLine(index);
        requestTl = new SimpleStringProperty(catchNull(logModel.getRequestLine(index),PCI_Constants_Req.TypeOfTransaction.getIndex()));
        requestDir = new SimpleStringProperty(catchNull(logModel.getRequestLine(index),PCI_Constants_Req.Direction.getIndex()));
        requestTime = new SimpleStringProperty(catchNull(logModel.getRequestLine(index),PCI_Constants_Req.Time.getIndex()));
        completiontl = new SimpleStringProperty(catchNull(logModel.getCompletionLine(index),PCI_Constants_Com.TypeOfTransaction.getIndex()));
        completionDir = new SimpleStringProperty(catchNull(logModel.getCompletionLine(index),PCI_Constants_Com.Direction.getIndex()));
        completionTime = new SimpleStringProperty(catchNull(logModel.getCompletionLine(index),PCI_Constants_Com.Time.getIndex()));
    }

    public String getRequestTl() {
        return requestTl.get();
    }

    public void setRequestTl(String requestTl) {
        this.requestTl.set(requestTl);
    }

    public String getRequestDir() {
        return requestDir.get();
    }

    public void setRequestDir(String requestDir) {
        this.requestDir.set(requestDir);
    }

    public String getRequestTime() {
        return requestTime.get();
    }

    public void setRequestTime(String requestTime) {
        this.requestTime.set(requestTime);
    }

    public String getCompletiontl() {
        return completiontl.get();
    }

    public void setCompletiontl(String completiontl) {
        this.completiontl.set(completiontl);
    }

    public String getCompletionDir() {
        return completionDir.get();
    }

    public void setCompletionDir(String completionDir) {
        this.completionDir.set(completionDir);
    }

    public String getCompletionTime() {
        return completionTime.get();
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime.set(completionTime);
    }
    public static String catchNull(String[] array,int indexOfValue){
        try{
            return array[indexOfValue];
        }catch(Exception e){
            Logger.info("Error PCI_Model:catchNull() at index: "+indexOfValue);
            return "-";
        }
    }
}
