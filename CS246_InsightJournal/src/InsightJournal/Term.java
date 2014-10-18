/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InsightJournal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xandron
 */
public class Term {
    String key;
    List<String> synList = new ArrayList<>();
    
    public String getKey() {
        return key;
    }
    public List<String> getSynList() {
        return synList;
    }
    
    public void setKey(String newKey) {
        key = newKey;
    }
    public void setSynList(List<String> newSynList) {
        synList = newSynList;
    }
    
    public boolean matches(String content) {
        // System.out.println("Entered matches");
        for (String syn : synList) {
            // System.out.println("Trying to match " + syn + " to \"" + content + "\"");
            if (content.toLowerCase().contains(syn.toLowerCase())) {
                // System.out.println("Found a match in matches!");
                return true;
            }
        }
        return false;
    }
    
    public String display(){
        return key + " " + synList;
    }
}
