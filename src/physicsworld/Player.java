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
import org.jbox2d.dynamics.contacts.*;
import java.awt.geom.*; 




public class Player implements Paintable{
    
    public Body playerBody, footBody, headBody, hit;//bodies for player,sensors,and contacts
    public float radius = 0.2f;//radius of player
    //creates new animations
    public static Animation left = new Animation();
    public static Animation right = new Animation();
    public static Animation rightStop = new Animation();
    public static Animation leftStop = new Animation();
    public static Animation rightStopped = new Animation();
    public static Animation leftStopped = new Animation();
    public static Animation death = new Animation();
    public static Animation currentAnim, newAnim;//variables to track current animation
    public Vec2 playerP, playerF, playerC, playerV, playerLF, jump, bounce;//vec2s for applying impulses and calculating postion
    public Fixture footFixture, playerFixture, headFixture;//fixtures for player and sensors
    public PolygonShape footShape, headShape;//shapes for sensors
    
    public float velocity;//variable for velocity of object contacting head sensor
    public static boolean dead = false;//boolean for death
    public Contact oldContact;//variable to hold previous contact with head sensor
    private int xp, yp, ySpeed;//variables for x and y positions and yspeed for death animation
    
    public Player(BodyDef bodyDef, World world){//takes body defintion and physics world to create body in
        playerLF = new Vec2(-2.0f, 0.0f);//Vec2 for left force on player
        playerF = new Vec2(2.0f,0.0f);//Vec2 for right force on player
        jump = new Vec2(0.0f, 1.0f);//Vec2 for jump impulse 
        
        bounce = new Vec2(0.1f, 0.0f);//Vec2 for force on player when exceeds left bounds of screen
        
        playerBody = world.createBody(bodyDef);//adds player body to physics world given body definition
        CircleShape playerShape = new CircleShape();//creates new circle shape for player fixture
        playerShape.m_radius = radius;//sets radius of player
        FixtureDef playerFD = new FixtureDef();//creates fixture definition for player fixture
        playerFD.shape = playerShape;//sets circle shape to player fixture 
        playerFD.friction = 0.3f;//sets friction
        playerFD.density = 1.0f;//sets mass
        playerFixture = playerBody.createFixture(playerFD);//adds fixture to player body
        playerFixture.setUserData(1);//sets pointer to player body fixture
        playerBody.setLinearDamping(1.0f);//sets max velocity of player body
        
        playerP = playerBody.getPosition();//gets start position of player boddy for use in positioning sensors
        
        BodyDef headBodyDef = new BodyDef();//creates body definition for head sensor
        headBodyDef.position.set(playerP.x-0.1f,playerP.y+0.13f);//sets position of head sensor
        headBodyDef.type = BodyType.DYNAMIC;//sets head sensor to dynamic
        headBody = world.createBody(headBodyDef);//adds head sensor to physics world
        headShape = new PolygonShape();//creates shape for head fixture
        headShape.setAsBox(0.2f, 0.09f);//sets size of head fixture
        FixtureDef headFD = new FixtureDef();//creates fixture definition for head fixture
        headFD.shape = headShape;//sets head shape to head fixture
        headFD.isSensor = true;//sets head fixture to sensor
        headFixture = headBody.createFixture(headFD);//adds head fixture to head sensor body
        headFixture.setUserData(2);//sets pointer to head fixture
        
        BodyDef footBodyDef = new BodyDef();//creates body definition for player foot
        footBodyDef.position.set(playerP.x-.125f,playerP.y-0.13f);//sets player foot postion
        footBodyDef.type = BodyType.DYNAMIC;//sets player foor to dynamic
        footBody = world.createBody(footBodyDef);//adds player foot body to physics world
        footShape = new PolygonShape();//creates shape for player foot fixtue
        footShape.setAsBox(0.25f, 0.09f);//sets size of player foot sensor
        FixtureDef footFD = new FixtureDef();//creates fixture definition for foot sensor
        footFD.shape = footShape;//sets player foot shape to foot sensor fixture
        footFD.isSensor = true;//sets foot fixture to sensor
        footFixture = footBody.createFixture(footFD);//adds foot fixture to player foot sensor body
        footFixture.setUserData(3);//sets pointer to player foot sensot fixture
        
        for(int i = 0;i<2;i++){//adds frames to animations
            right.addFrame(Game.spriteSheet.getSubimage((i*75)+25, 235, 70, 30), 250);}
        rightStop.addFrame(Game.spriteSheet.getSubimage(390, 110, 35, 50), 250);
        rightStop.addFrame(Game.spriteSheet.getSubimage(390, 110, 35, 50), 250);
        rightStopped.addFrame(Game.spriteSheet.getSubimage(25, 55, 30, 50), 250);
        rightStopped.addFrame(Game.spriteSheet.getSubimage(25, 55, 30, 50), 250);
        for(int i = 0;i<2;i++){//adds frames to animations
            left.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage((i*75)+25, 235, 70, 30)), 250);}
        leftStop.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(390, 110, 35, 50)), 250);
        leftStop.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(390, 110, 35, 50)), 250);
        leftStopped.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(25, 55, 30, 50)), 250);
        leftStopped.addFrame(Game.horizontalflip(Game.spriteSheet.getSubimage(25, 55, 30, 50)), 250);
        death.addFrame(Game.spriteSheet.getSubimage(115, 60, 40, 45), 250);
        death.addFrame(Game.spriteSheet.getSubimage(115, 60, 40, 45), 250);
    }

    public void update(){//updates player animation and handles collision reactions, and updates sensor positions
        
        playerP = playerBody.getPosition();//gets player position   
        
        playerV = playerBody.getLinearVelocity();//gets player velocity
        playerC = playerBody.getWorldCenter();//gets world center of mass for player
        newAnim = currentAnim;
        
        if(Game.keys.contains(Game.keyR)){//chooses right or rightStop animation based on user input and player velocity
                playerBody.applyForce(playerF, playerC);
                newAnim = right;
            }else if(playerV.x>0&&Game.keys.contains(Game.keyR)!= true&&Game.keys.contains(Game.keyL)!= true){
                newAnim = rightStop;
            }
        if(Game.keys.contains(Game.keyL)){//chooses left or leftStop animation based on user input and player velocity
                playerBody.applyForce(playerLF, playerC);
                newAnim = left;
            }else if(playerV.x<0&&Game.keys.contains(Game.keyL)!= true&&Game.keys.contains(Game.keyR)!= true){
                newAnim = leftStop;
            }//chooses stopped animations based on player velocity
        if(Math.abs(playerV.x)<0.2&&newAnim == leftStop){newAnim = leftStopped;}
        if(Math.abs(playerV.x)<0.2&&newAnim == rightStop){newAnim = rightStopped;}
        
        
        
        if(Game.keys.contains(Game.keyS)){//checks to see if player is elligable to jump and applies jump impulse
            if(!Game.footContacts.isEmpty()&& playerV.y<0.1){playerBody.applyLinearImpulse(jump, playerC);}
            }
        if(currentAnim != newAnim){//if animation has changed starts new animation
            currentAnim = newAnim;
            currentAnim.start();
        }
        if(playerP.x-Game.offset<0.3f){//checks to see if player is out of bounds and changes player positoin accordingly
            Vec2 position = new Vec2(Game.offset+0.3f,playerP.y);
            //moves player back on screen
            playerBody.setTransform(position, 0.0f);
            //applies light impulse to keep player from sticking
            playerBody.applyLinearImpulse(bounce, playerP);
        }
        for(int i = 0; i<Game.headContacts.size();i++){//itterates over collisions with player head sensor
            Contact hc = (Contact)Game.headContacts.get(i);//gets contact from list
            if(hc.getFixtureA().getUserData() == headFixture.getUserData()){
                hit = hc.getFixtureB().getBody();//if head sensor is fixture A
                Vec2 hitV = hit.getLinearVelocity();//get velocity of fixture B
                velocity = hitV.y;//assign velocity to fixture B y velocity
            }
            if(hc.getFixtureB().getUserData() == headFixture.getUserData()){
                hit = hc.getFixtureA().getBody();//if head sensot is fixture B
                Vec2 hitV = hit.getLinearVelocity();//get velocity of fixture A
                velocity = hitV.y;//assign velocity to fixtue A y velocity

            }
            if(Math.abs(velocity)> 7.0f&&hc != oldContact){
               dead = true;//if velocity of contact fixture is high enough player dies
//               
               oldContact = hc;break;//track contact and break 
            }
            
        }
        if(dead){currentAnim = death;}//set animation to death if player is dead
        Vec2 footPos = new Vec2(playerP.x-0.125f,playerP.y-0.2f);//update foot sensor position based on player position
        footBody.setTransform(footPos, 0.0f);
        Vec2 headPos = new Vec2(playerP.x-0.1f,playerP.y+0.2f);//update head sensor position based on player position
        headBody.setTransform(headPos, 0.0f);
    }
    @Override
    public void paint(Graphics2D g){//overrides paintables paint function
        playerP = playerBody.getPosition();//get current player position
        xp = (int)((playerP.x-Game.offset)*Game.mpRatio);//convert position point to rendering point
        yp = (int)(playerP.y*Game.mpRatio);//based of meter to pixel ratio
        yp = Game.screenH-yp;//invert y coordinate
        if(currentAnim != right&&currentAnim != left){
        yp += -30;//adjust image center position based on animation
        xp += -10;}else{xp += -37;}
        AffineTransform rotate = new AffineTransform();//creates a transform object to rotate player image
        
            Game.degrees = Game.degrees*-1;//reverses angle in physics world for rendering
        if(dead){
            Game.degrees = 0.0f;//if dead set rotation to 0
            ySpeed += 10;//speed for death animation
            yp -= ySpeed;//updates death animation position
            
            if(yp<-10){//checks to see if death animation is finished
                Game.lifeIcons.remove(0);//removes 1 life
                Game.iconStart += 1;//adds to start rendering position for life icons list
                Vec2 start = new Vec2(Game.offset+0.3f,10.0f);//new player start position
                Vec2 stop = new Vec2(0.0f,0.0f);//new player velocity
                playerBody.setTransform(start, 0.0f);//move player to start position
                playerBody.setLinearVelocity(stop);//reset player velocity
                currentAnim = rightStopped;//set animation to right Stopped
                dead = false;//resets dead boolean
                ySpeed = 0;//reset death animation speed variable
                
            }
        }
        rotate.setToTranslation(xp, yp);//sets image location based on player location
        //rotates image based on angle of point of contact
        rotate.rotate(Game.degrees, currentAnim.getImage().getWidth(null)/2, currentAnim.getImage().getHeight(null)/2);
        
        //displays current player image
        g.drawImage(currentAnim.getImage(), rotate, null);
        
    }
    
    
    
}
