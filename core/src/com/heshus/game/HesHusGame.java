package com.heshus.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.heshus.game.controller.DayManager;

public class HesHusGame extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private OrthographicCamera camera;
	private Sprite sprite;
	private Texture spriteTexture;
	private float speed = 200;

	@Override
	public void create () {
		batch = new SpriteBatch();
		tiledMap = new TmxMapLoader().load("map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		spriteTexture = new Texture("bucket.png");
		sprite = new Sprite(spriteTexture);

		sprite.setPosition(400 - sprite.getWidth() / 2, 200);

		// Set this class as the input processor
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		handleInput(Gdx.graphics.getDeltaTime());
		handleLogic(Gdx.graphics.getDeltaTime());

		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		tiledMap.dispose();
		spriteTexture.dispose();
	}

	private void handleInput(float deltaTime) {
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			sprite.translateX(-speed * deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			sprite.translateX(speed * deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			sprite.translateY(speed * deltaTime);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			sprite.translateY(-speed * deltaTime);
		}
	}

	//This will change when screens are added
	private void handleLogic(float deltaTime){
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			DayManager.incrementDay();
		}

		//Teleport player to centre of screen to test if working
		if(DayManager.gameOver){
			sprite.setPosition(400 - sprite.getWidth() / 2, 200);
		}
	}

	// InputProcessor methods

	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchCancelled(int i, int i1, int i2, int i3) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
