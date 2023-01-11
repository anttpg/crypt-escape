package com.cryptescape.game.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cryptescape.game.Constants;
import com.cryptescape.game.Filters;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.InputHandler;
import com.cryptescape.game.hud.Inventory;
import com.cryptescape.game.hud.items.InventoryItem;

public class DroppedItem extends Freeform {
	InventoryItem savedItem;

	public DroppedItem(InventoryItem item, Room room) {
		super(
				GameScreen.player.getX(), 
				GameScreen.player.getY() - GameScreen.player.getHeight() / 4f, 
				Constants.TILESIZE/5f  * (item.getWidth()/item.getHeight()) * item.getScale(), 
				Constants.TILESIZE/5f * item.getScale(),
				item.getName(), 
				room
				);
		
		setItem(item);
		

		super.setTextureRegion(item.getRegion());
		super.createInteractionRectangle(
				Constants.TILESIZE/5f  * (item.getWidth()/item.getHeight()) * item.getScale(), 
				Constants.TILESIZE/5f * item.getScale(), (short) -2);
		
	  }

	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	public void setItem(InventoryItem item) {
		this.savedItem = item;
		item.setVisible(false);
		savedItem.getBody().setActive(false);
	}

	public void pickup() {
		savedItem.setVisible(true);
		savedItem.getBody().setActive(true);
		Inventory.addItem(savedItem);
		super.getParentRoom().queueForDisposal(this);
		InputHandler.tab_pressed = true;
		//InputHandler.tab_pressed = true;
	}

}
