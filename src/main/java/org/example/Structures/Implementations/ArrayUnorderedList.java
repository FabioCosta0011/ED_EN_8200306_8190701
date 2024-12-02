package org.example.Structures.Implementations;


import org.example.Structures.Exceptions.ElementNotFoundException;
import org.example.Structures.Interfaces.UnorderedListADT;

/**
 * This class implements an unordered list using an array structure.
 * It allows adding elements to the front, rear, or after a specific target
 * element.
 *
 * @param <T> the type of elements stored in this list
 */
public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    /**
     * Creates an empty unordered list with the default capacity.
     */
    public ArrayUnorderedList() {
        super();
    }

    /**
     * Creates an empty unordered list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public ArrayUnorderedList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Adds the specified element to the front of this list.
     * Expands the list capacity if necessary.
     *
     * @param element the element to be added to the front of the list
     */
    @Override
    public void addToFront(T element) {
        // Check if capacity needs to be expanded
        if (size() == list.length) {
            expandCapacity();
        }

        // Shift all elements to the right to make space at the front
        for (int scan = rear; scan > 0; scan--) {
            list[scan] = list[scan - 1];
        }

        // Place the new element at the front
        list[0] = element;
        rear++;
    }

    /**
     * Adds the specified element to the rear of this list.
     * Expands the list capacity if necessary.
     *
     * @param element the element to be added to the rear of the list
     */
    @Override
    public void addToRear(T element) {
        // Check if capacity needs to be expanded
        if (size() == list.length) {
            expandCapacity();
        }

        // Place the new element at the rear of the list
        list[rear] = element;
        rear++;
    }

    /**
     * Adds the specified element after the specified target element in this list.
     * Expands the list capacity if necessary. Throws an ElementNotFoundException if
     * the target element is not found.
     *
     * @param element the element to be added after the target
     * @param target  the element after which the new element will be added
     * @throws ElementNotFoundException if the target element is not found in the
     *                                  list
     */
    @Override
    public void addAfter(T element, T target) {
        // Check if capacity needs to be expanded
        if (size() == list.length) {
            expandCapacity();
        }

        // Find the index of the target element
        int scan = 0;
        while (scan < rear && !target.equals(list[scan])) {
            scan++;
        }

        // If target is not found, throw exception
        if (scan == rear) {
            throw new ElementNotFoundException("list");
        }

        // Shift elements to make room for the new element
        scan++; // Insert after the target element
        for (int scan2 = rear; scan2 > scan; scan2--) {
            list[scan2] = list[scan2 - 1];
        }

        // Insert the new element
        list[scan] = element;
        rear++;
    }

    /**
     * Returns the element at the specified index in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified index in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T getElement(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        return list[index];
    }
}
