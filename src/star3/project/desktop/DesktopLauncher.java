package star3.project.desktop;

import star3.project.Star3Teeko;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Teeko";
		config.width = 512;
		config.height = 512;
		
		new LwjglApplication(new Star3Teeko(), config);
	}
}
