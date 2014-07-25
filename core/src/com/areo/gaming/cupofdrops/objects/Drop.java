package com.areo.gaming.cupofdrops.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Drop extends Actor {

	private Sprite sprite;
	private Circle bounds;
	private Vector2 origin;
	private Body body;
	private Fixture fixture;
	
	public Drop(String texturePath, World world, float x, float y, 
			float radius, float density, float friction, float restitution) {
		
		createSprite(texturePath, radius);
		createModel(world, x, y, radius, density, friction, restitution);
	}
	
	private void createSprite(String texturePath, float radius) {
		Texture texture = new Texture(Gdx.files.internal("data/" + texturePath));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite = new Sprite(texture);
		bounds = new Circle();
		bounds.radius = radius * 2;
	}

	private void createModel(World world, float x, float y, float radius,
			float density, float friction, float restitution) {
		/*
		 * Define the body of the ball.
		 */
		BodyDef ballBodyDef = new BodyDef();
		ballBodyDef.position.set(x, y);
		ballBodyDef.type = BodyType.DynamicBody;
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		/*
		 * Define the fixture of the ball.
		 */
		FixtureDef ballFixtureDef = new FixtureDef();
		ballFixtureDef.density = density;
		ballFixtureDef.friction = friction;
		ballFixtureDef.restitution = restitution;
		ballFixtureDef.shape = shape;
		/*
		 * Put the ball into the world.
		 */
		body = world.createBody(ballBodyDef);
		fixture = body.createFixture(ballFixtureDef);
		origin = new Vector2((float) radius, (float) radius);
		shape.dispose();		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = this.getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(sprite, 
				   body.getPosition().x - bounds.radius / 2, 
				   body.getPosition().y - bounds.radius / 2, 
				   origin.x, 
				   origin.y, 
				   bounds.radius, 
				   bounds.radius, 
				   1.0f, 
				   1.0f,
				   body.getAngle() * MathUtils.radiansToDegrees
				   );
		/*
		 * Detect if the ball has stopped moving, then remove it from the stage.
		 */
		if (body.getLinearVelocity().len() < 0.001f) {
			this.getParent().removeActor(this);
			this.body.destroyFixture(fixture);
		}
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public float getX() {
		return body.getPosition().x;
	}
	
	public float getY() {
		return body.getPosition().y;
	}
	
	public Rectangle getRectBounds() {
		return new Rectangle(bounds.x, bounds.y, bounds.radius, bounds.radius);
	}
}
