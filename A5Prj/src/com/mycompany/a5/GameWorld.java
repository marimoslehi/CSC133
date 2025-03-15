package com.mycompany.a5;

import java.util.Observable;

import java.util.Random;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.List;


public class GameWorld extends Observable {
    private static GameWorld instance;        // Singleton instance
    private GameObjectCollection gameObjects; // Collection of all game objects
    private int score;                        // Current score
    private int astronautsRescued;            // Number of rescued astronauts
    private int aliensSneaked;                // Number of aliens sneaked into spaceship
    private int time;                         // Clock time (ticks)
    private Spaceship spaceship;              // The singleton spaceship
    private boolean soundOn;                  // Sound state
    private Sound alienCollisionSound;
    private Sound astronautCollisionSound;
    private Sound spaceshipDoorSound;
    private Sound alienAlienCollisionSound;
    private BGSound backgroundMusic;
    private boolean isPaused = false;
    private final List<Observer> observerList = new ArrayList<>();

    // Private Constructor (Singleton Pattern)
    private GameWorld() {
        gameObjects = new GameObjectCollection();
        score = 0;
        astronautsRescued = 0;
        aliensSneaked = 0;
        time = 0;
        soundOn = false; // Sound is off by default
        createSounds();
    }

    // Singleton Accessor
    public static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
        }
        return instance;
    }

    // Initialize the game world with objects
    public void init() {
    	// Create the Spaceship
        spaceship = new Spaceship(50, new Point(500, 500), ColorUtil.BLUE); // Size: 50, Position: Center, Color: Blue
        gameObjects.add(spaceship);

        Random random = new Random();

        // Create Astronauts (Red Triangles, Different Sizes)
        for (int i = 0; i < 5; i++) {
            int size = random.nextInt(40) + 20; // Size between 20 and 60
            Astronaut astronaut = new Astronaut(size, getRandomLocation(), ColorUtil.rgb(255, 0, 0), 100); // Red color
            gameObjects.add(astronaut);
        }

        // Create Aliens (Blue Squares, Different Sizes)
        for (int i = 0; i < 3; i++) {
            int size = random.nextInt(40) + 20; // Size between 20 and 60
            Alien alien = new Alien(size, getRandomLocation(), ColorUtil.rgb(0, 0, 255)); // Blue color
            gameObjects.add(alien);
        }

        // Debugging: Print all game objects in the console
        listGameObjects();

        // Notify observers to refresh the game view
        setChanged();
        notifyObservers();
    }

    // Generate random locations for game objects
    private Point getRandomLocation() {
        Random rand = new Random();
        int x = rand.nextInt(1000);
        int y = rand.nextInt(1000);
        return new Point(x, y);
    }


    
    private void createSounds() {
        try {
            backgroundMusic = new BGSound("/background_music.mp3");
            alienCollisionSound = new Sound("/alien_collision.mp3");
            spaceshipDoorSound = new Sound("/spaceship_door.mp3");
            alienAlienCollisionSound = new Sound("/alien_alien_collision.mp3");
        } catch (Exception e) {
            System.err.println("Error loading sound files: " + e.getMessage());
        }
        if (backgroundMusic != null) {
            System.out.println("Background music loaded successfully.");
        }
    }
    // Sound control
    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;

        if (soundOn) {
            System.out.println("Sound is ON. Playing background music.");
            backgroundMusic.play();
        } else {
            System.out.println("Sound is OFF. Pausing background music.");
            backgroundMusic.pause();
        }

        notifyStateChange();
    }

    public boolean isSoundOn() {
        return soundOn;
    }
    
    public void playAlienCollisionSound() {
        if (soundOn && alienCollisionSound != null) {
            alienCollisionSound.play();
        }
    }
    
    public void playAlienAlienCollisionSound() {
        if (soundOn && alienAlienCollisionSound != null) {
            alienAlienCollisionSound.play();
        }
    }

    public void playSpaceshipDoorSound() {
        if (soundOn && spaceshipDoorSound != null) {
            spaceshipDoorSound.play();
        }
    }

    public void toggleBackgroundMusic(boolean play) {
        if (play && soundOn) {
            backgroundMusic.play();
        } else {
            backgroundMusic.pause();
        }
    }

    public Sound getAlienCollisionSound() {
        return alienCollisionSound;
    }

    public Sound getAstronautCollisionSound() {
        return astronautCollisionSound;
    }

    public Sound getSpaceshipDoorSound() {
        return spaceshipDoorSound;
    }

    // GameObjectCollection getter
    public GameObjectCollection getGameObjects() {
        return gameObjects;
    }

    // **Spaceship Movement**
    public void moveSpaceshipUp() {
        spaceship.moveUp();
        notifyStateChange();
    }

    public void moveSpaceshipDown() {
        spaceship.moveDown();
        notifyStateChange();
    }

    public void moveSpaceshipLeft() {
        spaceship.moveLeft();
        notifyStateChange();
    }

    public void moveSpaceshipRight() {
        spaceship.moveRight();
        notifyStateChange();
    }

    // Spaceship Door Control
    
    public void expandSpaceshipDoor() {
        if (spaceship != null) { // Ensure the spaceship instance exists
            spaceship.expand(); // Call the expand method on the spaceship
            System.out.println("Expanded the spaceship door.");
            notifyStateChange(); // Notify observers of the state change
        } else {
            System.err.println("Error: Spaceship instance is null. Cannot expand door.");
        }
    }

    public void contractSpaceshipDoor() {
        if (spaceship != null) { // Ensure the spaceship instance exists
            spaceship.contract(); // Call the contract method on the spaceship
            System.out.println("Contracted the spaceship door.");
            notifyStateChange(); // Notify observers of the state change
        } else {
            System.err.println("Error: Spaceship instance is null. Cannot contract door.");
        }
    }

    // Object creation
    public void createNewAlien() {
        Alien newAlien = new Alien(30, getRandomLocation(), ColorUtil.GREEN);
        gameObjects.add(newAlien);
        notifyStateChange();
    }

    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            System.out.println("Game paused.");
            if (soundOn) backgroundMusic.pause();
        } else {
            System.out.println("Game resumed.");
            if (soundOn) backgroundMusic.play();
        }
        setChanged();
        notifyStateChange();
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    // **Tick method**
    public void tick() {
    	if (isPaused) return; // Skip updates if paused

        // Game update logic here
    	moveObjects();
        System.out.println("Game tick executed.");
        setChanged();
        notifyObservers();
    }
    


    
    private void moveObjects() {
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Alien) {
                ((Alien) obj).move();
            } else if (obj instanceof Astronaut) {
                ((Astronaut) obj).move();
            }
        }
    }


 // Handle collisions
    private void detectAndHandleCollisions() {
        IIterator itOuter = gameObjects.getIterator();

        while (itOuter.hasNext()) {
            GameObject obj1 = itOuter.getNext();

            IIterator itInner = gameObjects.getIterator();
            while (itInner.hasNext()) {
                GameObject obj2 = itInner.getNext();

                if (obj1 != obj2 && obj1.collidesWith(obj2)) {
                    if (obj1 instanceof Alien && obj2 instanceof Astronaut) {
                        handleAlienAstronautCollision();
                    } else if (obj1 instanceof Alien && obj2 instanceof Alien) {
                        handleAlienAlienCollision();
                    }
                }
            }
        }
    }

    // **Update Score**
    private void updateScore() {
        System.out.println("Score updated: " + score);
    }
    

    // Interaction with Aliens
    public void interactWithAlien() {
        Point spaceshipLocation = spaceship.getLocation();
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Alien && spaceship.isNear(obj)) {
                System.out.println("Spaceship interacted with Alien!");
                aliensSneaked++;
                updateScore();
                notifyStateChange();
                return;
            }
        }
    }

    public void healSelectedAstronaut() {
        IIterator it = gameObjects.getIterator();

        while (it.hasNext()) {
            GameObject obj = it.getNext();

            if (obj instanceof Astronaut && ((Astronaut) obj).isSelected()) {
                ((Astronaut) obj).heal(); // Heal the astronaut
                System.out.println("Healed astronaut at: " + obj.getLocation());
                ((Astronaut) obj).setSelected(false); // Deselect after healing
                break;
            }
        }

        System.out.println("No astronaut selected to heal.");
    }
    
    private boolean isNearby(GameObject obj1, GameObject obj2) {
        double distance = calculateDistance(obj1.getLocation(), obj2.getLocation());
        return distance <= 50; // Example proximity threshold
    }
    
 

    public void moveSpaceshipToAstronaut() {
        Point astronautLocation = findClosestAstronaut();
        if (astronautLocation != null) {
            spaceship.setLocation(astronautLocation);
            System.out.println("Spaceship moved to Astronaut.");
        }
        notifyStateChange();
    }

    public void moveSpaceshipToAlien() {
        Point alienLocation = findClosestAlien();
        if (alienLocation != null) {
            spaceship.setLocation(alienLocation);
            System.out.println("Spaceship moved to Alien.");
        }
        notifyStateChange();
    }
    
    private Point findClosestAstronaut() {
        Point closest = null;
        double minDistance = Double.MAX_VALUE;
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Astronaut) {
                double distance = calculateDistance(obj.getLocation(), spaceship.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = obj.getLocation();
                }
            }
        }
        return closest;
    }

    private double calculateDistance(Point p1, Point p2) {
        int dx = p2.getX() - p1.getX(); // Difference in X coordinates
        int dy = p2.getY() - p1.getY(); // Difference in Y coordinates
        return Math.sqrt(dx * dx + dy * dy); // Calculate Euclidean distance
    }

	private Point findClosestAlien() {
        Point closest = null;
        double minDistance = Double.MAX_VALUE;
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Alien) {
                double distance = calculateDistance(obj.getLocation(), spaceship.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = obj.getLocation();
                }
            }
        }
        return closest;
    }



    
    

    public void displayScore() {
        System.out.println("Score: " + score);
        System.out.println("Astronauts Rescued: " + astronautsRescued);
        System.out.println("Aliens Sneaked: " + aliensSneaked);
        System.out.println("Time: " + time);
    }

    private void listGameObjects() {
    	 IIterator it = gameObjects.getIterator();
         System.out.println("Game Objects in the World:");
         while (it.hasNext()) {
             System.out.println(it.getNext());
         }
    }

    public void notifyStateChange() {
        setChanged();
        notifyObservers();
    }

    public void fight() {
        // Iterate through game objects and handle collisions
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Alien) {
                // Trigger a fight scenario between an Alien and the closest Astronaut
                Point alienLocation = obj.getLocation();
                Point closestAstronaut = findClosestAstronaut(alienLocation);

                if (closestAstronaut != null) {
                    System.out.println("A fight occurred between an Alien and an Astronaut!");
                    Alien alien = (Alien) obj;
                    alien.fight();
                    score -= 5; // Example: Deduct score for each fight
                    notifyStateChange();
                }
            }
        }
    }

    private Point findClosestAstronaut(Point location) {
        Point closest = null;
        double minDistance = Double.MAX_VALUE;
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Astronaut) {
                double distance = calculateDistance(location, obj.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = obj.getLocation();
                }
            }
        }
        return closest;
    }

    public void tick(int ticks) {
        for (int i = 0; i < ticks; i++) {
            tick(); // Call the main tick method for each iteration
        }
    }
    

    public void printMap() {
        System.out.println("Game World Map:");
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            System.out.println(it.getNext());
        }
    }

    public String getTime() {
        return "Time: " + time;
    }

    public String getScore() {
    	return String.valueOf(score);
    }

    public String getAstronautsRescued() {
    	return String.valueOf(astronautsRescued);
    }

    public String getAliensSneaked() {
        return "Aliens Sneaked: " + aliensSneaked;
    }
    

    public String getRemainingAstronauts() {
        int count = 0;
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Astronaut) {
                count++;
            }
        }
        return "Remaining Astronauts: " + count;
    }

    public String getRemainingAliens() {
        int count = 0;
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Alien) {
                count++;
            }
        }
        return "Remaining Aliens: " + count;
    }
    
    public void rescueAstronaut(Astronaut astronaut) {
        if (gameObjects.contains(astronaut)) {
            astronautsRescued++;
            score += 10; // +10 points for rescuing
            gameObjects.remove(astronaut);
            System.out.println("Astronaut rescued! Score: " + score);
            playSpaceshipDoorSound();
            notifyStateChange();
        }
           
    }

    
    /*public void rescue(Astronaut astronaut) {
        if (isNearby(astronaut)) {
            astronaut.rescue(); // Mark the astronaut as rescued
            GameWorld.getInstance().rescueAstronaut1(astronaut); // Notify GameWorld
            System.out.println("Rescuer has rescued the astronaut at location: " + astronaut.getLocation());
        }
    }*/
    
    public void handleAlienAstronautCollision() {
        score -= 5; // -5 points for alien-astronaut collision
        System.out.println("Alien collided with Astronaut! Score: " + score);
        playAlienCollisionSound();
        notifyStateChange();
    }
    
    public void handleAlienAlienCollision() {
        score += 3; // +3 points for alien-alien collision
        createNewAlien(); // Spawn a new alien for game difficulty
        System.out.println("Two Aliens collided! Score: " + score);
        playAlienAlienCollisionSound();
        notifyStateChange();
    }

	private boolean isNearby(Astronaut astronaut) {
		 // Ensure the spaceship and astronaut are valid
	    if (spaceship == null || astronaut == null) {
	        return false;
	    }

	    // Calculate the distance between the spaceship and the astronaut
	    double distance = calculateDistance(spaceship.getLocation(), astronaut.getLocation());

	    // Define a proximity threshold (e.g., 50 units)
	    return distance <= 50; // Replace 50 with your desired proximity threshold
	}

	@Override
	public void addObserver(Observer o) {
	    if (!observerList.contains(o)) {
	        super.addObserver(o);
	        observerList.add(o); // Add to custom list
	        System.out.println("Observer added: " + o.getClass().getSimpleName());
	    } else {
	        System.out.println("Duplicate observer prevented: " + o.getClass().getSimpleName());
	    }
	}

    public boolean hasObserver(Observer o) {
        return observerList.contains(o);
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged(); // Mark observable as changed
        super.notifyObservers(arg);
    }

    @Override
    public void deleteObserver(Observer o) {
        super.deleteObserver(o);
        observerList.remove(o); // Remove from custom list
        System.out.println("All observers cleared.");
    }

    @Override
    public void deleteObservers() {
        super.deleteObservers();
        observerList.clear(); // Clear custom list
    }

	public void addObserver(ScoreView sv) {
		// TODO Auto-generated method stub
		
	}

	
    
    public void selectAstronaut(int x, int y) {
        IIterator it = gameObjects.getIterator();

        while (it.hasNext()) {
            GameObject obj = it.getNext();

            if (obj instanceof Astronaut) {
                Astronaut astronaut = (Astronaut) obj;

                if (astronaut.contains(x, y)) {
                    astronaut.setSelected(!astronaut.isSelected()); // Toggle selection
                    System.out.println("Astronaut at " + astronaut.getLocation() + " selected: " + astronaut.isSelected());
                } else {
                    astronaut.setSelected(false); // Deselect others
                }
            }
        }
        setChanged();
        notifyObservers();
    }
    
 // Deselect all astronauts
    public void deselectAllAstronauts() {
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Astronaut) {
                ((Astronaut) obj).setSelected(false);
            }
        }
    }
    public void selectAstronautAt(Point clickPoint) {
        deselectAllAstronauts(); // Deselect all astronauts first
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Astronaut) {
                Astronaut astronaut = (Astronaut) obj;
                if (astronaut.contains((int) clickPoint.getX(), (int) clickPoint.getY())) {
                    astronaut.setSelected(true);
                    System.out.println("Astronaut selected: " + astronaut);
                    break;
                }
            }
        }
        notifyStateChange(); // Notify MapView to repaint
    }

	public void removeGameObject(Astronaut astronaut) {
		// TODO Auto-generated method stub
		
	}

	public void removeGameObject(Alien alien) {
		// TODO Auto-generated method stub
		
	}
	
	private void handleCollision(GameObject obj1, GameObject obj2) {
	    if (obj1 instanceof Astronaut && obj2 instanceof Alien) {
	        Astronaut astronaut = (Astronaut) obj1;
	        Alien alien = (Alien) obj2;
	        alien.fight(astronaut); // Trigger fight logic
	    } else if (obj1 instanceof Spaceship && obj2 instanceof Astronaut) {
	        Spaceship spaceship = (Spaceship) obj1;
	        Astronaut astronaut = (Astronaut) obj2;

	        // Check if astronaut is inside the spaceship's bounding square
	        if (spaceship.contains(astronaut.getLocation())) {
	            astronaut.rescue();
	            gameObjects.remove(astronaut); // Remove astronaut from the game world
	            score += 10; // Increment score
	            System.out.println("Astronaut rescued! Current score: " + score);
	        }
	    }
	}

    
    






}
