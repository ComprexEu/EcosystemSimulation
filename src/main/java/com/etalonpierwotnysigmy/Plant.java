package com.etalonpierwotnysigmy;

public abstract class Plant extends Entity{
    protected boolean isGrown;
    protected int foodValue;
    protected int timeToRegrow;
    protected int growthState;
    Plant(){
        isGrown = true;
    }

    void changeGrowthStatus(){
        if (growthState == 0)
            isGrown = !isGrown;
        else
            this.growthState -= 1;
    }

    public void resetGrowthState() {
        this.growthState = this.timeToRegrow;
    }

    public void setFoodValue(int foodValue) {
        this.foodValue = foodValue;
    }

    public void setTimeToRegrow(int timeToRegrow) {
        this.timeToRegrow = timeToRegrow;
    }

    public int getFoodValue() {
        return foodValue;
    }

    public boolean isGrown() {
        return isGrown;
    }
}
