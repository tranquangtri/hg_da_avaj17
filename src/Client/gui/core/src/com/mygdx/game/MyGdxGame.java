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

public class MyGdxGame extends ApplicationAdapter {
	public static final int WIDTH_WORLD = 100;
	public static final int HEIGHT_WORLD = 100;
	private GameWorld world;
	private SpriteBatch batch;
	private Camera camera;
	private float width_windows;
	private float height_windows;
	private GameWorld gameWorld;
	@Override
	public void create () {
		width_windows = Gdx.graphics.getWidth();
		height_windows = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		camera = new OrthographicCamera(WIDTH_WORLD, HEIGHT_WORLD * (height_windows /width_windows));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		gameWorld = GameWorld.getInstance();
		IGameScene myGameScene = new MyGameScene();
		gameWorld.load(myGameScene);
		gameWorld.setCamera(camera);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		gameWorld.handleInput();
		gameWorld.update();
		batch.begin();
		/*--------------------*/

		gameWorld.draw(batch);

		/*--------------------*/
		batch.end();

	}

	@Override
	public void dispose () {
		batch.dispose();
		gameWorld.getGameScene().dispose();
	}
}
