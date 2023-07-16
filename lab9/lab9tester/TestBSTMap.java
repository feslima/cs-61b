package lab9tester;

import static org.junit.Assert.*;

import org.junit.Test;
import lab9.BSTMap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests by Brendan Hu, Spring 2015, revised for 2018 by Josh Hug
 */
public class TestBSTMap {

    @Test
    public void sanityGenericsTest() {
        try {
            BSTMap<String, String> a = new BSTMap<String, String>();
            BSTMap<String, Integer> b = new BSTMap<String, Integer>();
            BSTMap<Integer, String> c = new BSTMap<Integer, String>();
            BSTMap<Boolean, Integer> e = new BSTMap<Boolean, Integer>();
        } catch (Exception e) {
            fail();
        }
    }

    //assumes put/size/containsKey/get work
    @Test
    public void sanityClearTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1 + i);
            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i));
            assertTrue(b.get("hi" + i).equals(1 + i));
            assertTrue(b.containsKey("hi" + i));
        }
        assertEquals(455, b.size());
        b.clear();
        assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    // assumes put works
    @Test
    public void sanityContainsKeyTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        assertTrue(b.containsKey("waterYouDoingHere"));
    }

    // assumes put works
    @Test
    public void sanityGetTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(null, b.get("starChild"));
        assertEquals(0, b.size());
        b.put("starChild", 5);
        assertTrue(((Integer) b.get("starChild")).equals(5));
        b.put("KISS", 5);
        assertTrue(((Integer) b.get("KISS")).equals(5));
        assertNotEquals(null, b.get("starChild"));
        assertEquals(2, b.size());
    }

    // assumes put works
    @Test
    public void sanitySizeTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(0, b.size());
        b.put("hi", 1);
        assertEquals(1, b.size());
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        assertEquals(456, b.size());
    }

    //assumes get/containskey work
    @Test
    public void sanityPutTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("hi", 1);
        assertTrue(b.containsKey("hi"));
        assertTrue(b.get("hi") != null);
    }

    @Test
    public void testShouldRemoveLeftLeafWithoutChildCorrectly() {
        BSTMap<Integer, Integer> b = new BSTMap<Integer, Integer>();
        b.put(50, 50);
        b.put(25, 25);
        b.put(75, 75);
        b.put(10, 10);
        b.put(33, 33);
        b.put(56, 56);
        b.put(89, 89);

        b.remove(10);
        assertEquals(b.size(), 6);
        assertFalse(b.containsKey(10));

        Set<Integer> expected = new HashSet<>(Arrays.asList(25, 33, 50, 56, 75, 89));
        assertEquals(b.keySet(), expected);
    }

    @Test
    public void testShouldRemoveNodeWithSingleRightChildCorrectly() {
        BSTMap<Integer, Integer> b = new BSTMap<Integer, Integer>();
        b.put(50, 50);
        b.put(25, 25);
        b.put(75, 75);
        b.put(10, 10);
        b.put(33, 33);
        b.put(56, 56);
        b.put(89, 89);

        b.remove(10);
        assertEquals(b.size(), 6);
        assertFalse(b.containsKey(10));

        Set<Integer> expected = new HashSet<>(Arrays.asList(25, 33, 50, 56, 75, 89));
        assertEquals(b.keySet(), expected);

        b.remove(25);
        assertEquals(b.size(), 5);
        assertFalse(b.containsKey(25));

        expected = new HashSet<>(Arrays.asList(33, 50, 56, 75, 89));
        assertEquals(b.keySet(), expected);
    }

    @Test
    public void testShouldRemoveNodeWithTwoChildrenCorrectly() {
        BSTMap<Integer, Integer> b = new BSTMap<Integer, Integer>();
        b.put(50, 50);
        b.put(25, 25);
        b.put(75, 75);
        b.put(11, 11);
        b.put(33, 33);
        b.put(56, 56);
        b.put(89, 89);
        b.put(30, 30);
        b.put(40, 40);
        b.put(52, 52);
        b.put(61, 61);
        b.put(82, 82);
        b.put(95, 95);


        b.remove(56);
        assertEquals(b.size(), 12);
        assertFalse(b.containsKey(56));

        Set<Integer> expected = new HashSet<>(Arrays.asList(11, 25, 30, 33, 40, 50, 52, 61, 75, 82, 89, 95));
        assertEquals(b.keySet(), expected);
    }

    @Test
    public void testShouldRemoveNodeWithTwoChildrenAndLeftSuccessorNodeCorrectly() {
        BSTMap<Integer, Integer> b = new BSTMap<Integer, Integer>();
        b.put(50, 50);
        b.put(25, 25);
        b.put(75, 75);
        b.put(11, 11);
        b.put(33, 33);
        b.put(61, 61);
        b.put(89, 89);
        b.put(30, 30);
        b.put(40, 40);
        b.put(52, 52);
        b.put(82, 82);
        b.put(95, 95);

        b.remove(50);
        assertEquals(b.size(), 11);
        assertFalse(b.containsKey(50));

        Set<Integer> expected = new HashSet<>(Arrays.asList(11, 25, 30, 33, 40, 52, 61, 75, 82, 89, 95));
        assertEquals(b.keySet(), expected);
    }

    @Test
    public void testShouldRemoveNodeWithTwoChildrenAndSuccessorNodeWithRightChildCorrectly() {
        BSTMap<Integer, Integer> b = new BSTMap<Integer, Integer>();
        b.put(50, 50);
        b.put(25, 25);
        b.put(75, 75);
        b.put(11, 11);
        b.put(33, 33);
        b.put(61, 61);
        b.put(89, 89);
        b.put(30, 30);
        b.put(40, 40);
        b.put(52, 52);
        b.put(82, 82);
        b.put(95, 95);
        b.put(55, 55);

        b.remove(50);
        assertEquals(b.size(), 12);
        assertFalse(b.containsKey(50));

        Set<Integer> expected = new HashSet<>(Arrays.asList(11, 25, 30, 33, 40, 52, 55, 61, 75, 82, 89, 95));
        assertEquals(b.keySet(), expected);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestBSTMap.class);
    }
}
