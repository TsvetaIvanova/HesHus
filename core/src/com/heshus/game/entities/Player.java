package com.heshus.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player {

    private float x;
    private float y;
    private float speed;
    public Sprite sprite;
    public Player(Sprite sprite, float x, float y){
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        speed = 400.f;
    }

    public void update(){
        handleMovement(Gdx.graphics.getDeltaTime());
    }

    private void handleMovement(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x+=(-speed * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x+=(speed * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y+=(speed * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y+=(-speed * deltaTime);
        }
        sprite.setX(x);
        System.out.println(sprite.getX());
        sprite.setY(y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}

