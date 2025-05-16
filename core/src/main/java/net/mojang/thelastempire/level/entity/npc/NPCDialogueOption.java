package net.mojang.thelastempire.level.entity.npc;

public class NPCDialogueOption {

	private String dialogue;
	private String response;
	private String emotionChange;
	private String levelName;

	public NPCDialogueOption(String dialogue, String response, String emotionChange, String levelName) {
		this.dialogue = dialogue;
		this.response = response;
		this.emotionChange = emotionChange;
		this.levelName = levelName;
	}

	public String getDialogue() {
		return dialogue;
	}

	public String getResponse() {
		return response;
	}

	public String getEmotionChange() {
		return emotionChange;
	}

	public String getLevelName() {
		return levelName;
	}

	public boolean isDialogue(String str) {
		return dialogue.equals(str);
	}

	public boolean isResponse(String str) {
		return response.equals(str);
	}

}
