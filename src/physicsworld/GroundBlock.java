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
import org.jbox2d.collision.shapes.PolygonShape;
import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class GroundBlock implements Paintable {//implements my paitable interface and overides paint function
    
    public int start=0;//variable for start position of ground image list
    public Body groundBody;//body for ground block
    public static ArrayList groundImages = new ArrayList();//list for ground images
    public GroundBlock(BodyDef bodyDef, World world){//takes body definition and world parameters
        groundBody = world.createBody(bodyDef);//adds ground block to world with given body defintion
        PolygonShape groundBlock = new PolygonShape();//creates shape for ground block
        groundBlock.setAsBox(1000000000000.0f, 1.0f);//sets size of ground block
        FixtureDef fixtureDef = new FixtureDef();//creates fixture definiton for ground block fixture
        fixtureDef.shape = groundBlock;//sets ground block shape to fixture defintion
        fixtureDef.friction = 1.0f;//sets friction of ground
        groundBody.createFixture(fixtureDef);//adds fixture to ground body
        for(int i = 0;i<17;i++){//adds ground images to list for rendering
            groundImages.add(Game.ground.getSubimage(40, 20, 50, 20));
        }
    }
    @Override
    public void paint(Graphics2D g){//overides paint function of paintable
        
        
        BufferedImage image = (BufferedImage)groundImages.get(0);//checks if ground image is out of render area to enable scrolling
        if((start*image.getWidth())-((Game.offset*100)+Game.offsetStart)<-image.getWidth()){
            groundImages.remove(0);//removes if render area has passed
            groundImages.add(Game.ground.getSubimage(40, 20, 50, 20));//adds new image to end of list
            start++;}//adds to list start variable
        
        for(int i = 0;i<groundImages.size();i++){
            //itterates over ground image render list and paints images
            image = (BufferedImage)groundImages.get(i);
            g.drawImage(image,(int)(((start+i)*(image.getWidth()))-(Game.offset*Game.mpRatio)), 580, null);
        }
    }
}

