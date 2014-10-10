/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InsightJournal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Xandron
 */
public class PropertiesHandler {
    static String scripture;
    static String terms;
    static String journal;
    public void getPropValues() throws IOException {
        Properties prop = new Properties();
        //String propFileName = "C:\\Users\\Xandron\\Documents\\NetBeansProjects\\CS246_milestone3\\src\\cs246_milestone3\\config.properties";
        String propFileName = "/InsightJournal/config.properties";
        //String propFileName = "x.config";
        try {
        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        prop.load(getClass().getResourceAsStream(propFileName));
//        if (inputStream == null) {
//            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // get the property value and print it out
        scripture = prop.getProperty("scripture");
        terms = prop.getProperty("terms");
        journal = prop.getProperty("journal");
    }
}
