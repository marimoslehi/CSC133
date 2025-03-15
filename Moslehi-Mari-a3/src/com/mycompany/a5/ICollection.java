package com.mycompany.a5;

// Interface for collections that store game objects
public interface ICollection {
    void add(GameObject newObject);        // Method to add a game object to the collection
    IIterator getIterator();               // Method to get an iterator for the collection
}
