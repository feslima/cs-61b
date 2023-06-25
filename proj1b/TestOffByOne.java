import org.junit.Test;

import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testShouldReturnTrue() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('b', 'a'));
        assertTrue(offByOne.equalChars('r', 'q'));
        assertTrue(offByOne.equalChars('q', 'r'));
        assertTrue(offByOne.equalChars('&', '%'));
    }

    @Test
    public void testShouldReturnFalse() {
        assertFalse(offByOne.equalChars('a', 'e'));
        assertFalse(offByOne.equalChars('z', 'a'));
        assertFalse(offByOne.equalChars('a', 'a'));
    }
}
