/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InsightJournal;

import InsightJournal.Book;
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
public class BookNGTest {
    public BookNGTest() {}
    @BeforeClass
    public static void setUpClass() throws Exception {}
    @AfterClass
    public static void tearDownClass() throws Exception {}
    @BeforeMethod
    public void setUpMethod() throws Exception {}
    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
    /**
     * Test of getName method, of class Book.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Book b = new Book("2 Nephi");
        String expResult = "2 Nephi";
        String result = b.getName();
        assertEquals(result, expResult);
    }

    /**
     * Test of getSW method, of class Book.
     */
    @Test
    public void testGetSW() {
        System.out.println("getSW");
        Book instance = new Book("Alma");
        String expResult = "Book of Mormon";
        String result = instance.getSW();
        assertEquals(result, expResult);
    }

    /**
     * Test of getMaxChapter method, of class Book.
     */
    @Test
    public void testGetMaxChapter() {
        System.out.println("getMaxChapter");
        Book instance = new Book("2 Nephi");
        int expResult = 33;
        int result = instance.getMaxChapter();
        assertEquals(result, expResult);
    }

    /**
     * Test of getMaxVerse method, of class Book.
     */
    @Test
    public void testGetMaxVerse() {
        System.out.println("getMaxVerse");
        Book instance = new Book("Moses");
        int[] expResult = {42, 31, 25, 32, 59, 68, 69, 30};
        assertNotNull(instance.getMaxVerse());
        int[] result = instance.getMaxVerse();
        assertEquals(result, expResult);
    }

    /**
     * Test of setName method, of class Book.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        Book instance = new Book("Omni");
        instance.setName("Job");
        String expResult = "Job";
        String result = instance.getName();
        assertEquals(result, expResult);
    }
    
}
