package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.npc.NPC;
import net.mojang.thelastempire.level.entity.npc.NPCDialogue;
import net.mojang.thelastempire.level.entity.npc.NPCDialogueOption;
import net.mojang.thelastempire.level.entity.player.Player;

public class GuiDialogue extends GuiScreen {

	private final static TextureRegion BACKDROP_TEXTURE = new TextureRegion(Resources.getTexture("dialogue"), 0, 0, 128,
			51);
	private final static float SCALE = 4f;

	private NPC npc;
	private Array<NPCDialogue> dialogues;
	private Array<String> dialogues2;
	private TextureRegion npcTexture;
	private TextureRegion playerTexture;

	private int dialogueIndex = 0;
	private int dialogueChar = 0;

	private Array<String> optionKeys = new Array<>();
	private int selectedOption = 0;
	private boolean inOptionSelection = false;
	private boolean optionHandled = false;

	private Runnable onDialogueFinish;
	private Level level;

	public GuiDialogue(Array<NPCDialogue> dialogues, NPC npc, Runnable onDialogueFinish, Level level) {
		this.level = level;
		this.dialogues = dialogues;
		this.npc = npc;
		this.npcTexture = new TextureRegion(npc.getTexture(), 0, 0, 16, 14);
		this.playerTexture = new TextureRegion(Resources.getTexture("char"), 0, 0, 16, 14);
		this.onDialogueFinish = onDialogueFinish;
	}

	public GuiDialogue(Player player, Runnable onDialogueFinish, Level level) {
		this.level = level;
		this.dialogues2 = player.getDialogues();
		this.playerTexture = new TextureRegion(player.getTexture(), 0, 0, 16, 14);
		this.onDialogueFinish = onDialogueFinish;
	}
	
	public GuiDialogue(Array<String> dialogues, String textureName, Runnable onDialogueFinish, Level level) {
		this.level = level;
		this.dialogues2 = dialogues;
		this.playerTexture = new TextureRegion(Resources.getTexture(textureName), 0, 0, 16, 14);
		this.onDialogueFinish = onDialogueFinish;
	}

	@Override
	public void tick() {
		super.tick();

		String str = getCurrentDialogueString();

		if (!inOptionSelection) {
			if (dialogueChar < str.length()) {
				dialogueChar++;
				playTypeSound();
			}
		}

		if (npc == null) {
			return;
		}

		NPCDialogue current = getCurrentDialogue();
		if (current.hasOptions() && dialogueChar >= current.getText().length() && !inOptionSelection
				&& !optionHandled) {
			prepareOptions(current);
		}
	}

	private void prepareOptions(NPCDialogue current) {
		optionKeys.clear();
		optionKeys.addAll(current.getOptions().keys().toArray());
		selectedOption = 0;
		inOptionSelection = true;
		optionHandled = false;
	}

