package com.heshus.game.screens.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;

import static com.heshus.game.engine.HesHusGame.settings;
import static com.heshus.game.engine.Play.state;
/**
 * Loads, draws and controls buttons to make a settings menu
 */
public class SettingsMenu {
    private final BitmapFont font;
    private final Stage stage;
    private final int returnState;//state to go back to once done
    private final TextButton returnButton;
    private final TextButton applyButton;
    private Table table;
    private Camera camera;
    private final CheckBox fullScreenCheckbox;
    private final ArrayList<Vector2> supportedResolutions;
    private int resolutionSelectIndex;
    private TextButton.TextButtonStyle textButtonStyle;
    private CheckBox.CheckBoxStyle checkBoxStyle;
    private TextButton.TextButtonStyle squareButtonStyle;
    private Table resolutionTable;
    private TextButton leftButton;
    private TextButton rightButton;
    private TextButton resolutionButton;
    private Sound clickSound;
    /**
     * Initialises everything!
     * @param returnState this is the state that gets set when return or apply is clicked!
     * @param camera camera to help with drawing
     * @param viewport viewport to help with drawing
     * @param scale a scale for all the buttons
     */
    public SettingsMenu(int returnState, Camera camera, ExtendViewport viewport, float scale) {
        this.returnState = returnState;
        this.camera = camera;
        //sound on click
        clickSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/switch2.ogg"));

        //All resolutions that will be in settings menu
        supportedResolutions = new ArrayList<>();
        supportedResolutions.add(new Vector2(1024,576));
        supportedResolutions.add(new Vector2(1280,720));
        supportedResolutions.add(new Vector2(1600, 900));
        supportedResolutions.add(new Vector2(1920, 1080));
        resolutionSelectIndex = supportedResolutions.indexOf(new Vector2(settings.getInteger("windowWidth"),settings.getInteger("windowHeight")));//gets index of arraylist matching current resolution
        
        //Set up font
        font = new BitmapFont(Gdx.files.internal("Fonts/monogram/pixel.fnt"), false);
        font.getData().setScale(.5F*scale);
        font.setColor(Color.BLACK);

        //BUTTONS//
        //set styles//
        buttonStyles();

        //Apply button
        applyButton = new TextButton("Apply", textButtonStyle);
        applyButton.setTransform(true);
        applyButton.padBottom(6);

        //Buttons for resolution. We have a left and right button that increment an index through an array of supported variables.
        resolutionTable = new Table();
        String resolution = settings.getInteger("windowWidth") +" X "+ settings.getInteger("windowHeight");//set button text to current resolution
        resolutionButton = new TextButton(resolution, squareButtonStyle);
        leftButton = new TextButton("<",textButtonStyle);
        rightButton = new TextButton(">",textButtonStyle);

        //Checkbox for fullscreen
        fullScreenCheckbox = new CheckBox("Fullscreen", checkBoxStyle);
        fullScreenCheckbox.setChecked(settings.getBoolean("isFullScreen"));
        fullScreenCheckbox.setTransform(true);

        //small single row table for resolution buttons (so we can have different column sizes from rest of table)
        resolutionTable.add(leftButton).width(leftButton.getWidth()*scale/3).height(leftButton.getHeight()*scale).padRight(2*scale);
        resolutionTable.add(resolutionButton).width(resolutionButton.getWidth()*2).height(resolutionButton.getHeight()*scale).padRight(2*scale);
        resolutionTable.add(rightButton).width(leftButton.getWidth()*scale/3).height(rightButton.getHeight()*scale).padLeft(2*scale);

        //Return button:
        returnButton = new TextButton("Return", textButtonStyle); //Set the button up
        returnButton.padBottom(6);
        returnButton.setTransform(true);

        buttonsOnClickLogic();
        //Table combining it all! phew...
        table = new Table();
        table.add(returnButton).width(returnButton.getWidth()*scale).height(returnButton.getHeight()*scale).padBottom(3*scale);
        table.row();
        table.add(fullScreenCheckbox).width(fullScreenCheckbox.getWidth()*scale).height(fullScreenCheckbox.getHeight()*scale).padLeft(10*scale).padBottom(3*scale);
        table.row();
        table.add(resolutionTable).padBottom(3*scale);
        table.row();
        table.add(applyButton).width(applyButton.getWidth()*scale).height(applyButton.getHeight()*scale).padBottom(3*scale);
        stage = new Stage(viewport);
        stage.addActor(table);
    }

