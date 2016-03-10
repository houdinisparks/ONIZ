package com.oniz.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oniz.Game.ZGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "ONIZ";
//		config.width = 272;
//		config.height = 408;
		config.width = 450;
		config.height = 800;
//		config.fullscreen = true;
		new LwjglApplication(new ZGame(), config);
	}
}
