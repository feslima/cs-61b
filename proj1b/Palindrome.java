import java.util.ArrayList;
import java.util.List;

public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> result = new ArrayDeque<>();

        for (int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }

        return result;
    }

    private String dequeToReversedWord(Deque<Character> deque) {
        List<String> elements = new ArrayList<>();
        int nChars = deque.size();
        for (int i = 0; i < nChars; i++) {
            elements.add(deque.removeLast().toString());
        }

        return String.join("", elements);
    }

    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        return word.equals(dequeToReversedWord(d));
    }

    public boolean isPalindrome(String word, CharacterComparator cc){
        int nChars = word.length();
        boolean isEven = nChars % 2 == 0;

        int maxIter = isEven ? nChars / 2 : (nChars - 1) / 2;

        Deque<Character> d = wordToDeque(word);

        boolean result = true;
        for (int i = 0; i < maxIter; i++) {
           Character start = d.removeFirst();
           Character end = d.removeLast();

           boolean comparison = cc.equalChars(start, end);

           if (!comparison) {
               result = false;
               break;
           }
        }

        return result;
    }

}
