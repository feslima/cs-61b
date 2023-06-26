/**
 * Performs some basic linked list tests.
 */
public class ArrayDequeTest {

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

    public static String getElements(ArrayDeque ad) {
        String elements = "";
        int size = ad.size();
        if (size == 0) {
            return elements;
        }

        int counter = 0;
        while (counter < size) {
            String delimiter = " | ";
            if (counter == size - 1) {
                delimiter = "";
            }
            elements = elements + ad.get(counter).toString() + delimiter;
            counter += 1;
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
        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, ad1.size()) && passed;
        passed = checkEmpty(false, ad1.isEmpty()) && passed;
        passed = checkElements("front", getElements(ad1)) && passed;

        ad1.addLast("middle");
        passed = checkSize(2, ad1.size()) && passed;
        passed = checkElements("front | middle", getElements(ad1)) && passed;

        ad1.addLast("back");
        passed = checkSize(3, ad1.size()) && passed;
        passed = checkElements("front | middle | back", getElements(ad1)) && passed;

        System.out.println("Printing out deque: ");
        ad1.printDeque();

        printTestStatus(passed);
    }

    /**
     * Adds an item, then removes an item, and ensures that dll is empty afterwards.
     */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeFirst();
        // should be empty
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        ad1.addFirst(10);
        ad1.addFirst(20);
        ad1.addFirst(30);

        ad1.removeFirst();
        passed = checkElements("20 | 10", getElements(ad1)) && passed;

        ad1.removeLast();
        passed = checkElements("20", getElements(ad1)) && passed;

        ad1.removeLast();
        passed = checkElements("", getElements(ad1)) && passed;
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        printTestStatus(passed);
    }

    public static void getTest() {

        System.out.println("Running get test.");

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        boolean passed = checkIntEqual(null, ad1.get(0));

        ad1.addFirst(10);
        ad1.addFirst(20);
        ad1.addFirst(30);

        passed = checkIntEqual(30, ad1.get(0)) && passed;
        passed = checkIntEqual(10, ad1.get(2)) && passed;
        passed = checkIntEqual(20, ad1.get(1)) && passed;
        passed = checkIntEqual(null, ad1.get(4)) && passed;

        printTestStatus(passed);
    }

    public static void circularDemoTest() {
        // this test uses the values from the circular array deque slide
        System.out.println("Running circular array deque demo test.");

        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        ad1.addLast("a");
        ad1.addLast("b");
        ad1.addFirst("c");
        ad1.addLast("d");
        ad1.addLast("e");
        ad1.addFirst("f");
        ad1.addLast("g");
        ad1.addLast("h");

        boolean passed = checkElements("f | c | a | b | d | e | g | h", getElements(ad1));

        ad1.addLast("Z");

        passed = checkElements("f | c | a | b | d | e | g | h | Z", getElements(ad1)) && passed;

        ad1.removeLast();
        passed = checkElements("f | c | a | b | d | e | g | h", getElements(ad1)) && passed;

        printTestStatus(passed);
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        getTest();

        circularDemoTest();
    }
}
