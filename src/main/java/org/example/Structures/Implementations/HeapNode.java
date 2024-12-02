package org.example.Structures.Implementations;

public class HeapNode<T> extends BinaryTreeNode<T>
{
    protected HeapNode<T> parent;


    /**
     * Creates a new heap node with the specified data
     * @param obj data
     */
    HeapNode(T obj)
    {
        super(obj);
        parent = null;
    }
}