package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import static sun.java2d.cmm.ColorTransform.In;

interface IGameScene{
    void draw(SpriteBatch batch);
    void update();
    void dispose();
    void handleInput();
    void setCamera(Camera camera);
}
interface IActor{
    void loadState(StatePos statePos);
    void draw(SpriteBatch batch);
    void update();
    public boolean isTouched(float x, float y);
    void dispose();
}

class StatePos{
    public float x;
    public float y;
    public StatePos(float x, float y){
        this.x = x;
        this.y = y;
    }
}
class Card implements IActor{
    private final Sprite sprite;
    private StatePos originState;
    public Card(Sprite sprite){
        this.sprite = sprite;
        originState = new StatePos(sprite.getX(), sprite.getY());
    }
    @Override
    public void loadState(StatePos statePos) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }


    @Override
    public void update() {

    }

    @Override
    public boolean isTouched(float x, float y) {
        if ((x > sprite.getX()) && (x < sprite.getX() + sprite.getWidth())
                && (y > sprite.getY()) && (y < sprite.getY() + sprite.getHeight())){

            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
class MyGameScene implements IGameScene{
    ArrayList<IActor> desks;
    public static final int WIDTH_WORLD = 100;
    public static final int HEIGHT_WORLD = 100;
    private static final float WIDTH_CARD = 6;
    private static final float HEIGHT_CARD = WIDTH_CARD / 500 *726;
    private static final float WIDTH_CARD_OVERLAP = 0.2f;
    private Camera camera;
    public MyGameScene(){
        desks = new ArrayList<>();
        float posFirstCard = (WIDTH_WORLD - WIDTH_CARD * (1-WIDTH_CARD_OVERLAP) * 13) / 2;
        for (int i =2; i <=14; i++){
            Sprite spriteCard =  new Sprite(new Texture("cards/" + Integer.toString(i) + ".png"));
            spriteCard.setSize(WIDTH_CARD, HEIGHT_CARD);
            spriteCard.setPosition(posFirstCard + WIDTH_CARD * (1-WIDTH_CARD_OVERLAP)*(i-2), 10 );
            desks.add(new Card(spriteCard));
        }
    }
    @Override
    public void draw(SpriteBatch batch) {
        for (IActor item : desks){
            item.draw(batch);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void dispose() {
        for (IActor item : desks){
            item.dispose();
        }
    }

    @Override
    public void handleInput() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector3 vector3 = camera.unproject(new Vector3(x, y, 0));
        int count = desks.size();
        for (int i = count -1; i>=0; i--){
            if (desks.get(i).isTouched(vector3.x, vector3.y)){

            }
        }
    }

    @Override
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}

final class GameWorld{
    private static GameWorld gameWorld = null;
    private IGameScene currentGameScene = null;
    private Camera camera;
    private GameWorld(){

    }
    public static GameWorld getInstance(){
        if (gameWorld == null) {
            gameWorld = new GameWorld();
        }
        return gameWorld;
    }
    public void load(IGameScene gameScene){
        currentGameScene = gameScene;
    }
    public IGameScene getGameScene(){
        return currentGameScene;
    }
    public void draw(SpriteBatch batch){
        if (currentGameScene != null)
            currentGameScene.draw(batch);
    }
    public void handleInput(){
        currentGameScene.handleInput();
    }
    public void setCamera(Camera camera){
        this.camera = camera;
        currentGameScene.setCamera(camera);
    }
    public void update(){
        currentGameScene.update();
    }
}