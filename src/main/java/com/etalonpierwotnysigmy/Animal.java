package com.etalonpierwotnysigmy;

public abstract class Animal extends Entity{
    private int health;
    private int sightRange;
    private int speed;
    private int hunger;
    private int thirst;

    private Position findNextPosition(Entity[][] entityMap) {
        return null; // logika do znalezienia nowej pozycji
    }

    public void move(Entity[][] entityMap, Position nextPosition) {
        entityMap[nextPosition.getX()][nextPosition.getY()] = entityMap[position.getX()][position.getY()];
        entityMap[position.getX()][position.getY()] = null;
        position = nextPosition;
    }


    public void update() {
        // logika odpowiadająca za poruszanie (później abstrakcyjna i zrobiona osobno dla poszczególnych typów zwierząt)
    }

    public int getHealth() {
        return health;
    }

    public int getSightRange() {
        return sightRange;
    }

    public int getSpeed() {
        return speed;
    }
    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }
}
