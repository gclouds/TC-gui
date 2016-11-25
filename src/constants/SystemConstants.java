/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

import java.io.File;

/**
 *
 * @author gauravsi
 */
public class SystemConstants {

    public static final String defaultMakeFilePath = File.separator + "sim" + File.separator + "Makefile";
    public static final String REGRESS_FILE_NAME = File.separator + "temp_regression.list";
    public static final String defaultMakeFileName = "Makefile";
    public static final String defaultRegerssSwitchName = "regress_l";
	public static TimeScale defaultTimeScale = TimeScale.MILI;
    public static ToValue toValue=ToValue.TO_HEX;
   
    //public static String defaultMakeFilePath = "";
    ////public static String defaultMakeFilePath = "";
    //public static String defaultMakeFilePath = "";

}
