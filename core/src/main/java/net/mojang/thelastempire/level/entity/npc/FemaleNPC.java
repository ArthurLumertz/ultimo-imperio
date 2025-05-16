package net.mojang.thelastempire.level.entity.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;

public class FemaleNPC extends NPC {

	private static Texture[] textures = {
			Resources.getNPC("woman0"),
			Resources.getNPC("woman1"),
			Resources.getNPC("woman2")
	};

	public FemaleNPC(Level level, int id, float x, float y, float xt, float yt, String name, String levelName, Array<NPCDialogue> dialogues, String direction, boolean canMove) {
		super(level, x, y, xt, yt, name, levelName, dialogues, direction, canMove);

		createAnimation(textures[id]);
		texture = new TextureRegion(textures[id], 0, 0, 16, 32);
	}

}
