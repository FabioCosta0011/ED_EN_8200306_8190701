package org.example.Structures.Implementations;


import org.example.Structures.Exceptions.ElementNotFoundException;
import org.example.Structures.Interfaces.BinaryTreeADT;

import java.util.Iterator;

public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {
    protected int count;
    protected BinaryTreeNode<T> root;

    /**
     * Creates an empty binary tree
     */
    public LinkedBinaryTree() {
        count = 0;
        root = null;
    }

    /**
     * Creates a binary tree with the specified element as its root
     * @param element root element
     */
    public LinkedBinaryTree(T element) {
        count = 1;
        root = new BinaryTreeNode<T>(element);
    }

    /**
     * Creates a binary tree from the two specified {@link LinkedBinaryTree binary trees}
     * @param element root element
     * @param leftSubtree left subtree
     * @param rightSubtree right subtree
     */
    public LinkedBinaryTree(T element, LinkedBinaryTree<T> leftSubtree,
                            LinkedBinaryTree<T> rightSubtree) {
        root = new BinaryTreeNode<T>(element);
        count = 1;

        if (leftSubtree != null) {
            count = count + leftSubtree.size();
            root.left = leftSubtree.root;
        } else
            root.left = null;

        if (rightSubtree != null) {
            count = count + rightSubtree.size();
            root.right = rightSubtree.root;
        } else
            root.right = null;
    }

    /**
     * Removes the left subtree of this binary tree
     */
    public void removeLeftSubtree() {
        if (root.left != null)
            count = count - root.left.numChildren() - 1;
        root.left = null;
    }

    /**
     * Removes the right subtree of this binary tree
     */
    public void removeRightSubtree() {
        if (root.right != null)
            count = count - root.right.numChildren() - 1;

        root.right = null;
    }

    /**
     * Removes all nodes from this binary tree
     */
    public void removeAllElements() {
        count = 0;
        root = null;
    }

    /**
     * Checks if this binary tree is empty
     * @return true if empty; false if not empty
     */
    public boolean isEmpty() {
        return (count == 0);
    }

    /**
     * Gets binary tree size
     * @return size
     */
    public int size() {
        return count;
    }


    /**
     * Checks if the binary tree contains a given element
     * @param targetElement target element
     * @return true if it contains; false if it doesn't contain
     */
    public boolean contains(T targetElement) {
        T temp;
        boolean found = false;

        try {
            temp = find(targetElement);
            found = true;
        } catch (Exception ElementNotFoundException) {
            found = false;
        }

        return found;
    }

    /**
     * Finds a given element in the binary tree
     * @param targetElement target element to be searched
     * @return element to be found
     * @throws ElementNotFoundException thrown if element not found
     */
    public T find(T targetElement) throws ElementNotFoundException {
        BinaryTreeNode<T> current = findAgain(targetElement, root);

        if (current == null)
            throw new ElementNotFoundException("binary tree");

        return (current.element);
    }


    /**
     * Finds a given element in the binary tree
     * @param targetElement target element to be searched
     * @param next next {@link BinaryTreeNode} to check
     * @return found element
     */
    private BinaryTreeNode<T> findAgain(T targetElement,
                                        BinaryTreeNode<T> next) {
        if (next == null)
            return null;

        if (next.element.equals(targetElement))
            return next;

        BinaryTreeNode<T> temp = findAgain(targetElement, next.left);

        if (temp == null)
            temp = findAgain(targetElement, next.right);

        return temp;
    }

    /**
     * Returns a string representation of the binary tree
     * @return {@link String}
     */
    public String toString() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        preorder(root, tempList);

        return tempList.toString();
    }


    /**
     * Performs an inorder traversal on the binary tree by calling an overloaded, recursive inorder method that starts
     * with the root
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        inorder(root, tempList);

        return tempList.iterator();
    }


    /**
     * Performs a recursive inorder traversal
     * @param node starting node
     * @param tempList {@link Iterator} list
     */
    protected void inorder(BinaryTreeNode<T> node,
                           ArrayUnorderedList<T> tempList) {
        if (node != null) {
            inorder(node.left, tempList);
            tempList.addToRear(node.element);
            inorder(node.right, tempList);
        }
    }

    /**
     * Performs a preorder traversal on the binary tree by calling an overloaded, recursive preorder method that starts
     * with the root
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        preorder(root, tempList);

        return tempList.iterator();
    }


    /**
     * Performs a recursive preorder traversal
     * @param node starting node
     * @param tempList {@link Iterator} list
     */
    protected void preorder(BinaryTreeNode<T> node,
                            ArrayUnorderedList<T> tempList) {
        if (node != null) {
            tempList.addToRear(node.element);
            preorder(node.left, tempList);
            preorder(node.right, tempList);
        }
    }

    /**
     * Performs a postorder traversal on the binary tree by calling an overloaded, recursive postorder method that
     * starts with the root
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        postorder(root, tempList);

        return tempList.iterator();
    }

    /**
     * Performs a recursive postorder traversal
     * @param node starting node
     * @param tempList {@link Iterator} list
     */
    protected void postorder(BinaryTreeNode<T> node,
                             ArrayUnorderedList<T> tempList) {
        if (node != null) {
            postorder(node.left, tempList);
            postorder(node.right, tempList);
            tempList.addToRear(node.element);
        }
    }

    /**
     * Performs a levelorder traversal on the binary tree, using a {@link ArrayUnorderedList tempList}
     * @return {@link Iterator}
     */
    public Iterator<T> iteratorLevelOrder() {
        ArrayUnorderedList<BinaryTreeNode<T>> nodes =
                new ArrayUnorderedList<BinaryTreeNode<T>>();
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        BinaryTreeNode<T> current;

        nodes.addToRear(root);

        while (!nodes.isEmpty()) {
            current = (BinaryTreeNode<T>) (nodes.removeFirst());

            if (current != null) {
                tempList.addToRear(current.element);
                if (current.left != null)
                    nodes.addToRear(current.left);
                if (current.right != null)
                    nodes.addToRear(current.right);
            } else
                tempList.addToRear(null);
        }

        return tempList.iterator();
    }
}