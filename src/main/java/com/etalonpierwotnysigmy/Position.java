package com.etalonpierwotnysigmy;

public class Position {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Position addPositions(Position position1, Position position2) {
        return new Position(position1.x + position2.x, position1.y + position2.y);
    }

    public static Position subtractPositions(Position position1, Position position2) {
        return new Position(position1.x - position2.x, position1.y - position2.y);
    }

    public static double positionVectorLength(Position position) {
        return Math.sqrt(position.x * position.x + position.y * position.y);
    }
}
