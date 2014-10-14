package InsightJournal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
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
    private List<String> termsToFind = new ArrayList<>();
    
    public static void main(String[] args){
        if (args.length == 3) {
            // System.out.println(args[1]); // test argument
            Journal j = new Journal(args);
        }
        else
            System.out.println("Cannot find filename");
    }
    
    Journal(String[] args)
    {
        if (args[0].split("\\.")[1].equals("txt") &&
                args[1].split("\\.")[1].equals("xml") &&
                args[2].split("\\.")[1].equals("txt")){
            String fileName = args[1];
            // Get everything in from the XML file
            readInputFile(fileName);
            // Get the document created for writing
            try {
                writeXMLDocument();
            } catch (Exception ex) {
                Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
            }
            // displayTest(); // This is to test the XML is loaded (Replace in tests)
            
            // Get everything loaded in from the config.properties file.
            PropertiesHandler prop = new PropertiesHandler();
            try {
                prop.getPropValues();
                //System.out.println(PropertiesHandler.scripture);
                readScriptureFile(PropertiesHandler.scripture);
                readTermsFile(PropertiesHandler.terms);
            } catch (IOException ex) {
                Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // display desired output for the journal (match terms and scriptures with entries)
            // display();
        }
        else
            System.out.println("Sorry, your files do not match the necessary types");
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
    private void writeXMLDocument() throws Exception{
        System.out.println("Building document");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.newDocument();
        
        // Root element
        Element rootElement = doc.createElement("journal");
        doc.appendChild(rootElement);
        
        List<String> entryKeys = new ArrayList<>(entries.keySet());
        for (String entryKey : entryKeys) {
            // The entry we are working with
            Entry currentEntry = entries.get(entryKey);
            
            // Entry element
            Element entryEle = doc.createElement("entry");
            rootElement.appendChild(entryEle);
            
            // Add Entry Date
            Attr attr = doc.createAttribute("date");
            attr.setValue(entryKey);
            entryEle.setAttributeNode(attr);
            
            // Scripture Time
        }
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("C:\\Users\\Xandron\\Documents\\NetBeansProjects\\InsightJournal\\CS246_InsightJournal\\src\\InsightJournal\\myUpdatedJournal.xml"));
        
        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);
        
        transformer.transform(source, result);
        
        System.out.println("File saved!");
    }
    
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
    public final void displayTest() {
        System.out.println("Journal Test Display:");
        List<String> keys = new ArrayList<>(entries.keySet());
        for (String key: keys) {
            System.out.println(entries.get(key).display());
        }
    }
    
    public final void display() {
        List<String> keys = new ArrayList<>(entries.keySet());
        List<String> tempEntryKeys = new ArrayList<>();
        
        // Start finding all the Scripture References
        System.out.println("Scripture References:");
        for (String book: booksToFind) {
            for (String key: keys) {
                if (entries.get(key).hasbook(book)) {
                    tempEntryKeys.add(key);
                }
            }
            if (!tempEntryKeys.isEmpty()) {
                System.out.println(book);
                for (String key: tempEntryKeys) {
                    System.out.println("\t" + key);
                }
                tempEntryKeys.clear();
            }
        }
        
        // Start finding all the Terms References
        System.out.println("\nScripture References:");
        for (String term: termsToFind) {
            for (String key: keys) {
                if (entries.get(key).hasTerm(term)) {
                    tempEntryKeys.add(key);
                }
            }
            if (!tempEntryKeys.isEmpty()) {
                System.out.println(term);
                for (String key: tempEntryKeys) {
                    System.out.println("\t" + key);
                }
                tempEntryKeys.clear();
            }
        }
    }
    
    private void readScriptureFile(String scriptureFile) throws FileNotFoundException, IOException {
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
    
    private void readTermsFile(String termsFile) throws FileNotFoundException, IOException {
        FileReader in = new FileReader(termsFile);
        BufferedReader br = new BufferedReader(in);
        
        String currentLine = br.readLine();
        while(currentLine != null) {
            String term = currentLine.split(":")[0].trim();
            termsToFind.add(term);
            currentLine = br.readLine();
        }
        br.close();
    }
}