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
import org.jbox2d.collision.shapes.*;
import java.awt.*;
import java.util.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import org.jbox2d.collision.*;




public class Player implements Paintable{
    public BodyDef bodyDef;
    public Body playerBody, sensorBody;
    
    public float radius = 0.27f;
    public Vec2 wp;
    public static Animation left = new Animation();
    public static Animation right = new Animation();
    public static Animation rightStop = new Animation();
    public static Animation leftStop = new Animation();
    public static Animation rightStopped = new Animation();
    public static Animation leftStopped = new Animation();
    public static Animation currentAnim;
    public Animation newAnim;
    public Vec2 playerP, playerF, playerC, playerV, playerLF, jump;
    public Fixture sensorFixture;
    public PolygonShape sensorShape;
    public Player(BodyDef bodyDef, World world){
        
        playerBody = world.createBody(bodyDef);
        CircleShape playerShape = new CircleShape();
        playerShape.m_radius = radius;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.friction = 0.3f;
        fixtureDef.density = 1.0f;
        playerBody.createFixture(fixtureDef);
        playerP = playerBody.getPosition();
        BodyDef sensorDef = new BodyDef();
        sensorDef.position.set(playerP.x,playerP.y-0.2f);
        sensorDef.type = BodyType.DYNAMIC;
        sensorBody = world.createBody(sensorDef);
        sensorShape = new PolygonShape();
        
        sensorShape.setAsBox(0.09f, 0.09f);
        FixtureDef sensorFD = new FixtureDef();
        sensorFD.shape = sensorShape;
        sensorFD.isSensor = true;
        sensorFixture = sensorBody.createFixture(sensorFD);
        
        sensorFixture.setUserData(3);
        playerBody.setLinearDamping(1.0f);
        for(int i = 0;i<2;i++){
            right.addFrame(Game.spriteSheet.getSubimage((i*75)+25, 235, 70, 30), 250);}
        rightStop.addFrame(Game.spriteSheet.getSubimage(390, 110, 35, 50), 250);
        rightStop.addFrame(Game.spriteSheet.getSubimage(390, 110, 35, 50), 250);
        rightStopped.addFrame(Game.spriteSheet.getSubimage(25, 55, 30, 50), 250);
        rightStopped.addFrame(Game.spriteSheet.getSubimage(25, 55, 30, 50), 250);
        for(int i = 0;i<2;i++){
            left.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage((i*75)+25, 235, 70, 30)), 250);}
        leftStop.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(390, 110, 35, 50)), 250);
        leftStop.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(390, 110, 35, 50)), 250);
        leftStopped.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(25, 55, 30, 50)), 250);
        leftStopped.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(25, 55, 30, 50)), 250);
        
    }
//    public void checkBounds(){
//        Vec2 p = playerBody.getPosition();
//        if(p.x<0.5f){
//            Vec2 position = new Vec2(0.5f,p.y);
//            Vec2 bounce = new Vec2(0.2f, 0.0f);
//            playerBody.setTransform(position, 0.0f);
//            playerBody.applyLinearImpulse(bounce, p);
//        }
//    }
    public void update(){
        playerLF = new Vec2(-2.0f, 0.0f);
        playerF = new Vec2(2.0f,0.0f);
        playerP = playerBody.getPosition();    
        jump = new Vec2(0.0f, 2.0f);
        playerV = playerBody.getLinearVelocity();
        playerC = playerBody.getWorldCenter();
        newAnim = currentAnim;
        
        if(Game.keys.contains(Game.keyR)){
                playerBody.applyForce(playerF, playerC);
                newAnim = right;
            }else if(playerV.x>0&&Game.keys.contains(Game.keyR)!= true&&Game.keys.contains(Game.keyL)!= true){
                newAnim = rightStop;
            }
        if(Game.keys.contains(Game.keyL)){
                playerBody.applyForce(playerLF, playerC);
                newAnim = left;
            }else if(playerV.x<0&&Game.keys.contains(Game.keyL)!= true&&Game.keys.contains(Game.keyR)!= true){
                newAnim = leftStop;
            }
        if(Math.abs(playerV.x)<0.2&&newAnim == leftStop){newAnim = leftStopped;}
        if(Math.abs(playerV.x)<0.2&&newAnim == rightStop){newAnim = rightStopped;}
        
        
        
        if(Game.keys.contains(Game.keyS)){
            if(!Game.contacts.isEmpty()&& playerV.y<0.1){playerBody.applyLinearImpulse(jump, playerC);}
                
//                for(int i = 0;i<Game.contacts.size();i++){
//                    Contact contact = (Contact)Game.contacts.get(i);
//                    Manifold manifold = contact.getManifold();
//                   
//                    if(manifold.localPoint.y>0.0f&& playerV.y<0.1){
//                        playerBody.applyLinearImpulse(jump, playerC);
//                        break;
//                    }
//                   
//                   
//                }    
                
            }
        if(currentAnim != newAnim){
            currentAnim = newAnim;
            currentAnim.start();
        }
        if(playerP.x<0.5f){
            Vec2 position = new Vec2(0.5f,playerP.y);
            Vec2 bounce = new Vec2(0.2f, 0.0f);
            playerBody.setTransform(position, 0.0f);
            playerBody.applyLinearImpulse(bounce, playerP);
        }
        Vec2 sensorPos = new Vec2(playerP.x,playerP.y-0.2f);
        sensorBody.setTransform(sensorPos, 0.0f);
    }
    @Override
    public void paint(Graphics2D g){
        wp = playerBody.getPosition();
        int xp = (int)((wp.x/8)*800);
        int yp = (int)((wp.y/6)*600);
        yp = 600-yp;
        if(currentAnim != right&&currentAnim != left){
        yp +=-23;
        xp += -17;}else{xp += -35;}
        Vec2[] vertices = sensorShape.getVertices();
        Vec2 point = sensorBody.getWorldPoint(vertices[0]);
        int x = (int)((point.x/8)*800);
        int y = (int)((point.y/6)*600);
        y = 600-y;
        g.setColor(Color.yellow);
        
        g.fillRect(x, y, 10, 10);
        g.drawImage(currentAnim.getImage(), xp, yp, null);
        
    }
    
    
    
}
