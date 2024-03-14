package com.heshus.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.heshus.game.entities.Player;

public class Play implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);

        //want: camera doesn't show black area on edge of map
        //if the player is too close to the edge of the map, don't move the camera
        //if (PLAYER TOO HIGH / TOO LOW) {
        //    if (PLAYER TOO FAR LEFT / RIGHT) {
                //camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        //    }
        //}

        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        camera.update();

        renderer.setView(camera);
        renderer.render(); //takes a layers[] argument if we want to specifically render certain layers

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        renderer.getBatch().end();

    }

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("testmap.tmx");
        //make this one line: map = new TmxMapLoader().load(path);
        //remember to put both the map and all tilemaps in assets folder
        //also have to consider: if you create the map elsewhere (not directly in the assets folder) (and save it to desktop or something)
        //then you have to go into the .tsx files and change the filepaths of the tilemap png's

        renderer = new OrthogonalTiledMapRenderer(map); //can also take a scale argument
        camera = new OrthographicCamera(); //don't need to specify width and height because resize() is called after show()

        player = new Player(new Sprite(new Texture("player.png")), (TiledMapTileLayer) map.getLayers().get(0));
        float startX = 34 * player.getCollisionLayer().getTileWidth();
        float startY = (player.getCollisionLayer().getHeight() - 25) * player.getCollisionLayer().getTileHeight();
        player.setPosition(startX, startY);
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        float zoomlevel = 2f;
        camera.viewportWidth = width / zoomlevel;
        camera.viewportHeight = height / zoomlevel;
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
    }
}

