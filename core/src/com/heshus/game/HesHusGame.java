package com.heshus.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class HesHusGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private OrthographicCamera camera;
	private Sprite sprite;
	private Texture spriteTexture;

	@Override
	public void create () {
		batch = new SpriteBatch();
		tiledMap = new TmxMapLoader().load("map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// Load the texture and create the sprite
		spriteTexture = new Texture("bucket.png"); // Replace "sprite.png" with your sprite's file name
		sprite = new Sprite(spriteTexture);


		sprite.setPosition(400 - sprite.getWidth() / 2, 200);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();
		sprite.draw(batch); // Draw the sprite
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		tiledMap.dispose();
		spriteTexture.dispose(); // Dispose of the sprite texture as well
	}
}
