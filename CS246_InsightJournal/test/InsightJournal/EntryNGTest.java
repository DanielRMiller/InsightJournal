/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InsightJournal;

import InsightJournal.Scripture;
import InsightJournal.Entry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Xandron
 */
public class EntryNGTest {
    
    public EntryNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getText method, of class Entry.
     */
    @Test
    public void testGetText() {
        System.out.println("getText");
        Entry instance = new Entry("01/24/2014",  "I read Moses 1:39 and had a really good day today.");
        String expResult = "I read Moses 1:39 and had a really good day today.";
        String result = instance.getText();
        assertEquals(result, expResult);
    }

    /**
     * Test of getScriptureList method, of class Entry.
     */
    @Test
    public void testGetScriptureList() {
        System.out.println("getScriptureList");
        Entry instance = new Entry("01/24/2014",  "I read Moses 1:39 and had a really good day today.");
        List<Scripture> expResult = new ArrayList<>();
        expResult.add(new Scripture("Moses", 1, 39));
        List<Scripture> result = instance.getScriptureList();
        assertEquals(result.size(), expResult.size());
        assertEquals(result.get(0).getBook().getName() , "Moses");
        assertEquals(result.get(0).getChapter() , 1);
        assertEquals(result.get(0).getVerses().get(0) , (Integer)39);
    }

    /**
     * Test of getDate method, of class Entry.
     */
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        Entry instance = new Entry("01/24/2014",  "I read Moses 1:39 and had a really good day today.");
        Date expResult = new Date(2014, 01, 24);
        Date result = instance.getDate();
        assertEquals(result, expResult);
    }

    /**
     * Test of getTopicList method, of class Entry.
     */
    @Test
    public void testGetTopicList() {
        System.out.println("getTopicList");
        Entry instance = new Entry("01/24/2014",  "I read Moses 1:39 and had a really good day today.");
        List<String> expResult = new ArrayList<>();
        expResult.add("good");
        List result = instance.getTopicList();
        assertEquals(result, expResult);
    }

    /**
     * Test of setText method, of class Entry.
     */
    @Test
    public void testSetText() {
        System.out.println("setText");
        Entry instance = new Entry("01/24/2014", "I read Moses 1:39 and had a really good day today.");
        instance.setText("Blah Blah Blah");
        String expResult = "Blah Blah Blah";
        String result = instance.getText();
        assertEquals(result, expResult);
    }

    /**
     * Test of setScriptureList method, of class Entry.
     */
    @Test
    public void testSetScriptureList() {
        System.out.println("setScriptureList");
        Entry instance = new Entry("01/24/2014",  "I read Moses 1:39 and had a really good day today.");
        List<Scripture> expResult = new ArrayList<>();
        expResult.add(new Scripture("Moses", 1, 39));
        List<Scripture> result;
        result = instance.getScriptureList();
        assertEquals(result, expResult);
    }

    /**
     * Test of setDate method, of class Entry.
     */
    @Test
    public void testSetDate() {
        System.out.println("setDate");
        Entry instance = new Entry("01/24/2014", "I read Moses 1:39 and had a really good day today.");
        instance.setDate(new Date(2/12/2014));
        Date expResult = new Date(2/12/2014);
        Date result = instance.getDate();
        assertEquals(result, expResult);
    }

    /**
     * Test of setTopicList method, of class Entry.
     */
    @Test
    public void testSetTopicList() {
        System.out.println("setTopicList");
        Entry instance = new Entry("01/24/2014", "I read Moses 1:39 and had a really good day today.");
        List<String> temp;
        temp = new ArrayList<>();
        temp.add("great");
        instance.setTopicList(temp);
        List<String> expResult = temp;
        List<String> result = instance.getTopicList();
        assertEquals(result, expResult);
    }

    /**
     * Test of print method, of class Entry.
     */
    @Test
    public void testPrint() {
        System.out.println("print");
        int num = 0;
        Entry instance = new Entry("01/24/2014", "I read Moses 1:39 and had a really good day today.");
        String expResult = "01/24/2014 I read Moses 1:39 and had a really good day today.";
        String result = instance.print(num);
        assertEquals(result, expResult);
    }
}
