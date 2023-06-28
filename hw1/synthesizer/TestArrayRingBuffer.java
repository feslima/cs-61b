package synthesizer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the ArrayRingBuffer class.
 *
 * @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void testBoundedQueueContract() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(4);

        assertTrue(arb.isEmpty());
        arb.enqueue(9.3);
        arb.enqueue(15.1);
        arb.enqueue(31.2);
        assertFalse(arb.isFull());
        arb.enqueue(-3.1);
        assertTrue(arb.isFull());
        Double d = arb.dequeue();
        assertNotNull(d);
        assertEquals(9.3, d, 1e-2);
        assertEquals(15.1, arb.peek(), 1e-2);
    }

    @Test
    public void testLastIndexWrapAround() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(2);
        assertTrue(arb.isEmpty());
        arb.enqueue(9.3);
        arb.enqueue(15.1); // last index wrap around should happen here
        assertTrue(arb.isFull());
        assertEquals(9.3, arb.dequeue(), 1e-2);
        assertFalse(arb.isFull());
        arb.enqueue(31.2);
        assertTrue(arb.isFull());
        assertEquals(15.1, arb.dequeue(), 1e-2);
        assertFalse(arb.isFull());
        assertFalse(arb.isEmpty());
    }

    @Test
    public void testFirstIndexWrapAround() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(2);
        assertTrue(arb.isEmpty());
        arb.enqueue(9.3);
        arb.enqueue(15.1);
        assertTrue(arb.isFull());
        assertEquals(9.3, arb.dequeue(), 1e-2);
        assertFalse(arb.isFull());
        arb.enqueue(31.2);
        assertTrue(arb.isFull());
        assertEquals(15.1, arb.dequeue(), 1e-2); // first index wrap around should happen here
        assertEquals(31.2, arb.dequeue(), 1e-2);
        assertFalse(arb.isFull());
        assertTrue(arb.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testWhenDequeueingEmptyBuffer_thenRuntimeExceptionIsThrown(){
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(2);
        assertTrue(arb.isEmpty());
        arb.dequeue();
    }

    @Test(expected = RuntimeException.class)
    public void testWhenPeekiingEmptyBuffer_thenRuntimeExceptionIsThrown(){
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(2);
        assertTrue(arb.isEmpty());
        arb.peek();
    }

    @Test(expected = RuntimeException.class)
    public void testWhenQueueingFullBuffer_thenRuntimeExceptionIsThrown(){
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(2);
        assertTrue(arb.isEmpty());
        arb.enqueue(9.3);
        arb.enqueue(15.1);
        assertTrue(arb.isFull());
        arb.enqueue(31.2);
    }
}
