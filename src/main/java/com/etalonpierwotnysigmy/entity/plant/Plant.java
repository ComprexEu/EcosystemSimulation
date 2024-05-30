package com.etalonpierwotnysigmy.entity.plant;

import com.etalonpierwotnysigmy.entity.Entity;

public abstract class Plant extends Entity {
    protected boolean isGrown;
    protected int foodValue;
    protected int timeToRegrow;
    protected int growthState;
    public Plant(){
        isGrown = true;
    }

    public void changeGrowthStatus(){
        if (growthState == 0)
            isGrown = true;
        else
            growthState -= 1;
    }

    public void resetGrowthState() {
        growthState = timeToRegrow;
    }

    public int getFoodValue() {
        return foodValue;
    }

    public boolean isGrown() {
        return isGrown;
    }

    public void setGrown(boolean grown) {
        isGrown = grown;
    }
}
