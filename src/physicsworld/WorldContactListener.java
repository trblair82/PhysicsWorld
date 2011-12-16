/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;

/**
 *
 * @author trblair
 */
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import org.jbox2d.collision.*;
import java.util.ArrayList;
public class WorldContactListener implements ContactListener {
    
    public WorldContactListener(){
        
    }
    @Override
    public void beginContact(Contact contact){
        if(contact.getFixtureA().getUserData() == Game.player.sensorFixture.getUserData()
                &&contact.getFixtureB() != Game.player.playerBody.m_fixtureList){
            Game.contacts.add(contact);
        
        }
        if(contact.getFixtureB().getUserData() == Game.player.sensorFixture.getUserData()
                &&contact.getFixtureA() != Game.player.playerBody.m_fixtureList){
            Game.contacts.add(contact);
        }
    }
    
    @Override
    public void endContact(Contact contact){
        if(contact.getFixtureA().getUserData() == Game.player.sensorFixture.getUserData()){
            Game.contacts.remove(contact);
        
        }
        if(contact.getFixtureB().getUserData() == Game.player.sensorFixture.getUserData()){
            Game.contacts.remove(contact);
        }
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold){
        
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse){
        
    }
}
