package com.heshus.game.engine;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.heshus.game.entities.Player;
import com.heshus.game.manager.ActivityManager;
import com.heshus.game.manager.Day;
import com.heshus.game.manager.DayManager;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.audio.Sound;

import java.awt.*;

import com.heshus.game.screens.states.GameOverScreen;
import com.heshus.game.screens.states.PauseMenu;
import com.heshus.game.screens.states.SettingsMenu;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

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
    private static BitmapFont font;
    private TiledMapTileLayer collisionLayer;
    private ActivityManager activityManager;
    // private Game game;
    private float volume = 0.5f;

    private Sprite blankTexture, textBubble, dimTexture;
    private Texture TblankTexture, textBubbleTexture;
    private ExtendViewport extendViewport;

    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;

    private Texture counterBoxTexture;
    private Texture verticalBarTexture;
    private Sprite verticalBarSprite;

    private float bubbleTimer, bubblePeriod = 3;

    // Walking sounds
    private Sound walkingSound1;
    private Sound walkingSound2;
    private Sound walkingSound3;
    private Sound walkingSound4;
    private int currentWalkingSoundIndex = 0;
    private boolean isWalking = false;

    private float walkingSoundTimer = 0;

    // 1/4th of a second delay between sounds, because our avatar is running everywhere
    private final float WALKING_SOUND_DELAY = 0.25f;
    private Music backgroundMusic;

    private Texture increaseVolumeTexture;
    private Texture lowerVolumeTexture;
    private Texture volumeOffTexture;
    private Texture volumeOnTexture;
    private Stage stage;

    private Sprite moonSprite;
    private Texture moonTexture;

    private Sound clickSound;
    private Texture playerTexture;
    private InputMultiplexer inputMultiplexer;
    private Button increaseVolumeButton;
    private Button lowerVolumeButton;
    private Button volumeOffButton;
    private Button volumeOnButton;

    public Play(HesHusGame game, Texture playerSpriteSelection) {
        this.game = game;
        this.playerTexture = playerSpriteSelection;

    }
    @Override
    public void render(float delta) {
        update();
        draw();
        stage.act(delta);
        stage.draw();

    }

    private void draw(){
        ScreenUtils.clear(0,0,0,1);
        //CAMERA
        int cameraSmoothness = 4; //higher looks smoother! makes it take longer for camera to reach player pos
        camera.position.set(((player.getX() + player.getWidth() / 2)+(camera.position.x *(cameraSmoothness-1)))/cameraSmoothness, ((player.getY() + player.getHeight() / 2)+(camera.position.y *(cameraSmoothness-1)))/cameraSmoothness, 0);
        lockCameraInTiledMapLayer(camera,(TiledMapTileLayer) map.getLayers().get(1)); //locks camera position so it cannot show out of bounds
        camera.position.set(Math.round(camera.position.x) ,Math.round(camera.position.y),0);//This is needed to stop black lines between tiles. I think something to do with the tilemaprenderer and floats causes this
        camera.viewportWidth = Math.round(camera.viewportWidth);
        camera.viewportHeight = Math.round(camera.viewportHeight);
        camera.update();


        float padding = 10; // Adjust padding as needed
        float buttonSize = 50; // The size of the buttons, adjust as needed
        // Calculate the positions based on the updated camera position
        float baseX = camera.position.x + camera.viewportWidth / 2 - buttonSize - padding;
        float baseY = camera.position.y + camera.viewportHeight / 2 - padding - buttonSize;
        // Set the position for each volume button
        increaseVolumeButton.setPosition(baseX - 2 * buttonSize, baseY);
        lowerVolumeButton.setPosition(baseX - 3 * buttonSize, baseY);
        volumeOffButton.setPosition(baseX - buttonSize, baseY);
        volumeOnButton.setPosition(baseX, baseY);

        //Tilemap
        renderer.setView(camera);
        renderer.render(); //takes a layers[] argument if we want to specifically render certain layers
        renderer.getBatch().begin();
        //Player
        player.draw(renderer.getBatch());


        switch (state) {
            case(GAME_RUNNING):
                activityManager.checkActivity();
                //HUD
                //Drawing energy bar
                renderer.getBatch().setColor(Color.GRAY);
                renderer.getBatch().draw(blankTexture, (camera.position.x - camera.viewportWidth/2) + 3, (camera.position.y - camera.viewportHeight/2) + 3, 204, 44);
                renderer.getBatch().setColor(Color.YELLOW);
                renderer.getBatch().draw(blankTexture, (camera.position.x - camera.viewportWidth/2) + 5, (camera.position.y - camera.viewportHeight/2) + 5, 200 * ((float) DayManager.currentDay.getEnergy() /100), 40);
                renderer.getBatch().setColor(Color.WHITE);

                //Draw activity text
                if(!activityManager.getText().isEmpty()){
                    //logic for drawing bubble
                    font.getData().setScale(1f);
                    //Get text length
                    GlyphLayout layout = new GlyphLayout();
                    layout.setText(font, activityManager.getText());
                    renderer.getBatch().draw(textBubble, activityManager.getTextPosition().x - 2, activityManager.getTextPosition().y, layout.width + 4, 50);
                    activityManager.drawTextBubble((SpriteBatch) renderer.getBatch(), font);
                    font.getData().setScale(2f);
                    //changes timer
                    bubbleTimer += Gdx.graphics.getDeltaTime();
                    //removes bubble after timer ends
                    if(bubbleTimer > bubblePeriod){
                        bubbleTimer -= bubblePeriod;
                        activityManager.setText("", 0, 0);
                    }


                }

                //Dims screen when energy lost
                dimTexture.setAlpha((float)0.4 + DayManager.currentDay.getEnergy());
                dimTexture.draw(renderer.getBatch());

                ///////////////////////////////////////////////////////////////////////////
                // The Counter and Counter Icons                                         //
                // Upper-left corner position for the counter box set and will not move //
                float counterBoxX = camera.position.x - camera.viewportWidth / 2;
                float counterBoxY = (camera.position.y + camera.viewportHeight / 2) - counterBoxTexture.getHeight();

                renderer.getBatch().draw(counterBoxTexture, counterBoxX, counterBoxY);

                //Setup variables for counters
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

                //Draw counters on top of box
                font.draw(renderer.getBatch(), String.valueOf(DayManager.overallEatScore), counterBoxX + 43, firstRowY+18);
                font.draw(renderer.getBatch(), String.valueOf(DayManager.overallStudyScore), counterBoxX + 43, secondRowY+27);
                font.draw(renderer.getBatch(), String.valueOf(DayManager.overallRecreationalScore), counterBoxX + 43, thirdRowY+36);

                // Draw the Day icon in the first row
                for (int i = 0; i < DayManager.currentDay.getDayNumber(); i++) {
                    renderer.getBatch().draw(verticalBarSprite, verticalBarStartX+15 + (5 + iconSpacingX) * i, verticalBarStartY, 5, 20);
                }
                //End of main renderer
                renderer.getBatch().end();
                break;
                case (GAME_PAUSED):
                    //Pause menu
                    renderer.getBatch().end();
                    pauseMenu.update(camera);
                    pauseMenu.draw();
                    break;
                case (GAME_SETTINGS):
                    //Settings menu
                    renderer.getBatch().end();
                    settingsMenu.update();
                    break;

                    //stage.draw();
                }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

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
                Gdx.input.setInputProcessor(inputMultiplexer);
                player.update(Gdx.graphics.getDeltaTime());
                break;
            case (GAME_SETTINGS)://we do the same settings or paused
            case (GAME_PAUSED):
                player.update(0);
                player.setVelocity(new Vector2(0,0));
                pauseMenu.update(camera);
                break;
        }

        // check walking is happening
        if (Math.abs(player.getVelocity().x) > 0 || Math.abs(player.getVelocity().y) > 0) {
            if (!isWalking) {
                isWalking = true;
                currentWalkingSoundIndex = 0;
                walkingSoundTimer = WALKING_SOUND_DELAY;
            }
        } else {
            isWalking = false;
        }

        if(DayManager.gameOver){
            game.setScreen(new GameOverScreen(game));
        }

        playWalkingSound(Gdx.graphics.getDeltaTime());
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void show() {



        // Initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 450);
        extendViewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);
        // Load the map and set up the renderer
        map = new TmxMapLoader().load("MapRelated/testmap.tmx");
        collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 1f);

        // Set up the player
        //int playerSpriteNumber = 5;
        //Texture playerTexture = new Texture("Icons/player-" + Integer.toString(playerSpriteNumber) + ".png");
        Sprite playerSprite = new Sprite(playerTexture);
        player = new Player(playerSprite, collisionLayer);
        float startX = 30 * collisionLayer.getTileWidth();
        float startY = (collisionLayer.getHeight() - 26) * collisionLayer.getTileHeight();
        player.setPosition(startX, startY);
        //Gdx.input.setInputProcessor(player);

        // Set up the activity manager
        activityManager = new ActivityManager(collisionLayer);
        activityManager.setPlayer(player);

        // Set up the font
        font = new BitmapFont();
        font.getData().setScale(2);

        // Set up texture for energy bar
        TblankTexture = new Texture("Icons/WhiteSquare.png");
        blankTexture = new Sprite(TblankTexture);

        // Set up text bubble
        textBubbleTexture = new Texture("Icons/textBubble.png");
        textBubble = new Sprite(textBubbleTexture);

        //setup menu
        pauseMenu = new PauseMenu(extendViewport, camera);
        settingsMenu = new SettingsMenu(GAME_PAUSED,camera,extendViewport,2);
        //set state
        state = GAME_RUNNING;
        // Set up the counter and counter components
        counterBoxTexture = new Texture("Icons/counter-box.png");


        verticalBarTexture = new Texture("Icons/vertical-bar.png");
        verticalBarSprite = new Sprite(verticalBarTexture);

        walkingSound1 = Gdx.audio.newSound(Gdx.files.internal("Sounds/tile1.mp3"));
        walkingSound2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/tile2.mp3"));
        walkingSound3 = Gdx.audio.newSound(Gdx.files.internal("Sounds/tile3.mp3"));
        walkingSound4 = Gdx.audio.newSound(Gdx.files.internal("Sounds/tile4.mp3"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/background-music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(volume);
        backgroundMusic.play();

        dimTexture = new Sprite(blankTexture);
        dimTexture.setColor(Color.BLACK);
        dimTexture.setSize(collisionLayer.getWidth() * 16, collisionLayer.getHeight() * 16);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/switch2.ogg"));


        stage = new Stage(extendViewport, renderer.getBatch());

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage); // Stage first to ensure UI input is prioritized
        inputMultiplexer.addProcessor(player); // Then player
        Gdx.input.setInputProcessor(inputMultiplexer);


        // Prepare textures for buttons
        increaseVolumeTexture = new Texture(Gdx.files.internal("Icons/increase-volume.png"));
        lowerVolumeTexture = new Texture(Gdx.files.internal("Icons/lower-volume.png"));
        volumeOffTexture = new Texture(Gdx.files.internal("Icons/volume-off.png"));
        volumeOnTexture = new Texture(Gdx.files.internal("Icons/volume-on.png"));



        // Create and set up buttons
        Button.ButtonStyle increaseVolumeStyle = new Button.ButtonStyle();
        increaseVolumeStyle.up = new TextureRegionDrawable(new TextureRegion(increaseVolumeTexture));
        increaseVolumeButton = new Button(increaseVolumeStyle);
        //increaseVolumeButton.setPosition(900, 670); // Example position

        increaseVolumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                volume = Math.min(volume + 0.1f, 1.0f);
                backgroundMusic.setVolume(volume);
            }
        });

        Button.ButtonStyle lowerVolumeStyle = new Button.ButtonStyle();
        lowerVolumeStyle.up = new TextureRegionDrawable(new TextureRegion(lowerVolumeTexture));
        lowerVolumeButton = new Button(lowerVolumeStyle);
        //lowerVolumeButton.setPosition(950, 670); // Example position
        lowerVolumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                volume = Math.max(volume - 0.1f, 0.0f);
                backgroundMusic.setVolume(volume);
            }
        });

        Button.ButtonStyle volumeOffStyle = new Button.ButtonStyle();
        volumeOffStyle.up = new TextureRegionDrawable(new TextureRegion(volumeOffTexture));
        volumeOffButton = new Button(volumeOffStyle);
        // volumeOffButton.setPosition(1000, 670); // Example position
        volumeOffButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                if (backgroundMusic.isPlaying()) {
                    backgroundMusic.pause();
                    volume = 0;
                }
            }
        });

        // Play the music if it is not already playing
        Button.ButtonStyle volumeOnStyle = new Button.ButtonStyle();
        volumeOnStyle.up = new TextureRegionDrawable(new TextureRegion(volumeOnTexture));
        volumeOnButton = new Button(volumeOnStyle);
        //volumeOnButton.setPosition(1050, 670); // Example position
        volumeOnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                if (!backgroundMusic.isPlaying()) {
                    backgroundMusic.play();
                    volume = 1.0f;
                }
            }
        });

        stage.addActor(increaseVolumeButton);
        stage.addActor(lowerVolumeButton);
        stage.addActor(volumeOffButton);
        stage.addActor(volumeOnButton);




    }
    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width,height);
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
        player.getTexture().dispose();
        font.dispose();
        counterBoxTexture.dispose();
        verticalBarTexture.dispose();
        walkingSound1.dispose();
        walkingSound2.dispose();
        walkingSound3.dispose();
        walkingSound4.dispose();
        settingsMenu.dispose();
        pauseMenu.dispose();

        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
        stage.dispose();
        increaseVolumeTexture.dispose();
        lowerVolumeTexture.dispose();
        volumeOffTexture.dispose();
        volumeOnTexture.dispose();
        TblankTexture.dispose();
        textBubbleTexture.dispose();
        backgroundMusic.dispose();
        increaseVolumeTexture.dispose();
        volumeOffTexture.dispose();
        volumeOnTexture.dispose();
        lowerVolumeTexture.dispose();
    }

    private void playWalkingSound(float delta) {
        if (!isWalking || walkingSoundTimer < WALKING_SOUND_DELAY) {
            walkingSoundTimer += delta;
            return;
        }


        walkingSoundTimer = 0;

        Sound soundToPlay = null;
        switch (currentWalkingSoundIndex) {
            case 0:
                soundToPlay = walkingSound1;
                break;
            case 1:
                soundToPlay = walkingSound2;
                break;
            case 2:
                soundToPlay = walkingSound3;
                break;
            case 3:
                soundToPlay = walkingSound4;
                break;
        }
        if (soundToPlay != null) {
            soundToPlay.play(volume);
            currentWalkingSoundIndex = (currentWalkingSoundIndex + 1) % 4;
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
     * Get font in another class
     * @return games value of font
     */
    public static BitmapFont getFont(){
        return font;
    }

}

