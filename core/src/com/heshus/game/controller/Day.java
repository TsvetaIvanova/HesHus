package com.heshus.game.controller;

public class Day {
    private int dayNumber, studyScore, eatScore, energy, recreationalScore;
    private float time;
    public Day(int _dayNumber, float _time, int _eatScore, int _energy, int _studyScore, int _recreationalScore){
        this.dayNumber = _dayNumber;
        this.time = _time;
        this.eatScore = _eatScore;
        this.energy = _energy;
        this.studyScore = _studyScore;
        this.recreationalScore = _recreationalScore;
    }

    public int getDayNumber(){
        return this.dayNumber;
    }
    public void incrementDayNumber(){
        this.dayNumber += 1;
    }

    public int getStudyScore(){
        return this.studyScore;
    }
    public int getEatScore(){
        return this.eatScore;
    }
    public int getRecreationalScore(){
        return this.recreationalScore;
    }

    public void resetTime() {
        this.time = 480;
    }

    public void resetEnergy() {
        this.energy = 100;
    }

    public void resetEatScore() {
        this.eatScore = 0;
    }

    public void resetStudyScore() {
        this.studyScore = 0;
    }

    public void resetRecreationalScore() {
        this.recreationalScore = 0;
    }
}
