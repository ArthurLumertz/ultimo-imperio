package net.mojang.thelastempire.engine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Resources {

	private static AssetManager assetManager = new AssetManager();

	public static Texture getTexture(String name) {
		return assetManager.get(name + ".png", Texture.class);
	}
	
	public static Texture getFont(String name) {
		return assetManager.get(name + ".gif", Texture.class);
	}

	public static Sound getSound(String name) {
		return assetManager.get("sounds/" + name + ".ogg", Sound.class);
	}

	public static Music getMusic(String name) {
		return assetManager.get("sounds/" + name + ".ogg", Music.class);
	}
	
	public static Texture getNPC(String name) {
		return getTexture("npcs/" + name);
	}
	
	public static void loadResources() {
		assetManager.load("tiles.png", Texture.class);
		assetManager.load("default.gif", Texture.class);
		assetManager.load("bold.gif", Texture.class);
		assetManager.load("dialogue.png", Texture.class);
		assetManager.load("char.png", Texture.class);
		assetManager.load("dompedro.png", Texture.class);
		assetManager.load("gui.png", Texture.class);
		assetManager.load("furniture.png", Texture.class);
		assetManager.load("plants.png", Texture.class);
		assetManager.load("particles.png", Texture.class);
		assetManager.load("houses.png", Texture.class);
		assetManager.load("firefly.png", Texture.class);
		assetManager.load("barrackentrance.png", Texture.class);
		assetManager.load("icons.png", Texture.class);
		assetManager.load("decoration.png", Texture.class);
		assetManager.load("cursor.png", Texture.class);

		assetManager.load("npcs/oldman.png", Texture.class);
		assetManager.load("npcs/man0.png", Texture.class);
		assetManager.load("npcs/man1.png", Texture.class);
		assetManager.load("npcs/man2.png", Texture.class);
		assetManager.load("npcs/soldier.png", Texture.class);
		assetManager.load("npcs/woman0.png", Texture.class);
		assetManager.load("npcs/woman1.png", Texture.class);
		assetManager.load("npcs/woman2.png", Texture.class);
		
		assetManager.load("screens/main_menu_backdrop.png", Texture.class);

		assetManager.load("sounds/intro.ogg", Music.class);
		assetManager.load("sounds/dompedroiifinal.ogg", Music.class);
		assetManager.load("sounds/birds_ambiance.ogg", Music.class);
		assetManager.load("sounds/rain.ogg", Music.class);
		assetManager.load("sounds/fireplace.ogg", Music.class);
		assetManager.load("sounds/button_click.ogg", Sound.class);
		assetManager.load("sounds/shoot.ogg", Sound.class);
		assetManager.load("sounds/damage.ogg", Sound.class);
		assetManager.load("sounds/hurt_player1.ogg", Sound.class);
		assetManager.load("sounds/open_door.ogg", Sound.class);
		assetManager.load("sounds/type.ogg", Sound.class);
		assetManager.load("sounds/dodge.ogg", Sound.class);
		assetManager.load("sounds/step_earth.ogg", Sound.class);
		assetManager.load("sounds/reload.ogg", Sound.class);
		assetManager.load("sounds/step_stone.ogg", Sound.class);
		assetManager.load("sounds/step_wood.ogg", Sound.class);
		
		
		assetManager.finishLoading();
	}

}
