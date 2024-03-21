package com.heshus.game.manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.heshus.game.engine.HesHusGame;
import com.heshus.game.engine.Play;
import com.heshus.game.entities.Player;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Manages all activities in the game that the player can perform
 * by introducing property tags to the tiled map the player interacts with each property tag
 * and according to what the type of activity is the player's energy and time is incremented or decremented
 */

public class ActivityManager {

    private final TiledMapTileLayer collisionLayer;
    private Player player;

    private String activityText = "";
    private Vector2 textPosition = new Vector2();

    GlyphLayout layout = new GlyphLayout();


    public ActivityManager(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }


    //decide activity based on the avatar's position for collision-based interactions
    public void checkActivity() {
        // based on the x, y coordinates of the player
        float avatarX = player.getX();
        float avatarY = player.getY();

        // Convert avatar position to tile coordinates
        int x = (int) avatarX;
        int y = (int) avatarY;
        // checking for the property tag
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x/collisionLayer.getTileWidth() + 1, y/collisionLayer.getTileHeight() + 1);
        if (cell != null && cell.getTile() != null) {
            if (cell.getTile().getProperties().containsKey("eat") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                performEatingActivity(cell);
            } else if (cell.getTile().getProperties().containsKey("study") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                performStudyingActivity(cell);
            } else if (cell.getTile().getProperties().containsKey("recreation") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                performRecreationalActivity(cell);
            } else if (cell.getTile().getProperties().containsKey("sleep") && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                performSleepingActivity();
            }
        }
    }


    // incrementing each property tag activity, if time and have not run out it will decrement energy with 10 from 100 and increment time with 2
    private void performEatingActivity(TiledMapTileLayer.Cell cell) {
        if(!(DayManager.currentDay.getEnergy() <= 0) && !(DayManager.currentDay.getTime() >= 24)) {
            decrementEnergy(10);
            incrementTime(2);
            DayManager.currentDay.incrementEatScore();
            String holdText = "You feel refreshed";
            layout.setText(Play.getFont(), holdText);
            setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width / 2), Math.round(player.getY() / 16) * 16);
        }
        else{
            noEnergyOrSleep();
        }
    }

    // incrementing each property tag activity, if time and have not run out it will decrement energy with 20 from 100 and increment time with 4
    private void performStudyingActivity(TiledMapTileLayer.Cell cell) {
        if(!(DayManager.currentDay.getEnergy() <= 0) && !(DayManager.currentDay.getTime() >= 24)) {
            decrementEnergy(20);
            incrementTime(4);

            DayManager.currentDay.incrementStudyScore();
            String holdText = "You feel smarter";
            layout.setText(Play.getFont(), holdText);
            setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width / 2), Math.round(player.getY() / 16) * 16);
        }
        else{
            noEnergyOrSleep();
        }
    }

    // incrementing each property tag activity, if time and have not run out it will decrement energy with 20 from 100 and increment time with 3
    private void performRecreationalActivity(TiledMapTileLayer.Cell cell) {
        if(!(DayManager.currentDay.getEnergy() <= 0) && !(DayManager.currentDay.getTime() >= 24)){

            decrementEnergy(20);
            incrementTime(3);

            DayManager.currentDay.incrementRecreationalScore();
            String holdText = "You have recreationed";
            layout.setText(Play.getFont(), holdText);
            setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width/2), Math.round(player.getY() / 16) * 16);
        }
        else{
            noEnergyOrSleep();
        }

    }

    // incrementing each property tag activity, the player can only sleep when they have ran out of energy
    private void performSleepingActivity() {
        // decided to define day over with reaching 840 time
        if (DayManager.currentDay.getTime() >= 24 || DayManager.currentDay.getEnergy() <= 0) {
            String holdText = "You feel well rested";
            layout.setText(Play.getFont(), holdText);
            setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width/2), Math.round(player.getY() / 16) * 16);

            // if the game is not over the avatar will move to the next day and reset their energy
            if (!DayManager.gameOver) {
                DayManager.incrementDay();

            }
        }
    }


    // decrements energy by a set amount in "energy"
    private void decrementEnergy(int energy) {
        DayManager.currentDay.setEnergy(Math.max(0, DayManager.currentDay.getEnergy() - energy));
    }
    // increments time by a setTime parameter
    /**
     * @param setTime
     * accepts as parameter a setTime for different activities
     */
    private void incrementTime(int setTime) {
        float newTime = DayManager.currentDay.getTime() + setTime;
        if (newTime >= 24) {
            //"You need to sleep" we display a message to the player, move to next day
        } else {
            DayManager.currentDay.setTime(newTime);
        }
    }
    /**
     * Sets the player object for the activity manager to interact with.
     * @param player The player whose activities are to be managed.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    //Methods for text bubble
    /**
     * Sets the text and position for displaying a text bubble above the player.
     * @param text The text to be displayed in the bubble.
     * @param x The x-coordinate for the bubble's position.
     * @param y The y-coordinate for the bubble's position.
     */

    public void setText(String text, float x, float y){
        activityText = text;
        textPosition.set(x, y + 40);
    }

    public String getText(){
        return activityText;
    }

    public Vector2 getTextPosition() {
        return textPosition;
    }

    public void drawTextBubble(SpriteBatch batch, BitmapFont font){
        font.setColor(new Color(Color.BLACK));
        font.draw(batch, activityText, textPosition.x, textPosition.y + 37);
        font.setColor(new Color(Color.WHITE));
    }

    //method for sleep bubble

    public void noEnergyOrSleep(){
        String holdText = "You should get some sleep";
        layout.setText(Play.getFont(), holdText);
        setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width/2), Math.round(player.getY() / 16) * 16);
    }

}