package assignment3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

//import scoreannotation.Score;
//import testutils.NoExitSecurityManager;





/**
 * This is the sample test cases for students
 *
 * @author lisahua
 */
public class SampleTest {
    private static Set<String> dict;
    private static ByteArrayOutputStream outContent;

    private static final int SHORT_TIMEOUT = 300; // ms
    private static final int SEARCH_TIMEOUT = 30000; // ms

    private SecurityManager initialSecurityManager;

    @Rule // Comment this rule out when debugging to remove timeouts
    public Timeout globalTimeout = new Timeout(SEARCH_TIMEOUT);

    @Before // this method is run before each test
    public void setUp() {
        Main.initialize();
        dict = Main.makeDictionary();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        initialSecurityManager = System.getSecurityManager();
		//System.setSecurityManager(new NoExitSecurityManager());
    }

    @After
	public void cleanup() {
		System.setSecurityManager(initialSecurityManager);
	}

    private boolean verifyLadder(ArrayList<String> ladder, String start, String end) {
        String prev = null;
        if (ladder == null)
            return true;
        for (String word : ladder) {
            if (!dict.contains(word.toUpperCase()) && !dict.contains(word.toLowerCase())) {
                return false;
            }
            if (prev != null && !differByOne(prev, word))
                return false;
            prev = word;
        }
        return ladder.size() > 0
                && ladder.get(0).toLowerCase().equals(start)
                && ladder.get(ladder.size() - 1).toLowerCase().equals(end);
    }

    private static boolean differByOne(String s1, String s2) {
        if (s1.length() != s2.length())
            return false;

        int diff = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i) && diff++ > 1) {
                return false;
            }
        }

        return true;
    }
    

    @Test
    public void testNoNeighbors() {
    	dict.add("*.*.*"); //No neighbors
        assertFalse(Main.findNeighbors("*.*.*", "yield")==null);
        assertTrue(Main.findNeighbors("*.*.*", "yield").isEmpty());
    }
    
    @Test
    public void testAllNeighbors() {
    	Queue<String> neighbors = Main.findNeighbors("ABCDE", "YIELD");
        assertFalse(neighbors==null);
        assertFalse(neighbors.isEmpty());
        assertTrue(neighbors.size()==2);
    }

    /**
     * No Word Ladder
     **/
    @Test
    public void testNoWordLadderBFS() {
        ArrayList<String> res = Main.getWordLadderBFS("aldol", "drawl");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(res == null || res.size() == 0 || res.size() == 2);
    }

    @Test
    public void testNoWordLadderDFS() {
        ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(res == null || res.size() == 0 || res.size() == 2);
    }

    /**
     * Has Word Ladder
     **/
    @Test
    public void testBFS1() {
        ArrayList<String> res = Main.getWordLadderBFS("plain", "words");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "plain", "words"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);
        assertTrue(res.size() < 10);
    }

    @Test
    public void testDFS1() {
        ArrayList<String> res = Main.getWordLadderDFS("plain", "words");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "plain", "words"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);
    }

    @Test
    public void testBFS2() {
        ArrayList<String> res = Main.getWordLadderBFS("house", "lover");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "house", "lover"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);
        assertTrue(res.size() < 9);
    }

    @Test
    public void testDFS2() {
        ArrayList<String> res = Main.getWordLadderDFS("house", "lover");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "house", "lover"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);
    }

    @Test
    public void testPrintLadderEmpty() {
        ArrayList<String> res = Main.getWordLadderBFS("twixt", "hakus");
        outContent.reset();
        Main.printLadder(res);
        String str = outContent.toString().replace("\n", "").replace(".", "").trim();
        assertEquals("no word ladder can be found between twixt and hakus", str);
    }

    @Test
    public void testPrintLadderNoRungs() {
        ArrayList<String> res = Main.getWordLadderBFS("alone", "atone");
        outContent.reset();
        Main.printLadder(res);
        String str = outContent.toString().replace("\r", "").replace("\n", "").replace(".", "").trim();
        assertEquals("a 0-rung word ladder exists between alone and atonealoneatone", str);
    }
    
    
}



