package com.heshus.game.manager;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class ActivityManager {

    private final TiledMapTileLayer collisionLayer;



    public ActivityManager(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }


    //decide activity based on the avatar's position for collision-based interactions
    public void checkActivity(float avatarX, float avatarY) {
        // to work with Vector2 and getSpritePosition()
        int x = (int) avatarX;
        int y = (int) avatarY;

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
        if (cell != null && cell.getTile() != null) {
            if (cell.getTile().getProperties().containsKey("eating")) {
                performEatingActivity();
            } else if (cell.getTile().getProperties().containsKey("study")) {
                performStudyingActivity();
            } else if (cell.getTile().getProperties().containsKey("recreation")) {
                performRecreationalActivity();
            } else if (cell.getTile().getProperties().containsKey("sleeping")) {
                performSleepingActivity();
            }
        }
    }


    //
    public void performActivity(String activityType) {
        if (DayManager.gameOver) return;

        switch (activityType) {
            case "eat":
                performEatingActivity();
                break;
            case "study":
                performStudyingActivity();
                break;
            case "recreation":
                performRecreationalActivity();
                break;
            case "sleep":
                performSleepingActivity();
                break;

        }
    }

    // // incrementing overall.. for now will adjust later
    private void performEatingActivity() {
        decrementEnergy();
        incrementTime();
        DayManager.overallEatScore++;
    }

    // incrementing overall.. for now will adjust later
    private void performStudyingActivity() {
        decrementEnergy();
        incrementTime();
        DayManager.overallStudyScore++;
    }

    // incrementing overallRecreationalScore for now will adjust later
    private void performRecreationalActivity() {
        decrementEnergy();
        incrementTime();
        DayManager.overallRecreationalScore++;
    }

    private void performSleepingActivity() {
        // decided to define day over with reaching 840 time
        if (DayManager.currentDay.getTime() >= 840 || DayManager.currentDay.getEnergy() <= 0) {


            // if the game is not over the avatar will move to the next day and reset their energy
            if (!DayManager.gameOver) {
                DayManager.incrementDay();
                resetForNewDay();
            }
        }
    }

    private void resetForNewDay() {
        // reset both time to be back at 480(8am) and energy to 100
        DayManager.currentDay.resetTime();
        DayManager.currentDay.resetEnergy();


        //
        if (DayManager.currentDay.getDayNumber() > 7) {
            DayManager.gameOver = true;

        }
    }



    private void decrementEnergy() {
        DayManager.currentDay.setEnergy(Math.max(0, DayManager.currentDay.getEnergy() - 10));
    }

    private void incrementTime() {
        float newTime = DayManager.currentDay.getTime() + (float) 60;
        if (newTime >= 840) {
            //"You need to sleep" we display a message to the player, move to next day
            performSleepingActivity();
        } else {
            DayManager.currentDay.setTime(newTime);
        }
    }
}