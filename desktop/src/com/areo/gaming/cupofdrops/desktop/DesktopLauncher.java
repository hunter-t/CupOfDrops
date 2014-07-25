package com.areo.gaming.cupofdrops.desktop;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.areo.gaming.cupofdrops.game.MyGdxGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		/*
		 * Detect screen resolution, considering that user may have more than 1 
		 * graphic device.
		 */
		GraphicsDevice graphicDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		float screenWidth = graphicDevice.getDisplayMode().getWidth();
		float screenHeight = graphicDevice.getDisplayMode().getHeight();
		/*
		 * Setup desktop launch configuration.
		 */
		config.width = (int) (screenWidth * 0.8f);
		config.height = (int) (screenHeight * 0.8f);
		config.useGL30 = true;
		config.resizable = true;
		config.fullscreen = false;
		config.vSyncEnabled = true;
		config.title = MyGdxGame.PROJECT_NAME;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
