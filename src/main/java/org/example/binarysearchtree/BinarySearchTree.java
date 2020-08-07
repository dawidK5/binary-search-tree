/**
 * <code>BinarySearchTree</code> (also called BST) is a class that generates a binary tree for storing values of a
 * specific <code>Comparable</code> type T. Adding nodes creates multiple instances of inner class called NodeObj.
 * This class (BST) bears some resemblance to the Java <code>TreeSet</code>, but this BST does not self-balance and can
 * ONLY be balanced using the method provided and this class is NOT 'thread-safe'.
 * BST can be exported as: a list (pre/in/post -order) or a TreeSet. Basic functionality of public methods includes
 * adding and removing values and nodes/subtrees from a different BST, getting parent nodes, keys, adding values from
 * arrays, lists, getting tree height and more.
 *
 * @author dawidK5
 * @version %I%, %G%
 *
 */
package org.example.binarysearchtree;

import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;



public class BinarySearchTree<T extends Comparable<T>> {
    private NodeObj root; // root node

    private int temp; // temporary value placeholder for comparison
    private final HashSet<T> allNodeKeys; // list of all unique node keys
    private String[] answer; // array for displaying node key values in rows
    private T DEFAULT_EMPTY_T;  // stores default object value e.g. null for string
    private final NodeObj EMPTY_NODE = new NodeObj(); // empty node for null returns

    /**
     * Class constructor
     */
    BinarySearchTree() {
        this.root = new NodeObj();
        this.allNodeKeys = new HashSet<T>(10);
    }

    /**
     * Class constructor specifying the root node value/key
     * @param item          the value of type T that root key will be set to
     */
    BinarySearchTree (T item) {
        this.root = new NodeObj(item);
        this.allNodeKeys = new HashSet<T>(10);
        this.allNodeKeys.add(root.key);
    }

    /**
     * Class constructor
     * @param itemsArray    the array of values to be added to the tree
     */
    BinarySearchTree (T[] itemsArray) {
        this.root = new NodeObj();
        this.allNodeKeys = new HashSet<T>(itemsArray.length);
        this.allNodeKeys.add(root.key);
        addAllFromArray(itemsArray);
    }

    /**
     * NodeObj is an inner class of <code>BinarySearchTree</code> which allows to create a new node every time a
     * value is added to this binary search tree. Each instance contains 2 pointers: left and right, and a key of
     * type T.
     */
    class NodeObj {
        NodeObj left, right;
        T key;

        /**
         * Class constructor specifying node key
         * @param key       the value of type T that will be added to the node
         */
        private NodeObj (T key) {
            this.key = key;
            left = null;
            right = null;
        }

        /**
         * Class constructor used when key is not known yet
         */
        private NodeObj () {
            this.key = getDefaultValue(); // null for generic type T
            left = null;
            right = null;
        }

        /**
         * @return          safe <code>null</code> if left child is missing or object doesn't exists;
         *                  left child node or a left subtree otherwise
         */
        public NodeObj getLeftChild() {
            return ((this==null || this.left == null) ? EMPTY_NODE : this.left);
        }

        /**
         * @return          safe <code>null</code> if right child is missing or object doesn't exists;
         *                  right child node or a right subtree otherwise
         */
        public NodeObj getRightChild() {
            return ((this==null || this.right == null) ? EMPTY_NODE : this.right);
        }

        /**
         * @return          safe <code>null</code> if node has no key;
         *                  node key of type T otherwise
         */
        public T getKey() {
            return ((this != null) ? this.key : EMPTY_NODE.key);
        }

        /**
         * Sets the value of current key to the value specified and updates the list of all keys in this BST
         */
        public void setKey(T key) {
            allNodeKeys.remove(this.key);
            this.key = key;
            allNodeKeys.add(key);
        }
    }

    /**
     * Returns the root of this binary search tree (BST) as an object
     * @return              the root of this tree as NodeObj
     */
    public NodeObj getRoot() {
        return this.root;
    }

    /**
     * Returns the number of active, non-empty nodes in this tree
     * @return              the int signifying the number of values in this BST
     */
    public synchronized int size() {
        int i = 0;
        for (T element : allNodeKeys) {
            if (element != null) {
                i++;
            }
        }
        return i;
    }

