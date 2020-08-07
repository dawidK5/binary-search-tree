/**
 * <code>BinarySearchTreeTest</code> is a JUnit test class for BinarySearchTree (BST) and its subclass NodeObj.
 * @author  dawidK5
 * @version %I%, %G%
 */

package org.example.binarysearchtree;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;


public class BinarySearchTreeTest {

    String[] quoteArray = {"So-called", "natural", "language", "is", "wonderful", "for", "the", "purposes",
            "it", "was", "created", "for", "such", "as", "to", "be", "rude", "in", "to", "tell", "jokes", "in",
            "to", "cheat", "or", "to", "make", "love", "in", "but", "it", "is", "hopelessly", "inadequate",
            "when", "we", "have", "to", "deal", "unambiguously", "with", "situations", "of", "great",
            "intricacy", "situations", "which", "unavoidably", "arise", "in", "such", "activities", "as",
            "legislation", "arbitration", "mathematics", "or", "programming"}; // quote by E. W. Dijkstra

    String key = quoteArray[15]; // "be"
    String absentKey = "bank"; // "bank" is not present in quoteArray and thus it won't be present in bst1
    BinarySearchTree<String> bst1, bst2, bst3;
    BinarySearchTree<String>.NodeObj testNode;
    int i;

    int[] postOrderIndices = {54, 51, 48, 29, 23, 15, 13, 38, 10, 43, 36, 32, 44, 33, 17, 5, 20, 8, 3, 53, 27, 55, 26,
            2, 42, 57, 24, 41, 16, 19, 12, 7, 47, 39, 14, 35, 46, 40, 34, 9, 6, 4, 1, 0};
    int[] preOrderIndices = {0, 1, 2, 3, 5, 10, 13, 48, 51, 54, 15, 23, 29, 38, 17, 32, 36, 43, 33, 44, 8, 20, 26,
            27, 53, 55, 4, 6, 7, 24, 42, 57, 12, 16, 41, 19, 9, 14, 39, 47, 34, 35, 40, 46};
    int[] inOrderIndices = {0, 51, 54, 48, 13, 15, 29, 23, 10, 38, 5, 43, 36, 32, 17, 33, 44, 3, 8, 20, 2, 53, 27, 26,
            55, 1, 42, 24, 57, 7, 16, 41, 12, 19, 6, 14, 39, 47, 9, 35, 34, 46, 40, 4};

    /**
     * Makes a tree <code>bst1</code> where each word from the quote is a node in that tree. Finds a node
     * <code>testNode</code> with value "be". Initialises empty tree <code>bst2</code>. Loads correct value orderings in
     * form of indices for pre, in and post order of reading the values stored in the tree.
     */
    @BeforeEach
    void setUp() {
        bst1 = new BinarySearchTree<String>(quoteArray);
        testNode = bst1.getNode(key);
        bst2 = new BinarySearchTree<String>();
        bst3 = new BinarySearchTree<String>(key);
    }

    @AfterEach
    void tearDown() {
        bst1 = bst2 = bst3 = null;
        testNode = null;
    }


    @Test
        // Ensures BST class can obtain existing node of value "be" that is not null and imaginary node, which is null
    void testGetNode() {
        assertNotNull(testNode);
        assertNull(bst1.getNode(absentKey).getKey());
    }

    @Test
        // Ensures testNode has correct key
    void testInspectNode() { assertEquals(key, testNode.getKey()); }

    @Test
        // Asserts that root (first element added to the tree) can be returned
    void testGetRoot() {
        assertEquals(bst3.getRoot().getKey(), bst1.getNode(key).getKey());
        assertNull(bst2.getRoot().getKey());
    }

    @Test
        // Ensures bst1 has correct shape
    void testGetHeight() { assertEquals(10, bst1.getHeight()); }

    @Test
        // Ensures the node with value "be" has a right child, but no left child
    void testGetChild() {
        assertNull(testNode.getLeftChild().getKey());
        assertNotNull(testNode.getRightChild().getKey());
    }

    @Test
        // Asserts key of test node can be changed (testNode's key originally has value "be", "bank" afterwards)
    void testSetKey() {
        bst1.getNode(key).setKey(absentKey);
        assertEquals(absentKey, testNode.getKey());
    }

    @Test
        // Ensures non-balanced tree can shrink to become more compact and efficient
    void testRedrawBalanced() {
        bst1.redrawBalanced();
        assertEquals(6, bst1.getHeight());
    }

    @Test
        // Ensures only the unique values get added successfully to BST
    void testAdd() {
        assertTrue(bst2.add(absentKey));
        assertFalse(bst1.add(key));
    }

    @Test
        // Ensures that a node from bst2 can be added to bst1
    void testAddNode() {
        bst2.add(absentKey);
        bst1.addNode(bst2.getNode(absentKey));
        assertSame(bst1.getNode(key).getLeftChild(), bst2.getNode(absentKey));
    }

    @Test
        // Asserts trees are the same when input values for nodes are the same
    void testAddAllFromList() {
        bst2.addAllFromList(Arrays.asList(quoteArray));
        assertEquals(bst1.toString(), bst2.toString());
    }

    @Test
        // Ensures node values are considered present in a tree
    void testContains() {
        assertTrue(bst1.contains(key));
        assertFalse(bst1.contains(absentKey));
    }

    @Test
        // Asserts that a depth of a node in a tree can be found
    void testGetNodeDepth() {
        assertEquals(8, bst1.getNodeDepth(key));
    }

    @Test
        // Ensures parent nodes are found correctly
    void testGetParentNode() {
        assertSame(testNode, bst1.getParentNode(testNode.getRightChild()));
    }

    @Test
        // Asserts that values present in the tree can be removed
    void testRemove() {
        assertTrue(bst1.remove(key));
        assertFalse(bst1.remove(absentKey));
    }

    @Test
        // Ensures a single node can be removed from the tree
    void testRemoveNode() {
        assertEquals(key, bst1.removeNode(testNode));
        assertNull(bst1.getNode(key).getKey());
    }

    @Test
        // Asserts tree can be returned in form of pre-order of nodes. Makes sure values are ordered correctly.
    void testAsListPreOrder() {
        i=0;
        for (String aWord: bst1.asListPreOrder()) {
            assertTrue(aWord.equals(quoteArray[preOrderIndices[i]]));
            i++;
        }
    }

    @Test
        // Ensures a tree might be returned as a list with node values sorted in descending order
    void testAsListInOrder() {
        i=0;
        for (String aWord: bst1.asListInOrder()) {
            assertTrue(aWord.equals(quoteArray[inOrderIndices[i]]));
            i++;
        }
    }

    @Test
        // Ensures the values in a tree can be returned in post-order
    void testAsListPostOrder() {
        i=0;
        for (String aWord: bst1.asListPostOrder()) {
            assertEquals(aWord, quoteArray[postOrderIndices[i]]);
            i++;
        }
    }

    @Test
        // Ensures bst2 is empty and bst1 contains some nodes
    void testIsEmpty() {
        assertTrue(bst2.isEmpty());
        assertFalse(bst1.isEmpty());
    }
    @Test
        // Ensures all elements from a binary search tree can be added to a TreeSet
    void testToTreeSet() {
        assertEquals(bst1.size(), bst1.toTreeSet().size());
    }

    @Test
        // Asserts empty and non-empty binary search trees can be displayed in console without throwing exceptions
    void testToString() {
        assertNotNull(bst1.toString());
        assertNotNull(bst2.toString());
    }
}
