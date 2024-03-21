package com.heshus.game.manager;

public class Day {
    private int dayNumber, studyScore, eatScore, energy, recreationalScore;
    private float time;
    public Day(int _dayNumber, float _time, int _energy){
        this.dayNumber = _dayNumber;
        this.time = _time;
        this.energy = _energy;
    }

    public int getDayNumber(){
        return this.dayNumber;
    }
    public void incrementDayNumber(){
        this.dayNumber += 1;
    }

    public void incrementEatScore() { DayManager.overallEatScore++; }

    public void incrementStudyScore()
    {
        DayManager.overallStudyScore++;
    }

    public void incrementRecreationalScore()
    {
        DayManager.overallRecreationalScore++;
    }

    public void resetTime() {
        this.time = 8;
    }

    public void resetEnergy() {
        this.energy = 100;
    }


    public float getTime() { return this.time; }

    public int getEnergy() { return this.energy; }

    public void setEnergy(int energy)
    {
        if(energy >= 0){
            this.energy = energy;
        }
        else{
            this.energy = 0;
        }
    }

    public void setTime(float time) { this.time = time; }

    public void resetDay() {
        this.dayNumber = 0;
    }
}

