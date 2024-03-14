package com.heshus.game.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.heshus.game.manager.ActivityManager;
import com.heshus.game.entities.Player;
import com.heshus.game.manager.DayManager;

//LUKE IMPORTS
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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class HesHusGame extends Game {

	@Override
	public void create () {
		//entry point to the game
		setScreen(new Play());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}



	/*
	private SpriteBatch batch;
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private OrthographicCamera camera;
	public Sprite sprite;
	private Texture spriteTexture;
	private float speed = 400;
	 */


	//@Override
	//public void create () {
		/*
		batch = new SpriteBatch();
		tiledMap = new TmxMapLoader().load("testmap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);



		camera = new OrthographicCamera();
		// Changing setToOrtho from 800,480 to scale the window
		camera.setToOrtho(false, 1600, 800);

		spriteTexture = new Texture("bucket.png");
		sprite = new Sprite(spriteTexture);

		//placed the avatar before the first building they would need to interact with
		sprite.setPosition(50 - sprite.getWidth() / 2, 200);

		TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("");
		//activityManager = new ActivityManager(collisionLayer);

		// Set this class as the input processor
		Gdx.input.setInputProcessor(this);

		 */
	//}




	/*
	public Vector2 getSpritePosition() {
		return new Vector2(sprite.getX(), sprite.getY());
	}
	*/

	/*
	@Override
	public void dispose() {
		batch.dispose();
		tiledMap.dispose();
		spriteTexture.dispose();
	}

	 */

	/*
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
	*/


	//This will change when screens are added
	/*
	private void handleLogic(float deltaTime){
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			DayManager.incrementDay();
		}

		//Teleport player to centre of screen to test if working
		if(DayManager.gameOver){
			sprite.setPosition(400 - sprite.getWidth() / 2, 200);
		}
	}
	 */


}
