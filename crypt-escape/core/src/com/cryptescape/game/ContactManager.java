package com.cryptescape.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactManager {

	public static void createCollisionListener() {
		GameScreen.world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				for (Door d : GameScreen.player.getRoom().getDoors()) {
					if(d != null) {
						if (contact.getFixtureA().getBody() == GameScreen.player.getBody()
								&& contact.getFixtureB().getBody() == d.getInteractionBody()) {
							d.setPlayerInRange(true);
						}
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
				for (Door d : GameScreen.player.getRoom().getDoors()) {
					if(d != null) {
						if (contact.getFixtureA().getBody() == GameScreen.player.getBody()
								&& contact.getFixtureB().getBody() == d.getInteractionBody()) {
							d.setPlayerInRange(false);
						
						}
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
