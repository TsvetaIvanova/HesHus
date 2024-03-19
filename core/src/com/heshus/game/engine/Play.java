package com.heshus.game.engine;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.heshus.game.entities.Player;
import com.heshus.game.manager.ActivityManager;
import com.heshus.game.manager.DayManager;
import com.heshus.game.screens.states.PauseMenu;
import com.heshus.game.screens.states.SettingsMenu;

import static com.heshus.game.engine.HesHusGame.settings;

public class Play implements Screen {
    public static final int GAME_RUNNING = 0;
    public static final int GAME_PAUSED = 1;
    public static final int GAME_SETTINGS = 2;
    public static final int GAME_OVER = 3;
    public static final int GAME_MAINMENU =4;
    public static int state;
    private final HesHusGame game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private BitmapFont font;
    private TiledMapTileLayer collisionLayer;
    private ActivityManager activityManager;
    private Sprite energyBar;
    private Texture energyBarTexture;
    private ExtendViewport extendViewport;
    private PauseMenu pauseMenu;
    private Texture counterBoxTexture;
    private Texture burgerIconTexture, studyIconTexture, playIconTexture;
    private Sprite burgerIconSprite, studyIconSprite, playIconSprite;
    private Texture verticalBarTexture;
    private Sprite verticalBarSprite;
    private SettingsMenu settingsMenu;

    public Play(HesHusGame game) {
        this.game = game;
    }
    @Override
    public void render(float delta) {
        update();
        draw();

    }

