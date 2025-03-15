package com.mycompany.a5;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Transform;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

import java.util.Observable;
import java.util.Random;

public class GameWorld extends Observable {
    private static GameWorld instance = null;      // Singleton instance
    private GameObjectCollection gameObjects; // Collection of all game objects
    private Spaceship spaceship;              // Singleton spaceship
    private int score;                        // Current score
    private int astronautsRescued;            // Number of rescued astronauts
    private int aliensSneaked;                // Number of aliens sneaked into the spaceship
    private int time;                         // Elapsed time in ticks
    private boolean soundOn;                  // Sound toggle
    private boolean isPaused;                 // Pause state
    private Transform theVTM;                 // Viewing Transformation Matrix
    private float worldLeft, worldRight, worldBottom, worldTop;
    private final Random random = new Random();
   
 

    // Singleton Pattern
    private GameWorld() {
        gameObjects = new GameObjectCollection();
        score = 0;
        astronautsRescued = 0;
        aliensSneaked = 0;
        time = 0;
        soundOn = false;
        isPaused = false;
    }

    public static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
        }
        return instance;
    }

    // Initialize the game world
    public void init() {
        // Initialize world boundaries and VTM
        worldLeft = 0;
        worldRight = 1000;
        worldBottom = 0;
        worldTop = 1000;
        theVTM = Transform.makeIdentity();
        updateVTM();

        // Create game objects
        createSpaceship();
        createHierarchicalAlien();
        createRandomAstronauts();
        createRandomAliens();
    }



	private void updateVTM() {
        theVTM.setIdentity();
        theVTM.translate(-worldLeft, -worldBottom);
        float scaleX = 1.0f / (worldRight - worldLeft);
        float scaleY = 1.0f / (worldTop - worldBottom);
        theVTM.scale(scaleX, scaleY);
    }

    private void createSpaceship() {
        spaceship = new Spaceship(50, ColorUtil.BLUE);
        spaceship.translate((worldRight - worldLeft) / 2, (worldTop - worldBottom) / 2);
        gameObjects.add(spaceship);
    }

    private void createHierarchicalAlien() {
        Alien alien = new Alien(50, ColorUtil.BLACK); // Create an Alien

        // Create and add sub-objects
        AlienPart body = new AlienPart("Body", 40, 60, ColorUtil.rgb(0, 0, 0));
        body.translate(0, 0); // Center the body
        AlienPart head = new AlienPart("Head", 20, 20, ColorUtil.rgb(0, 0, 0));
        head.translate(0, 50); // Above the body
        alien.addSubObject(body);
        alien.addSubObject(head);

     // Create and add dynamic sub-objects (legs)
        AlienPart leftLeg = new AlienPart("Leg", 10, 30, ColorUtil.rgb(0, 0, 0));
        leftLeg.translate(-15, -40); // Left of the body
        AlienPart rightLeg = new AlienPart("Leg", 10, 30, ColorUtil.rgb(0, 0, 0));
        rightLeg.translate(15, -40); // Right of the body
        alien.addDynamicSubObject(leftLeg);
        alien.addDynamicSubObject(rightLeg);

        // Place the Alien in the game world
        alien.translate(500, 500);
        gameObjects.add(alien);
        
        
        
        System.out.println("Creating hierarchical alien...");
    }

    private void createRandomAstronauts() {
        for (int i = 0; i < 5; i++) {
            int size = 20 + random.nextInt(40);  // Random size between 20 and 60
            int color = ColorUtil.rgb(255, 0, 0); // Red color
            int health = 100;                   // Full health

            float x = random.nextFloat() * (worldRight - worldLeft);
            float y = random.nextFloat() * (worldTop - worldBottom);
            Point location = new Point((int) x, (int) y);

            Astronaut astronaut = new Astronaut(size, location, color, health);
            gameObjects.add(astronaut);
        }
    }

    private void createRandomAliens() {
        for (int i = 0; i < 3; i++) {
            int size = 20 + random.nextInt(40);  // Random size between 20 and 60
            int color = ColorUtil.rgb(0, 0, 255); // Blue color

            float x = random.nextFloat() * (worldRight - worldLeft);
            float y = random.nextFloat() * (worldTop - worldBottom);

            Alien alien = new Alien(size, color);
            alien.translate(x, y); // Set its initial location
            gameObjects.add(alien);
        }
    }

    public void createShockWave() {
        if (!isPaused) {
            ShockWave wave = new ShockWave(spaceship.getLocation());
            gameObjects.add(wave);
            System.out.println("ShockWave created at: " + spaceship.getLocation());
            notifyStateChange();
        }
    }

    // Zoom and Pan
    public void zoom(boolean zoomIn) {
        float zoomFactor = zoomIn ? 0.9f : 1.1f;
        float width = (worldRight - worldLeft) * zoomFactor;
        float height = (worldTop - worldBottom) * zoomFactor;

        worldLeft -= (width - (worldRight - worldLeft)) / 2;
        worldRight += (width - (worldRight - worldLeft)) / 2;
        worldBottom -= (height - (worldTop - worldBottom)) / 2;
        worldTop += (height - (worldTop - worldBottom)) / 2;

        updateVTM();
        notifyStateChange();
    }

    public void pan(float dx, float dy) {
        worldLeft += dx;
        worldRight += dx;
        worldBottom += dy;
        worldTop += dy;

        updateVTM();
        notifyStateChange();
    }

    public void tick() {
        if (isPaused) return;

        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof IMove) {
                ((IMove) obj).move();
            }
        }

        time++;
        notifyStateChange();
    }

    public void paint(Graphics g) {
        g.setTransform(theVTM);

        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            obj.draw(g);
        }
    }

    public void notifyStateChange() {
        setChanged();
        notifyObservers();
    }

    public void togglePause() {
        isPaused = !isPaused;
        notifyStateChange();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void removeGameObject(Astronaut astronaut) {
        gameObjects.remove(astronaut);
        notifyStateChange();
    }

    public void removeGameObject(Alien alien) {
        gameObjects.remove(alien);
        notifyStateChange();
    }

 // Heal the selected astronaut
    public void healSelectedAstronaut() {
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Astronaut && ((Astronaut) obj).isSelected()) {
                ((Astronaut) obj).heal();
                notifyStateChange();
                return;
            }
        }
        System.out.println("No astronaut selected to heal.");
    }

    // Expand the spaceship door
    public void expandSpaceshipDoor() {
        if (spaceship != null) {
            spaceship.expandDoor();
            notifyStateChange();
        }
    }

    // Contract the spaceship door
    public void contractSpaceshipDoor() {
        if (spaceship != null) {
            spaceship.contractDoor();
            notifyStateChange();
        }
    }

    // Move spaceship
    public void moveSpaceshipRight() {
        if (spaceship != null) {
            spaceship.translate(10, 0); // Move 10 units to the right
            notifyStateChange();
        }
    }

    public void moveSpaceshipLeft() {
        if (spaceship != null) {
            spaceship.translate(-10, 0); // Move 10 units to the left
            notifyStateChange();
        }
    }

    public void moveSpaceshipUp() {
        if (spaceship != null) {
            spaceship.translate(0, 10); // Move 10 units up
            notifyStateChange();
        }
    }

    public void moveSpaceshipDown() {
        if (spaceship != null) {
            spaceship.translate(0, -10); // Move 10 units down
            notifyStateChange();
        }
    }
    


    // Move spaceship to nearest astronaut
    public void moveSpaceshipToAstronaut() {
        if (spaceship == null) return;

        IIterator it = gameObjects.getIterator();
        Astronaut nearest = null;
        double minDistance = Double.MAX_VALUE;

        while (it.hasNext()) {
            GameObject obj = it.getNext();

            if (obj instanceof Astronaut) {
                Point2D spaceshipLocation = spaceship.getLocation(); // Ensure this returns Point2D
                Point2D astronautLocation = obj.getLocation();       // Ensure this returns Point2D

                if (spaceshipLocation != null && astronautLocation != null) {
                    // Calculate distance without using Math.pow
                    double deltaX = spaceshipLocation.getX() - astronautLocation.getX();
                    double deltaY = spaceshipLocation.getY() - astronautLocation.getY();
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = (Astronaut) obj;
                    }
                }
            }
        }

        if (nearest != null) {
            spaceship.moveTo(nearest.getLocation());
            notifyStateChange();
        }
    }

    public void moveSpaceshipToAlien() {
        if (spaceship == null) return;

        IIterator it = gameObjects.getIterator();
        Alien nearest = null;
        double minDistance = Double.MAX_VALUE;

        while (it.hasNext()) {
            GameObject obj = it.getNext();

            if (obj instanceof Alien) {
                Point2D spaceshipLocation = spaceship.getLocation();
                Point2D alienLocation = obj.getLocation();

                if (spaceshipLocation != null && alienLocation != null) {
                    // Calculate distance without using Math.pow
                    double deltaX = spaceshipLocation.getX() - alienLocation.getX();
                    double deltaY = spaceshipLocation.getY() - alienLocation.getY();
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = (Alien) obj;
                    }
                }
            }
        }

        if (nearest != null) {
            spaceship.moveTo(nearest.getLocation());
            notifyStateChange();
        }
    }
    
    // Display score
    public void displayScore() {
        System.out.println("Score: " + score);
        System.out.println("Astronauts Rescued: " + astronautsRescued);
        System.out.println("Aliens Sneaked: " + aliensSneaked);
    }

    // Handle a fight between the spaceship and aliens/astronauts
    public void fight() {
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Alien && spaceship.collidesWith(obj)) {
                ((Alien) obj).takeDamage(20);
                spaceship.takeDamage(10);
                notifyStateChange();
                return;
            }
        }
    }

    // Enable/Disable sound
    public void setSoundOn(boolean isSoundOn) {
        soundOn = isSoundOn;
        notifyStateChange();
    }

    // Check if sound is enabled
    public boolean isSoundOn() {
        return soundOn;
    }

    // Tick with a specified elapsed time
    public void tick(int elapsedTime) {
        if (isPaused) return;

        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof IMove) {
                ((IMove) obj).move(elapsedTime);
            }
        }

        time += elapsedTime;
        notifyStateChange();
    }

    // Select astronaut at a specific point
    public void selectAstronautAt(Point point) {
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            GameObject obj = it.getNext();
            if (obj instanceof Astronaut) {
                ((Astronaut) obj).setSelected(((Astronaut) obj).contains(point.getX(), point.getY()));
            }
        }
        notifyStateChange();
    }

    // Print the map
    public void printMap() {
        IIterator it = gameObjects.getIterator();
        while (it.hasNext()) {
            System.out.println(it.getNext());
        }
    }

    // Create a new alien
    public void createNewAlien() {
        Alien alien = new Alien(50, ColorUtil.BLUE);
        alien.translate(random.nextFloat() * (worldRight - worldLeft),
                        random.nextFloat() * (worldTop - worldBottom));
        gameObjects.add(alien);
        notifyStateChange();
    }

	public void addObserver(ScoreView sv) {
		// TODO Auto-generated method stub
		
	}

	public GameObjectCollection getGameObjects() {
		return gameObjects;

	}
	
	   public void addGameObject(GameObject obj) {
	        gameObjects.add(obj);
	        setChanged();
	        notifyObservers();
	    }

}
