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
import org.jbox2d.collision.shapes.PolygonShape;
import java.awt.*;
import java.util.*;

public class Block implements Paintable {
    public Color color;
    public BodyDef bodyDef;
    public double width, height;
    public Body blockBody;
    public static ArrayList blockBodies = new ArrayList();
    
    public Block(BodyDef bodyDef, World world){
  
        blockBody = world.createBody(bodyDef);
        PolygonShape dynamicBlock = new PolygonShape();
        width = (Math.random()*0.5)+0.1;
        height = (Math.random()*0.5)+0.1;
        dynamicBlock.setAsBox((float)width, (float)height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBlock;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        blockBody.createFixture(fixtureDef);
        blockBodies.add(blockBody);
        int c = (int)(Math.random()*100)%Game.colors.length;
        color = (Color)Game.colors[c];
        
  
        
    }
    @Override
    public void paint(Graphics2D g){
        
        ArrayList xpoints = new ArrayList();
        ArrayList ypoints = new ArrayList();
        Fixture fix = blockBody.getFixtureList();
        PolygonShape poly = (PolygonShape)fix.m_shape;
        for(int i = 0;i<4;i++){
            Vec2 wp = blockBody.getWorldPoint(poly.m_vertices[i]);
            int xp = (int)((wp.x/8)*800);
            int yp = (int)((wp.y/6)*600);
            yp = 600-yp;
            xpoints.add(xp);
            ypoints.add(yp);
            
        }
         Object[] x = xpoints.toArray();
         Object[] y = ypoints.toArray();
         int[] xints = new int[4];
         int[] yints = new int[4];
         for(int a = 0;a<4;a++){
             xints[a] = (Integer) x[a];
             yints[a] = (Integer) y[a];
             
         }
         
        Polygon p = new Polygon(xints, yints, 4);
        g.setColor(color);
        g.fillPolygon(p);
        
        
        
        
    }
    
}
