package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

class GameObject{
	ArrayList<Sprite> arrayList;
	ArrayList<Float> widthdest;
	ArrayList<Float> widthdelta;
	public GameObject(){
		arrayList = new ArrayList<>();
		widthdest = new ArrayList<>();
		widthdelta = new ArrayList<>();
		for (int i = 2; i <= 14; i++) {
			Sprite sprite1 = new Sprite(new Texture("cards/" + Integer.toString(i) + ".png"));
			sprite1.setSize(8 , 8 * sprite1.getHeight() /  sprite1.getWidth());
			sprite1.setPosition(46,30);
			widthdest.add( 11.0f + (i - 2) * 6.0f);
			widthdelta.add(46 - widthdest.get(i-2));
			arrayList.add(sprite1);
		}

	}
	int time = 0;
	Object mutex = new Object();
	public void changeMode(){
		synchronized (mutex){
			mode =1;
		}
	}
	int mode = 0;
	public void draw(SpriteBatch batch){
		int count = 0;
		for (Sprite item : arrayList) {
			item.draw(batch);
			count++;
		}
	}
	float deltatime = 0;
	public void changed(){
		int index= 0;
		float time = Gdx.graphics.getDeltaTime();
		deltatime += time;
		if (deltatime > 1) return;
		for (Sprite item : arrayList) {
			float velocite = widthdelta.get(index);

			item.setPosition(item.getX()-velocite*(time), item.getY() - (30.0f-10.0f)*(time));
			index++;
		}
	}
	public void dispose(){
		for (Sprite item : arrayList){
			item.getTexture().dispose();
		}
	}
}
public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	GameObject gameObject;
	Camera camera;
	int a = 0;
	Sprite bgr;
	@Override
	public void create () {
		batch = new SpriteBatch();
		gameObject = new GameObject();
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(100, 100 * (height /width));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		bgr = new Sprite(new Texture("bgr.jpg"));
		bgr.setPosition(0,0);
		bgr.setSize(100,100);
		camera.update();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		bgr.draw(batch);
		gameObject.draw(batch);
		batch.end();
		gameObject.changed();
	}

	@Override
	public void dispose () {
		batch.dispose();
		gameObject.dispose();
	}
}
