package com.heshus.game.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.heshus.game.entities.Player;
import com.heshus.game.manager.ActivityManager;
import com.heshus.game.manager.DayManager;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.awt.*;


public class Play implements Screen {

    private final HesHusGame game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private static BitmapFont font;
    private TiledMapTileLayer collisionLayer;
    private ActivityManager activityManager;
    // private Game game;

    private Sprite blankTexture, textBubble;
    private Texture TblankTexture, textBubbleTexture;
    private ExtendViewport extendViewport;

    public Play(HesHusGame game) {
        this.game = game;

    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        int cameraSmoothness = 4; //higher looks smoother! makes it take longer for camera to reach player pos
        camera.position.set(((player.getX() + player.getWidth() / 2)+(camera.position.x *(cameraSmoothness-1)))/cameraSmoothness, ((player.getY() + player.getHeight() / 2)+(camera.position.y *(cameraSmoothness-1)))/cameraSmoothness, 0);
        lockCameraInTiledmaplayer(camera,(TiledMapTileLayer) map.getLayers().get(1)); //locks camera position so it cannot show out of bounds
        camera.position.set(Math.round(camera.position.x) ,Math.round(camera.position.y),0);//This is needed to stop black lines between tiles. I think something to do with the tilemaprenderer and floats causes this
        camera.viewportWidth = Math.round(camera.viewportWidth);
        camera.viewportHeight = Math.round(camera.viewportHeight);
        camera.update();

        renderer.setView(camera);
        renderer.render(); //takes a layers[] argument if we want to specifically render certain layers
        renderer.getBatch().begin();
        player.draw(renderer.getBatch());


        activityManager.checkActivity();
        // Just for testing of counter
        font.draw(renderer.getBatch(), "Eat: " + DayManager.currentDay.getEatScore(), 100, Gdx.graphics.getHeight() + 100);
        font.draw(renderer.getBatch(), "Study: " + DayManager.currentDay.getStudyScore(), 100, Gdx.graphics.getHeight() + 70);
        String dayCounter = "Day: " + DayManager.currentDay.getDayNumber() + " of 7 days";
        font.draw(renderer.getBatch(), dayCounter, 100, Gdx.graphics.getHeight() + 40);
        font.draw(renderer.getBatch(), "Recreational Activity: " + DayManager.currentDay.getRecreationalScore(), 100, Gdx.graphics.getHeight() + 10);

        //Drawing energy bar
        renderer.getBatch().setColor(Color.GRAY);
        renderer.getBatch().draw(blankTexture, (camera.position.x - camera.viewportWidth/2) + 3, (camera.position.y - camera.viewportHeight/2) + 3, 204, 44);
        renderer.getBatch().setColor(Color.YELLOW);
        renderer.getBatch().draw(blankTexture, (camera.position.x - camera.viewportWidth/2) + 5, (camera.position.y - camera.viewportHeight/2) + 5, 200 * ((float) DayManager.currentDay.getEnergy() /100), 40);
        renderer.getBatch().setColor(Color.WHITE);

        //Draw activity text
        if(!activityManager.getText().isEmpty()){
            font.getData().setScale(1f);
            GlyphLayout layout = new GlyphLayout();
            layout.setText(font, activityManager.getText());
            renderer.getBatch().draw(textBubble, activityManager.getTextPosition().x - 2, activityManager.getTextPosition().y, layout.width + 4, 50);
            activityManager.drawTextBubble((SpriteBatch) renderer.getBatch(), font);
            font.getData().setScale(2f);
        }

        renderer.getBatch().end();


    }


    @Override
    public void show() {
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
        float startX = 34 * collisionLayer.getTileWidth();
        float startY = (collisionLayer.getHeight() - 25) * collisionLayer.getTileHeight();
        player.setPosition(startX, startY);
        Gdx.input.setInputProcessor(player);

        // Set up the activity manager
        activityManager = new ActivityManager(collisionLayer);
        activityManager.setPlayer(player); // Ensure you have a setPlayer method in ActivityManager

        // Set up the font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        // Set up texture for energy bar
        TblankTexture = new Texture("WhiteSquare.png");
        blankTexture = new Sprite(TblankTexture);

        // Set up text bubble
        textBubbleTexture = new Texture("textBubble.png");
        textBubble = new Sprite(textBubbleTexture);

        // Other initializations as needed...
    }




//    @Override
//    public void show() {
//        TmxMapLoader loader = new TmxMapLoader();
//        map = loader.load("testmap.tmx");
//        //make this one line: map = new TmxMapLoader().load(path);
//        //remember to put both the map and all tilemaps in assets folder
//        //also have to consider: if you create the map elsewhere (not directly in the assets folder) (and save it to desktop or something)
//        //then you have to go into the .tsx files and change the filepaths of the tilemap png's
//
//        renderer = new OrthogonalTiledMapRenderer(map); //can also take a scale argument
//        camera = new OrthographicCamera(); //don't need to specify width and height because resize() is called after show()
//
//        player = new Player(new Sprite(new Texture("player.png")), (TiledMapTileLayer) map.getLayers().get(0));
//        float startX = 34 * player.getCollisionLayer().getTileWidth();
//        float startY = (player.getCollisionLayer().getHeight() - 25) * player.getCollisionLayer().getTileHeight();
//        activityManager.setPlayer(player);
//        this.activityManager = new ActivityManager(collisionLayer);
//        player.setPosition(startX, startY);
//        Gdx.input.setInputProcessor(player);
//
//        // Testing counter
//        this.font = new BitmapFont();
//        font.setColor(Color.WHITE);
//        font.getData().setScale(2);
//    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(((width+7)/16)*16, ((height+7)/16)*16); //updates size of window for viewport when things get resized, rounds up to the nearest tilewidth
        camera.update();
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
    }

    private void lockCameraInTiledmaplayer(OrthographicCamera cam, TiledMapTileLayer layer){
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

    public static BitmapFont getFont(){
        return font;
    }

}

