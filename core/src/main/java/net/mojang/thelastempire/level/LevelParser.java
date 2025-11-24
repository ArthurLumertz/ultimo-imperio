package net.mojang.thelastempire.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.level.entity.animal.FireflyEntity;
import net.mojang.thelastempire.level.entity.boss.Boss;
import net.mojang.thelastempire.level.entity.npc.NPC;
import net.mojang.thelastempire.level.entity.object.BarrackEntrance;
import net.mojang.thelastempire.level.entity.object.House;
import net.mojang.thelastempire.level.entity.object.LampPost;
import net.mojang.thelastempire.level.entity.object.decoration.EntityDecoration;
import net.mojang.thelastempire.level.entity.object.event.EntityEvent;
import net.mojang.thelastempire.level.entity.object.furniture.EntityFurniture;
import net.mojang.thelastempire.level.entity.player.Player;
import net.mojang.thelastempire.level.item.Item;
import net.mojang.thelastempire.level.tile.Tile;
import net.mojang.thelastempire.math.Vec2;

public class LevelParser {

	private Level level;
	private String currentLevel;
	private String levelSound;

	public LevelParser(Level level) {
		this.level = level;
	}

	private void loadLevelData(String[] rawData) {
		int w = level.getWidth();
		int h = level.getHeight();

		for (int y = 0; y < h; y++) {
			String[] rawColumns = rawData[y].split(",");

			for (int x = 0; x < w; x++) {
				String rawValue = rawColumns[x];
				byte value = Byte.parseByte(rawValue);

				if (value == Tile.grass.getId()) {
                    level.setData(x, y, MathUtils.random(1, 4));
                }
				level.setTile(x, h - y - 1, value);
			}
		}
	}

	private void loadLevelJson(String rawJson) {
		JsonValue rootValue = new JsonReader().parse(rawJson);

		float globalLight = rootValue.getFloat("globalLight", 1f);
		level.setGlobalLight(globalLight);

		boolean canMove = rootValue.getBoolean("canMove", true);
		String direction = rootValue.getString("direction", "down");

		levelSound = rootValue.getString("levelSound", null);
		if (levelSound != null) {
			TheLastEmpire.getTheLastEmpire().playMusic(levelSound, true);
		}

		JsonValue rawHouses = rootValue.get("houses");
		JsonValue rawLights = rootValue.get("lights");
		JsonValue rawNpcs = rootValue.get("npcs");
		JsonValue rawFurniture = rootValue.get("furniture");
		JsonValue rawEntities = rootValue.get("entities");
		JsonValue rawDialogues = rootValue.get("dialogues");
		JsonValue rawBosses = rootValue.get("bosses");
		JsonValue rawItems = rootValue.get("items");
		JsonValue rawDecorations = rootValue.get("decorations");
		JsonValue rawCameraOffset = rootValue.get("cameraOffset");

		parseHouses(rawHouses);
		parseLights(rawLights);
		parseNpcs(rawNpcs);
		parseFurniture(rawFurniture);
		parseEntities(rawEntities);
		parseBoss(rawBosses);
		parseDecorations(rawDecorations);
		Vec2 cameraOffset = parseCameraOffset(rawCameraOffset);
		Item[] items = parseItems(rawItems);

		Array<String> dialogues = parseDialogues(rawDialogues);
		String dialogueFinishLevel = rootValue.getString("dialogueFinishLevel", null);

		float xSpawn = rootValue.getFloat("xSpawn", level.getWidth() / 2f);
		float ySpawn = rootValue.getFloat("ySpawn", level.getHeight() / 2f);
		Player player = new Player(level, xSpawn, ySpawn, canMove, direction, cameraOffset);
		if (dialogues != null) {
			player.setDialogues(dialogues, dialogueFinishLevel);
		}
		if (items != null) {
			player.setItems(items);
		}
		level.addEntity(player);
	}
	
	private Vec2 parseCameraOffset(JsonValue rawCameraOffset) {
		if (rawCameraOffset == null) {
			return Vec2.newPermanent(0, 0);
		}
		
		float x = rawCameraOffset.getFloat("x");
		float y = rawCameraOffset.getFloat("y");
		return Vec2.newPermanent(x, y);
	}

	private void parseDecorations(JsonValue rawDecorations) {
		if (rawDecorations == null) {
			return;
		}

		for (JsonValue rawDecoration : rawDecorations) {
			EntityDecoration decoration = EntityDecoration.createDecoration(rawDecoration, level);
			level.addEntity(decoration);
		}
	}

	private Item[] parseItems(JsonValue rawItems) {
		if (rawItems == null) {
			return null;
		}

		Array<Item> items = new Array<Item>();
		for (JsonValue rawItem : rawItems) {
			Item item = Item.createItem(rawItem);
			items.add(item);
		}

		return items.toArray(Item.class);
	}

