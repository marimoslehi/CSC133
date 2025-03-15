package com.mycompany.a5;

public interface ICollider {
    boolean collidesWith(GameObject otherObject);
    void handleCollision(GameObject otherObject);
}
