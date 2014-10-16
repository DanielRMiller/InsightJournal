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
        System.out.println("entered parseForScriptures");
        // This is the Regular Expression that we worked on a lot.
        String exp = "((\\d\\s)?+[a-zA-Z]+\\s\\d{1,3}:\\d{1,3})|([a-zA-Z]+)\\schapter\\s(\\d{1,3})";
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(newContent);
        // When we find a match, matcher.find() will be true
        while (matcher.find()) {
            String scripString = "";
            // If this is in the 1st format we leave it as is.
            if (matcher.group(1) != null) {
                scripString = matcher.group(1);
            } // close if
            // If this is in the 2nd format (we use 2-4 because of the "# " for
            //   2 Nephi is group 2) then we output without the chapter.
            if (matcher.group(3) != null) {
                scripString = (matcher.group(3) + " " + matcher.group(4));
            } // close if
            if (scripString != null) {
                System.out.println("scripString is: " + scripString);
                System.out.println("scriptureList is: " + scriptureList);
                parseScripture(scripString);
                System.out.println("scriptureList is: " + scriptureList);
            } // close if
        } // close while
    } // close parse
    
    void parseForTopics(String newContent) {
    }

    private void parseScripture(String scripString) {
        System.out.println("entered parseScripture");
        Scripture newScripture = new Scripture();
        // This is the Regular Expression that we worked on a lot.
        String exp = "(((\\d\\s)?+[a-zA-Z]+)\\s(\\d{1,3}):(\\d{1,3}))";
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(scripString);
        // When we find a match, matcher.find() will be true
        if (matcher.find()) {
            // parse the book out to a new scripture
            newScripture.setBook(matcher.group(2));
            System.out.println("newScripture book is: " + newScripture.getBook());
            newScripture.setChapter(Integer.parseInt(matcher.group(4)));
            System.out.println("newScripture chapter is: " + newScripture.getChapter());
            newScripture.setStartVerse(Integer.parseInt(matcher.group(5)));
            System.out.println("newScripture StartVerse is: " + newScripture.getStartVerse());
            newScripture.setEndVerse(0);
            scriptureList.add(newScripture);
            for (Scripture tempScrip : scriptureList) {
                System.out.println(tempScrip.display());
            }
        } // close if
    }
}