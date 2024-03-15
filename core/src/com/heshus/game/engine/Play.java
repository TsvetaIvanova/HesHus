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
import com.heshus.game.entities.Player;
import com.heshus.game.manager.ActivityManager;
import com.heshus.game.manager.DayManager;

public class Play implements Screen {

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
    private Sprite burgerIcon;
    private Sprite bookIcon;
    private Sprite gameIcon;
    private Texture burgerIconTexture;
    private Texture bookIconTexture;
    private Texture gameIconTexture;


    public Play(HesHusGame game) {
        this.game = game;

    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);

        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        //camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);
        camera.update();

        renderer.setView(camera);
        renderer.render(); //takes a layers[] argument if we want to specifically render certain layers
        renderer.getBatch().begin();
        player.draw(renderer.getBatch());


        activityManager.checkActivity();
        // Just for testing of counter
//        font.draw(renderer.getBatch(), "Eat: " + DayManager.currentDay.getEatScore(), 100, Gdx.graphics.getHeight() + 100);
//        font.draw(renderer.getBatch(), "Study: " + DayManager.currentDay.getStudyScore(), 100, Gdx.graphics.getHeight() + 70);
//        String dayCounter = "Day: " + DayManager.currentDay.getDayNumber() + " of 7 days";
//        font.draw(renderer.getBatch(), dayCounter, 100, Gdx.graphics.getHeight() + 40);
//        font.draw(renderer.getBatch(), "Recreational Activity: " + DayManager.currentDay.getRecreationalScore(), 100, Gdx.graphics.getHeight() + 10);

        //Drawing energy bar
        renderer.getBatch().setColor(Color.GRAY);
        renderer.getBatch().draw(energyBar, (camera.position.x - camera.viewportWidth/2) + 3, (camera.position.y - camera.viewportHeight/2) + 3, 204, 44);
        renderer.getBatch().setColor(Color.YELLOW);
        renderer.getBatch().draw(energyBar, (camera.position.x - camera.viewportWidth/2) + 5, (camera.position.y - camera.viewportHeight/2) + 5, 200 * ((float) DayManager.currentDay.getEnergy() /100), 40);
        renderer.getBatch().setColor(Color.WHITE);

        drawActivityIcons();

        renderer.getBatch().end();


    }
    private void drawActivityIcons() {
        float iconSize = 16;
        float iconSpacing = 5;
        float maxIcons = 5;
        float padding = 10;
        float boxMargin = 10;


        GlyphLayout eatLayout = new GlyphLayout(font, "Eat: ");
        GlyphLayout studyLayout = new GlyphLayout(font, "Study: ");
        GlyphLayout recreationLayout = new GlyphLayout(font, "Recreational Activity: ");
        GlyphLayout dayCounterLayout = new GlyphLayout(font, "Day: " + DayManager.currentDay.getDayNumber() + " of 7 days");


        float textWidth = Math.max(dayCounterLayout.width, Math.max(eatLayout.width, Math.max(studyLayout.width, recreationLayout.width)));


        float boxWidth = padding * 2 + textWidth + (iconSize + iconSpacing) * maxIcons;


        float boxHeight = padding + dayCounterLayout.height + padding + (iconSize + padding) * 3 + padding;

        float boxX = boxMargin;
        float boxY = camera.viewportHeight - boxHeight - boxMargin;
        renderer.getBatch().setColor(Color.BLACK);
        renderer.getBatch().draw(energyBarTexture, boxX, boxY, boxWidth, boxHeight);
        renderer.getBatch().setColor(Color.WHITE);


        font.draw(renderer.getBatch(), dayCounterLayout, boxX + padding, boxY + boxHeight - padding);

        float iconY = boxY + boxHeight - padding - dayCounterLayout.height - padding - iconSize;


        font.draw(renderer.getBatch(), "Eat: ", boxX + padding, iconY + iconSize);
        float iconsStartX = boxX + padding + eatLayout.width;
        for (int i = 0; i < DayManager.currentDay.getEatScore(); i++) {
            burgerIcon.setSize(iconSize, iconSize);
            burgerIcon.setPosition(iconsStartX + i * (iconSize + iconSpacing), iconY);
            burgerIcon.draw(renderer.getBatch());
        }


        iconY -= iconSize + padding;


        font.draw(renderer.getBatch(), "Study: ", boxX + padding, iconY + iconSize);
        iconsStartX = boxX + padding + studyLayout.width;
        for (int i = 0; i < DayManager.currentDay.getStudyScore(); i++) {
            bookIcon.setSize(iconSize, iconSize);
            bookIcon.setPosition(iconsStartX + i * (iconSize + iconSpacing), iconY);
            bookIcon.draw(renderer.getBatch());
        }


        iconY -= iconSize + padding;


        font.draw(renderer.getBatch(), "Recreational Activity: ", boxX + padding, iconY + iconSize);
        iconsStartX = boxX + padding + recreationLayout.width;
        for (int i = 0; i < DayManager.currentDay.getRecreationalScore(); i++) {
            gameIcon.setSize(iconSize, iconSize);
            gameIcon.setPosition(iconsStartX + i * (iconSize + iconSpacing), iconY);
            gameIcon.draw(renderer.getBatch());
        }
    }


    @Override
    public void show() {
        // Initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

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
        energyBarTexture = new Texture("WhiteSquare.png");
        energyBar = new Sprite(energyBarTexture);

        burgerIconTexture = new Texture("burgerDouble.png");
        bookIconTexture = new Texture("study.png");
        gameIconTexture = new Texture("game.png");

        burgerIcon = new Sprite(new Texture("burgerDouble.png"));
        bookIcon = new Sprite(new Texture("study.png"));
        gameIcon = new Sprite(new Texture("game.png"));
    }






    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void resize(int width, int height) {

        camera.viewportWidth = width / 1f;
        camera.viewportHeight = height / 1f;
        camera.update();

        renderer.setView(camera);
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
        burgerIconTexture.dispose();
        bookIconTexture.dispose();
        gameIconTexture.dispose();

    }
}

