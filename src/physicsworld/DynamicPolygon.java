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

public class DynamicPolygon implements Paintable {//implements paintable and overrides paint function
    public Color color;//variable for color of polygon
    public Body polyBody;//body for dynamic polygon
    public static Fixture polyFixture;//fixture for polygon
    
    
    public DynamicPolygon(BodyDef bodyDef, PolygonShape polyShape){//takes body defintion and shape as parameters
  
        polyBody = PhysicsWorld.world.createBody(bodyDef);//adds polygon to physics world with given body definition
        polyBody.setAngularVelocity((float)Math.random()*3);//sets random angle of rotation
        FixtureDef fixtureDef = new FixtureDef();//creates fixture definiton
        fixtureDef.shape = polyShape;//sets given shape to fixture definition
        fixtureDef.density = 2.0f;//sets mass
        fixtureDef.friction = 0.3f;//sets friction
        polyFixture = polyBody.createFixture(fixtureDef);//adds fixture to polygon
        polyFixture.setUserData(4);//sets pointer to polygon fixtue
        
        int c = (int)(Math.random()*100)%Game.colors.length;//choses random color from list for polygon
        color = (Color)Game.colors[c];
        
  
        
    }
    @Override
    public void paint(Graphics2D g){//overides paintable paint function
        
        ArrayList xpoints = new ArrayList();//array for x coordinates of polygon vertices
        ArrayList ypoints = new ArrayList();//array for y coordinates of polygon vertices
        Fixture fix = polyBody.getFixtureList();//gets fixture of polygon
        PolygonShape poly = (PolygonShape)fix.m_shape;//gets shape from polygon fixture
        for(int i = 0;i<poly.m_vertexCount;i++){//itterates over list of vertices for polygon
            Vec2 wp = polyBody.getWorldPoint(poly.m_vertices[i]);//gets world point of each vertice
            int xp = (int)((wp.x-Game.offset)*Game.mpRatio);//converts world x point to rendering x point
            int yp = (int)(wp.y*Game.mpRatio);//converts world y point to y rendering point
            yp = Game.screenH-yp;//reverses y point
            xpoints.add(xp);//adds x to list
            ypoints.add(yp);//adds y to list
            
        }
         Object[] x = xpoints.toArray();//creates object array from array list
         Object[] y = ypoints.toArray();//creates object array from array list
         int[] xints = new int[xpoints.size()];//creates int list for x
         int[] yints = new int[ypoints.size()];//creates int list for y
         for(int a = 0;a<xpoints.size();a++){//itterates over object lists and casts objects to interger
             xints[a] = (Integer) x[a];
             yints[a] = (Integer) y[a];
             
         }
         
        Polygon p = new Polygon(xints, yints, xpoints.size());//creates new polygon for rendering based on physics world location
        g.setColor(color);//sets color to random variable
        g.fillPolygon(p);//fills polygon
        
        
        
        
    }
    
}
