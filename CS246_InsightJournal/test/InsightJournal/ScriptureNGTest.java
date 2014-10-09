/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InsightJournal;

import InsightJournal.Scripture;
import InsightJournal.Book;
import java.util.ArrayList;
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
public class ScriptureNGTest {
    
    public ScriptureNGTest() {
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
     * Test of getBook method, of class Scripture.
     */
    @Test
    public void testGetBook() {
        System.out.println("getBook");
        Scripture instance = new Scripture("Moses", 1, 39);
        String expResult = "Moses";
        String result = "";
        assertNotNull(instance.getBook());
        assertEquals(result, expResult);
    }

    /**
     * Test of getChapter method, of class Scripture.
     */
    @Test
    public void testGetChapter() {
        System.out.println("getChapter");
        Scripture instance = new Scripture("Moses", 1, 39);
        int expResult = 1;
        int result = instance.getChapter();
        assertEquals(result, expResult);
    }

    /**
     * Test of getVerses method, of class Scripture.
     */
    @Test
    public void testGetVerses() {
        System.out.println("getVerses");
        Scripture instance = new Scripture("Moses", 1, 39);
        List expResult = new ArrayList();
        expResult.add((Integer) 39);
        List result = instance.getVerses();
        assertEquals(result, expResult);
    }

    /**
     * Test of setBook method, of class Scripture.
     */
    @Test
    public void testSetBook() {
        System.out.println("setBook");
        Scripture instance = new Scripture("Moses", 1, 7);
        instance.setBook("2 Nephi");
        Book expResult = new Book("Moses");
        Book result = instance.getBook();
        assertEquals(result, expResult);
    }

    /**
     * Test of setChapter method, of class Scripture.
     */
    @Test
    public void testSetChapter() {
        System.out.println("setChapter");
        Scripture instance = new Scripture("Moses", 1, 39);
        instance.setChapter(2);
        int expResult = 2;
        int result = instance.getChapter();
        assertEquals(result, expResult);
    }

    /**
     * Test of setVerses method, of class Scripture.
     */
    @Test
    public void testSetVerses() {
        System.out.println("setVerses");
        Scripture instance = new Scripture("Moses", 1, 39);
        List<Integer> verses = new ArrayList<>();
        verses.add(12);
        instance.setVerses(verses);
        List<Integer> expResult = verses;
        List<Integer> result = instance.getVerses();
        assertEquals(result, expResult);
    }
}
