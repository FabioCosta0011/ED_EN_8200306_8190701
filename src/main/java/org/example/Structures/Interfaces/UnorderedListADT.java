/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.example.Structures.Interfaces;


public interface UnorderedListADT<T> extends ListADT<T> {
    
    /**
     * Adds an element to the front of the list.
     *
     * @param element the element to be added
     */
    public void addToFront(T element);
    
    /**
     * Adds an element to the rear of the list.
     *
     * @param element the element to be added
     */
    public void addToRear(T element);
    
    /**
     * Adds an element after an element of the list.
     *
     * @param element the element to be added
     * @param previous the previous element of the new element.
     */
    public void addAfter(T element, T previous);
}
