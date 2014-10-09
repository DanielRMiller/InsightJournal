package InsightJournal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Xandron
 */
public class Journal{

   private Map<String, Entry> entries = new TreeMap<>();
   private String inputFileName;
   private String outputFileName;
   private final GUI gui = new GUI();
   private List<String> booksToFind = new ArrayList<>();
   private Map<String, List<String>> termsToFind = new HashMap<>();
   
    public static void main(String[] args){
        if (args.length == 1) {
            Journal j = new Journal(args[0]);
        }
        else
            System.out.println("Cannot find filename");
    }

   Journal(String fileName)
   {
      readInputFile(fileName);
      writeOutputFile();
      //display();
      PropertiesHandler prop = new PropertiesHandler();
       try {
           prop.getPropValues();
           readScriptureFile(PropertiesHandler.scripture);
           readTermsFile(PropertiesHandler.terms);
       } catch (IOException ex) {
           Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
    private void readInputFile(String filename) {
        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
           
            System.out.println("Loading file \"" + filename + "\"\n");
           
            Element rootElement = doc.getDocumentElement();
            entries = parseJournal(rootElement);
            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Sorry, but your file was not able to be parsed.");
        }
    }
    private Map<String, Entry> parseJournal(Element rootElement){          
        Map<String, Entry> entryMap = new HashMap<>();
        NodeList journalNodes = rootElement.getChildNodes();
        for (int i = 0; i < journalNodes.getLength(); i++) {
            Node journalNode = journalNodes.item(i);
            if (journalNode.getNodeType() == Node.ELEMENT_NODE){
                Element journalElement = (Element) journalNode;
                if (journalElement.getNodeName().equals("entry")) {
                    Entry e = parseEntry(journalElement);
                    entryMap.put(e.getDate(), e);
                } // end entry if
            } // end element if
        } // end for
        return entryMap;
    }
    
    private Entry parseEntry(Element rootEntryElement){
        Entry rEntry = new Entry();
        rEntry.setDate(rootEntryElement.getAttribute("date"));
        NodeList entryNodes = rootEntryElement.getChildNodes();
        for (int i = 0; i < entryNodes.getLength(); i ++) {
            // Changing all children to child by child basis
            Node entryNode = entryNodes.item(i);
            if (entryNode.getNodeType() == Node.ELEMENT_NODE){
                Element entryElement = (Element) entryNode;
                switch (entryElement.getNodeName()) {
                    case "scripture":
                        rEntry.addScripture(parseScripture(entryElement));
                        break;
                    case "topic":
                        rEntry.addTopic(entryElement.getTextContent());
                        break;
                    case "content":
                        rEntry.setContent(entryElement.getTextContent().trim().replaceAll("\\n\\s+", "\n"));
                        break;
                }
            }
        }
        return rEntry;
    }
    private void writeOutputFile() {}

    private Scripture parseScripture(Element rootScriptureElement) {
        Scripture rScripture = new Scripture();
        NamedNodeMap attributes = rootScriptureElement.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attributeAtI = attributes.item(i);
            switch (attributeAtI.getNodeName()) {
                    case "book":
                        rScripture.setBook(attributeAtI.getNodeValue());
                        break;
                    case "chapter":
                        rScripture.setChapter(Integer.parseInt(attributeAtI.getNodeValue()));
                        break;
                    case "startverse":
                        rScripture.setStartVerse(Integer.parseInt(attributeAtI.getNodeValue()));
                        break;
                    case "endverse":
                        rScripture.setEndVerse(Integer.parseInt(attributeAtI.getNodeValue()));
                        break;
            }
        }
        return rScripture;
    }
    public final void display() {
        System.out.println("Journal Display:");
        List<String> keys = new ArrayList<>(entries.keySet());
        for (String key: keys) {
            System.out.println(entries.get(key).display());
        }
    }

    private void readScriptureFile(String scriptureFile) throws IOException{
        FileReader in = new FileReader(scriptureFile);
        BufferedReader br = new BufferedReader(in);
        
        String currentLine = br.readLine();
        while(currentLine != null) {
            String book = currentLine.split(":")[0].trim();
            //System.out.println(book);
            booksToFind.add(book);
            currentLine = br.readLine();
        }
        br.close();
    }

    private void readTermsFile(String termsFile) throws IOException{        
        FileReader in = new FileReader(termsFile);
        BufferedReader br = new BufferedReader(in);
        String currentLine = br.readLine();
        List<String> termLines = new ArrayList<>();
        List<String> synonyms = new ArrayList<>();
        while(currentLine != null) {
            termLines.add(currentLine);
            currentLine = br.readLine();
        }
        for (String termLine : termLines){
            String[] termParts = termLine.split(":");
            String termKey = termParts[0];
            String[] temps = termParts[1].split(",");
            for (String temp : temps) {
                synonyms.add(temp);
            }
            termsToFind.put(termKey, synonyms);
            synonyms = new ArrayList<>();
        }
        br.close();
    }
}