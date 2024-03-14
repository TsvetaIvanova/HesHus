package com.heshus.game.manager;

public class DayManager {
    public static Day currentDay = new Day(1, 480, 0, 100, 0, 0);
    public static boolean gameOver = false;
    static int overallEatScore = 0, overallStudyScore = 0, overallRecreationalScore = 0;

    public static void incrementDay(){
        if(currentDay.getDayNumber() < 7){
            //Change day number
            currentDay.incrementDayNumber();
            //Add current day scores to the overall scores
            overallEatScore += currentDay.getEatScore();
            overallRecreationalScore += currentDay.getRecreationalScore();
            overallStudyScore += currentDay.getStudyScore();
            //Reset variables for a new "day"
            currentDay.resetTime();
            currentDay.resetEnergy();
            currentDay.resetEatScore();
            currentDay.resetStudyScore();
            currentDay.resetRecreationalScore();
        }
        else{
            gameOver = true;
        }
    }


}
