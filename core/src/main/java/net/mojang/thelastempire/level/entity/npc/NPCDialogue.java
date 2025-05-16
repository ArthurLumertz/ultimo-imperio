package net.mojang.thelastempire.level.entity.npc;

import com.badlogic.gdx.utils.ObjectMap;

public class NPCDialogue {

	private String id;
	private String text;
	private ObjectMap<String, NPCDialogueOption> options;

	public NPCDialogue(String id, String text) {
		this.id = id;
		this.text = text;
	}

	public NPCDialogue(String id, String text, ObjectMap<String, NPCDialogueOption> options) {
		this.id = id;
		this.text = text;
		this.options = options;
	}

	public boolean hasOptions() {
		return options != null && options.size > 0;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public ObjectMap<String, NPCDialogueOption> getOptions() {
		return options;
	}
	

}
