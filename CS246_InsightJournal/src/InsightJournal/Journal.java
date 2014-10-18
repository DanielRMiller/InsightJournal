package InsightJournal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
    private List<Term> masterTermList = new ArrayList<>();
    
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
        // System.out.println("Entered Journal");
        // Check if we have the right file types
        if (args[0].split("\\.")[1].equals("txt") &&
                args[1].split("\\.")[1].equals("xml") &&
                args[2].split("\\.")[1].equals("txt")){
            String inTXT = args[0];
            String XML = args[1];
            String outTXT = args[2];
            
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
            
            // Get everything in from the file into journal
            readInputFile(inTXT);
            
            // Test what is currently in the Journal
            // displayTest();
            
            // Write to the XML document
            try {
                writeXMLDocument();
            } catch (Exception ex) {
                Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Write to the TXT document
            writeTextDocument(outTXT);
            
            // display desired output for the journal (match terms and scriptures with entries)
            display();
        }
        else
            System.out.println("Sorry, your files do not match the necessary types");
    }
    
    private void readInputFile(String filename) {
        // System.out.println("Entered readinputFile");
        String fileType = filename.split("\\.")[1];
        if (fileType.equals("xml")){
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
        } else if (fileType.equals("txt"))  {
            try {                
                FileReader fin = new FileReader(filename);
                BufferedReader br = new BufferedReader(fin);
                Entry newEntry = new Entry(masterTermList);
                String currentLine = ""; 
                String newContent = "";
                br.mark(1);
                while(br.ready()) {
                    currentLine = br.readLine();
                    if (currentLine.equals("-----")) { // Assume dashed line is always followed by date line
                        newEntry.setDate(br.readLine()); // Set the date to 2nd line
                        newContent = "";
                    }
                    else {
                        while (!currentLine.equals("-----") && br.ready()) { // keep reading till we can't or we hit a new entry
                            // System.out.println("In while loop");
                            newContent = newContent + currentLine;
                            br.mark(255);
                            currentLine = br.readLine();
                        }
                        if (!currentLine.equals("-----"))
                            newContent = newContent + currentLine;
                        if (br.ready())
                            br.reset();
                        
                        // After we got all the content
                        // System.out.println("newEntry Date: " + newEntry.getDate());
                        // newEntry.parseForScriptures(newContent);
                        // System.out.println("newEntry Scriptures: " + newEntry.getScriptureList());
                        // newEntry.parseForTopics(newContent);
                        // System.out.println("newEntry Topics: " + newEntry.getTermList());
                        newEntry.setContent(newContent);
                        // System.out.println("newEntry Content: " + newEntry.getContent());
                        // Why is getDate null?
                        if (newEntry.getDate() != null)
                            entries.put(newEntry.getDate(), newEntry);
                        newEntry = new Entry(masterTermList);
                    }
                }
            } catch (Exception e) { // If format not correct for first entry
                System.out.println("FAILED FILE READ IN!");
                e.printStackTrace();
            }
        } // Exit text reader
    }
    private Map<String, Entry> parseJournal(Element rootElement){
        // System.out.println("Entered parseJournal");
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
        // System.out.println("Entered parseEntry");
        Entry rEntry = new Entry(masterTermList);
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
                        rEntry.addTerm(entryElement.getTextContent());
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
        // System.out.println("Entered writeXMLDocument");
        // System.out.println("Building document");
        
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
            List<Scripture> scriptures = currentEntry.getScriptureList();
            for (Scripture scripture : scriptures) {
                // Scripture element
                Element scriptureEle = doc.createElement("scripture");
                entryEle.appendChild(scriptureEle);
                
                // Add Scripture's Book
                Attr book = doc.createAttribute("book");
                book.setValue(scripture.getBook());
                scriptureEle.setAttributeNode(book);
                // Add Scripture's Chapter
                Attr chapter = doc.createAttribute("chapter");
                chapter.setValue(Integer.toString(scripture.getChapter()));
                scriptureEle.setAttributeNode(chapter);
                // Add Scripture's Book
                Attr startverse = doc.createAttribute("startverse");
                startverse.setValue(Integer.toString(scripture.getStartVerse()));
                scriptureEle.setAttributeNode(startverse);
                // Add Scripture's Book
                Attr endverse = doc.createAttribute("endverse");
                endverse.setValue(Integer.toString(scripture.getEndVerse()));
                scriptureEle.setAttributeNode(endverse);
            }
            // Term time
            List<String> topics = currentEntry.getTermList();
            for (String topic : topics) {
                // Term element
                Element topicEle = doc.createElement("topic");
                topicEle.appendChild(doc.createTextNode(topic));
                entryEle.appendChild(topicEle);
            }
            
            // Content
            Element contentEle = doc.createElement("content");
            contentEle.appendChild(doc.createTextNode(currentEntry.getContent()));
            entryEle.appendChild(contentEle);
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
        
        // System.out.println("File saved!");
    }
    
    private Scripture parseScripture(Element rootScriptureElement) {
        // System.out.println("Entered parseScripture");
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
        // System.out.println("Entered displayTest");
        System.out.println("Journal Test Display:");
        List<String> keys = new ArrayList<>(entries.keySet());
        // for (String key: keys) {System.out.println(entries.get(key).display());}
    }
    
    public final void display() {
        // System.out.println("Entered display");
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
        System.out.println("\nTerm References:");
        for (Term topic: masterTermList) {
            String term = topic.getKey();
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
    
    private void readScriptureFile(String scriptureFile) 
            throws FileNotFoundException, IOException {
        // System.out.println("Entered readscriptureFile");
        //System.out.println("Read Scripture File!");
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
    
    private void readTermsFile(String termsFile) 
            throws FileNotFoundException, IOException {
        // System.out.println("Entered readTermsFile!");
        FileReader in = new FileReader(termsFile);
        BufferedReader br = new BufferedReader(in);
        Term newTerm = new Term();
        
        String currentLine = "";
        while(br.ready()) {
            currentLine = br.readLine();
            // System.out.println("Found a new Line");
            String[] topicAndSyns = currentLine.split(":");
            newTerm.setKey(topicAndSyns[0]);
            String[] synArray = topicAndSyns[1].split(",");
            List<String> synList= new ArrayList<>();
            for (String syn : synArray) {
                synList.add(syn);
            }
            newTerm.setSynList(synList);
            // System.out.println(newTerm.display());
            masterTermList.add(newTerm);
            
            // for (Term t : masterTermList) { System.out.println(t.display());}
            newTerm = new Term();
        }
        br.close();
        // for (Term t : masterTermList) {System.out.println(t.display());}
    }

    private void writeTextDocument(String fileName) {
        // System.out.println("Entered writeTextDocument");
        BufferedWriter bw;
        
        try {
            System.out.println("Writing entries to text file: " + fileName);
            File fout = new File(fileName);
            bw = new BufferedWriter(new FileWriter(fout));
            
            int counter = 0;
            
            for (Map.Entry<String, Entry> entry : entries.entrySet()) {
                String key = entry.getKey();
                Entry value = entry.getValue();
                
                //System.out.println(key + ": " + value.getContent());
                
                if (counter == 0) {
                    bw.write("-----\n");
                    counter++;
                } else
                    bw.write("\n-----\n");
                bw.write(key + "\n\n");
                bw.write(value.getContent() + "\n");
                
            }
            
            System.out.println("Successfully wrote the text file!");
            
            bw.close();
        } catch (Exception e) {
            System.err.println("Unable to write \"WrittenJournal.txt\"");
        }
    }
}