	private void handleOptionInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			skipDialogue();
		}
		
		if (!inOptionSelection || optionKeys.size == 0) return;

		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			selectedOption = (selectedOption - 1 + optionKeys.size) % optionKeys.size;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			selectedOption = (selectedOption + 1) % optionKeys.size;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			handleOptionSelection();
		}
	}

	@Override
	public void draw(Graphics g) {
		handleOptionInput();
		handleSkipDialogue();

		String currentDialogue = getCurrentDialogueString();
		int c = currentDialogue.split("\n").length;

		g.setColor(0f, 0f, 0f, 0.5f);
		g.drawRect(0, 0, g.getScreenWidth(), g.getScreenHeight());
		g.setColor(1f, 1f, 1f, 1f);
		float dialogWidth = 128 * SCALE;
		float dialogHeight = (inOptionSelection || c > 5 ? 70 : 50) * SCALE;
		float x = (g.getScreenWidth() - dialogWidth) / 2f;
		float y = (g.getScreenHeight() - dialogHeight) / 2f - (inOptionSelection || c > 5 ? 30 : 0);

		g.drawTexture(BACKDROP_TEXTURE, x, y, dialogWidth, dialogHeight);

		drawNPC(g);
		drawDialogueText(g);
		if (inOptionSelection)
			drawOptions(g);
	}

	private void handleSkipDialogue() {
		if (inOptionSelection)
			return;

		if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
				|| Gdx.input.isKeyJustPressed(Input.Keys.E)) && dialogueChar >= getCurrentDialogueString().length()) {

			if (dialogueIndex < getDialogueSize() - 1) {
				dialogueIndex++;
				dialogueChar = 0;
				inOptionSelection = false;
				optionHandled = false;
			} else {
				if (npc != null) {
					npc.endChatting(true);
				}
				TheLastEmpire.getTheLastEmpire().setGuiScreen(null, false);
				if (onDialogueFinish != null) {
					onDialogueFinish.run();
				}
			}
		}
	}

	private void drawNPC(Graphics g) {
		float width = 16 * 3;
		float height = 14 * 3;
		float s = 4f;

		int c = getCurrentDialogueString().split("\n").length;
		if (c > 5) {
			s = 4.4f;
		}
		
		float x = (g.getScreenWidth() - width) / 2f;
		float y = (g.getScreenHeight() + 50 * s) / 2f;

		if (npc == null) {
			g.drawTexture(playerTexture, x, y, width, height);
			return;
		}

		NPCDialogue currentDialogue = getCurrentDialogue();
		TextureRegion texture = npcTexture;

		if (currentDialogue != null && "player-choice".equals(currentDialogue.getId())) {
			texture = playerTexture;
		}

		g.drawTexture(texture, x, y, width, height);
	}

	private void drawDialogueText(Graphics g) {
		String dialogue = getCurrentDialogueString();
		String[] lines = dialogue.substring(0, Math.min(dialogueChar, dialogue.length())).split("\n");

		float ty = (g.getScreenHeight() + g.getFontSize()) / 2f + (lines.length * g.getFontSize() / 2f);

		for (String line : lines) {
			g.drawCenteredString(line, g.getScreenWidth(), ty, 0xFFFFFF);
			ty -= g.getFontSize() * 1.5f;
		}
	}

	private void drawOptions(Graphics g) {
		float oy = (g.getScreenHeight() / 2f) - (optionKeys.size * g.getFontSize() * 1.5f);		    

		for (int i = 0; i < optionKeys.size; i++) {
			String text = optionKeys.get(i);
			String[] lines = text.split("\n");

		    int c = (i == selectedOption) ? 0xFFFF00 : 0xAAAAAA;
		    g.drawCenteredString("> ", g.getScreenWidth() - g.getStringWidth(lines[0]) - 20, oy, c);
			
			for (int j = 0; j < lines.length; j++) {
				String line = lines[j];
				g.drawCenteredString(line, g.getScreenWidth(), oy, c);
				if (lines.length > 1) {					
					oy -= g.getFontSize() * 1.5f;
				}
			}
			
			oy -= g.getFontSize() * 1.5f;
		}
	}
	
	private void skipDialogue() {
		theLastEmpire.setGuiScreen(null, false);
		npc.endChatting(false);
	}

	private void handleOptionSelection() {
		NPCDialogue current = getCurrentDialogue();
		String choice = optionKeys.get(selectedOption);
		NPCDialogueOption option = current.getOptions().get(choice);

		if (option == null)
			return;

		if (option.getLevelName() != null) {
			level.loadLevel(option.getLevelName(), true, false);
			return;
		}

		dialogues.insert(dialogueIndex + 1, new NPCDialogue("npc-response", option.getResponse()));
		dialogues.insert(dialogueIndex + 1, new NPCDialogue("player-choice", option.getDialogue()));

		npc.setEmotion(option.getEmotionChange());

		dialogueIndex++;
		dialogueChar = 0;
		inOptionSelection = false;
		optionHandled = true;
	}

	private NPCDialogue getCurrentDialogue() {
		return dialogues.get(dialogueIndex);
	}

	private String getCurrentDialogueString() {
		if (npc != null) {
			return dialogues.get(dialogueIndex).getText();
		} else {
			return dialogues2.get(dialogueIndex);
		}
	}

	private void playTypeSound() {
		TheLastEmpire.getTheLastEmpire().playSound("type", 0.2f, MathUtils.random(0.8f, 1.2f));
	}

	private int getDialogueSize() {
		if (npc == null) {
			return dialogues2.size;
		}
		return dialogues.size;
	}

	@Override
	public boolean shouldFadeIn() {
		return false;
	}
}
