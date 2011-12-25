/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;

/**
 *
 * @author trblair
 */
import org.jbox2d.collision.*;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.dynamics.contacts.Contact;
   

public class WorldContactListener implements ContactListener {
    
    public WorldContactListener(){//contact listener to add to physics world
        
    }
    @Override
    public void beginContact(Contact contact){//adds contact to list when contact begins
        
        //if contac fixture A is player add contact to bodycontacts list
        if(contact.getFixtureA().getUserData() == Game.player.playerFixture.getUserData()){
            Game.bodyContacts.add(contact);
        
        }//if contact fixture B is player add contact to bodycontacts list
        if(contact.getFixtureB().getUserData() == Game.player.playerFixture.getUserData()){
            Game.bodyContacts.add(contact);
        }
        
    }
    
    @Override
    public void endContact(Contact contact){//removes contacts from list when contact has ended
        //if contact fixture A is player remove contact from bodycontacts list
        if(contact.getFixtureA().getUserData() == Game.player.playerFixture.getUserData()){
            Game.bodyContacts.remove(contact);
            Game.canJump = false; 
        }//if contact fixture B is player remoce contact from bodycontacs list
        if(contact.getFixtureB().getUserData() == Game.player.playerFixture.getUserData()){
            Game.bodyContacts.remove(contact);
            Game.canJump = false;
//            
        }
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold){
        
        
//        
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse){
        
    }
    

}
