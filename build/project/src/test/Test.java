/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.util.List;
import java.util.Map;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.ReadLogger;

/**
 *
 * @author gauravsi
 */
public class Test {

    public static void main(String[] args) {
        try {
            System.out.println("test.Test.main()");
            ReadLogger logger = new ReadLogger("C:\\Users\\gauravsi\\Downloads\\Creating GUI for TC VIPs\\Logfiles for Transaction Logger\\tc_pcie_tl_transaction_0.log.txt");
            System.out.println("test.Test.main()");
            Map<String, List> map = logger.getMap();
            System.out.println("test.Test.main()");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
