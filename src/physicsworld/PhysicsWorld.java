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
import org.jbox2d.collision.AABB;
public class PhysicsWorld {
    public static Game g;
    public static World world;
    public static AABB worldAABB;

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        g = new Game();
        worldAABB = new AABB();
        worldAABB.lowerBound.set(new Vec2((float) -100.0, (float) -100.0));
        worldAABB.upperBound.set(new Vec2((float) 100.0, (float) 100.0));
        Vec2 gravity = new Vec2(0.0f, -10.0f);
        boolean doSleep = true;
        world = new World(gravity,doSleep);
        g.gameSetup();
        g.gameLoop();
        // TODO code application logic here
    }
}
