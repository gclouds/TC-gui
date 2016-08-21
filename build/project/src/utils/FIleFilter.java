/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.FileNameMap;

/**
 *
 * @author gauravsi
 */
public class FIleFilter implements FilenameFilter {

    private String fileExtension;

    public FIleFilter(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public boolean accept(File dir, String name) {
        return (name.endsWith(this.fileExtension));

    }
}
