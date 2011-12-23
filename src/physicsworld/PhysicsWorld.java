/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;

/**
 *
 * @author trblair
 */
import org.jbox2d.dynamics.*;
import org.jbox2d.common.Vec2;

public class PhysicsWorld {
    public static Game g;//main game object
    public static World world;//physics world object
    

    
    
    public static void main(String[] args) {
        g = new Game();//instantiates new game
        
        Vec2 gravity = new Vec2(0.0f, -10.0f);//sets gravity to use in physics world
        boolean doSleep = true;//sets world to do sleep mode, bodys sleep when at rest
        world = new World(gravity,doSleep);//creates new world with given gravity
        world.setContactListener(new WorldContactListener());//adds my contact listener to the physics world
        
        g.gameSetup();//runs gamesetup function 
        
        g.gameLoop();//runs game loop
        
        // TODO code application logic here
    }
}
