package com.heshus.game.screens.states;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PauseMenu {
    private Stage stage;
    private Texture myTexture;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTexRegionDrawable;
    private ImageButton button;

    public PauseMenu(ExtendViewport viewport, Camera camera) {
        myTexture = new Texture("assets/WhiteSquare.png");
        myTextureRegion = new TextureRegion(myTexture);
        myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        button = new ImageButton(myTexRegionDrawable); //Set the button up
        button.setPosition(camera.position.x,camera.position.y);

        stage = new Stage(viewport);
        stage.stageToScreenCoordinates(new Vector2(camera.position.x, camera.position.y));//Set up a stage for the ui
        stage.addActor(button); //Add the button to the stage to perform rendering and take input.
    }
    public void draw(){
        stage.draw();
    }
    public void update(Camera camera){
        button.setPosition(camera.position.x,camera.position.y);
    }
}

