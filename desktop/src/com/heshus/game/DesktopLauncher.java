package com.heshus.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.heshus.game.engine.HesHusGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	//private void settingsSetup{
	//}
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("HesHus Game");
		//changing setWindowedMode from w=800, h=480, to 256 and 256 to account
		//for the changed TiledMap
		//config.setWindowedMode(256, 256);
		config.setWindowedMode(1020, 574);
		config.setResizable(false);
		config.useVsync(true);
		new Lwjgl3Application(new HesHusGame(), config);
	}
}
