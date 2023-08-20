import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;

public class TrieTest {
    private Trie trie;

    @Before
    public void setUp() {
        ArrayList<String> words = new ArrayList<>();

        In in = new In("words.txt");
        while (!in.isEmpty()) {
            words.add(in.readLine());
        }

        trie = new Trie(words);
    }

    @Test
    public void testInsertionAndSearch() {
        assertTrue(trie.hasWord("Americans"));
        assertFalse(trie.hasWord("asdas"));
    }

    @Test
    public void testKeysWithPrefix() {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("Asian", "Asian's", "Asians"));
        ArrayList<String> actual = trie.keysWithPrefix("Asia", -1);

        assertEquals(expected.size(), actual.size());

        assertEquals(new HashSet<String>(expected), new HashSet<>(actual));
    }

    @Test
    public void testKeysWithPrefixAndMaxWords() {
        Set<String> expected = new HashSet<String>(Arrays.asList(
                "Christian",
                "Christian's",
                "Christianities",
                "Christianity",
                "Christianity's",
                "Christians",
                "Christmas",
                "Christmas's",
                "Christmases"
        ));
        int maxWords = 4;
        ArrayList<String> actual = trie.keysWithPrefix("Christ", maxWords);

        assertEquals(maxWords, actual.size());

        assertTrue(expected.containsAll(actual));
    }

}
