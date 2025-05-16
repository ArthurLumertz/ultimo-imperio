package net.mojang.thelastempire.level.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.BulletEntity;
import net.mojang.thelastempire.level.entity.boss.DomPedroII;
import net.mojang.thelastempire.level.entity.player.Player;

public class GunItem extends Item {

	public GunItem(int id, String name) {
		super(id, name);
	}
	
	@Override
	public void onUse(Player player, Level level) {
		Vector2 mousePos = Graphics.instance.unproject(Gdx.input.getX(), Gdx.input.getY());
		
		BulletEntity bullet = new BulletEntity(level, player.x + 0.5f, player.y + 1.0f, mousePos.x, mousePos.y, DomPedroII.class);
		level.addEntity(bullet);
		
		TheLastEmpire.getTheLastEmpire().playSound("shoot", 0.5f, MathUtils.random(0.8f, 1.2f));
	}
	
	

}
