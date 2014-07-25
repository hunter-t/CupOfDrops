package com.areo.gaming.cupofdrops.screens;

import java.util.Random;

import com.areo.gaming.cupofdrops.objects.Drop;
import com.areo.gaming.cupofdrops.objects.SceneObject;
import com.areo.gaming.cupofdrops.objects.SpawnZone;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class LevelScreen implements Screen {
	
	private static final float VIEWPORT_WIDTH = 8;
	private static final float CUP_WIDTH = 2;
	private static final float DROPS_RADIUS = 0.15f;
	
	private Stage stage;
	private SceneObject cup;
	private SpawnZone spawnZone;
	private World world;
	private OrthographicCamera camera;

	/*
	 * This method is invoked when the screen is set as current screen.
	 */
	@Override
	public void show() {
		stage = setupStage();
		world = new World(new Vector2(0, -9.81f), true);
		createGround();
		createCup();
		createSpawnZone();
		generateDrop();
	}
	
	/*
	 * Set camera position and stage viewport.
	 */
	private Stage setupStage() {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(VIEWPORT_WIDTH, 
				VIEWPORT_WIDTH * screenHeight / screenWidth);
		camera.position.set(0, camera.viewportHeight / 2, 0);
		camera.update();
		return new Stage(new StretchViewport(camera.viewportWidth, 
				camera.viewportHeight));

	}
	
	private void createGround() {
		/*
		 * Define the ground body.
		 */
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0, 0);
		groundBodyDef.type = BodyType.StaticBody;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((float) Gdx.graphics.getWidth(), 0.25f);
		/*
		 * Define the ground fixture.
		 */
		FixtureDef groundFixtureDef = new FixtureDef();
		groundFixtureDef.density = 1;
		groundFixtureDef.friction = 0.5f;
		groundFixtureDef.restitution = 0.25f;
		groundFixtureDef.shape = shape;
		/*
		 * Put the ground into the world.
		 */
		world.createBody(groundBodyDef).createFixture(groundFixtureDef);
		Image ground = new Image(new Texture("data/ground.png"));
		ground.setSize(Gdx.graphics.getWidth(), 0.25f);
		stage.addActor(ground);
		shape.dispose();
	}
	
	private void createCup() {
		cup = new SceneObject(
				"Cup", 
				"cup.png", 
				"objects.json", 
				world, 
				VIEWPORT_WIDTH / 2 - CUP_WIDTH / 2, 
				0.5f,
				CUP_WIDTH,
				20, 
				2.5f, 
				0.1f
				);
		cup.getBody().setTransform(cup.getBody().getPosition(), 0.15f);
		stage.addActor(cup);
	}
	
	/*
	 * Define a spawn zone – rectangle area above of the cup.
	 * All drops will be produced in there.
	 */
	public void createSpawnZone() {
		spawnZone = new SpawnZone(
				this,
				VIEWPORT_WIDTH / 2 - cup.getWidth() / 2,
				cup.getHeight() - cup.getHeight() / 3,
				cup.getWidth(),
				cup.getHeight() - cup.getHeight() / 3
				);
		
		stage.addActor(spawnZone);
	}
	
	public void generateDrop() {
		Random rand = new Random();
		int whole = (int) (rand.nextInt((int) spawnZone.getBounds().width) 
				+ spawnZone.getBounds().x);
		int decimal = (rand.nextInt(9 + 1)) + 1;
		float x = whole + (decimal / 10f);
		
		Drop drop = new Drop(
				"ball.png",
				world,
				x,
				3.5f,
				DROPS_RADIUS,
				1,
				0.5f,
				0.65f
				);
		spawnZone.trackDrop(drop);
		stage.addActor(drop);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1 / 60f, 10, 3);
		stage.draw();
		stage.act();
	}
	
	public Stage getStage() {
		return stage;
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
