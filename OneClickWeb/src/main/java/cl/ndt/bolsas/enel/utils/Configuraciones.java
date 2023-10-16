/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.ndt.bolsas.enel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 * @author Lenovo
 */
public class Configuraciones {

    public final String CONFIG_SERVICE = "BolsasEnel.properties";
    String OS = System.getProperty("os.name").toLowerCase();
    File confDir = null;

    public String getProperties(String key, String propertiesFile) {

        Properties prop = new Properties();
        String keyValue = null;

        if (OS.indexOf("win") >= 0) {
            confDir = new File("E:\\Users\\Andex\\ndt\\wildfly-17.0.1.Final - QA\\standalone\\configuration");
        } else {
            confDir = new File(System.getProperty("jboss.server.config.dir"));
        }

        File fileProp = new File(confDir, propertiesFile);
        FileInputStream inputStream = null;
        try {

            inputStream = new FileInputStream(fileProp);
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

            // load a properties file
            prop.load(reader);

            // get the property value and print it out
            keyValue = prop.getProperty(key, "Not Found");

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return keyValue;
    }
}
