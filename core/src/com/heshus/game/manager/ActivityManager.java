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


    // incrementing overall.. for now will adjust later
    private void performEatingActivity(TiledMapTileLayer.Cell cell) {
        if(!(DayManager.currentDay.getEnergy() <= 0) && !(DayManager.currentDay.getTime() >= 24)) {
            decrementEnergy();
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

    // incrementing overall.. for now will adjust later
    private void performStudyingActivity(TiledMapTileLayer.Cell cell) {
        if(!(DayManager.currentDay.getEnergy() <= 0) && !(DayManager.currentDay.getTime() >= 24)) {
            decrementEnergy();
            incrementTime(4);

            DayManager.currentDay.incrementStudyScore();
            // added just for testing
            //DayManager.incrementDay();
            String holdText = "You feel smarter";
            layout.setText(Play.getFont(), holdText);
            setText(holdText, Math.round(player.getX() / 16) * 16 + 8 - (layout.width / 2), Math.round(player.getY() / 16) * 16);
        }
        else{
            noEnergyOrSleep();
        }
    }

    // incrementing overallRecreationalScore for now will adjust later
    private void performRecreationalActivity(TiledMapTileLayer.Cell cell) {
        if(!(DayManager.currentDay.getEnergy() <= 0) && !(DayManager.currentDay.getTime() >= 24)){

            decrementEnergy();
            incrementTime(3);

            DayManager.currentDay.incrementRecreationalScore();
            // added just for testing
            //DayManager.incrementDay();
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



    private void decrementEnergy() {
        DayManager.currentDay.setEnergy(Math.max(0, DayManager.currentDay.getEnergy() - 10));
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

    //Methods for text bubble

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