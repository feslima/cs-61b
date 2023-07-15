package lab9;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }

        int cmp = p.key.compareTo(key);
        if (cmp == 0) {
            return p.value;
        }

        if (cmp > 0) {
            // this node key is greater than key
            return getHelper(key, p.left);
        }

        // this node key is lesser than key
        return getHelper(key, p.right);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        Node newNode = new Node(key, value);
        if (size == 0) {
            root = newNode;
            size += 1;
            return root;
        }

        int cmp = p.key.compareTo(key);
        if (cmp == 0) {
            p.value = value;
        }

        if (cmp > 0) {
            // this node key is greater than key
            if (p.left == null) {
                // ready for insertion
                p.left = newNode;
                size += 1;
                return newNode;
            }

            // keep looking
            return putHelper(key, value, p.left);
        }

        // this node key is lesser than key
        if (p.right == null) {
            // ready for insertion
            p.right = newNode;
            size += 1;
            return newNode;
        }

        // keep looking
        return putHelper(key, value, p.right);
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    private void inOrderTraversal(Node p, Set<K> keyset) {
        if (p == null) {
            return;
        }

        inOrderTraversal(p.left, keyset);
        keyset.add(p.key);
        inOrderTraversal(p.right, keyset);
    }

    @Override
    public Set<K> keySet() {
        Set<K> keyset = new HashSet<>();
        inOrderTraversal(root, keyset);
        return keyset;
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        Node removed = removeHelper(key, root, root);

        if (removed != null) {
            return removed.value;
        }
        return null;
    }

    private Node removeHelper(K key, Node p, Node parent) {
        if (p == null) {
            return null;
        }

        int cmp = p.key.compareTo(key);
        if (cmp > 0) {
            // this node key is greater than key
            return removeHelper(key, p.left, p);
        }

        if (cmp < 0) {
            // this node key is lesser than key
            return removeHelper(key, p.right, p);
        }

        size -= 1;

        Node toBeDeleted = new Node(p.key, p.value);
        // if the node being deleted has no children, simply delete it
        if (p.right == null && p.left == null) {
            if (parent.left.key.compareTo(p.key) == 0) {
                parent.left = null;
            }
            if (parent.right.key.compareTo(p.key) == 0) {
                parent.right = null;
            }
            return toBeDeleted;
        }

        /* if the node being deleted has one child, delete the node and plug the child in the spot where the
        deleted node was */
        if (p.right != null && p.left == null) {
            p.key = p.right.key;
            p.value = p.right.value;
            p.right = null;
            return toBeDeleted;
        }

        if (p.left != null && p.right == null) {
            p.key = p.left.key;
            p.value = p.left.value;
            p.left = null;
            return toBeDeleted;
        }

        // if the node has two children, replace the node with the successor node
        ArrayList<Node> successorAndParent = findSuccessorWithParent(p.right, p);
        Node successor = successorAndParent.get(0);
        Node successorParent = successorAndParent.get(1);
        p.key = successor.key;
        p.value = successor.value;
        if (successorParent.left != null && successorParent.left.key.compareTo(successor.key) == 0) {
            successorParent.left = null;
        }
        if (successorParent.right != null && successorParent.right.key.compareTo(successor.key) == 0) {
            successorParent.right = null;
        }
        /* if the successor node has a right child, after plugging the successor node into the spot of the deleted node,
         * take the former child of the successor node and turn into the left child of the former parent of the
         * successor node
         */
        if (successor.right != null) {
            successorParent.left = successor.right;
        }

        return toBeDeleted;
    }

    // Keep visiting the left child until there is no more child left, then return the childless node
    private ArrayList<Node> findSuccessorWithParent(Node p, Node parent) {
        if (p.left == null) {
            ArrayList<Node> result = new ArrayList<Node>();
            result.add(p);
            result.add(parent);
            return result;
        }

        return findSuccessorWithParent(p.left, p);
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
