package com.areo.gaming.cupofdrops.objects;

import com.areo.gaming.cupofdrops.utils.BodyEditorLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * A hybrid entity class. It extends Scene2D Actor class and creates a Box2D body.
 * To define body fixture it uses a BodyEditorLoader created by Aurelien Ribon.
 * Body fixture must be created with Physics body editor and saved in JSON-file.
 *  
 * @author R. Arslanov.
 */
public class SceneObject extends Actor {
	
	private Sprite sprite;
	private Rectangle bounds;
	private Vector2 origin;
	private Body body;
	
	/**
	 * Creates a scene object.
	 * 
	 * @param name Name of fixture defined in the Physics Body Editor.
	 * @param texturePath Path to the texture file (located by default in assets/data folder).
	 * @param jsonPath Path to the JSON file which contains fixture parameters
	 * (located by default in assets/data folder).
	 * @param world Current Box2d World you want to add a scenery object to. 
	 * @param x The x-coordinate of the scenery position
	 * @param y The y-coordinate of the scenery position
	 * @param density 
	 */
	public SceneObject(String name, String texturePath, String jsonPath, 
			World world, float x, float y, float width, float density, 
			float friction, float restitution) {
		
		createSprite(texturePath, width);
		createModel(name, jsonPath, world, x, y, 
				width, density, friction, restitution);
	}
		
	public void createSprite(String texturePath, float width) {
		Texture texture = new Texture(Gdx.files.internal("data/" + texturePath));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture);
		sprite.setSize(width, width * sprite.getHeight() / sprite.getWidth());
		/*
		 * Specify object's bounds.
		 */
		bounds = new Rectangle();
		bounds.height = sprite.getHeight();
		bounds.width = sprite.getWidth();
	}
	
	
	public void createModel(String name, String jsonPath, World world, 
			float x, float y, float scale, float density, float friction, 
			float restitution) {
		/*
		 * Create a loader for the file created in the editor.
		 */
		BodyEditorLoader loader = 
			new BodyEditorLoader(Gdx.files.internal("data/" + jsonPath));
		/*
		 * Define the object's body.
		 */
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DynamicBody;
		/*
		 * Define the object's fixture.
		 */
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		/*
		 * Put body into the world and save the reference to it.
		 */
		body = world.createBody(bodyDef);
		/*
		 * Create the fixture using the loader and attach it to the body.
		 */
		loader.attachFixture(body, name, fixtureDef, scale);
		/*
		 * Save the body's origin (we need it to draw the sprite correctly).
		 */
		origin = loader.getOrigin(name, scale).cpy();
	}
	
	@Override
	public float getHeight() {
		return bounds.height;
	}
	
	@Override
	public float getWidth() {
		return bounds.width;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = this.getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(sprite, 
				   body.getPosition().x, 
				   body.getPosition().y, 
				   origin.x, 
				   origin.y, 
				   bounds.width, 
				   bounds.height, 
				   1.0f, 
				   1.0f,
				   body.getAngle() * MathUtils.radiansToDegrees
				   );
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
}
