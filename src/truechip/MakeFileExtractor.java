/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package truechip;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gauravsi
 */
public class MakeFileExtractor {

    List<String> readMakeFileArg;
    public MakeFileExtractor(List<String> readMakeFileArg) {
        this.readMakeFileArg=readMakeFileArg;
    }
    
    public String[] getTargets(){
        return readMakeFileArg.get(0).substring(1, readMakeFileArg.get(0).length()-1).split(" ");
    }
    
    public List<String> getSwitches(){
        List<String> output=new ArrayList<>();
        for(int i=1;i<readMakeFileArg.size();i++){
            String get = readMakeFileArg.get(i);
            if(get.contains("?=")){
                output.add(get.split("?=")[0].trim());
            }
        }
        return output;
    }
}
