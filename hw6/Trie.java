import java.io.File;
import java.util.*;

public class Trie {
    private static class TrieNode {
        private final Map<Character, TrieNode> children;
        private boolean isEndOfWord = false;
        private boolean isRoot = false;

        TrieNode() {
            children = new HashMap<>();
        }

        TrieNode(boolean isRoot) {
            this();
            this.isRoot = isRoot;
        }
    }

    private final TrieNode root;

    public static final Comparator<String> WORD_COMPARATOR = (o1, o2) -> {
        /* for same length words, keep lexicographic ascending order.
         * Otherwise, order words by size.
         */
        if (o1.length() == o2.length()) {
            return o1.compareTo(o2);
        }
        return o2.length() - o1.length();
    };

    public Trie(Iterable<String> words) {
        root = new TrieNode(true);

        for (String word : words) {
            insertWord(word);
        }
    }

    private void insertWord(String word) {
        TrieNode currentNode = root;
        for (Character ch : word.toCharArray()) {
            if (currentNode.children.containsKey(ch)) {
                currentNode = currentNode.children.get(ch);
            } else {
                TrieNode newNode = new TrieNode();
                currentNode.children.put(ch, newNode);
                currentNode = newNode;
            }
        }

        if (!currentNode.isRoot) {
            currentNode.isEndOfWord = true;
        }
    }

    private TrieNode searchPrefix(String word) {
        TrieNode currentNode = root;
        for (Character ch : word.toCharArray()) {
            currentNode = currentNode.children.get(ch);
            if (currentNode == null) {
                return null;
            }
        }
        return currentNode;

    }

    private void collectAllWords(TrieNode currentNode, String currentWord, List<String> wordCollector, int maxWords) {
        for (char ch : currentNode.children.keySet()) {
            if (maxWords > 0 && wordCollector.size() >= maxWords) {
                break;
            }

            TrieNode childNode = currentNode.children.get(ch);

            if (childNode.isEndOfWord) {
                wordCollector.add(currentWord + ch);
            }

            collectAllWords(childNode, currentWord + ch, wordCollector, maxWords);
        }
    }

    /**
     * Find words in trie based on prefix
     *
     * @param prefix   Word prefix to search for.
     * @param maxWords Maximum number of words to search. If set to -1, search for all words in trie.
     * @return List of words found in trie.
     */
    public ArrayList<String> keysWithPrefix(String prefix, int maxWords) {
        ArrayList<String> words = new ArrayList<>();
        TrieNode currentNode = searchPrefix(prefix);

        if (currentNode == null) {
            return words;
        }

        collectAllWords(currentNode, prefix, words, maxWords);
        return words;
    }

    public boolean hasWord(String word) {
        TrieNode currentNode = root;
        for (Character ch : word.toCharArray()) {
            currentNode = currentNode.children.get(ch);
            if (currentNode == null) {
                return false;
            }
        }

        if (currentNode.isRoot) {
            return false;
        }
        return currentNode.isEndOfWord;
    }

    private static void validateFilePath(String filepath) {
        File f = new File(filepath);

        if (!f.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }
    }

    public static Trie buildTrieFromFile(String filepath) {
        validateFilePath(filepath);

        ArrayList<String> words = new ArrayList<>();

        In in = new In(filepath);
        while (!in.isEmpty()) {
            words.add(in.readLine());
        }

        return new Trie(words);
    }
}
