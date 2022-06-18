package com.tahakorkem.snakex;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tahakorkem.snakex.SnakeX;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("SnakeX");
		config.setWindowedMode(760, 760);
		config.setResizable(false);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new SnakeX(), config);
	}
}
