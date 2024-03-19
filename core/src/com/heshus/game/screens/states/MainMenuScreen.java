package com.heshus.game.screens.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heshus.game.editor.CustomiseSprite;
import com.heshus.game.engine.HesHusGame;
import com.heshus.game.engine.Play;

public class MainMenuScreen implements Screen {

    final HesHusGame game;
    OrthographicCamera camera;

    public MainMenuScreen(final HesHusGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */

    @Override
    public void render(float delta) {
            ScreenUtils.clear(0, 0.2f, 0, 1);

            camera.update();
            game.batch.setProjectionMatrix(camera.combined);

            game.batch.begin();
            game.font.draw(game.batch, "Welcome to HeslingtonHustle!!! ", 100, 150);
            game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
            game.batch.end();

            if (Gdx.input.isTouched()) {
                //game.setScreen(new Play(game));
                //rather than jumping straight to the game, first choose a character
                game.setScreen(new CustomiseSprite(game, camera));
                dispose();
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


    //...Rest of class omitted for succinctness.

}
