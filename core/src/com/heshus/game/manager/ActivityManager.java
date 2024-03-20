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

public class ActivityManager {

    private final TiledMapTileLayer collisionLayer;
    private Player player;

    private String activityText = "";
    private Vector2 textPosition = new Vector2();

    GlyphLayout layout = new GlyphLayout();


    public ActivityManager(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }


    /**
     * Checks whether and which activity is performed based on location of player
     */
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


    /**
     * If available, controls variables indicating eating has been performed
     * @param cell current tile player is on
     */
    private void performEatingActivity(TiledMapTileLayer.Cell cell) {
        if(!(DayManager.currentDay.getEnergy() <= 0) && !(DayManager.currentDay.getTime() >= 24)) {
            decrementEnergy(10);
            incrementTime(2);
            DayManager.currentDay.incrementEatScore();
            // added just for testing
            //DayManager.incrementDay();
            String holdText = "You feel refreshed";
            layout.setText(Play.getFont(), holdText);
            setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width / 2), Math.round(player.getY() / 16) * 16);
        }
        else{
            noEnergyOrSleep();
        }
    }

    /**
     * If available, controls variables indicating studying has been performed
     * @param cell current tile player is on
     */
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

    /**
     * If available, controls variables indicating recreation has been performed
     * @param cell current tile player is on
     */
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

    private void performSleepingActivity() {
        // decided to define day over with reaching 840 time
        if (DayManager.currentDay.getTime() >= 24 || DayManager.currentDay.getEnergy() <= 0) {
            String holdText = "You feel well rested";
            layout.setText(Play.getFont(), holdText);
            setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width/2), Math.round(player.getY() / 16) * 16);

            // if the game is not over the avatar will move to the next day and reset their energy
            if (!DayManager.gameOver) {
                DayManager.incrementDay();
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



    private void decrementEnergy(int energy) {
        DayManager.currentDay.setEnergy(Math.max(0, DayManager.currentDay.getEnergy() - energy));
    }

    private void incrementTime(int setTime) {
        float newTime = DayManager.currentDay.getTime() + setTime;
        if (newTime >= 24) {
            //"You need to sleep" we display a message to the player, move to next day
        } else {
            DayManager.currentDay.setTime(newTime);
        }
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *
     * @param text to be displayed
     * @param x horizontal position of text
     * @param y vertical position of text
     */
    public void setText(String text, float x, float y){
        activityText = text;
        textPosition.set(x, y + 40);
    }

    /**
     * Gets the current text needing to be displayed
     * @return text
     */
    public String getText(){
        return activityText;
    }

    /**
     * Gets the position to display the text
     * @return position to display text
     */
    public Vector2 getTextPosition() {
        return textPosition;
    }

    /**
     * Draws a text bubble above current text position (usually the current cell)
     * @param batch instance of spritebatch
     * @param font instance of font
     */
    public void drawTextBubble(SpriteBatch batch, BitmapFont font){
        font.setColor(new Color(Color.BLACK));
        font.draw(batch, activityText, textPosition.x, textPosition.y + 37);
        font.setColor(new Color(Color.WHITE));
    }


    /**
     * Manages text when no energy or time is available
     */
    public void noEnergyOrSleep(){
        String holdText = "You should get some sleep";
        layout.setText(Play.getFont(), holdText);
        setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width/2), Math.round(player.getY() / 16) * 16);
    }

}