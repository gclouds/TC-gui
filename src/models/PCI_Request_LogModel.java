/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;

import javafxapplication2.pciLogUtil.ValuesConstants;
import logger.Logger;

/**
 *
 * @author gauravsi
 */
public class PCI_Request_LogModel {

    public int size;
    public static ValuesConstants typeOFValue;
    public List<String[]> requestLine;
    public List<String[]> completionLine;
    public PCI_Request_LogModel(List<String[]> request,List<String[]> completion) {
        requestLine = new ArrayList();
        completionLine = new ArrayList();
        Logger.info("size of request in arugment: "+request.size());
        for (int i = 0; i < request.size(); i++) {
            Logger.info(i+"-"+request.size()+"-size of line: "+request.get(i).length+"+"+completion.get(i).length);
            if ((request.get(i).length+ completion.get(i).length) > 10) {
               Logger.info(i+"---"+request.size()+"-size of line: "+request.get(i).length+"+"+completion.get(i).length);
               completionLine.add(completion.get(i));
               requestLine.add(request.get(i));
            }
        }
        size=requestLine.size();
        Logger.info("models.PCI_Request_LogModel.<init>()"+size);
    }

    public void setTypeOFValue(ValuesConstants typeOFValue) {
        this.typeOFValue = typeOFValue;
    }

    public String[] getRequestLine(int index) {
        Logger.info("PCI_Request_LogModel>>>getRequestLine"+index);
        return requestLine.get(index);
    }
    public String[] getCompletionLine(int index) {
        Logger.info("PCI_Request_LogModel>>>getCompletionLine"+index);
        return completionLine.get(index);
    }

    public String valueAtIndex(String[] getLine,int index){
        Logger.info(getLine.length+">>valueAtIndex>>"+index+typeOFValue.name());
        if(typeOFValue.name().equalsIgnoreCase("BINARY")){
            return Integer.toBinaryString(new Integer(getLine[index]));
        }else if(typeOFValue.name().equalsIgnoreCase("HEX")){
            return Integer.toHexString(new Integer(getLine[index]));
        }else if(typeOFValue.name().equalsIgnoreCase("DECIMAL")){
            return Integer.toString(new Integer(getLine[index]));
        }else{
            return Integer.toString(new Integer(getLine[index]));
        }
    }
    
}
