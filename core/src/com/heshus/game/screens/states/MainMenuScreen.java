package com.heshus.game.screens.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.heshus.game.editor.CustomiseSprite;
import com.heshus.game.engine.HesHusGame;

import static com.heshus.game.engine.Play.*;

/**
 * Displays buttons to quit, start new game, change settings
 * Also does zoomed in scrolling of our map :)
 */
public class MainMenuScreen implements Screen {

    final HesHusGame game;
    private ExtendViewport extendViewport;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    int mapPixelWidth;
    int mapPixelHeight;
    BitmapFont font;
    TextButton settingsButton;
    TextButton quitButton;
    TextButton newButton;
    private Stage stage;
    private Table mainTable;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextButton.TextButtonStyle newGameTextButtonStyle;
    private SettingsMenu settingsMenu;
    int xSpeed;
    int ySpeed;
    private Sound clickSound;
    private TextButton gotItButton;
    private boolean isNewGameClicked;
    /**
    *Constructor initiates variables and sets up listeners for buttons
    * @param game instance of central class HesHusGame
    * */
    public MainMenuScreen(final HesHusGame game) {
        this.game = game;
        state = GAME_MAINMENU;
        //Map for background initialisation
        map = new TmxMapLoader().load("MapRelated/testmap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 1f);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        mapPixelWidth = layer.getWidth() * layer.getTileWidth() ; //just calculate the width and height of tilemap
        mapPixelHeight = layer.getHeight() * layer.getTileHeight();

        //Camera initialisation
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 225); //must be same ratio as screen or black lines between tiles can appear
        camera.position.x=0;
        camera.position.y=0;
        xSpeed=1;//we will move camera by these each frame
        ySpeed=1;
        extendViewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);

        //set up font
        font = new BitmapFont(Gdx.files.internal("Fonts/monogram/pixel.fnt"), false);
        font.getData().setScale(.5F);
        font.setColor(Color.BLACK);

        //BUTTONS
        //set style
        initialiseTextButtonStyles();

        //Settings button:
        settingsButton = new TextButton("SETTINGS", textButtonStyle); //Set the button up
        settingsButton.padBottom(7);//center text in graphic
        settingsButton.setScale(1F);

        //Quit button:
        quitButton = new TextButton("QUIT :(", textButtonStyle); //Set the button up
        quitButton.padBottom(6);

        //New Game button
        newButton = new TextButton("NEW GAME!!", newGameTextButtonStyle); //Set the button up
        newButton.padBottom(6);

        //Got it! button for controls popup
        gotItButton = new TextButton("Got it!", textButtonStyle); //Set the button up
        gotItButton.padBottom(6);

        //set logic
        buttonsOnClickLogic();

