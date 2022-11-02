package com.cryptescape.game.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cryptescape.game.GameScreen;

public class BagItem extends InventoryItem{
    
    public BagItem(World world, String name, float x, float y) {
        super(world, name, name, x, y, 7f);
        
        Vector2[] vertices = new Vector2[] {new Vector2(0.2f,0.5f), new Vector2(0.15f,0.05f), new Vector2(0.9f,0.05f), new Vector2(0.9f,0.5f)};
        super.makeChainFixture(world, x, y, 1f, vertices);
    }

}
