public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> result = new ArrayDeque<Character>();

        for (int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }

        return result;
    }
}
