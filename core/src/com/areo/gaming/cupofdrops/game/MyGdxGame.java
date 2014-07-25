package com.areo.gaming.cupofdrops.game;

import com.areo.gaming.cupofdrops.screens.LevelScreen;
import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	
	public static final String PROJECT_NAME = "Cup of Drops";
	
	@Override
	public void create () {
		LevelScreen levelScreen = new LevelScreen();
		setScreen(levelScreen);
	}
}
