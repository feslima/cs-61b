import org.junit.Test;

import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testAssertWordIsPalindrome() {
        String[] palindromeList = {"madam", "nun", "a", "racecar", "noon", ""};

        for (String word : palindromeList) {
            assertTrue(palindrome.isPalindrome(word));
        }

        String[] nonPalindromeList = {"persiflage", "cat", "horse", "rancor", "aaaaab"};
        for (String word : nonPalindromeList) {
            assertFalse(palindrome.isPalindrome(word));
        }
    }

    @Test
    public void testIsPalindromeOffByOne() {
        CharacterComparator offByOne = new OffByOne();

        assertTrue(palindrome.isPalindrome("flake", offByOne));

        // words derived from "madam"
        assertFalse(palindrome.isPalindrome("madop", offByOne));
        assertTrue(palindrome.isPalindrome("madbl", offByOne));
    }
}
