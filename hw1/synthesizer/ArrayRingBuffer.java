 package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T>  {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        first = 0;
        last = 0;
        fillCount = 0;
        rb = (T[]) new Object[this.capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (fillCount == capacity) {
            throw new RuntimeException("Buffer is full!");
        }

        rb[last] = x;
        fillCount += 1;
        last += 1;

        if (last >= capacity) {
            last = 0;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Buffer is empty!");
        }

        T item = rb[first];
        rb[first] = null;
        fillCount -= 1;
        first += 1;

        if (first >= capacity) {
            first = 0;
        }
        return item;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Buffer is empty!");
        }

        return rb[first];
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
}