    /**
     * Recursive helper for adding nodes with redrawBalanced method
     * @param ordKeys       the list of in-order values
     * @param left          the starting index position
     * @param right         the end index position
     * @see                 #redrawBalanced()
     */
    private void recurAdd(List<T> ordKeys, int left, int right) {
        int mid = (left+right)/2;
        add(ordKeys.get(mid));
        if (left < right) {
            recurAdd(ordKeys, left, mid-1);
            recurAdd(ordKeys, mid+1, right);
        }
    }

    /**
     * This method retrieves all values in the tree as an ordered list, deletes the root node and reinserts the
     * values to create a balanced tree.
     */
    public void redrawBalanced() {
        List<T> ordKeys = this.asListInOrder();
        System.out.printf("Values that will be reloaded : %s .\n", ordKeys.toString());
        System.out.printf("Current height: %d .\n", getHeight() );
        root.key = null;
        root.left = null;
        root.right = null;
        root = null;
        allNodeKeys.clear();
        root = new NodeObj();
        recurAdd(ordKeys, 0, ordKeys.size()-1);
        System.out.println(toString());
        System.out.printf("New height: %d .\n", getHeight() );
    }

    /**
     * Adds a link from appropriate node/subtree in this tree to to the node given (also from another another tree).
     * If root node value is null, it replaces this root pointer.
     * @param myNode    the NodeObj to be added to the tree
     * @return          <code>null</code> if node given is empty or if the tree contains a node with the same key;
     *                  myNode's key otherwise
     */
    public T addNode(NodeObj myNode) { // adds a node or a subtree
        T targetKey = myNode.key;
        if (this.root.key == null) {
            this.root = myNode;
            this.root.key = myNode.key;
            this.allNodeKeys.add(targetKey);
            return targetKey;
        }
        if (contains(targetKey)) {
            System.out.printf("Node with value %s already exists. If you want to merge with an already"
                            +" balanced subtree, pass both child nodes to this method instead.\n",
                    (targetKey!=null ? targetKey.toString() : "null"));
            return null;
        }
        int difference;
        NodeObj aNode = this.root;
        boolean flag = true;
        // find position for node to be placed
        while (flag) {
            T currKey = aNode.key;
            difference = targetKey.compareTo(currKey); // uses default compareTo ordering
            if (difference < 0) {
                if (aNode.left == null) {
                    aNode.left = myNode;
                    flag = false;
                } else {
                    aNode = aNode.left;
                }
            } else if (difference > 0) {
                if (aNode.right == null) {
                    aNode.right = myNode;
                    flag = false;
                } else {
                    aNode = aNode.right;
                }
            }
        }
        // aNode is now the parent node
        // add possible subtree keys to all keylist
        recurKeyPreOrd(myNode, myNode);
        return targetKey;
    }

    /**
     * Recursive helper method to obtain myNode's subtree key values and add them to the list of keys
     * @param myNode        the NodeObj whose key and its children's keys will be added to list of keys
     * @param subtreeTop    the NodeObj that represent the top of the subtree to be merged with this BST
     * @see                 #addNode(NodeObj)
     */
    private void recurKeyPreOrd(NodeObj myNode, NodeObj subtreeTop) {
        T key = myNode.getKey();
        if (key != null) {
            if (contains(key)) {
                removeNode(myNode, iterGetParent(myNode, subtreeTop));
            }
            this.allNodeKeys.add(myNode.getKey());
        }

        if (myNode.getLeftChild().getKey() != null) {
            recurKeyPreOrd(myNode.left, myNode);
        }
        if (myNode.getRightChild().getKey() != null) {
            recurKeyPreOrd(myNode.right, myNode);
        }
    }

    /**
     * Adds a value of type T to the binary search tree by creating a new NodeObj. If root's key is empty/null,
     * the key is replaced with the value and a short message is printed to the console.
     * @param word      the value of type T to be added to the tree
     * @return          <code>true</code> if a new unique value was added successfully;
     *                  <code>false</code> otherwise
     */
    public boolean add(T word) {
        if (!contains(word) && !(root.key==null)) {
            checkAndPlace(root, word);
            return true;
        } else if (root.key==null) {
            System.out.printf("The key of the root node will be set to %s . \n",
                    (word!=null ? word.toString() : "null"));
            root.key = word;
            this.allNodeKeys.add(word);
            return true;
        }
        return false;
    }

