import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestBoggle {
    @Test
    public void testExampleBoard() {
        Boggle.dictPath = "words.txt";
        Object[] words = Boggle.solve(7, "exampleBoard.txt").toArray();
        Object[] expected = Arrays.asList(
                "thumbtacks",
                "thumbtack",
                "setbacks",
                "setback",
                "ascent",
                "humane",
                "smacks"
        ).toArray();

        assertArrayEquals(expected, words);
    }

    @Test
    public void testExampleBoard2() {
        Boggle.dictPath = "trivial_words.txt";
        Object[] words = Boggle.solve(20, "exampleBoard2.txt").toArray();
        Object[] expected = Arrays.asList(
                "aaaaa",
                "aaaa"
        ).toArray();

        assertArrayEquals(expected, words);
    }
}
