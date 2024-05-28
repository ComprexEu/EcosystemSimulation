package com.etalonpierwotnysigmy.entity;

import com.etalonpierwotnysigmy.simulation.Position;

public abstract class Entity {
    protected Position position;
    protected boolean moved; // zmienna dbająca o to, aby wykonywany był tylko jeden ruch w iteracji


    public boolean didntMove() {
        return !moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

}
