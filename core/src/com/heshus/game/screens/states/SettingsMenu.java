package com.heshus.game.screens.states;

import com.badlogic.gdx.Gdx;
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
    public SettingsMenu(int returnState, Camera camera, ExtendViewport viewport, float scale) {
        this.returnState = returnState;
        this.camera = camera;

        //for resolution support
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
        ///////////////////////////////////////////////////////////////////////////////////////
        //BUTTONS//
        //set styles//
        buttonStyles();
        //set buttons!

        //Apply button
        applyButton = new TextButton("Apply", textButtonStyle);
        applyButton.setTransform(true);
        applyButton.padBottom(6);
        applyButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                applySettings();
                state = returnState;
                return false;
            }
        });

        //Buttons for resolution. We have a left and right button that increment an index through an array of supported variables.
        resolutionTable = new Table();
        String resolution = settings.getInteger("windowWidth") +" X "+ settings.getInteger("windowHeight");//set button text to current resolution
        TextButton resolutionButton = new TextButton(resolution, squareButtonStyle);
        TextButton leftButton = new TextButton("<",textButtonStyle);
        leftButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (resolutionSelectIndex>0) {//makes sure index not out of range
                    resolutionSelectIndex--;
                    resolutionButton.setText((int) supportedResolutions.get(resolutionSelectIndex).x +" X "+ (int) supportedResolutions.get(resolutionSelectIndex).y);//horrific line, just updates text on resolution button
                }
                return false;
            }
        });

        TextButton rightButton = new TextButton(">",textButtonStyle);
        rightButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (resolutionSelectIndex+1<supportedResolutions.size()){// makes sure index not out of range
                        resolutionSelectIndex++;
                        resolutionButton.setText((int) supportedResolutions.get(resolutionSelectIndex).x +" X "+ (int) supportedResolutions.get(resolutionSelectIndex).y);//horrific line, just updates text on resolution button
                }
                return false;
            }
        });

        //Checkbox for fullscreen
        fullScreenCheckbox = new CheckBox("Fullscreen", checkBoxStyle);
        fullScreenCheckbox.setChecked(settings.getBoolean("isFullScreen"));
        fullScreenCheckbox.setTransform(true);
        fullScreenCheckbox.addListener(new InputListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resolutionTable.setVisible(fullScreenCheckbox.isChecked());
            }
        });
        //small single row table for resolution buttons (so we can have different column sizes from rest of table)
        resolutionTable.add(leftButton).width(leftButton.getWidth()*scale/3).height(leftButton.getHeight()*scale).padRight(2*scale);
        resolutionTable.add(resolutionButton).width(resolutionButton.getWidth()*2).height(resolutionButton.getHeight()*scale).padRight(2*scale);
        resolutionTable.add(rightButton).width(leftButton.getWidth()*scale/3).height(rightButton.getHeight()*scale).padLeft(2*scale);

        //Return button:
        returnButton = new TextButton("Return", textButtonStyle); //Set the button up
        returnButton.padBottom(6);
        returnButton.setTransform(true);
        returnButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //reset buttons
                resolutionSelectIndex = supportedResolutions.indexOf(new Vector2(settings.getInteger("windowWidth"),settings.getInteger("windowHeight")));
                fullScreenCheckbox.setChecked(settings.getBoolean("isFullScreen"));
                resolutionButton.setText(((int) supportedResolutions.get(resolutionSelectIndex).x) +" X "+ (int) supportedResolutions.get(resolutionSelectIndex).y);//horrific line, just updates text on resolution button
                state = returnState;
                return false;
            }
        });
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
    private void applySettings() {//
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
        settings.flush();//this has to be called for the changes to happen!
    }

    public void update() {
        resolutionTable.setVisible(!fullScreenCheckbox.isChecked());
        Gdx.input.setInputProcessor(stage);
        table.setPosition(camera.position.x, camera.position.y);
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }

}
