package com.heshus.game.screens.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.heshus.game.engine.HesHusGame.settings;
import static com.heshus.game.engine.Play.state;
public class SettingsMenu {
    private BitmapFont font;
    private Stage stage;
    private int returnState;//state to go back to once done
    private TextButton returnButton;
    private TextButton applyButton;
    private Table table;
    private Camera camera;
    private ExtendViewport viewport;
    private CheckBox fullScreenCheckbox;
    private boolean isFullScreen;
    public SettingsMenu(int returnState, Camera camera, ExtendViewport viewport) {
        this.returnState = returnState;
        this.viewport = viewport;
        this.camera = camera;


        //set up font
        font = new BitmapFont(Gdx.files.internal("Fonts/monogram/pixel.fnt"), false);
        font.getData().setScale(.5F);
        font.setColor(Color.BLACK);

        //Setup button styles
        Texture buttonTexture = new Texture("UI/button_up.png");
        TextureRegion buttonTextureRegion = new TextureRegion(buttonTexture, buttonTexture.getWidth(), buttonTexture.getHeight());
        TextureRegionDrawable buttonTextureRegionDrawable =new TextureRegionDrawable(buttonTextureRegion);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(buttonTextureRegionDrawable, buttonTextureRegionDrawable, buttonTextureRegionDrawable, font );

        CheckBox.CheckBoxStyle checkBoxStyle = getCheckBoxStyle();

        //Return button:
        returnButton = new TextButton("Return", textButtonStyle); //Set the button up
        returnButton.padBottom(6);
        returnButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                state = returnState;
                return false;
            }
        });
        //Apply button
        applyButton = new TextButton("Apply", textButtonStyle);
        applyButton.padBottom(6);
        applyButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                applySettings();
                state = returnState;
                return false;
            }
        });


        fullScreenCheckbox = new CheckBox("Fullscreen", checkBoxStyle);
        fullScreenCheckbox.addListener(new InputListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                fullScreenCheckbox.setChecked(!fullScreenCheckbox.isChecked());
            }
        });

        table = new Table();
        
        table.add(returnButton);
        table.row();
        table.add(applyButton);
        table.row();
        table.add(fullScreenCheckbox);
        stage = new Stage(viewport);
        stage.addActor(table);
    }

    private CheckBox.CheckBoxStyle getCheckBoxStyle() {
        //Just setting up checkboxes, would have maybe been better setting up a Skin
        Texture uncheckedBoxTex = new Texture("UI/unchecked.png");
        TextureRegion uncheckedBoxTextureRegion = new TextureRegion(uncheckedBoxTex, uncheckedBoxTex.getWidth(), uncheckedBoxTex.getHeight());
        TextureRegionDrawable uncheckedBoxTextureRegionDrawable =new TextureRegionDrawable(uncheckedBoxTextureRegion);

        Texture checkedBoxTex = new Texture("UI/checked.png");
        TextureRegion checkedBoxTextureRegion = new TextureRegion(checkedBoxTex, checkedBoxTex.getWidth(), checkedBoxTex.getHeight());
        TextureRegionDrawable checkedBoxTextureRegionDrawable =new TextureRegionDrawable(checkedBoxTextureRegion);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(uncheckedBoxTextureRegionDrawable, checkedBoxTextureRegionDrawable, font, Color.BLACK );
        return checkBoxStyle;
    }

    private void applySettings() {
        settings.putBoolean ("isFullScreen",fullScreenCheckbox.isChecked());

        int width = settings.getInteger("windowWidth");
        int height = settings.getInteger("windowHeight");
        if (settings.getBoolean("isFullScreen")){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        else{
            Gdx.graphics.setWindowedMode(width, height);
        }
        settings.flush();
    }

    public void update() {
        Gdx.input.setInputProcessor(stage);
        table.setPosition(camera.position.x, camera.position.y);
        stage.draw();

    }


}
