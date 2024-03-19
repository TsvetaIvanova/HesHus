package com.heshus.game.screens.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.awt.*;
import static com.heshus.game.engine.Play.state;
public class SettingsMenu {
    private BitmapFont font;
    private Stage stage;
    private int returnState;//state to go back to once done
    private TextButton returnButton;
    private Table table;
    private Camera camera;
    private ExtendViewport viewport;
    public SettingsMenu(int returnState, Camera camera, ExtendViewport viewport) {
        this.returnState = returnState;
        this.viewport = viewport;
        this.camera = camera;
        //set up font
        font = new BitmapFont(Gdx.files.internal("Fonts/monogram/pixel.fnt"), false);
        font.getData().setScale(.5F);
        font.setColor(Color.BLACK);

        //Setup textures and variables
        Texture buttonTexture = new Texture("UI/button_up.png");
        TextureRegion buttonTextureRegion = new TextureRegion(buttonTexture, buttonTexture.getWidth(), buttonTexture.getHeight());
        TextureRegionDrawable buttonTextureRegionDrawable =new TextureRegionDrawable(buttonTextureRegion);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(buttonTextureRegionDrawable, buttonTextureRegionDrawable, buttonTextureRegionDrawable, font );

        //Return button:
        returnButton = new TextButton("Return", textButtonStyle); //Set the button up
        returnButton.padBottom(6);
        returnButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                state = returnState;
                return false;
            }
        });

        table = new Table();
        table.add(returnButton);
        stage = new Stage(viewport);
        stage.addActor(table);
    }

    public void update() {
        Gdx.input.setInputProcessor(stage);
        System.out.println(camera.viewportWidth);
        table.setPosition(camera.position.x, camera.position.y);
        stage.draw();

    }


}
