package com.heshus.game.screens.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heshus.game.engine.HesHusGame;
import com.heshus.game.engine.Play;

import java.util.Random;
public class MainMenuScreen implements Screen {

    final HesHusGame game;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    Vector2 goTo;
    int mapPixelWidth;
    int mapPixelHeight;
    float clock;
    BitmapFont font;
    TextButton settingsButton;

    public MainMenuScreen(final HesHusGame game) {
        this.game = game;


        //set up font
        font = new BitmapFont(Gdx.files.internal("Fonts/monogram/pixel.fnt"), false);
        font.getData().setScale(1.5F);
        font.setColor(Color.BLACK);

        //BUTTONS
        //Setup textures and variables
        Texture buttonTexture = new Texture("UI/button_up.png");
        TextureRegion buttonTextureRegion= new TextureRegion(buttonTexture, buttonTexture.getWidth(), buttonTexture.getHeight());
        TextureRegionDrawable buttonTextureRegionDrawable =new TextureRegionDrawable(buttonTextureRegion);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(buttonTextureRegionDrawable, buttonTextureRegionDrawable, buttonTextureRegionDrawable, font );

        //Settings button:
        settingsButton = new TextButton("SETTINGS", textButtonStyle); //Set the button up
        settingsButton.padBottom(10);
        settingsButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return false;
            }
        });

        goTo = new Vector2(0,0);
        map = new TmxMapLoader().load("testmap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 1f);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        mapPixelWidth = layer.getWidth() * layer.getTileWidth() ;
        mapPixelHeight = layer.getHeight() * layer.getTileHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 240);
        camera.position.x=0;
        camera.position.y=0;

        clock =4;
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
            //tilemap
            renderer.setView(camera);
            renderer.render();
            //text
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            game.font.draw(game.batch, "Welcome to HeslingtonHustle!!! ", camera.position.x, camera.position.y);
            game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);

            //buttons
            settingsButton.setPosition(camera.position.x+camera.viewportWidth/6,camera.position.y+ camera.viewportHeight/3);
            settingsButton.draw(game.batch, 1);
            game.batch.end();

            if (Gdx.input.isTouched()) {
                game.setScreen(new Play(game));
                dispose();
            }
            //Camera
            //int cameraSmoothness = 400; //higher looks smoother! makes it take longer for camera to reach player pos
            //camera.position.set((goTo.x+(camera.position.x *(cameraSmoothness-1)))/cameraSmoothness, ((goTo.y/ 2)+(camera.position.y *(cameraSmoothness-1)))/cameraSmoothness, 0);

            lockCameraInTiledMapLayer(camera,(TiledMapTileLayer) map.getLayers().get(0)); //locks camera position so it cannot show out of bounds
            camera.position.set(Math.round(camera.position.x) ,Math.round(camera.position.y),0);//This is needed to stop black lines between tiles. I think something to do with the tilemaprenderer and floats causes this
            camera.viewportWidth = Math.round(camera.viewportWidth);
            camera.viewportHeight = Math.round(camera.viewportHeight);
            camera.update();
            System.out.println(goTo.y);

            clock+=delta;
            if (clock>4){
                if (camera.position.y>200||camera.position.y==0){
                    goTo.y = 0;
                    goTo.x = 0;
                }
                else {
                    goTo.y =0;
                    goTo.x =mapPixelWidth;
                }
                clock=0;
            }

    }
    private void lockCameraInTiledMapLayer(OrthographicCamera cam, TiledMapTileLayer layer){
        //get variables needed to find edges of map!
        int mapPixelOffsetY =(int) layer.getOffsetY();
        int mapPixelOffsetX =(int) layer.getOffsetX();
        int mapPixelWidth = layer.getWidth() * layer.getTileWidth() + mapPixelOffsetX;
        int mapPixelHeight = layer.getHeight() * layer.getTileHeight() + mapPixelOffsetY;
        //if the camera viewport is large enough to see outside the map on both sides at once, there is no useful way to lock it. this shouldn't happen
        if (cam.viewportWidth>mapPixelWidth || cam.viewportHeight>mapPixelHeight){
            return;
        }

        //check if camera would show out of bounds, lock it in bounds if it would
        if ((cam.position.x- (cam.viewportWidth/2)< mapPixelOffsetX))//is the camera too far left.
        {
            cam.position.x = mapPixelOffsetX + cam.viewportWidth/2;
        }
        else if ((cam.position.x+ (cam.viewportWidth/2)> mapPixelWidth))//is the camera too far right.
        {
            cam.position.x = mapPixelWidth - cam.viewportWidth/2;
        }
        if ((cam.position.y- (cam.viewportHeight/2)< mapPixelOffsetY))//is the camera too low.
        {
            cam.position.y = mapPixelOffsetY + cam.viewportHeight/2;
        }
        else if ((cam.position.y+ (cam.viewportHeight/2)> mapPixelHeight))//is the camera too high.
        {
            cam.position.y = mapPixelHeight - cam.viewportHeight/2;
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
