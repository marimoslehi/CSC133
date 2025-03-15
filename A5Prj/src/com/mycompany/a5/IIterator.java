package com.mycompany.a5;

// Interface for iterators that traverse through a collection of game objects
public interface IIterator {
    boolean hasNext();                     // Check if there are more elements to process
    GameObject getNext();                  // Return the next game object in the collection
    void remove();                         // Remove the current element from the collection
}