    /**
     * Uses <code>add</code> method to insert all value from this array to the binary search tree. If the root key
     * has no value, the first value in the array is used to replace it and then other elements are added.
     * @param keyArray      the array of type T with values/keys to be inserted to the tree
     */
    public void addAllFromArray(T[] keyArray) {
        int i=0;
        if (this.root.key == null) {
            this.root.key = keyArray[0];
            this.allNodeKeys.add(root.key);
            i++;
        }
        while (i < keyArray.length) {
            add(keyArray[i]);
            i++;
        }
    }

    /**
     * Uses <code>add</code> method to insert all values from a list to the binary search tree. If the root key is
     * null, the first element of the list is assigned to the root and then all other elements are added.
     * @param myList        the list storing values of type T to be added to this BST
     */
    public void addAllFromList(List<T> myList) {
        int i=0;
        if (this.root.key == null) {
            this.root.key = myList.get(0);
            this.allNodeKeys.add(root.key);
            i++;
        }
        while (i < myList.size()) {
            add(myList.get(i));
            i++;
        }
    }

    /**
     * Checks if this tree contains the value given
     * @param word          the value to be checked for
     * @return              <code>true</code> if this BST contains a node with key with value of <code>word</code>;
     *                      <code>false</code> if there is no such value in the tree
     */
    public boolean contains(T word) {
        return this.allNodeKeys.contains(word);
    }

    /**
     * Helper method for safe return of default or <code>null</code> value for type T
     * @return              <code>null</code> or other default value used for empty keys
     */
    private T getDefaultValue() {
        return DEFAULT_EMPTY_T;
    }

    /**
     * Compares given value with subsequent node keys, starting at the root, finding a node with the same value
     * using natural ordering of type T
     * @param targetKey     the T-type value for the matching node/subtree to be found
     * @return              the NodeObj node or a subtree(if children are present); empty node if value not present
     */
    public NodeObj getNode(T targetKey) { // returns a node or a subtree
        if (!contains(targetKey) || targetKey==null) {
            System.out.printf("Node for the value \" %s \" not found, empty node has been returned. \n",
                    (targetKey!=null ? targetKey.toString() : "null"));
            return EMPTY_NODE; // Node has not been found in the set, the key is invalid
        }
        int difference=1;
        NodeObj aNode = this.root;
        while (difference!=0) {
            T currKey = aNode.key;
            difference = targetKey.compareTo(currKey); // uses default compareTo type ordering
            if (difference < 0) {
                aNode = aNode.left;
            } else if (difference > 0) {
                aNode = aNode.right;
            }
        }
        return aNode;
    }

    /**
     * Overloaded method that finds how deep a node resides in this BST
     * @param myNode        the NodeObj (depth of which we need)
     * @return              -1 if node is <code>null</code> or is not in this this tree; depth in range between 1
     *                      and BST's height otherwise
     */
    public int getNodeDepth(NodeObj myNode) {
        return getNodeDepth(myNode.getKey());
    }

    /**
     * Returns the level at which the specified value of type T resides in a node in this tree
     * @param targetKey     the node key of type T
     * @return              -1 if value is <code>null</code> or is not in this this tree; depth in range between 1
     *                      and BST's height otherwise
     */
    public int getNodeDepth(T targetKey) {
        if (!contains(targetKey) || targetKey==null) {
            System.out.printf("Node for the value \" %s \" not found.\n",
                    (targetKey!=null ? targetKey.toString() : "null"));
            return -1; // Node has not been found in the set, the key is invalid
        }
        int nodeHeight = 0;
        int difference = 1;
        NodeObj aNode = this.root;
        while (difference!=0) {
            T currKey = aNode.key;
            difference = targetKey.compareTo(currKey); // uses default compareTo type ordering
            if (difference < 0) {
                aNode = aNode.left;
            } else if (difference > 0) {
                aNode = aNode.right;
            }
            nodeHeight++;
        }
        return nodeHeight;
    }
    /**
     * Obtains parent node of the node given in that tree
     * @param myNode        the NodeObj of which we want to find parent in this BST
     * @return              the parent of the NodeObj given; empty node if myNode is not present in this tree
     */
    public NodeObj getParentNode(NodeObj myNode) {
        return iterGetParent(myNode, this.root);
    }

