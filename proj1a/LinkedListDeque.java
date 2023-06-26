public class LinkedListDeque<T> {
    public static class Node<T> {
        public T item;
        public Node<T> next;
        public Node<T> prev;

        public Node(T value, Node<T> n, Node<T> p) {
            item = value;
            next = n;
            prev = p;
        }
    }

    private int size;
    private Node<T> sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node<T>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T item) {
        Node<T> currentFirst = sentinel.next;
        Node<T> node = new Node<T>(item, currentFirst, sentinel);
        sentinel.next = node;
        currentFirst.prev = node;
        size += 1;
    }

    public void addLast(T item) {
        Node<T> currentLast = sentinel.prev;
        Node<T> node = new Node<T>(item, sentinel, currentLast);
        currentLast.next = node;
        sentinel.prev = node;
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        Node<T> currentFirst = sentinel.next;
        Node<T> newFirst = currentFirst.next;
        sentinel.next = newFirst;
        newFirst.prev = sentinel;
        size -= 1;
        return currentFirst.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        Node<T> currentLast = sentinel.prev;
        Node<T> newLast = currentLast.prev;
        sentinel.prev = newLast;
        newLast.next = sentinel;
        size -= 1;
        return currentLast.item;
    }

    public T get(int index) {
        T result = null;
        if (size == 0) {
            return result;
        }

        Node<T> p = sentinel.next;
        int counter = 0;
        while (p.item != null) {
            if (counter == index) {
                result = p.item;
            }
            p = p.next;
            counter += 1;
        }

        return result;
    }

    private T getRecursive(Node<T> node, int index, int counter) {
        if (index == counter) {
            return node.item;
        }
        if (counter > index) {
            return null;
        }

        return getRecursive(node.next, index, counter + 1);
    }

    public T getRecursive(int index) {
        if (size == 0 || index > size) {
            return null;
        }

        return getRecursive(sentinel.next, index, 0);

    }

    public void printDeque() {
        Node<T> p = sentinel.next;
        while (p.item != null) {
            System.out.println(p.item);
            p = p.next;
        }
    }

    public String getElements() {
        String elements = "";
        if (size == 0) {
            return elements;
        }

        Node<T> p = sentinel.next;
        int counter = 0;
        while (p.item != null) {
            String delimiter = " | ";
            if (counter == size - 1) {
                delimiter = "";
            }
            elements = elements + p.item.toString() + delimiter;
            counter += 1;
            p = p.next;
        }

        return elements;
    }
}
