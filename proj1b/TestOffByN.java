import org.junit.Test;

import static org.junit.Assert.*;

public class TestOffByN {

    // Your tests go here.
    @Test
    public void testShouldReturnTrue() {
        CharacterComparator offBy5 = new OffByN(5);

        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
    }

    @Test
    public void testShouldReturnFalse() {
        CharacterComparator offBy5 = new OffByN(5);

        assertFalse(offBy5.equalChars('f', 'h'));
    }
}
