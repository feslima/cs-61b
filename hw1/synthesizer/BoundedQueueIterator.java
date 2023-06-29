package synthesizer;

import java.util.Iterator;


public class BoundedQueueIterator<T> implements Iterator<T> {
    private final AbstractBoundedQueue<T> queue;

    public BoundedQueueIterator(AbstractBoundedQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public T next() {
        return queue.dequeue();
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }
}
