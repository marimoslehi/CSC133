package com.mycompany.a5;

import java.util.ArrayList;
import java.util.List;

// GameObjectCollection implements ICollection interface
public class GameObjectCollection implements ICollection {
    // The internal storage for game objects
    private List<GameObject> gameObjects;

    // Constructor to initialize the game object collection
    public GameObjectCollection() {
        gameObjects = new ArrayList<>();
    }

    // Method to add a game object to the collection
    @Override
    public void add(GameObject newObject) {
        gameObjects.add(newObject);
    }

    // Method to return an iterator over the collection
    @Override
    public IIterator getIterator() {
        return new GameObjectIterator();
    }

    // Inner class to implement the IIterator interface for the game objects
    private class GameObjectIterator implements IIterator {
        private int currentIndex = -1;  // Index of the current object in the list

        // Check if there are more elements in the collection
        @Override
        public boolean hasNext() {
            return currentIndex < gameObjects.size() - 1;
        }

        // Return the next element in the collection
        @Override
        public GameObject getNext() {
            if (hasNext()) {
                currentIndex++;
                return gameObjects.get(currentIndex);
            }
            return null;
        }

        // Remove the last element returned by the iterator
        @Override
        public void remove() {
            if (currentIndex >= 0) {
                gameObjects.remove(currentIndex);
                currentIndex--;  // Adjust the index after removal
            }
        }
        


    }

	public boolean contains(Astronaut astronaut) {
		// TODO Auto-generated method stub
		return false;
	}

	public void remove(Astronaut astronaut) {
		// TODO Auto-generated method stub
		
	}
}