	private void parseBoss(JsonValue rawBosses) {
		if (rawBosses == null) {
			return;
		}

		for (JsonValue rawBoss : rawBosses) {
			Boss boss = Boss.createBoss(rawBoss, level);
			level.addEntity(boss);
		}

	}

	private void parseEntities(JsonValue rawEntities) {
		if (rawEntities == null) {
			return;
		}
		for (JsonValue rawEntity : rawEntities) {
			String name = rawEntity.name();
			float xPos = rawEntity.getFloat("xPos");
			float yPos = rawEntity.getFloat("yPos");

			if ("Event".equals(name)) {
				String levelName = rawEntity.getString("levelName");
				float xSpawn = rawEntity.getFloat("xSpawn", level.getWidth() / 2);
				float ySpawn = rawEntity.getFloat("ySpawn", level.getWidth() / 2);

				EntityEvent event = new EntityEvent(level, xPos, yPos, levelName, xSpawn, ySpawn);
				level.addEntity(event);
			} else if ("Lamp Post".equals(name)) {
				String direction = rawEntity.getString("direction", "horizontal");
				LampPost lampPost = new LampPost(level, xPos, yPos, direction);
				level.addEntity(lampPost);
			} else if ("Firefly".equals(name)) {
				FireflyEntity firefly = new FireflyEntity(level, xPos, yPos);
				level.addEntity(firefly);
			} else if ("Barrack Entrance".equals(name)) {
				String levelToGo = rawEntity.getString("levelToGo", null);
				BarrackEntrance barrackEntrance = new BarrackEntrance(level, xPos, yPos, levelToGo);
				level.addEntity(barrackEntrance);
			}
		}
	}

	private void parseHouses(JsonValue rawHouses) {
		if (rawHouses != null) {
			for (JsonValue rawHouse : rawHouses) {
				float xPos = rawHouse.getFloat("xPos");
				float yPos = rawHouse.getFloat("yPos");
				String levelToGo = rawHouse.getString("levelToGo");
				int type = rawHouse.getInt("type");

				House house = new House(level, xPos, yPos, levelToGo, type);
				level.addEntity(house);
			}
		}
	}

	private void parseLights(JsonValue rawLights) {
		Graphics graphics = Graphics.instance;
		graphics.clearLights();

		if (rawLights != null) {
			for (JsonValue rawLight : rawLights) {
				float xPos = rawLight.getFloat("xPos");
				float yPos = rawLight.getFloat("yPos");
				float innerRadius = rawLight.getFloat("innerRadius");
				float outerRadius = rawLight.getFloat("outerRadius");
				float intensity = rawLight.getFloat("intensity");

				JsonValue rawColor = rawLight.get("color");
				float r = rawColor.getFloat("r", 1f);
				float g = rawColor.getFloat("g", 1f);
				float b = rawColor.getFloat("b", 1f);
				float a = rawColor.getFloat("a", 1f);

				Color color = Color.newTemp(r, g, b, a);
				Light light = new Light(xPos, yPos, intensity, innerRadius, outerRadius, color);

				graphics.setLight(light);
			}
		}
	}

	private void parseNpcs(JsonValue rawNpcs) {
		if (rawNpcs == null) {
			return;
		}

		for (JsonValue rawNpc : rawNpcs) {
			NPC npc = NPC.createNPC(level, rawNpc);
			level.addEntity(npc);
		}
	}

	private void parseFurniture(JsonValue rawFurnitures) {
		if (rawFurnitures == null) {
			return;
		}

		for (JsonValue rawFurniture : rawFurnitures) {
			EntityFurniture furniture = EntityFurniture.createFurniture(level, rawFurniture);
			level.addEntity(furniture);
		}
	}

	private Array<String> parseDialogues(JsonValue rawDialogues) {
		if (rawDialogues == null)
			return null;

		Array<String> dialogues = new Array<String>();
		for (JsonValue dialogue : rawDialogues) {
			dialogues.add(dialogue.toString());
		}

		return dialogues;
	}

	public void loadLevel(String levelName) {
		stopMusic();

		this.currentLevel = levelName;

		FileHandle handleData = Gdx.files.internal("levels/" + levelName + "/level.dat");
		FileHandle handleJson = Gdx.files.internal("levels/" + levelName + "/level.json");

		String dataSource = handleData.readString().replaceAll("[ \\t\\f\\r]+", "");
		String jsonSource = handleJson.readString();

		String[] rawData = dataSource.split("\n");
		int height = rawData.length;
		int width = rawData[0].split(",").length;

		level.createLevel(width, height);
		Graphics.instance.clearLights();

		loadLevelData(rawData);
		loadLevelJson(jsonSource);
	}

	public String getCurrentLevel() {
		return currentLevel;
	}

	public void stopMusic() {
		if (levelSound != null) {
			TheLastEmpire.getTheLastEmpire().stopMusic(levelSound);
		}
	}

}
