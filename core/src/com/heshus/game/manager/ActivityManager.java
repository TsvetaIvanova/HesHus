package com.heshus.game.manager;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.heshus.game.entities.Player;

public class ActivityManager {

    private final TiledMapTileLayer collisionLayer;
    private Player player;


    public ActivityManager(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }


    //decide activity based on the avatar's position for collision-based interactions
    public void checkActivity() {
        // Assuming you have a reference to the Player object named 'player'
        float avatarX = player.getX();
        float avatarY = player.getY();

        // Convert avatar position to tile coordinates
        int x = (int) avatarX;
        int y = (int) avatarY;

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
        if (cell != null && cell.getTile() != null) {
            if (cell.getTile().getProperties().containsKey("eat")) {
                // just added for testing
                // System.out.println("Eating activity detected.");
                //DayManager.incrementDay();
                performEatingActivity();
            } else if (cell.getTile().getProperties().containsKey("study")) {
                performStudyingActivity();
            } else if (cell.getTile().getProperties().containsKey("recreation")) {
                performRecreationalActivity();
            } else if (cell.getTile().getProperties().containsKey("sleep")) {
                performSleepingActivity();
            }
        }
    }


    // incrementing overall.. for now will adjust later
    private void performEatingActivity() {
        decrementEnergy();
        incrementTime();
        DayManager.overallEatScore++;
        DayManager.currentDay.incrementEatScore();
        // added just for testing
        DayManager.incrementDay();

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

    public void setPlayer(Player player) {
        this.player = player;
    }
}