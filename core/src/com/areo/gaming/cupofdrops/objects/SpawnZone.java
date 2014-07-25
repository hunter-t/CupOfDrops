package com.areo.gaming.cupofdrops.objects;

import java.util.ArrayList;

import com.areo.gaming.cupofdrops.screens.LevelScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SpawnZone extends Actor {

	private Rectangle bounds;
	private ArrayList<Drop> trackedDrops;
	private LevelScreen level;
	private boolean containingDrops;
	
	public SpawnZone(LevelScreen level, float x, float y, float width, float height) {
		this.level = level;
		bounds = new Rectangle(x, y, width, height);
		Image spawnPicture = new Image(new Texture("data/spawnzone.png"));
		spawnPicture.setPosition(bounds.x, bounds.y);
		spawnPicture.setSize(bounds.width, bounds.height);
		level.getStage().addActor(spawnPicture);
	}
	
	/*
	 * Manage the list of drops in stage.
	 * Cloning is necessary to avoid the ConcurrentModificationException.
	 */
	@SuppressWarnings("unchecked")
	public void trackDrop(Drop drop) {
		if (trackedDrops == null) {
			trackedDrops = new ArrayList<Drop>();
			trackedDrops.add(drop);
		} else {
			trackedDrops = (ArrayList<Drop>) trackedDrops.clone();
			trackedDrops.add(drop);
		}
	}
	
	@Override
	public void act(float delta) {
		containingDrops = false;
		if (trackedDrops != null) {
			for (Drop drop : trackedDrops)
				if (bounds.contains(drop.getX(), drop.getY()))
					containingDrops = true;
		}
		if (!containingDrops) level.generateDrop();
		super.act(delta);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
}
