package com.heshus.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {
    //movement velocity - Vector2 stores 2 values, for x and y
    private Vector2 velocity = new Vector2();
    private float speed = 200;
    private float gravity = 1;

    private TiledMapTileLayer collisionLayer;

    public Player(Sprite playerSprite, TiledMapTileLayer collisionLayer) {
        //call super constructor - i.e. the constructor of the Sprite class, which takes the player sprite as an argument
        super(playerSprite);
        this.collisionLayer = collisionLayer;

    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void draw(Batch spritebatch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(spritebatch);
    }

    public void update(float delta) {


        //surely gravity is not really relevant to our end goal
        //this player movement system should be modified to account for this - no gravity, up/down movement

        /*
        //apply gravity
        velocity.y -= gravity * delta;
        //clamp velocity - because otherwise it's y will become massively negative as we subtract from it every frame
        if (velocity.y > speed) {
            velocity.y = speed;
        } else if (velocity.y < speed) {
            velocity.y = -speed;
        }
         */


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

        } else if (velocity.x > 0) {
            //player moving upwards

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

}