package com.etalonpierwotnysigmy.entity;

import com.etalonpierwotnysigmy.simulation.Position;

public abstract class Entity {
    protected Position position;
    protected boolean updated;

    public Position getPosition() {
        return position;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
