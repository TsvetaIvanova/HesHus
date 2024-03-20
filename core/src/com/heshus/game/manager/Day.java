package com.heshus.game.manager;

/**
 * Represents the day of the week that the current play through is on
 * Constructor can include the 3 unused variables to count the scores for the current day and overall scores separately
 */
public class Day {
    private int dayNumber, studyScore, eatScore, energy, recreationalScore;
    private float time;

    /**
     * Constructor for the current day
     * @param _dayNumber Current day
     * @param _time Current day's time
     * @param _energy Current day's energy
     */
    public Day(int _dayNumber, float _time, int _energy){
        this.dayNumber = _dayNumber;
        this.time = _time;
        this.energy = _energy;
    }

    /**
     *
     * @return current day counter
     */
    public int getDayNumber(){
        return this.dayNumber;
    }

    /**
     * +1 to current day counter
     */
    public void incrementDayNumber(){
        this.dayNumber += 1;
    }

    /**
     * +1 to eat counter
     */
    public void incrementEatScore() { DayManager.overallEatScore++; }

    /**
     * +1 to study counter
     */
    public void incrementStudyScore()
    {
        DayManager.overallStudyScore++;
    }

    /**
     * +1 to recreation counter
     */
    public void incrementRecreationalScore()
    {
        DayManager.overallRecreationalScore++;
    }

    /**
     * reset current days time to 8am
     */
    public void resetTime() {
        this.time = 8;
    }

    /**
     * reset current days energy to 100
     */
    public void resetEnergy() {
        this.energy = 100;
    }

    /**
     *
     * @return current day's game time
     */
    public float getTime() { return this.time; }

    /**
     *
     * @return current day's energy
     */
    public int getEnergy() { return this.energy; }

    /**
     * Sets current day's energy to param
     * @param energy
     */
    public void setEnergy(int energy)
    {
        if(energy >= 0){
            this.energy = energy;
        }
        else{
            this.energy = 0;
        }
    }

    /**
     * Sets current day's time to param
     * @param time
     */
    public void setTime(float time) { this.time = time; }

    /**
     * Resets current day's day number for new game
     */
    public void resetDay() {
        this.dayNumber = 0;
    }
}