    /**
     * Sets up inputlisteners that then do all the logic and play a Sound that when a button is clicked
     */
    private void buttonsOnClickLogic(){
        //APPLY BUTTON
        applyButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();
                applySettings();
                state = returnState;
                return false;
            }
        });

        //RESOLUTION BUTTONS//
        //LEFT
        leftButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();

                if (resolutionSelectIndex>0) {//makes sure index not out of range
                    resolutionSelectIndex--;
                    resolutionButton.setText((int) supportedResolutions.get(resolutionSelectIndex).x +" X "+ (int) supportedResolutions.get(resolutionSelectIndex).y);//horrific line, just updates text on resolution button
                }
                return false;
            }
        });
        //RIGHT
        rightButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();

                if (resolutionSelectIndex+1<supportedResolutions.size()){// makes sure index not out of range
                    resolutionSelectIndex++;
                    resolutionButton.setText((int) supportedResolutions.get(resolutionSelectIndex).x +" X "+ (int) supportedResolutions.get(resolutionSelectIndex).y);//horrific line, just updates text on resolution button
                }
                return false;
            }
        });

        //FULLSCREEN
        fullScreenCheckbox.addListener(new InputListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();

                //if its full screen, we shouldn't see windowed resolution select
                resolutionTable.setVisible(fullScreenCheckbox.isChecked());
            }
        });

        //RETURN
        returnButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();

                //reset buttons to values found in settings (it would be weird to reopen settings to see fullscreen checked if the game was windowed)
                resolutionSelectIndex = supportedResolutions.indexOf(new Vector2(settings.getInteger("windowWidth"),settings.getInteger("windowHeight")));
                fullScreenCheckbox.setChecked(settings.getBoolean("isFullScreen"));
                resolutionButton.setText(((int) supportedResolutions.get(resolutionSelectIndex).x) +" X "+ (int) supportedResolutions.get(resolutionSelectIndex).y);//horrific line, just updates text on resolution button
                state = returnState;
                return false;
            }
        });
    }

    /**
     * Set up button styles!
     */
    private void buttonStyles(){
        //Set basic text button style (the blue one)
        Texture buttonTexture = new Texture("UI/button_up.png");
        TextureRegion buttonTextureRegion = new TextureRegion(buttonTexture, buttonTexture.getWidth(), buttonTexture.getHeight());
        TextureRegionDrawable buttonTextureRegionDrawable =new TextureRegionDrawable(buttonTextureRegion);
        textButtonStyle = new TextButton.TextButtonStyle(buttonTextureRegionDrawable, buttonTextureRegionDrawable, buttonTextureRegionDrawable, font );

        //Set up a checkboxstyle, unchecked and checked
        Texture uncheckedBoxTex = new Texture("UI/unchecked.png");//first the drawable of unchecked
        TextureRegion uncheckedBoxTextureRegion = new TextureRegion(uncheckedBoxTex, uncheckedBoxTex.getWidth(), uncheckedBoxTex.getHeight());
        TextureRegionDrawable uncheckedBoxTextureRegionDrawable =new TextureRegionDrawable(uncheckedBoxTextureRegion);
        Texture checkedBoxTex = new Texture("UI/checked.png");//then checked
        TextureRegion checkedBoxTextureRegion = new TextureRegion(checkedBoxTex, checkedBoxTex.getWidth(), checkedBoxTex.getHeight());
        TextureRegionDrawable checkedBoxTextureRegionDrawable =new TextureRegionDrawable(checkedBoxTextureRegion);
        checkBoxStyle = new CheckBox.CheckBoxStyle(uncheckedBoxTextureRegionDrawable, checkedBoxTextureRegionDrawable, font, Color.BLACK );

        //Throw in a cheeky square button //oppa button style
        squareButtonStyle = new TextButton.TextButtonStyle(uncheckedBoxTextureRegionDrawable, uncheckedBoxTextureRegionDrawable, uncheckedBoxTextureRegionDrawable, font );

        //add more styles here
    }

    /**
     * settings is a Preferences class (from libgdx)
     */
    private void applySettings() {
        //settings is a static Preferences class (from libgdx), basically a hash map. gets stored by
        settings.putBoolean ("isFullScreen",fullScreenCheckbox.isChecked());
        settings.putInteger("windowWidth", (int) supportedResolutions.get(resolutionSelectIndex).x);
        settings.putInteger("windowHeight", (int) supportedResolutions.get(resolutionSelectIndex).y);

        int width = settings.getInteger("windowWidth");
        int height = settings.getInteger("windowHeight");

        if (settings.getBoolean("isFullScreen")){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        else{
            Gdx.graphics.setWindowedMode(width, height);
        }
        //!!if this isn't called settings WON'T change!!//
        settings.flush();
    }

    /**
     * Update logic, draw all buttons
     */
    public void update() {
        //resolution control is just for windowed, so should not be displayed if fullscreen is checked
        resolutionTable.setVisible(!fullScreenCheckbox.isChecked());
        //intercept all inputs
        Gdx.input.setInputProcessor(stage);
        table.setPosition(camera.position.x, camera.position.y);
        stage.draw();
    }
    /**
     * Should be alled to release all resources
     */
    public void dispose(){
        stage.dispose();
    }

}
