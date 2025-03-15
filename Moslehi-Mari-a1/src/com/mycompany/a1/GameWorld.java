package com.mycompany.a1;  // Add this line 

import com.codename1.charts.models.Point;  // For handling points (locations)
import java.util.Vector;                   // For vector collection
import java.util.Random;                   // For random number generation


public class GameWorld {
    private Vector<GameObject> gameObjects;
    private int gameClock;
    private int score;
    private int astronautsRescued;
    private int aliensSneakedIn;
    private boolean exitRequested;

    public void init() {
        gameObjects = new Vector<>();
        gameClock = 0;
        score = 0;
        astronautsRescued = 0;
        aliensSneakedIn = 0;
        exitRequested = false;
        initGameObjects();
    }

    private void initGameObjects() {
        Random rand = new Random();
        gameObjects.add(new Spaceship(100, new Point(rand.nextFloat() * 1000, rand.nextFloat() * 1000), 0xFF0000));

        // Add four astronauts
        for (int i = 0; i < 4; i++) {
            gameObjects.add(new Astronaut(rand.nextInt(30) + 20, new Point(rand.nextFloat() * 1000, rand.nextFloat() * 1000), 0xFF0000, 5));
        }

        // Add three aliens
        for (int i = 0; i < 3; i++) {
            gameObjects.add(new Alien(rand.nextInt(30) + 20, new Point(rand.nextFloat() * 1000, rand.nextFloat() * 1000), 0x00FF00));
        }
    }
    
    public void expandSpaceshipDoor() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Spaceship) {
                ((Spaceship) obj).expand();
            }
        }
    }

    public void contractSpaceshipDoor() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Spaceship) {
                ((Spaceship) obj).contract();
            }
        }
    }

    public void moveSpaceshipRight() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Spaceship) {
                ((Spaceship) obj).moveRight();
            }
        }
    }

    public void moveSpaceshipLeft() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Spaceship) {
                ((Spaceship) obj).moveLeft();
            }
        }
    }

    public void moveSpaceshipUp() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Spaceship) {
                ((Spaceship) obj).moveUp();
            }
        }
    }

    public void moveSpaceshipDown() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Spaceship) {
                ((Spaceship) obj).moveDown();
            }
        }
    }

    public void tick() {
        gameClock++;
        for (GameObject obj : gameObjects) {
            if (obj instanceof IMoving) {
                ((IMoving) obj).move();
            }
        }
    }

    public void openSpaceshipDoor() {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Spaceship) {
                Spaceship spaceship = (Spaceship) obj;
                // Check for objects within the door
                Vector<GameObject> objectsToRemove = new Vector<>();
                for (GameObject go : gameObjects) {
                    if (go != spaceship && spaceship.isInsideDoor(go)) {
                        if (go instanceof Astronaut) {
                            astronautsRescued++;
                            score += 10;  // Adjust score based on astronaut's health
                        } else if (go instanceof Alien) {
                            aliensSneakedIn++;
                            score -= 10;
                        }
                        objectsToRemove.add(go);
                    }
                }
                // Remove rescued or sneaked-in objects
                gameObjects.removeAll(objectsToRemove);
                System.out.println("Spaceship door opened.");
                System.out.println("Astronauts rescued: " + astronautsRescued);
                System.out.println("Aliens sneaked in: " + aliensSneakedIn);
            }
        }
    }




    public void pretendAlienCollision() {
        Vector<Alien> aliens = new Vector<>();
        for (GameObject obj : gameObjects) {
            if (obj instanceof Alien) {
                aliens.add((Alien) obj);
            }
        }

        if (aliens.size() >= 2) {
            Random rand = new Random();
            Alien targetAlien = aliens.get(rand.nextInt(aliens.size()));
            Point newAlienLocation = new Point(targetAlien.getLocation().getX() + rand.nextFloat() * 10,
                                               targetAlien.getLocation().getY() + rand.nextFloat() * 10);
            gameObjects.add(new Alien(rand.nextInt(30) + 20, newAlienLocation, 0x00FF00));
            System.out.println("New alien generated at: " + newAlienLocation);
        } else {
            System.out.println("Not enough aliens for a collision.");
        }
    }

    public void pretendFight() {
        Vector<Astronaut> astronauts = new Vector<>();
        Vector<Alien> aliens = new Vector<>();

        for (GameObject obj : gameObjects) {
            if (obj instanceof Astronaut) {
                astronauts.add((Astronaut) obj);
            } else if (obj instanceof Alien) {
                aliens.add((Alien) obj);
            }
        }

        if (!aliens.isEmpty() && !astronauts.isEmpty()) {
            Random rand = new Random();
            Astronaut targetAstronaut = astronauts.get(rand.nextInt(astronauts.size()));
            targetAstronaut.decrementHealth();
            System.out.println("Fight occurred, astronaut health: " + targetAstronaut.getHealth());
        } else {
            System.out.println("No aliens or astronauts for a fight.");
        }
    }

    public void printGameState() {
    	System.out.println("Current game state:");
        System.out.println("Game clock: " + gameClock);
        System.out.println("Score: " + score);
        System.out.println("Astronauts rescued: " + astronautsRescued);
        System.out.println("Aliens sneaked in: " + aliensSneakedIn);
        int remainingAstronauts = 0;
        int remainingAliens = 0;
        for (GameObject obj : gameObjects) {
            if (obj instanceof Astronaut) {
                remainingAstronauts++;
            } else if (obj instanceof Alien) {
                remainingAliens++;
            }
        }
        System.out.println("Astronauts left in the world: " + remainingAstronauts);
        System.out.println("Aliens left in the world: " + remainingAliens);
    }

    public void printMap() {
    	System.out.println("Game map:");
        for (GameObject obj : gameObjects) {
            System.out.println(obj.toString());
        }
    }

    public boolean isExitRequested() {
        return exitRequested;
    }

    public void requestExit() {
        exitRequested = true;
    }

    public void cancelExitRequest() {
        exitRequested = false;
    }

    public void jumpToAstronaut() {
        Vector<Astronaut> astronauts = new Vector<>();
        for (GameObject obj : gameObjects) {
            if (obj instanceof Astronaut) {
                astronauts.add((Astronaut) obj);
            }
        }

        if (!astronauts.isEmpty()) {
            Random rand = new Random();
            Astronaut targetAstronaut = astronauts.get(rand.nextInt(astronauts.size()));
            Point astronautLocation = targetAstronaut.getLocation();
            for (GameObject obj : gameObjects) {
                if (obj instanceof Spaceship) {
                    ((Spaceship) obj).setLocation(astronautLocation);
                    System.out.println("Spaceship jumped to Astronaut at: (" + astronautLocation.getX() + ", " + astronautLocation.getY() + ")");
                }
            }
        } else {
            System.out.println("No astronauts available to jump to.");
        }
    }

    public void jumpToAlien() {
        Vector<Alien> aliens = new Vector<>();
        for (GameObject obj : gameObjects) {
            if (obj instanceof Alien) {
                aliens.add((Alien) obj);
            }
        }

        if (!aliens.isEmpty()) {
            Random rand = new Random();
            Alien targetAlien = aliens.get(rand.nextInt(aliens.size()));
            Point alienLocation = targetAlien.getLocation();
            for (GameObject obj : gameObjects) {
                if (obj instanceof Spaceship) {
                    ((Spaceship) obj).setLocation(alienLocation);
                    System.out.println("Spaceship jumped to Alien at: (" + alienLocation.getX() + ", " + alienLocation.getY() + ")");
                }
            }
        } else {
            System.out.println("No aliens available to jump to.");
        }
    }
}