    private void draw(){
        ScreenUtils.clear(0,0,0,1);
        //CAMERA
        int cameraSmoothness = 4; //higher looks smoother! makes it take longer for camera to reach player pos
        camera.position.set(((player.getX() + player.getWidth() / 2)+(camera.position.x *(cameraSmoothness-1)))/cameraSmoothness, ((player.getY() + player.getHeight() / 2)+(camera.position.y *(cameraSmoothness-1)))/cameraSmoothness, 0);
        lockCameraInTiledMapLayer(camera,(TiledMapTileLayer) map.getLayers().get(1)); //locks camera position so it cannot show out of bounds
        camera.position.set(Math.round(camera.position.x) ,Math.round(camera.position.y),0);//This is needed to stop occasional black lines between tiles. Makes camera movement slightly jagged/stuttery looking, padding the tilemap instead would fix this.
        camera.update();

        //Tilemap:
        renderer.setView(camera);
        renderer.render(); //takes a layers[] argument if we want to specifically render certain layers
        renderer.getBatch().begin();
        //Player
        player.draw(renderer.getBatch());

        switch (state) {
            case(GAME_RUNNING):
                //HUD
                //Drawing energy bar
                renderer.getBatch().setColor(Color.GRAY);
                renderer.getBatch().draw(energyBar, (camera.position.x - camera.viewportWidth/2) + 3, (camera.position.y - camera.viewportHeight/2) + 3, 204, 44);
                renderer.getBatch().setColor(Color.YELLOW);
                renderer.getBatch().draw(energyBar, (camera.position.x - camera.viewportWidth/2) + 5, (camera.position.y - camera.viewportHeight/2) + 5, 200 * ((float) DayManager.currentDay.getEnergy() /100), 40);
                renderer.getBatch().setColor(Color.WHITE);

                ///////////////////////////////////////////////////////////////////////////
                // The Counter and Counter Icons                                         //
                // Upper-left corner position for the counter box set and will not move //
                float counterBoxX = camera.position.x - camera.viewportWidth / 2;
                float counterBoxY = (camera.position.y + camera.viewportHeight / 2) - counterBoxTexture.getHeight();

                renderer.getBatch().draw(counterBoxTexture, counterBoxX, counterBoxY);

                float iconSize = 20;
                float iconSpacingX = 2;
                float iconSpacingY = 8;
                float verticalBarStartX = counterBoxX + iconSpacingX + 24;
                float verticalBarStartY = counterBoxY + counterBoxTexture.getHeight() - iconSpacingY - iconSize - iconSpacingY + 13;

                // setting up the font size and colour
                font.getData().setScale(1f);
                font.setColor(Color.BLACK);

                // Defining the Y position for each row
                float firstRowY = counterBoxY + counterBoxTexture.getHeight() - iconSpacingY - iconSize - 20;
                float secondRowY = firstRowY - iconSize - iconSpacingY;
                float thirdRowY = secondRowY - iconSize - iconSpacingY;

                font.draw(renderer.getBatch(), String.valueOf(DayManager.overallEatScore), counterBoxX + 43, firstRowY+18);
                font.draw(renderer.getBatch(), String.valueOf(DayManager.overallStudyScore), counterBoxX + 43, secondRowY+27);
                font.draw(renderer.getBatch(), String.valueOf(DayManager.overallRecreationalScore), counterBoxX + 43, thirdRowY+36);

                // Draw the Day icon in the first row
                for (int i = 0; i < DayManager.currentDay.getDayNumber(); i++) {
                    renderer.getBatch().draw(verticalBarSprite, verticalBarStartX+15 + (5 + iconSpacingX) * i, verticalBarStartY, 5, 20);
                }
                renderer.getBatch().end();
                break;

            case (GAME_PAUSED): 
                //Pause menu
                renderer.getBatch().end();
                pauseMenu.draw();
                break;
            case (GAME_SETTINGS):
                //Settings menu
                renderer.getBatch().end();
                settingsMenu.update();
                }

                
//        // Draw Eat icons in the second row
//        for (int i = 0; i < DayManager.currentDay.getEatScore(); i++) {
//            renderer.getBatch().draw(burgerIconSprite, counterBoxX + 20 + (iconSize + iconSpacingX) * i, firstRowY+7, iconSize, iconSize);
//        }
//        // Draw Study icons in the third row
//        for (int i = 0; i < DayManager.currentDay.getStudyScore(); i++) {
//            renderer.getBatch().draw(studyIconSprite, counterBoxX + 10 + 20 + (iconSize + iconSpacingX) * i, secondRowY + 10, iconSize, iconSize);
//        }
//        // Draw Recreation icons in the fourth row
//        for (int i = 0; i < DayManager.currentDay.getRecreationalScore(); i++) {
//            renderer.getBatch().draw(playIconSprite, counterBoxX + 20 + 30 + (iconSize + iconSpacingX) * i, thirdRowY + 15, iconSize, iconSize );
//        }
        


    }
    private void update(){
        //Detect if game should be paused or not
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)||Gdx.input.isKeyJustPressed(Input.Keys.P)){
            if (state!=GAME_PAUSED) {
                state = GAME_PAUSED;
            }
            else{
                state = GAME_RUNNING;
            }
        }
        //logic/physics - anything that moves
        switch (state){
            case (GAME_RUNNING):
                Gdx.input.setInputProcessor(player);
                player.update(Gdx.graphics.getDeltaTime());
                break;
            case (GAME_PAUSED):
                player.update(0);
                player.setVelocity(new Vector2(0,0));
                pauseMenu.update(camera);
                break;
        }
        activityManager.checkActivity();
    }

    @Override
    public void show() {
        //set state
        state = GAME_RUNNING;

        // Initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 450);
        extendViewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);

        // Load the map and set up the renderer
        map = new TmxMapLoader().load("testmap.tmx");
        collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 1f);

        // Set up the player
        Texture playerTexture = new Texture("player.png");
        Sprite playerSprite = new Sprite(playerTexture);
        player = new Player(playerSprite, collisionLayer);
        float startX = 30 * collisionLayer.getTileWidth();
        float startY = (collisionLayer.getHeight() - 26) * collisionLayer.getTileHeight();
        player.setPosition(startX, startY);
        Gdx.input.setInputProcessor(player);

        // Set up the activity manager
        activityManager = new ActivityManager(collisionLayer);
        activityManager.setPlayer(player); // Ensure you have a setPlayer method in ActivityManager

        // Set up the font
        font = new BitmapFont();
        font.getData().setScale(2);

        // Set up texture for energy bar
        energyBarTexture = new Texture("WhiteSquare.png");
        energyBar = new Sprite(energyBarTexture);

        //setup menus
        pauseMenu = new PauseMenu(extendViewport, camera);
        settingsMenu = new SettingsMenu(GAME_PAUSED, camera, extendViewport);

        // Set up the counter and counter components
        counterBoxTexture = new Texture("counter-box.png");

        burgerIconTexture = new Texture("burgerDouble.png");
        studyIconTexture = new Texture("study.png");
        playIconTexture = new Texture("game.png");
       
        burgerIconSprite = new Sprite(burgerIconTexture);
        studyIconSprite = new Sprite(studyIconTexture);
        playIconSprite = new Sprite(playIconTexture);
       
        verticalBarTexture = new Texture("vertical-bar.png");
        verticalBarSprite = new Sprite(verticalBarTexture);
        
        // Other initializations as needed...

    }
    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        //extendViewport.update(((width+7)/16)*16, ((height+7)/16)*16); //updates size of window for viewport when things get resized, rounds up to the nearest tilewidth
        extendViewport.update(width,height);
        //camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        //if we forget to dispose something it causes a memory leak!
        //if this is a big concern we should change our approach to assets
        //using the AssetManager means we have to load assets in a different way
        //BUT we can just call AssetManager.dispose() and it will for sure dispose all our assets correctly

        map.dispose();
        renderer.dispose();
        player.getTexture().dispose();
        font.dispose();
        counterBoxTexture.dispose();
        burgerIconTexture.dispose();
        studyIconTexture.dispose();
        playIconTexture.dispose();
        verticalBarTexture.dispose();
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


}

