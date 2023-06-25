import java.util.List;

public interface Deque<T> {
    public void addFirst(T item);
    public void addLast(T item);
    public T removeFirst();
    public T removeLast();
    public boolean isEmpty();
    public int size ();
    public void printDeque();
    public T get(int index);
    public String getElements();
    public List<T> getElementsAsList();
}