    /**
     * Helper method which finds the parent node iteratively by node key comparisons, starting with a specific root.
     * This method is also used for removing duplicated values when joining subtrees to this BST
     * @param myNode        the NodeObj of which we want to find parent in this BST
     * @param myRoot        the first node to be checked for being the parent;
     * @return              the parent of the NodeObj given; empty node if myNode is not present in this tree
     * @see                 #addNode(NodeObj)
     */
    private NodeObj iterGetParent(NodeObj myNode, NodeObj myRoot) {
        T targetKey = myNode.key;
        int difference=1; // lexicographical difference
        NodeObj aNode = myRoot;
        if (aNode.key.equals(myNode.key) || targetKey == null) {
            System.out.printf("Node with value %s is empty or has no parent in this tree.\n",
                    (targetKey!=null ? targetKey.toString() : "null"));
            return EMPTY_NODE;
        }
        while (! ( ((aNode.getLeftChild().getKey() != null) && aNode.getLeftChild().getKey().equals(targetKey)) ||
                ((aNode.getRightChild().getKey() != null) && aNode.getRightChild().getKey().equals(targetKey))) ) {
            difference = targetKey.compareTo(aNode.key);
            if (difference > 0) {
                aNode = aNode.right;
            } else {
                aNode = aNode.left;
            }
        }
        return aNode;
    }

    /**
     * Gets the height of the tree measured by nodes (root with non-empty key has height 1)
     * @return          height of this tree as an int
     */
    public int getHeight() {
        if (root==null) {
            return 0;
        }
        // 2 'if' blocks to avoid null pointer exception
        if (root.key == null) {
            return 0;
        }
        return recurCheckDepth(root, 1);
    }


    /**
     * Calls <code>removeNode</code> method uses the value given to find and pass in the node to be removed
     * @param value     the key value of type T of the node to be removed
     * @return          <code>true</code> if the node existed before and was removed successfully;
     *                  <code>false</code> otherwise
     */
    public boolean remove(T value) {
        return removeNode(getNode(value)) != null;
    }


    /**
     * Deletes parent nodes' pointers leading to myNode from the tree and sets this node's left, right and
     * key values to null. Replaces the pointers with different nodes if myNode has children
     * @param myNode    the NodeObj to be removed
     * @return          the node key of type T if removed successfully;
     *                  <code>null</code> if this node is not present in this tree
     * @see             #removeNode(NodeObj, NodeObj)
     */
    public T removeNode(NodeObj myNode) {
        if (!contains(myNode.key)) {
            return null;
        }
        return removeNode(myNode, iterGetParent(myNode, root));
    }

    /**
     * Helper method to remove the node given relative to its specified parent.
     *
     * @param myNode        the NodeObj to be removed
     * @param parentNode    the parent NodeObj of myNode
     * @return              the node key of type T if removed successfully;
     *                      <code>null</code> if this node is not present in this tree
     */
    private T removeNode(NodeObj myNode, NodeObj parentNode) {
        boolean leftOfParent;
        T nodeKey = myNode.key;
        int level = getNodeDepth(nodeKey);
        if (level == -1 || nodeKey == null) {
            System.out.printf("Node with value %s not present in this tree.\n",
                    (nodeKey!=null ? nodeKey.toString() : "null"));
            return null;
        }

        leftOfParent = myNode.key.compareTo(parentNode.key) < 0;
        // 3 cases: leaf, 1 child, 2 children
        // 1. myNode is a leaf
        if (myNode.left==null && myNode.right==null) { // if it is a leaf
            myNode.key = null;

            myNode = null;
            if (leftOfParent) {
                parentNode.left = null;
            } else {
                parentNode.right = null;
            }
            allNodeKeys.remove(nodeKey);
            return nodeKey;
        }
        // 2. myNode has 1 child
        if (myNode.left==null) {
            if (leftOfParent) {
                parentNode.left = myNode.right;
            } else {
                parentNode.right = myNode.right;
            }
            myNode=null;
            allNodeKeys.remove(nodeKey);
            return nodeKey;
        } else if (myNode.right==null) {
            if (leftOfParent) {
                parentNode.left = myNode.left;
            } else {
                parentNode.right = myNode.left;
            }
            myNode = null;
            allNodeKeys.remove(nodeKey);
            return nodeKey;
        }
        // 3. myNode has 2 children
        // swap the value to be removed with the smallest value in the right subtree
        NodeObj tempNode = myNode.right;
        while (tempNode.left != null) {
            tempNode = tempNode.left;
        }
        allNodeKeys.remove(nodeKey);
        myNode.key = tempNode.key;
        tempNode.key = null;
        tempNode.right = null;
        tempNode.left = null;
        getParentNode(myNode).left = null;
        return nodeKey;
    }

