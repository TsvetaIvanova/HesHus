package com.heshus.game.editor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heshus.game.engine.HesHusGame;
import com.heshus.game.engine.Play;
import java.util.ArrayList;

public class CustomiseSprite implements Screen {

    final HesHusGame game;
    OrthographicCamera camera;

    boolean validPlayer = false;

    //this is the default sprite selected
    //importantly, this number points to an ARRAY INDEX, not the number in the name of the .png file
    //so for player-1 to player-6 it ranges from 0 TO 5 not 1 to 6
    int playerSelection = 3;
    int totalPlayerSpriteChoices = 6;

    Texture leftarrowTexture;
    Texture rightarrowTexture;

    ArrayList<Texture> textureList = new ArrayList<Texture>();

    public CustomiseSprite(final HesHusGame game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
    }
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        //set up arrow key textures
        leftarrowTexture = new Texture("UI/keyboard_arrow_left_outline.png");
        rightarrowTexture = new Texture("UI/keyboard_arrow_right_outline.png");

        //add all player textures to the textureList
        for (int i = 0; i < totalPlayerSpriteChoices; i++) {
            textureList.add(new Texture("Icons/player-" + Integer.toString(i+1) +".png"));
        }

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */

    @Override
    public void render(float delta) {

        //left arrow: decrement playerSelection
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            playerSelection = (playerSelection-- <= 0) ? totalPlayerSpriteChoices-1 : playerSelection--;
            //System.out.println(Integer.toString(playerSelection));
        }
        //right arrow: increment playerSelection
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            playerSelection = (playerSelection++ >= totalPlayerSpriteChoices-1) ? 0 : playerSelection++;
            //System.out.println(Integer.toString(playerSelection));
        }

        //validate player choice (should always pass but good to be safe)
        validPlayer = (playerSelection <= totalPlayerSpriteChoices && playerSelection >= 0);

        //render the menu (but not if an invalid player choice somehow as it would crash)
        camera.update();
        if (validPlayer) {

            ScreenUtils.clear(0.2f, 0f, 0, 1);
            game.batch.begin();
            game.font.draw(game.batch, "CHOOSE A PLAYER", 100, 150);
            game.font.draw(game.batch, "Press ENTER to start", 100, 100);
            game.batch.draw(leftarrowTexture, 200, 200);
            game.batch.draw(rightarrowTexture, 500, 200);
            game.batch.draw(textureList.get(playerSelection), 320, 220, 128, 128);
            game.batch.end();


            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.setScreen(new Play(game, textureList.get(playerSelection)));
                dispose();
            }
        }

    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }

}
