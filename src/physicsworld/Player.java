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
import org.jbox2d.collision.shapes.CircleShape;
import java.awt.*;
import java.util.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import org.jbox2d.collision.*;




public class Player implements Paintable{
    public BodyDef bodyDef;
    public Body playerBody;
    public Color color;
    public float radius = 0.5f;
    public Vec2 wp;
    
    
    public Player(BodyDef bodyDef, World world){
        playerBody = world.createBody(bodyDef);
        CircleShape playerShape = new CircleShape();
        playerShape.m_radius = radius;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.friction = 0.3f;
        fixtureDef.density = 1.0f;
        playerBody.createFixture(fixtureDef);
        playerBody.setLinearDamping(1.0f);
        color = Color.YELLOW;
        
    }
    public void checkBounds(){
        Vec2 p = playerBody.getPosition();
        if(p.x<0.5f){
            Vec2 position = new Vec2(0.5f,p.y);
            Vec2 bounce = new Vec2(0.2f, 0.0f);
            playerBody.setTransform(position, 0.0f);
            playerBody.applyLinearImpulse(bounce, p);
        }
    }
    @Override
    public void paint(Graphics2D g){
        wp = playerBody.getPosition();
        int xp = (int)((wp.x/8)*800);
        int yp = (int)((wp.y/6)*600);
        yp = (600-yp)-50;
        xp += -50;
        g.setColor(color);
        g.fillOval(xp, yp, 100, 100);
    }
    
    
    
}
