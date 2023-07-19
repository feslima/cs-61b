package hw3.hash;

import org.junit.Test;


import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class TestSimpleOomage {

    private static class OomageWithColor {
        private final int red;
        private final int green;
        private final int blue;
        private final Oomage oomage;

        public OomageWithColor(Oomage o, int red, int green, int blue) {
            this.oomage = o;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

    }

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    private ArrayList<OomageWithColor> createAllPossibleSimpleColors() {
        ArrayList<OomageWithColor> combinations = new ArrayList<>();

        for (int r = 0; r <= 255; r += 5) {
            for (int g = 0; g <= 255; g += 5) {
                for (int b = 0; b <= 255; b += 5) {
                    Oomage oomage = new SimpleOomage(r, g, b);
                    OomageWithColor entry = new OomageWithColor(oomage, r, g, b);
                    combinations.add(entry);
                }
            }
        }

        return combinations;
    }

    @Test
    public void testHashCodePerfect() {
        /* Ensures the hashCode is perfect,
          meaning no two SimpleOomages should EVER have the same
          hashCode UNLESS they have the same red, blue, and green values!
         */
        ArrayList<OomageWithColor> combinations = createAllPossibleSimpleColors();

        for (int r = 0; r <= 255; r += 5) {
            for (int g = 0; g <= 255; g += 5) {
                for (int b = 0; b <= 255; b += 5) {
                    SimpleOomage ooA = new SimpleOomage(r, g, b);

                    for (OomageWithColor entry : combinations) {
                        Oomage ooA2 = entry.oomage;
                        int hash1 = ooA.hashCode();
                        int hash2 = ooA2.hashCode();

                        if (entry.red == r && g == entry.green && b == entry.blue) {
                            if (hash1 != hash2) {
                                fail("Failure with red: " + r + " green: " + g + " blue: " + b);
                            }
                        } else {
                            if (hash1 == hash2) {
                                fail("Failure with red: " + r + " green: " + g + " blue: " + b);
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /**
     * Calls tests for SimpleOomage.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
