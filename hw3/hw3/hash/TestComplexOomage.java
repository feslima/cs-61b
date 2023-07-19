package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();

        deadlyList.add(new ComplexOomage(Arrays.asList(0, 63, 195, 255)));
        deadlyList.add(new ComplexOomage(Arrays.asList(0, 63, 255, 195)));
        deadlyList.add(new ComplexOomage(Arrays.asList(0, 195, 63, 255)));
        deadlyList.add(new ComplexOomage(Arrays.asList(0, 195, 255, 63)));
        deadlyList.add(new ComplexOomage(Arrays.asList(0, 255, 63, 195)));
        deadlyList.add(new ComplexOomage(Arrays.asList(0, 255, 195, 63)));

        deadlyList.add(new ComplexOomage(Arrays.asList(63, 0, 195, 255)));
        deadlyList.add(new ComplexOomage(Arrays.asList(63, 0, 255, 195)));
        deadlyList.add(new ComplexOomage(Arrays.asList(63, 195, 0, 255)));
        deadlyList.add(new ComplexOomage(Arrays.asList(63, 195, 255, 0)));
        deadlyList.add(new ComplexOomage(Arrays.asList(63, 255, 0, 195)));
        deadlyList.add(new ComplexOomage(Arrays.asList(63, 255, 195, 0)));

        deadlyList.add(new ComplexOomage(Arrays.asList(195, 0, 63, 255)));
        deadlyList.add(new ComplexOomage(Arrays.asList(195, 0, 255, 63)));
        deadlyList.add(new ComplexOomage(Arrays.asList(195, 63, 0, 255)));
        deadlyList.add(new ComplexOomage(Arrays.asList(195, 63, 255, 0)));
        deadlyList.add(new ComplexOomage(Arrays.asList(195, 255, 0, 63)));
        deadlyList.add(new ComplexOomage(Arrays.asList(195, 255, 63, 0)));

        deadlyList.add(new ComplexOomage(Arrays.asList(255, 0, 63, 195)));
        deadlyList.add(new ComplexOomage(Arrays.asList(255, 0, 195, 63)));
        deadlyList.add(new ComplexOomage(Arrays.asList(255, 63, 0, 195)));
        deadlyList.add(new ComplexOomage(Arrays.asList(255, 63, 195, 0)));
        deadlyList.add(new ComplexOomage(Arrays.asList(255, 195, 0, 63)));
        deadlyList.add(new ComplexOomage(Arrays.asList(255, 195, 63, 0)));

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
