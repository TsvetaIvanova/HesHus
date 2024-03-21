package com.heshus.game.screens.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.heshus.game.engine.Play.*;

/**
 * Loads, draws and controls buttons to make a pause menu
 */
public class PauseMenu{
    private Stage stage;
    private Texture buttonTexture;
    private TextureRegion buttonTextureRegion;
    private TextureRegionDrawable buttonTextureRegionDrawable;
    private TextButton resumeButton;
    private TextButton quitButton;
    private TextButton settingsButton;
    private Table table;
    private int buttonWidth;
    private int buttonHeight;
    private float buttonScale;
    private Table areYouSure;
    private Sound clickSound;
    private BitmapFont font;

    /**
     *
     * @param viewport
     * @param camera
     */
    public PauseMenu(ExtendViewport viewport, Camera camera) {
        //set up font
        font = new BitmapFont(Gdx.files.internal("Fonts/monogram/pixel.fnt"), false);
        font.getData().setScale(1.5F);
        font.setColor(Color.BLACK);
        //sound on click
        clickSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/switch2.ogg"));

        //BUTTONS
        //set textbuttonstyle
        buttonTexture = new Texture("UI/button_up.png");
        buttonTextureRegion = new TextureRegion(buttonTexture, buttonTexture.getWidth(), buttonTexture.getHeight());
        buttonTextureRegionDrawable =new TextureRegionDrawable(buttonTextureRegion);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(buttonTextureRegionDrawable, buttonTextureRegionDrawable, buttonTextureRegionDrawable, font );
        //set button sizes
        buttonScale = 2;
        buttonHeight = (int) (buttonTexture.getHeight()*buttonScale);
        buttonWidth = (int) (buttonTexture.getWidth()*buttonScale);

        //Resume button:
        resumeButton = new TextButton("RESUME!!", textButtonStyle);
        resumeButton.padBottom(10);
        //Settings button:
        settingsButton = new TextButton("SETTINGS", textButtonStyle);
        settingsButton.padBottom(10);
        //Quit button:
        quitButton = new TextButton("QUIT :(", textButtonStyle);
        quitButton.padBottom(10);

        //sets up what heppens when buttons clicked
        buttonsOnClickLogic();

        //Table to store buttons
        table = new Table();
        table.add(resumeButton).width(buttonWidth).height(buttonHeight).padBottom((float) buttonHeight /5);
        table.row();
        table.add(settingsButton).width(buttonWidth).height(buttonHeight).padBottom((float) buttonHeight /5);
        table.row();
        table.add(quitButton).width(buttonWidth).height(buttonHeight).padBottom((float) buttonHeight /5);

        stage = new Stage(viewport);
        //Add the table to the stage to perform rendering and take input.
        stage.addActor(table);
    }
    /**
     * Draws everything!
     */
    public void draw(){
        stage.draw();
    }

    /**
     * Sets up what happens when each button is clicked
     */
    private void buttonsOnClickLogic(){
        resumeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();
                state = GAME_RUNNING;
            }
        });

        settingsButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            //set state to GAME_SETTINGS if clicked, in Play this results in a SettingsMenu being drawn instead of PauseMenu
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button); // Call the superclass method
                clickSound.play();
                state = GAME_SETTINGS;
            }
        });

        quitButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();
                //Timer to allow sound to play without getting cutoff
                Timer.schedule(new Timer.Task() {

                    public void run() {
                        Gdx.app.exit();
                    }
                }, 0.5f); // 0.5 seconds delay
            }
        });
    }
    /**
     * Grabs input, moves table to centre of screen
     * @param camera camera to set position to
     */
    public void update(Camera camera){
        Gdx.input.setInputProcessor(stage);
        table.setPosition(camera.position.x, camera.position.y);
    }

    /**
     * Should be called to release all resources
     */
    public void dispose(){
        stage.dispose();
        buttonTexture.dispose();
        buttonTexture.dispose();
        if (clickSound != null) clickSound.dispose();
    }


}

