/**
 * Performs some basic linked list tests.
 */
public class LinkedListDequeTest {
    private static class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> prev;

        public Node(T value, Node<T> n, Node<T> p) {
            item = value;
            next = n;
            prev = p;
        }
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static boolean checkElements(String expected, String actual) {
        boolean result = expected.equals(actual);

        if (!result) {
            System.out.println("Actual elements: " + actual + ", but expected: " + expected);
        }

        return result;
    }

    public static boolean checkIntEqual(Integer expected, Integer actual) {
        boolean result = expected == null && actual == null || expected.equals(actual);

        if (!result) {
            System.out.println("Actual element: " + actual + ", but expected: " + expected);
        }

        return result;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    public static String getElements(LinkedListDeque lld) {
        String elements = "";
        if (lld.size() == 0) {
            return elements;
        }

        Object p = lld.get(0);
        int counter = 0;
        while (p != null) {
            String delimiter = " | ";
            if (counter == lld.size() - 1) {
                delimiter = "";
            }
            elements = elements + p.toString() + delimiter;
            counter += 1;
            p = lld.get(counter);
        }

        return elements;
    }

    /**
     * Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     * <p>
     * && is the "and" operation.
     */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, lld1.size()) && passed;
        passed = checkEmpty(false, lld1.isEmpty()) && passed;
        passed = checkElements("front", getElements(lld1)) && passed;

        lld1.addLast("middle");
        passed = checkSize(2, lld1.size()) && passed;
        passed = checkElements("front | middle", getElements(lld1)) && passed;

        lld1.addLast("back");
        passed = checkSize(3, lld1.size()) && passed;
        passed = checkElements("front | middle | back", getElements(lld1)) && passed;

        System.out.println("Printing out deque: ");
        lld1.printDeque();

        printTestStatus(passed);
    }

    /**
     * Adds an item, then removes an item, and ensures that dll is empty afterwards.
     */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.removeFirst();
        // should be empty
        passed = checkEmpty(true, lld1.isEmpty()) && passed;

        lld1.addFirst(10);
        lld1.addFirst(20);
        lld1.addFirst(30);

        lld1.removeFirst();
        passed = checkElements("20 | 10", getElements(lld1)) && passed;

        lld1.removeLast();
        passed = checkElements("20", getElements(lld1)) && passed;

        lld1.removeLast();
        passed = checkElements("", getElements(lld1)) && passed;
        passed = checkEmpty(true, lld1.isEmpty()) && passed;

        printTestStatus(passed);
    }

    public static void getTest() {

        System.out.println("Running get test.");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed = checkIntEqual(null, lld1.get(0));

        lld1.addFirst(10);
        lld1.addFirst(20);
        lld1.addFirst(30);

        // iterative get
        passed = checkIntEqual(30, lld1.get(0)) && passed;
        passed = checkIntEqual(10, lld1.get(2)) && passed;
        passed = checkIntEqual(20, lld1.get(1)) && passed;
        passed = checkIntEqual(null, lld1.get(4)) && passed;

        // recursive get
        passed = checkIntEqual(30, lld1.getRecursive(0)) && passed;
        passed = checkIntEqual(10, lld1.getRecursive(2)) && passed;
        passed = checkIntEqual(20, lld1.getRecursive(1)) && passed;
        passed = checkIntEqual(null, lld1.getRecursive(4)) && passed;

        printTestStatus(passed);
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        getTest();
    }
}
