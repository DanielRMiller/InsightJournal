package InsightJournal;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Entry{
    private String date;
    private List<Scripture> scriptureList = new ArrayList<>();
    private List<String> topicList = new ArrayList<>();
    private String content;
    
    // CONSTRUCTOR
    Entry(){}
    Entry(String newDate, String newContent){
        date = newDate;
        content = newContent;
    }
    
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
    
    void parseForScriptures(String newContent) {
        // This is the Regular Expression that we worked on a lot.
        String exp = "((\\d\\s)?+[a-zA-Z]+\\s\\d{1,3}:\\d{1,3})|([a-zA-Z]+)\\schapter\\s(\\d{1,3})";
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(newContent);
        // When we find a match, matcher.find() will be true
        while (matcher.find()) {
            // If this is in the 1st format we leave it as is.
            if (matcher.group(1) != null) {
                System.out.println(matcher.group(1));
            } // close if
            // If this is in the 2nd format (we use 2-4 because of the "# " for
            //   2 Nephi is group 2) then we output without the chapter.
            if (matcher.group(3) != null) {
                System.out.println(matcher.group(3) + " " + matcher.group(4));
            } } } // close if, while, while
    
    void parseForTopics(String newContent) {
    }
}