package com.heshus.game.screens.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heshus.game.engine.HesHusGame;
import com.heshus.game.engine.Play;
import com.heshus.game.manager.Day;
import com.heshus.game.manager.DayManager;

public class GameOverScreen implements Screen {

    final HesHusGame game;
    OrthographicCamera camera;

    public GameOverScreen(final HesHusGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Game Over :(", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to go to the main menu!", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            DayManager.gameOver = false;
            game.setScreen(new MainMenuScreen(game));
            DayManager.currentDay = new Day(1, 8, 100);
            DayManager.overallEatScore = 0;
            DayManager.overallRecreationalScore = 0;
            DayManager.overallStudyScore = 0;
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
