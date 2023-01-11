package com.cryptescape.game.hud;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.hud.items.InventoryItem;

public class EquipmentSlot extends InventoryItem {
    private InventoryItem equippedItem = null;
    private Fixture possibleEquip;
    private ArrayList<String> acceptedTypes;

    public EquipmentSlot(World world, String name, float x, float y, int zindex, float scale) {
        super(world, name, GameScreen.atlas.findRegion(name), x, y, scale, zindex);
        super.makeSquareFixture(world, x, y, 1f);
        super.fixture.getBody().setActive(false);
        super.isDroppable = false;
        
        super.createInteractionSquare(scale/6f, scale/6f);
        super.createUserData(false); //Sets movable to false, as well as creating the default user data
    }
    
    public void setAcceptedItems(ArrayList<String> at) {
        acceptedTypes = at;
    }
    
    @Override
    public void act(float delta){
        super.defaultAct(delta);
        super.setInRange(); //Built to only sample the ones we need to check.
        
        if(mouseInRange) {
            if(Inventory.currentlyDragging != null) {
                possibleEquip = Inventory.currentlyDragging;
            }
            else if(possibleEquip != null && testForContainment(possibleEquip)) {
                for(Actor item : Inventory.getItemGroup().getChildren()) {
                    if(possibleEquip.getBody() == ((InventoryItem)item).getBody() && acceptedTypes.contains(((InventoryItem)item).getName())) {
                        equippedItem = ((InventoryItem)item);
                        equippedItem.getBody().setTransform(new Vector2(getX() + getWidth()/2f, + getY() + getHeight()/2f), 0);
                        equippedItem.getBody().setAwake(false);
                        possibleEquip = null;
                        break;
                    }
                }
            }
        }  
       
        else 
            possibleEquip = null;
        
    }

    public InventoryItem getEquippedItem() {
        return equippedItem;
    }
    
}
