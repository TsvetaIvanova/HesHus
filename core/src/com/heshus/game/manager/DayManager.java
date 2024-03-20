package com.heshus.game.manager;

/**
 * Manages how the current day is changed and whether the game has finished
 */
public class DayManager {
    public static Day currentDay = new Day(1, 8, 100);
    public static boolean gameOver = false;
    public static int overallEatScore = 0;
    public static int overallStudyScore = 0;
    public static int overallRecreationalScore = 0;

    /**
     * Controls what happens at the end of the day
     * If the current day is less than 7 then reset relevant variables
     * If the current day is 7 or greater, the game is over
     */
    public static void incrementDay(){
        if(currentDay.getDayNumber() < 7){
            //Change day number
            currentDay.incrementDayNumber();
            //Reset variables for a new "day"
            currentDay.resetTime();
            currentDay.resetEnergy();
        }
        else{
            gameOver = true;
        }
    }


}
