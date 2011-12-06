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
    public int x1, x2, x3, x4, y1, y2, y3, y4;
    public static Body blockBody;
    public static ArrayList blockBodies = new ArrayList();
    
    public Block(BodyDef bodyDef, World world){
  
        blockBody = world.createBody(bodyDef);
        PolygonShape dynamicBlock = new PolygonShape();
        dynamicBlock.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBlock;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        blockBody.createFixture(fixtureDef);
        blockBodies.add(blockBody);
        color = Color.BLUE;
  
        
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
