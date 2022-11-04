package com.cryptescape.game.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.cryptescape.game.GameScreen;

public class CannedFoodItem extends InventoryItem {
    public CannedFoodItem(World world, String name, float x, float y, int zindex) {
        super(world, name, new TextureRegion(GameScreen.atlas.findRegion(name)), x, y, 1.5f, zindex);      
        super.makeSquareFixture(world, x, y, 1f);
    }
    
    @Override
    public void act(float delta){
        super.defaultAct(delta);
    }
}
