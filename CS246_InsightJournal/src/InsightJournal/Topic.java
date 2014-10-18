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
public class Topic {
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
        for (String syn : synList) {
            if (content.contains(syn)) {
                return true;
            }
        }
        return false;
    }
}
