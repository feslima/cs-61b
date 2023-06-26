public class ArrayDeque<T> {
    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;
    private int capacity;

    public ArrayDeque() {
        size = 0;
        capacity = 8;

        items = (T[]) new Object[capacity];
        nextFirst = 0; // moves to the left (decrements) on addFirst
        nextLast = nextFirst + 1; // moves to the right (increments) on addLast
    }

    private void copyArray(T[] oldItems, T[] newItems) {
        int counter = 0;
        int pointer = (nextFirst + 1) % oldItems.length;
        while (counter < size) {
            newItems[counter] = oldItems[pointer];
            counter += 1;
            pointer = (pointer + 1) % oldItems.length;
        }
    }

    private void expand() {
        capacity = capacity * 2;
        T[] oldItems = items;
        items = (T[]) new Object[capacity];

        copyArray(oldItems, items);

        nextFirst = items.length - 1;
        nextLast = size;
    }

    private void downsize() {
        capacity = capacity / 2;
        T[] oldItems = items;
        items = (T[]) new Object[capacity];

        copyArray(oldItems, items);

        nextFirst = items.length - 1;
        nextLast = size;
    }

    private double getUsageRatioThreshold() {
        return 0.25;
    }

    private double getUsageRatio() {
        return size * 1.0 / items.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public T get(int index) {
        if (size == 0) {
            return null;
        }
        int previousFirst = (nextFirst + 1) % items.length;
        int readIndex = (previousFirst + index) % items.length;
        return items[readIndex];
    }

    public void addFirst(T item) {
        if (size == items.length) {
            expand();
        }

        items[nextFirst] = item;
        nextFirst -= 1;

        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }

        size += 1;
    }

    public void addLast(T item) {
        if (size == items.length) {
            expand();
        }

        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;

        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        int previousFirst = (nextFirst + 1) % items.length;
        T removedItem = items[previousFirst];
        items[previousFirst] = null;
        nextFirst = previousFirst;

        size -= 1;

        if (getUsageRatio() <= getUsageRatioThreshold()) {
            downsize();
        }

        return removedItem;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        int previousLast = nextLast - 1;
        if (previousLast < 0) {
            previousLast = items.length - 1;
        }

        T removedItem = items[previousLast];
        items[previousLast] = null;
        nextLast = previousLast;

        size -= 1;

        if (getUsageRatio() <= getUsageRatioThreshold()) {
            downsize();
        }

        return removedItem;
    }

    public String getElements() {
        String elements = "";
        if (size == 0) {
            return elements;
        }

        int counter = 0;
        int pointer = (nextFirst + 1) % items.length;
        while (counter < size) {
            String delimiter = " | ";
            if (counter == size - 1) {
                delimiter = "";
            }
            elements = elements + items[pointer].toString() + delimiter;
            counter += 1;
            pointer = (pointer + 1) % items.length;
        }

        return elements;
    }

    public void printDeque() {
        if (size == 0) {
            return;
        }

        int counter = 0;
        int pointer = (nextFirst + 1) % items.length;
        while (counter < size) {
            System.out.println(items[pointer].toString());
            counter += 1;
            pointer = (pointer + 1) % items.length;
        }
    }
}
