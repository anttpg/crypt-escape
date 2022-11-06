package com.cryptescape.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.cryptescape.game.rooms.Box;
import com.cryptescape.game.rooms.Door;
import com.cryptescape.game.rooms.Interactable;

public class ContactManager {

	public static void createCollisionListener() {
		GameScreen.world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				for (Door door : GameScreen.player.getRoom().getDoors()) {
					if(door != null) {
						if (contact.getFixtureA().getBody() == GameScreen.player.getBody()
								&& contact.getFixtureB().getBody() == door.getInteractionBody()) {
							door.setPlayerInRange(true);
						}
					}
				}
			
				for (Box box : GameScreen.player.getRoom().getBoxes()) {
				    if (contact.getFixtureA().getBody() == GameScreen.player.getBody()
				            && contact.getFixtureB().getBody() == box.getInteractionBody()) {
				        box.setPlayerInRange(true);
				    }
				}
			}
			

			@Override
			public void endContact(Contact contact) {
				for (Door door : GameScreen.player.getRoom().getDoors()) {
					if(door != null) {
						if (contact.getFixtureA().getBody() == GameScreen.player.getBody()
								&& contact.getFixtureB().getBody() == door.getInteractionBody()) {
							door.setPlayerInRange(false);
						
						}
					}
				}
				
				for (Box box : GameScreen.player.getRoom().getBoxes()) {
                    if (contact.getFixtureA().getBody() == GameScreen.player.getBody()
                            && contact.getFixtureB().getBody() == box.getInteractionBody()) {
                        box.setPlayerInRange(false);
                    }
                }
				
				
				
			}

			@Override
			public void postSolve(Contact arg0, ContactImpulse arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void preSolve(Contact arg0, Manifold arg1) {
				// TODO Auto-generated method stub
			}
		});

	}
}
