package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.Pointer;
import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.npc.NPC;
import net.mojang.thelastempire.level.entity.npc.NPCDialogue;
import net.mojang.thelastempire.level.entity.npc.NPCDialogueOption;
import net.mojang.thelastempire.level.entity.player.Player;

public class GuiDialogue extends GuiScreen {

	private final static float SCALE = 4f;

	private final static TextureRegion BACKDROP_TEXTURE = new TextureRegion(Resources.getTexture("dialogue"), 0, 0, 128,
			64);

	private static final Animation<TextureRegion> SKIP_ANIMATION;

	static {
		Texture t = Resources.getTexture("dialogue");

		int frames = 7;
		TextureRegion[] regions = new TextureRegion[frames + 3];
		for (int i = 0; i < frames; i++) {
			regions[i] = new TextureRegion(t, i * 8, 72, 8, 8);
		}
		for (int i = 0; i < 3; i++) {
			regions[frames + i] = new TextureRegion(regions[frames - 1]);
		}

		SKIP_ANIMATION = new Animation<TextureRegion>(0.2f, regions);
	}

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

	private boolean isSkippable = false;

	private float animationTimer;

	public GuiDialogue(Array<NPCDialogue> dialogues, NPC npc, Runnable onDialogueFinish, Level level,
			boolean isSkippable) {
		this.level = level;
		this.dialogues = dialogues;
		this.npc = npc;
		this.npcTexture = new TextureRegion(npc.getTexture(), 0, 0, 16, 14);
		this.playerTexture = new TextureRegion(Resources.getTexture("char"), 0, 0, 16, 14);
		this.onDialogueFinish = onDialogueFinish;
		this.isSkippable = isSkippable;
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && isSkippable) {
			skipDialogue();
		}

		if (!inOptionSelection || optionKeys.size == 0)
			return;

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

		g.setColor(0f, 0f, 0f, 0.5f);
		g.drawRect(0, 0, g.getScreenWidth(), g.getScreenHeight());
		g.setColor(1f, 1f, 1f, 1f);
		float dialogWidth = 128 * SCALE;
		float dialogHeight = 64 * SCALE;
		float x = (g.getScreenWidth() - dialogWidth) / 2f;
		float y = (g.getScreenHeight() - dialogHeight) / 2f;
		g.drawTexture(BACKDROP_TEXTURE, x, y, dialogWidth, dialogHeight);

		drawNPC(g, x, y, dialogHeight);

		float yy = y + dialogHeight * 0.45f;
		if (inOptionSelection) {
			yy += optionKeys.size * 16;
			drawOptions(g, yy);
		}
		drawDialogueText(g, yy);

		String current = getCurrentDialogueString();
		if (!inOptionSelection && dialogueChar >= current.length() - 1) {
			theLastEmpire.setPointer(Pointer.Type.SKIP);

			animationTimer += Gdx.graphics.getDeltaTime();
			TextureRegion t = SKIP_ANIMATION.getKeyFrame(animationTimer * 2f, true);

			float rs = 16;
			float xt = x + dialogWidth - 48;
			float yt = y + 40;
			g.setColor(0.3f, 0.3f, 0.3f, 1f);
			g.drawTexture(t, xt + (rs / 8), yt - (rs / 8), rs, rs);
			g.setColor(1f, 1f, 1f, 1f);

			g.drawTexture(t, xt, yt, rs, rs);

		}
	}

	private void handleSkipDialogue() {
		if (inOptionSelection)
			return;

		if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
				|| Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.E))
				&& dialogueChar >= getCurrentDialogueString().length()) {

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

	private void drawNPC(Graphics g, float xx, float yy, float h) {
		float width = 16 * 3;
		float height = 14 * 3;

		float x = xx + width * 2;
		float y = yy + h - g.getFontSize() * 1.75f;

		String name = npc != null ? npc.getName() : "Você";
		g.drawString(name, x, y, 0xFFFFFF);

		x = xx + width * 0.4f;
		y = yy + h - height - 10f;
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

	private void drawDialogueText(Graphics g, float y) {
		String dialogue = getCurrentDialogueString();
		String[] lines = dialogue.substring(0, Math.min(dialogueChar, dialogue.length())).split("\n");

		float ty = y + (lines.length * g.getFontSize() * 2f / 3f);

		for (String line : lines) {
			g.drawCenteredString(line, g.getScreenWidth(), ty, 0xFFFFFF);
			ty -= g.getFontSize() * 1.5f;
		}
	}

	private void drawOptions(Graphics g, float y) {
		float oy = y - (optionKeys.size * g.getFontSize() * 1.5f);

		for (int i = 0; i < optionKeys.size; i++) {
			String text = optionKeys.get(i);
			String[] lines = text.split("\n");

			int c = (i == selectedOption) ? 0xFFFF00 : 0xAAAAAA;
			g.drawCenteredString("> ", g.getScreenWidth() - g.getStringWidth(lines[0]) - 20, oy, c);

			for (int j = 0; j < lines.length; j++) {
				String line = lines[j];

				Rectangle rect = Rectangle.tmp.set((g.getScreenWidth() - 192) / 2f, oy - 24, 192, 24);
				if (mousePos.contains(rect)) {
					selectedOption = i;
					if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
						handleOptionSelection();
					}
				}

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
