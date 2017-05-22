package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

class GameObject{
	ArrayList<Texture> arrayList;
	public GameObject(){
		arrayList = new ArrayList<>();
		arrayList.add(new Texture("cards/2_of_clubs.png"));
		arrayList.add(new Texture("cards/2_of_diamonds.png"));
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
		if (mode == 1) {
			int count = 0;
			for (Texture item : arrayList) {
				batch.draw(item, 0 + count * 100 + time, count * 200);
				count++;
			}
		}
	}
	public void changed(){
		if (mode == 1) {
			synchronized (mutex) {
				time++;
			}
		}
	}
	public void dispose(){
		for (Texture item : arrayList){
			item.dispose();
		}
	}
}
public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	GameObject gameObject;
	int a = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
		gameObject = new GameObject();
		gameObject.changeMode();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
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
