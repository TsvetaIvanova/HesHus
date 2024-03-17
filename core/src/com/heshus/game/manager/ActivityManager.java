package com.heshus.game.manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x/collisionLayer.getTileWidth() + 1, y/collisionLayer.getTileHeight() + 1);
        if (cell != null && cell.getTile() != null) {
            if (cell.getTile().getProperties().containsKey("eat") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                // just added for testing
                // System.out.println("Eating activity detected.");
                //DayManager.incrementDay();
                performEatingActivity();
            } else if (cell.getTile().getProperties().containsKey("study") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                performStudyingActivity();
            } else if (cell.getTile().getProperties().containsKey("recreation") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                performRecreationalActivity();
            } else if (cell.getTile().getProperties().containsKey("sleep") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                performSleepingActivity();
            }
        }
    }


    // incrementing overall.. for now will adjust later
    private void performEatingActivity() {
        decrementEnergy();
        incrementTime(2);
        DayManager.currentDay.incrementEatScore();

    }

    // incrementing overall.. for now will adjust later
    private void performStudyingActivity() {
        decrementEnergy();
        incrementTime(4);

        DayManager.currentDay.incrementStudyScore();

    }

    // incrementing overallRecreationalScore for now will adjust later
    private void performRecreationalActivity() {
        decrementEnergy();
        incrementTime(3);


        DayManager.currentDay.incrementRecreationalScore();
    }

    private void performSleepingActivity() {
        // decided to define day over with reaching 840 time
        if (DayManager.currentDay.getTime() >= 24 || DayManager.currentDay.getEnergy() <= 0) {


            // if the game is not over the avatar will move to the next day and reset their energy
            if (!DayManager.gameOver) {
                DayManager.incrementDay();
//                DayManager.currentDay.resetEatScore();
////              DayManager.currentDay.resetStudyScore();
////              DayManager.currentDay.resetRecreationalScore();
                //resetForNewDay();
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

    private void incrementTime(int setTime) {
        float newTime = DayManager.currentDay.getTime() + setTime;
        if (newTime >= 24) {
            //"You need to sleep" we display a message to the player, move to next day
            performSleepingActivity();
            //Change these coordinates
            player.setPosition(648,224);
        } else {
            DayManager.currentDay.setTime(newTime);
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}