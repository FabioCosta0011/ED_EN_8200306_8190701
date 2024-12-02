package org.example.Structures.Interfaces;

public interface HeapADT<T> extends BinaryTreeADT<T>
{
    /** Adds the specified object to this heap. */
    public void addElement (T obj);

    /** Removes and returns a reference to the element with the
     lowest value in this heap. */
    public T removeMin();

    /** Removes the element with the lowest value in this heap. */
    public T findMin();
}