    /**
     * Recursive helper method for finding how deep is the deepest node in a tree or a subtree
     * @param aNode             the NodeObj of known depth in this BST
     * @param deepestHeight     the integer for the depth of aNode
     * @return                  the depth of the lowest leaf in this subtree
     * @see                     #getHeight()
     * @see                     #getNodeDepth(Comparable)
     */
    private int recurCheckDepth (NodeObj aNode, int deepestHeight) {
        if (aNode.left==null && aNode.right==null) {
            return deepestHeight;
        }
        if (aNode.left != null) {
            if (aNode.right != null) {
                return Math.max(recurCheckDepth(aNode.left, deepestHeight + 1), recurCheckDepth(aNode.right, deepestHeight + 1));
            }
            return recurCheckDepth(aNode.left, deepestHeight + 1);
        }
        return recurCheckDepth(aNode.right, deepestHeight + 1);
    }

    /**
     * Recursive helper method for <code>add</code> method. Compares the node keys to find the right position in
     * the tree. Places a new node and increments tree height if needed
     * @param currNodeObj       the node of which the key needs to be compared to find correct position
     * @param word              the value of type T to be used to initialise a new NodeObj
     * @see                     #add(Comparable)
     */
    private void checkAndPlace(NodeObj currNodeObj, T word) {
        temp = word.compareTo(currNodeObj.key);
        if (temp > 0) {
            if(currNodeObj.right==null) {
                currNodeObj.right = new NodeObj(word);
                this.allNodeKeys.add(currNodeObj.right.key);
            } else {
                checkAndPlace(currNodeObj.right, word);
            }
        } else {
            if(currNodeObj.left==null) {
                currNodeObj.left = new NodeObj(word);
                this.allNodeKeys.add(currNodeObj.left.key);
            } else {
                checkAndPlace(currNodeObj.left, word);
            }
        }
    }

    /**
     * Traverses through all nodes in this BST and returns an ArrayList of all keys in pre-order
     * @return              the <code>ArrayList</code> of all values in this tree in pre-oder (root, left, right)
     */
    public List<T> asListPreOrder() { // returns an ArrayList containing
        List<T> preOrdList = new ArrayList<T>();
        recurPreOrder(root, preOrdList);
        return preOrdList;
    }

    /**
     * Recursive helper function which adds the node values to the list in pre-order
     * @param myNode        the next NodeObj of which the get gets added to the list
     * @param preOrdList    the list that will contain the all node values in pre-order
     * @see                 #asListPreOrder()
     */
    private void recurPreOrder(NodeObj myNode, List<T> preOrdList) {
        preOrdList.add(myNode.key);
        if (myNode.getLeftChild().getKey() != null) {
            recurPreOrder(myNode.left, preOrdList);
        }
        if (myNode.getRightChild().getKey() != null) {
            recurPreOrder(myNode.right, preOrdList);
        }
    }

    /**
     * Traverses through all nodes in this BST and returns an ArrayList of all keys in natural order (in-order)
     * @return              the <code>ArrayList</code> of all values in this tree in in-order (left, root, right)
     */
    public List<T> asListInOrder() {
        List<T> inOrdList = new ArrayList<T>();
        recurInOrder(root, inOrdList);
        return inOrdList;
    }

