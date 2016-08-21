/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gauravsi
 */
    @XmlRootElement(name = "configurations")
public class ConfigWrapper {
        private Map<String, String> configs;

    public void setConfigs(Map<String, String> configs) {
        this.configs = configs;
    }

    public Map<String, String> getConfigs() {
        return configs;
    }

}
