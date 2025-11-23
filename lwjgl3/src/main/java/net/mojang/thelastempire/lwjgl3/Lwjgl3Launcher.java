package net.mojang.thelastempire.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import net.mojang.thelastempire.TheLastEmpire;

public class Lwjgl3Launcher {

	public static void main(String[] args) {
		if (StartupHelper.startNewJvmIfRequired())
			return;
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(TheLastEmpire.getTheLastEmpire(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("O Último Império");
		configuration.useVsync(true);
		configuration.setWindowedMode(960, 540);
		configuration.setResizable(false);
		configuration.setWindowIcon("icon.png");
		return configuration;
	}

}
