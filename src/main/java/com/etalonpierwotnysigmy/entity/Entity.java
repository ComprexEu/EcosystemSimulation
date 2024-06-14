package com.etalonpierwotnysigmy.entity;

import com.etalonpierwotnysigmy.simulation.Position;

public abstract class Entity {
    protected Position position;
    protected boolean moved;

    abstract protected String getTileColor();

    abstract protected String getText();

    abstract protected String getTextColor();

    abstract public String getLegendRow();

    public void printEntity() {
        String tileColor = getTileColor();
        String textColor = getTextColor();
        String text = getText();

        System.out.print(tileColor + textColor + text);

    }

    public boolean didntMove() {
        return !moved;
    }

    public Position getPosition() {
        return position;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

}