    /**
     * Recursive helper function which adds the node values to the list in natural, ascending order (in-order)
     * @param myNode        the next NodeObj of which the key gets added to the list
     * @param inOrdList     the list that will contain the all node values in ascending (in-order)
     * @see                 #asListInOrder()
     */
    private void recurInOrder(NodeObj myNode, List<T> inOrdList) {
        if (myNode.getLeftChild().getKey() != null) {
            recurInOrder(myNode.left, inOrdList);
        }
        inOrdList.add(myNode.key);
        if (myNode.getRightChild().getKey() != null) {
            recurInOrder(myNode.right, inOrdList);
        }
    }

    /**
     * Traverses through all nodes in this BST and returns an ArrayList of all keys in post-order
     * @return              the <code>ArrayList</code> of all values in this tree in post-order (left, right, root)
     */
    public List<T> asListPostOrder() {
        List<T> postOrdList = new ArrayList<T>();
        recurPostOrder(root, postOrdList);
        return postOrdList;
    }

    /**
     * Recursive helper function which adds the node values to the list in post-order
     * @param myNode        the next NodeObj of which the key gets added to the list
     * @param postOrdList   the list that will contain the all node values in post-order
     * @see                 #asListPostOrder()
     */
    private void recurPostOrder(NodeObj myNode, List<T> postOrdList) {
        if (myNode.getLeftChild().getKey() != null) {
            recurPostOrder(myNode.left, postOrdList);
        }
        if (myNode.getRightChild().getKey() != null) {
            recurPostOrder(myNode.right, postOrdList);
        }
        postOrdList.add(myNode.key);
    }

    /**
     * Adds all node values in this tree to a <code>TreeSet</code> of type T using an <code>Iterator</code>data
     * and returns that TreeSet
     * @return              the TreeSet of type T containing all keys from all nodes in this BST
     */
    public TreeSet<T> toTreeSet() {
        int i = 0;
        T word;
        List<T> allNodes = new ArrayList<T>(allNodeKeys);
        TreeSet<T> myTree = new TreeSet<T>();
        while (i < allNodes.size()) {
            word = allNodes.get(i);
            if (word != null) {
                myTree.add(word);
            }
            i++;
        }
        return myTree;
    }

    /**
     * Tells whether this tree has any values/nodes
     * @return              <code>true</code> if root is <code>null</code> (thus the rest of the tree must be null);
     *                      <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return root.key==getDefaultValue();
    }

    /**
     * Recursive helper function for displaying the BST in the console. Obtains all node keys and place them in
     * <code>answer</code> String array. Add 'l:' before left child value, 'r:' before right and '-' where there
     * are no children
     * @param myNode        the NodeObj node or subtree to extract the key from to be added to String array
     * @param level         the int representing node's row in the String array (NOT height)
     * @see                 #toString()
     */
    private void recursiveToString(NodeObj myNode, int level) {
        if (myNode.left!=null) {
            answer[level] += "l:"+myNode.left.key+"\t";
            recursiveToString(myNode.left, level+1);
        } else {
            answer[level] += "-\t";
        }
        if (myNode.right!=null) {
            answer[level] += "r:"+myNode.right.key+"\t";
            recursiveToString(myNode.right, level+1);
        } else {
            answer[level] += "-\t";
        }
    }

    /**
     * Converts the values in the BST nodes to a multi-level String. Such String can be displayed in console using
     * <code>System.out.println()</code>. Adds 'l:' before left child value, 'r:' before right and '-' where there
     * are no children. The hyphens appear in left to right order(same as parents) and they only displayed under
     * to non-empty nodes
     * @return              the String containing visual, human readable representation of a Binary Search Tree
     */
    public String toString() {
        String sResult;
        answer = new String[ allNodeKeys.size()+2 ];
        for (int i=0; i<answer.length; i++) {
            answer[i] = "";
        }
        answer[0] = root.key+"\t";
        recursiveToString(root, 1);
        sResult = Arrays.toString(answer);
        sResult = sResult.substring(0, sResult.lastIndexOf("\t")).replace(",", "\n")+"]";
        return sResult;
    }
}


