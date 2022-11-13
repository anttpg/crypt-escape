package com.cryptescape.game.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.hud.Inventory;
import com.cryptescape.game.hud.InventoryItem;

public class DroppedItem extends Freeform {
	InventoryItem savedItem;

	public DroppedItem(InventoryItem item, Room r) {
		super(
				GameScreen.player.getX(), 
				GameScreen.player.getY() - GameScreen.player.getHeight() / 4f, 
				Constants.TILESIZE/5f  * (item.getWidth()/item.getHeight()) * item.getScale(), 
				Constants.TILESIZE/5f * item.getScale(),
				item.getName(), 
				r
				);

		super.setTextureRegion(item.getRegion());
		super.createInteractionRadius(
				Constants.TILESIZE/5f  * (item.getWidth()/item.getHeight()) * item.getScale(), 
				Constants.TILESIZE/5f * item.getScale());
		
	}

	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	/**
	 * Copies the item given, and saves it for later use.
	 */
	public void setItem(InventoryItem item) {
		this.savedItem = new InventoryItem(item);
	}

}
