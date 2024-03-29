package com.heshus.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.heshus.game.manager.DayManager;

public class Player extends Sprite implements InputProcessor {
    //movement velocity - Vector2 stores 2 values, for x and y
    private Vector2 velocity = new Vector2();
    private float speed = 200;

    private TiledMapTileLayer collisionLayer;

    /**
     * Instantiate Player object
     * @param playerSprite player sprite
     * @param collisionLayer the layer of the Tiled map where collision information is stored
     */
    public Player(Sprite playerSprite, TiledMapTileLayer collisionLayer) {
        //call super constructor - i.e. the constructor of the Sprite class, which takes the player sprite as an argument
        super(playerSprite);
        this.collisionLayer = collisionLayer;

    }

    /**
     * Return the layer of the tilemap where collision information is stored
     * @return collisionLayer
     */
    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    /**
     * Draw is called to draw the Player to the screen
     * @param spritebatch the Batch of the renderer responsible for drawing the Player
     */
    public void draw(Batch spritebatch) {
        //call the draw method of the parent class
        super.draw(spritebatch);
    }

    /**
     * Update is called once per frame
     * @param delta the time since last update()
     */
    public void update(float delta) {

        //**********************
        //  COLLISION DETECTION
        //**********************

        //save old x,y positions
        float oldX = getX();
        float oldY = getY();
        //variables to say if we're colliding with something
        boolean collisionX = false, collisionY = false;
        //map tile properties
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();

        //update x position
        setX(getX() + velocity.x * delta);

        //do the collision detection
        if (velocity.x < 0) {
            //player moving left
            //want to check the tiles to the left, up/left and down/left

            //top left
            collisionX = collisionLayer.getCell((int)(getX() / tileWidth), (int)((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("collision");
            //disgusting line
            //get the cell at the player's top-left, get the tile at that cell, get that tile's properties, if it contains "blocked" then true

            //middle left
            if (!collisionX) { //if no collision yet
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + (getHeight() / 2)) / tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }
            //bottom left
            if (!collisionX) {
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }
        } else if (velocity.x > 0) {
            //player moving right

            //top right
            collisionX = collisionLayer.getCell((int)((getX() + getWidth())/ tileWidth), (int)((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("collision");

            //middle right
            if (!collisionX) {
                collisionX = collisionLayer.getCell((int)((getX() + getWidth())/ tileWidth), (int)((getY() + getHeight() / 2) / tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }

            //bottom right
            if (!collisionX) {
                collisionX = collisionLayer.getCell((int)((getX() + getWidth())/ tileWidth), (int)(getY() / tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }

        }

        //correct x-axis movement
        if (collisionX) {
            setX(oldX);
            velocity.x = 0;
        } //could put an else here to fix bug where colliding with something stops all movement in that direction

        //repeat for y position
        setY(getY() + velocity.y * delta);

        if (velocity.y < 0) {
            //player moving downwards

            //bottom left
            collisionY = collisionLayer.getCell((int)((getX())/ tileWidth), (int)(getY() / tileHeight))
                    .getTile().getProperties().containsKey("collision");

            //bottom middle
            if (!collisionY) {
                collisionY = collisionLayer.getCell((int)((getX() + getWidth() / 2)/ tileWidth), (int)(getY() / tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }

            //bottom right
            if (!collisionY) {
                collisionY = collisionLayer.getCell((int)((getX() + getWidth())/ tileWidth), (int)(getY() / tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }

        } else if (velocity.y > 0) {
            //player moving upwards
            //THIS NEEDS FIXING

            //top left
            collisionY = collisionLayer.getCell((int)((getX())/ tileWidth), (int)((getY() + getHeight())/ tileHeight))
                    .getTile().getProperties().containsKey("collision");

            //top middle
            if (!collisionY) {
                collisionY = collisionLayer.getCell((int)((getX() + getWidth() / 2)/ tileWidth), (int)((getY() + getHeight())/ tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }

            //top right
            if (!collisionY) {
                collisionY = collisionLayer.getCell((int)((getX() + getWidth())/ tileWidth), (int)((getY() + getHeight())/ tileHeight))
                        .getTile().getProperties().containsKey("collision");
            }
        }

        //react to y collision
        if (collisionY) {
            setY(oldY);
            velocity.y = 0;
        }

    }

    ////////////////////
    //INPUT HANDLING
    ///////////////////

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                velocity.y = speed;
                break;
            case Input.Keys.A:
                velocity.x = -speed;
                break;
            case Input.Keys.S:
                velocity.y = -speed;
                break;
            case Input.Keys.D:
                velocity.x = speed;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.D:
                velocity.x = 0;
                break;
            case Input.Keys.W:
            case Input.Keys.S:
                velocity.y = 0;
                break;
        }
        return true;
    }

    //DON'T NEED ANY OF THE REST OF THESE METHODS

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
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float a, float b) {
        return false;
    }
    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

}