        //Add everything to a table!
        mainTable = new Table();
        mainTable.add(newButton).colspan(2).padBottom(3);
        mainTable.row();
        mainTable.add(settingsButton).padRight(3);
        mainTable.add(quitButton);
        mainTable.setFillParent(true);
        stage = new Stage(extendViewport);
        stage.addActor(mainTable);
        //We draw this instead of mainTable when settingsButton is clicked
        settingsMenu = new SettingsMenu(state, camera, extendViewport, 1);
    }

    /**
     * sets up what happens when each button is clicked(could be in constructor but seems easier to read this way)
     */
    private void buttonsOnClickLogic() {
        //QUIT BUTTON: Quits (delay for the sound to play)
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                }, 0.5f);
            }
        });

        //NEWGAME BUTTON: Pops up controls, and the gotIt confirmation button
        newButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();
                isNewGameClicked=true;
                return false;
            }
        });
        //SETTINGS BUTTON: sets state to settings if clicked!
        settingsButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();
                state = GAME_SETTINGS;
                return false;
            }
        });
        //GOTITBUTTON: confirms player knows controls
        gotItButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clickSound.play();
                game.setScreen(new CustomiseSprite(game, camera));
                dispose();
                return false;
            }
        });
    }

    /**
     * set button styles
     */
    private void initialiseTextButtonStyles() {
        //Setup buttonstyles! (a new TextureRegionDrawable version for down would be nice and would kinda animate them)
        Texture buttonTexture = new Texture("UI/button_up.png");
        TextureRegion buttonTextureRegion= new TextureRegion(buttonTexture, buttonTexture.getWidth(), buttonTexture.getHeight());
        TextureRegionDrawable buttonTextureRegionDrawable =new TextureRegionDrawable(buttonTextureRegion);
        textButtonStyle = new TextButton.TextButtonStyle(buttonTextureRegionDrawable, buttonTextureRegionDrawable, buttonTextureRegionDrawable, font );
        //w i d e  b u t t o n (its wider)
        Texture newButtonTexture = new Texture("UI/wide_button.png");
        TextureRegion newButtonTextureRegion= new TextureRegion(newButtonTexture, newButtonTexture.getWidth(), newButtonTexture.getHeight());
        TextureRegionDrawable newButtonTextureRegionDrawable =new TextureRegionDrawable(newButtonTextureRegion);
        newGameTextButtonStyle = new TextButton.TextButtonStyle(newButtonTextureRegionDrawable, newButtonTextureRegionDrawable, newButtonTextureRegionDrawable, font );
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        clickSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/switch2.ogg"));
    }

    /**
     * Called when the screen should render itself.
     * Renders
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
            ScreenUtils.clear(0, 0.2f, 0, 1);
            //only one inputprocessor-buttton input listeners won't work without this (but you could multiplex it if needed)
            Gdx.input.setInputProcessor(stage);

            //draws tilemap
            renderer.setView(camera);
            renderer.render();

            switch (state) {
                case (GAME_MAINMENU):
                    game.batch.begin();
                    if (isNewGameClicked) {
                        //Display controls (doesn't actually center text, just gets kinda close)
                        font.draw(game.batch, "P or Esc to pause,E to interact ", camera.position.x- camera.viewportWidth/2 +camera.viewportWidth/6 , camera.position.y );
                        font.draw(game.batch, "   WASD or Arrowkeys to move    ", camera.position.x- camera.viewportWidth/2 +camera.viewportWidth/6 , camera.position.y +font.getCapHeight()*2 );

                        mainTable.setVisible(false);
                        stage.addActor(gotItButton);
                        gotItButton.setPosition(camera.position.x -gotItButton.getWidth()/2, camera.position.y - camera.viewportHeight / 2 + gotItButton.getHeight());
                        stage.draw();
                    }
                    else {
                        font.draw(game.batch, "Welcome to HeslingtonHustle!!! ", camera.position.x - camera.viewportWidth / 2, camera.position.y + camera.viewportHeight / 2);
                        //update position of table to middle of screen and draw
                        mainTable.setPosition(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2);
                        stage.draw();//draws all buttons!
                    }
                    game.batch.end();
                    break;
                case(GAME_SETTINGS):
                    //draws and updates settingsMenu
                    settingsMenu.update();
                    break;
            }

            //moving camera
            camera.position.x+=xSpeed;
            camera.position.y+=ySpeed;
            //"bounce" camera if it's about to show out of the map! (if the Tiledmap has a yOffSet or xOffSet will probably break)
            if(camera.position.y+ySpeed + camera.viewportHeight/2>= mapPixelHeight ||camera.position.y+ySpeed- camera.viewportHeight/2<=0 ){
                ySpeed*=-1;
            }
            if(camera.position.x+xSpeed + camera.viewportWidth/2>= mapPixelWidth ||camera.position.x+xSpeed- camera.viewportWidth/2<=0 ){
                xSpeed*=-1;
            }
            camera.update();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        extendViewport.update(width,height); //updates size of window for viewport when things get resized, rounds up to the nearest tilewidth
        camera.update();
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
        stage.dispose();
        settingsMenu.dispose();

    }

}
