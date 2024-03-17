package com.heshus.game.screens.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PauseMenu{
    private Stage stage;
    private Texture myTexture;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTexRegionDrawable;
    private Button resumeButton;
    private Button quitButton;
    private Button settingsButton;
    private Table table;

    public PauseMenu(ExtendViewport viewport, Camera camera) {
        myTexture = new Texture("assets/UI/button_up.png");
        myTextureRegion = new TextureRegion(myTexture, myTexture.getWidth(),myTexture.getHeight());
        myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        resumeButton = new Button(myTexRegionDrawable); //Set the button up

        resumeButton.setTransform(true);
        resumeButton.setScale(2);
        resumeButton.addListener(new InputListener() {
             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                 Gdx.app.exit();
                 return false;
             }
        });
        table = new Table();
        table.add(resumeButton);
        table.align(1);
        stage = new Stage(viewport);
        stage.addActor(table); //Add the button to the stage to perform rendering and take input.
    }
    public void draw(){
        stage.draw();
    }
    public void update(Camera camera){
        Gdx.input.setInputProcessor(stage);
        table.setPosition(camera.position.x, camera.position.y);
    }
}

