package net.mojang.thelastempire.level.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.gui.GuiDialogue;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Mob;
import net.mojang.thelastempire.level.entity.player.Player;
import net.mojang.thelastempire.level.phys.AABB;
import net.mojang.thelastempire.level.tile.Tile;
import net.mojang.thelastempire.level.tile.Tile.SoundType;

public class NPC extends Mob {

	protected Array<NPCDialogue> dialogues;

	protected TextureRegion texture;
	private int aiTimer = 0;

	private boolean chatting = false;

	private static final int UP_ANIMATION = 0;
	private static final int DOWN_ANIMATION = 1;
	private static final int LEFT_ANIMATION = 2;
	private static final int RIGHT_ANIMATION = 3;

	protected Animation<TextureRegion> currentAnimation;
	protected Animation<TextureRegion>[] allAnimations;

	private float animationTimer = 0;
	private String name;

	private float xt;
	private float yt;

	private String emotion = "neutral";
	private String levelName;

	private boolean canMove = true;

	public NPC(Level level, float x, float y, float xt, float yt, String name, String levelName,
			Array<NPCDialogue> dialogues, String direction, boolean canMove) {
		super(level, 100);
		this.levelName = levelName;
		this.name = name;
		this.dialogues = dialogues;
		this.speed = 0.04f;
		this.xt = xt;
		this.yt = yt;
		this.canMove = canMove;
		this.direction = direction;
		setPos(x, y);

		advanceAi(true, false);
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	@SuppressWarnings("unchecked")
	protected void createAnimation(Texture texture) {
		TextureRegion[] upFrames = new TextureRegion[] { new TextureRegion(texture, 32, 32, 16, 32),
				new TextureRegion(texture, 48, 32, 16, 32), };
		TextureRegion[] downFrames = new TextureRegion[] { new TextureRegion(texture, 32, 0, 16, 32),
				new TextureRegion(texture, 48, 0, 16, 32), };
		TextureRegion[] leftFrames = new TextureRegion[] { new TextureRegion(texture, 32, 64, 16, 32),
				new TextureRegion(texture, 48, 64, 16, 32), };
		TextureRegion[] rightFrames = new TextureRegion[] { new TextureRegion(texture, 0, 64, 16, 32),
				new TextureRegion(texture, 16, 64, 16, 32), };

		allAnimations = (Animation<TextureRegion>[]) new Animation<?>[4];
		allAnimations[UP_ANIMATION] = new Animation<TextureRegion>(0.4f, upFrames);
		allAnimations[DOWN_ANIMATION] = new Animation<TextureRegion>(0.4f, downFrames);
		allAnimations[LEFT_ANIMATION] = new Animation<TextureRegion>(0.4f, leftFrames);
		allAnimations[RIGHT_ANIMATION] = new Animation<TextureRegion>(0.4f, rightFrames);
		currentAnimation = allAnimations[direction.equals("up") ? UP_ANIMATION
				: direction.equals("down") ? DOWN_ANIMATION
						: direction.equals("left") ? LEFT_ANIMATION : RIGHT_ANIMATION];
	}

	@Override
	public void move(float xa, float ya) {
		float xaOrg = xa;
		float yaOrg = ya;

		Array<AABB> aABBs = level.getCubes(boundingBox.expand(xa, ya));

		for (AABB aABB : aABBs) {
			xa = aABB.clipXCollide(boundingBox, xa);
			ya = aABB.clipYCollide(boundingBox, ya);
		}

		boundingBox.move(xa, ya);

		boolean collided = false;

		if (xaOrg != xa) {
			xd = 0f;
			collided = true;
		}
		if (yaOrg != ya) {
			yd = 0f;
			collided = true;
		}

		if (collided) {
			advanceAi(false, true);
		}

		x = boundingBox.x0;
		y = boundingBox.y0;

		if (makeStepSound) {
			walkDist += (float) Math.sqrt(xa * xa + ya * ya) * 0.7f;

			int tx = MathUtils.floor(x);
			int ty = MathUtils.floor(y);
			Tile tile = Tile.tiles[level.getTile(tx, ty)];

			if (walkDist > nextStep) {
				nextStep++;
				if (tile != null) {
					SoundType soundType = tile.getSoundType();
					if (soundType != null) {
						TheLastEmpire tle = TheLastEmpire.getTheLastEmpire();
						float pitch = MathUtils.random(0.9f, 1.1f);
						tle.playSound(soundType.getStepSound(), 1.2f, pitch);
					}

				}
			}
		}
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;

		if (!canMove)
			return;

		if (!chatting) {
			if (xt > 0f && yt > 0f) {
				float dx = xt - x;
				float dy = yt - y;
				float distance = (float) Math.sqrt(dx * dx + dy * dy);
				
				if (distance > 1f) {
					float nx = dx / distance;
					float ny = dy / distance;

					up = ny > 0.2f;
					down = ny < -0.2f;
					left = nx < -0.2f;
					right = nx > 0.2f;

					if (up)
						direction = "up";
					else if (down)
						direction = "down";
					else if (left)
						direction = "left";
					else if (right)
						direction = "right";

					move(nx * speed, ny * speed);
				} else {
					up = false;
					down = false;
					left = false;
					right = false;

					if (dialogues.size > 0) {
						Player player = level.getPlayer();
						player.interact(this);
						startChatting();
					}
				}
			} else {
				baseTick(xd, yd);
				xd *= 0.71f;
				yd *= 0.71f;
			}
		}

		switch (direction) {
		case "up":
			currentAnimation = allAnimations[UP_ANIMATION];
			break;
		case "down":
			currentAnimation = allAnimations[DOWN_ANIMATION];
			break;
		case "left":
			currentAnimation = allAnimations[LEFT_ANIMATION];
			break;
		case "right":
			currentAnimation = allAnimations[RIGHT_ANIMATION];
			break;
		}

		if (!chatting && (xt > 0f && yt > 0f)) {
			advanceAi(false, false);
		}
	}

	protected void advanceAi(boolean force, boolean prev) {
		if (!canMove) return;

		if (!force) {
			aiTimer++;
			if (aiTimer < 40) return;
		}

		up = false;
		down = false;
		left = false;
		right = false;

		if (prev) {
			switch (direction) {
			case "up":    down = true; direction = "down"; break;
			case "down":  up = true;   direction = "up";   break;
			case "left":  right = true;direction = "right";break;
			case "right": left = true; direction = "left"; break;
		}
			aiTimer = 0;
			return;
		}

		int dir = MathUtils.random(3);
		switch (dir) {
			case 0: up = true;    direction = "up";    break;
			case 1: down = true;  direction = "down";  break;
			case 2: left = true;  direction = "left";  break;
			case 3: right = true; direction = "right"; break;
		}

		aiTimer = 0;
	}

	@Override
	public void draw(Graphics g) {
		float xx = xo + (x - xo) * TheLastEmpire.a;
		float yy = yo + (y - yo) * TheLastEmpire.a;

		if (up || down || left || right) {
			animationTimer += Gdx.graphics.getDeltaTime();
		}

		TextureRegion texture = currentAnimation.getKeyFrame(animationTimer, true);
		if (texture != null) {
			g.drawTexture(texture, xx, yy, 1f, 2f);
		}

		Player player = level.getPlayer();
		if (dialogues.size > 0) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.E) && player.boundingBox.intersects(boundingBox.grow(0.2f, 0.2f))
					&& !chatting) {
				player.interact(this);
				startChatting();
			}
		}
	}

	public void startChatting() {
		chatting = true;
		up = false;
		down = false;
		left = false;
		right = false;

		GuiDialogue guiDialogue = new GuiDialogue(dialogues, this, null, level);
		TheLastEmpire.getTheLastEmpire().setGuiScreen(guiDialogue, false);
	}

	public void endChatting(boolean shouldLoadLevel) {
		if (levelName != null && shouldLoadLevel) {
			level.loadLevel(levelName, true, false);
			return;
		}
		advanceAi(true, false);
		chatting = false;
		level.getPlayer().stopChatting();
	}

	@Override
	public boolean shouldRender() {
		return Graphics.instance.inViewport(boundingBox.grow(16f, 16f));
	}

	public Array<NPCDialogue> getDialogues() {
		return dialogues;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	@Override
	public String getName() {
		return name;
	}

	private static Array<NPCDialogue> parseDialogues(JsonValue dialogueData) {
		Array<NPCDialogue> dialogues = new Array<>();
		if (dialogueData == null)
			return dialogues;

		for (JsonValue entry : dialogueData) {
			String key = entry.name();

			if (entry.isString()) {
				dialogues.add(new NPCDialogue(key, entry.asString()));
			} else if (entry.isObject()) {
				String text = entry.getString("text");
				JsonValue optionsJson = entry.get("options");

				ObjectMap<String, NPCDialogueOption> options = new ObjectMap<>();

				for (JsonValue optionEntry : optionsJson) {
					String optionText = optionEntry.name();
					JsonValue optData = optionEntry;

					String dialogue = optData.getString("dialogue", null);
					String response = optData.getString("response", null);
					String levelName = optData.getString("levelName", null);
					String emotion = optData.getString("emotionChange", "neutral");

					options.put(optionText, new NPCDialogueOption(dialogue, response, emotion, levelName));
				}

				dialogues.add(new NPCDialogue(key, text, options));
			}
		}

		return dialogues;
	}

	public String getEmotion() {
		return emotion;
	}

	public static NPC createNPC(Level level, JsonValue rawNpc) {
		String npcType = rawNpc.name();
		float x = rawNpc.getFloat("xPos", -1);
		float y = rawNpc.getFloat("yPos", -1);
		float xt = rawNpc.getFloat("xtPos", -1);
		float yt = rawNpc.getFloat("ytPos", -1);
		int type = rawNpc.getInt("type", 0);
		String name = rawNpc.getString("name", "Unknown");
		String levelName = rawNpc.getString("levelName", null);
		boolean canMove = rawNpc.getBoolean("canMove", true);
		String direction = rawNpc.getString("direction", "down");

		if (x < 0f || y < 0f) {
			x = MathUtils.random(0, level.getWidth());
			y = MathUtils.random(0, level.getHeight());
		}

		JsonValue rawDialogues = rawNpc.get("dialogues");
		Array<NPCDialogue> dialogues = parseDialogues(rawDialogues);

		switch (npcType) {
		case "Male":
			return new MaleNPC(level, type, x, y, xt, yt, name, levelName, dialogues, direction, canMove);
		case "Female":
			return new FemaleNPC(level, type, x, y, xt, yt, name, levelName, dialogues, direction, canMove);
		default:
			throw new RuntimeException("Failed to find NPC: " + npcType + "!");
		}
	}

}
