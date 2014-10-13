package InsightJournal;

import java.util.*;

class Entry{
    private String date;
    private List<Scripture> scriptureList = new ArrayList<>();
    private List<String> topicList = new ArrayList<>();
    private String content;
    
    // CONSTRUCTOR
    Entry(){}
    Entry(String date, String content){}
    
    // GETTERS
    public String getContent(){return content;}
    public List<Scripture> getScriptureList(){return scriptureList;}
    public String getDate(){return date;}
    public List<String> getTopicList(){return topicList;}
    
    // SETTERS
    public void setContent(String newContent){content = newContent;}
    public void setScriptureList(List<Scripture> newScriptureList){scriptureList = newScriptureList;}
    public void setDate(String newDate){date = newDate;}
    public void setTopicList(List<String> newTopicList){topicList = newTopicList;}
    
    public String print(int num){return "";}
    private void findScripture(){}
    private void findTopics(){}
    private void extractScripture(){}
    private void extractTopic(){}
    private void scriptureFormat(int num){}
    public void addTopic(String newTopic){topicList.add(newTopic);}
    public void addScripture(Scripture newScripture){scriptureList.add(newScripture);}
    public String display(){
        String rString = "Entry Display:\n" + "Date : " + date + "\n";
        for (Scripture scripture : scriptureList) {
            rString = rString + scripture.display();
        }
        for (String topic : topicList) {
            rString = rString + topic + " ";
        }
        rString = rString + "\n" + content + "\n";
        return rString;
    }
    
    public boolean hasbook(String book) {
        for(Scripture tempScripture : scriptureList){
            if (tempScripture.getBook().equals(book)){
                return true;
            }
        }
        return false;
    }

    boolean hasTerm(String term) {
        for(String tempTopic : topicList){
            if (tempTopic.equals(term)){
                return true;
            }
        }
        return false;
    }
}