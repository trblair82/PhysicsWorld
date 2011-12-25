/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.jbox2d.dynamics.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.collision.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.File;
/**
 *
 * @author trblair
 */
public class Game extends Canvas {
    private BufferStrategy buffer;
    public static Vec2 worldP;
    public static Integer keyS, keyR, keyL;//variables for key presses
    public static Color colors[] = {Color.BLUE,Color.GREEN,Color.ORANGE};//list of colors for dynamic polygons
    private boolean gameRunning = true;//boolean for game loop
    public static boolean gameEnd = false;//boolean for game over
    public static ArrayList polygons = new ArrayList();//render list for dynamicpolygons
    public BodyDef polyBodyDef;//body definition for dynamic polygons
    public static Player player;//static player object
    public static ArrayList keys = new ArrayList();//list for key presses
    
    public static ArrayList bodyContacts = new ArrayList();//list for player body contacts
    private float critHit = 5;
    private float jump = 0.75f;
    public static ArrayList lifeIcons = new ArrayList();//list for life icons
    public static long startTime;//variable for system time
    public static BufferedImage spriteSheet, ground, background, gameover;//images used in game
    public static float offset = 0.0f;//variable to track screen offset
    public static float offsetStart = 6.0f;//offset starts at 600 pixles
    public Vec2 polygonPos = new Vec2();//Vec2 to hold start position of dynamic polygons
    public PolygonShape dynamicPolygon, dynamicTriangle;//shapes for blocks and triangle dynamic polygons
    public GroundBlock groundBlock;//ground block object
    public static int mpRatio = 100;//100 pixles to 1 meter
    public static int screenH = 600;//screen height
    public static int screenW = 800;//screen width
    private long levelTime = 60000;
    private long levelTimer = 0;
    public static long jumpTimer = 0;
    public static boolean canJump;
    public float velocity;
    public static Vec2 jumpForce = new Vec2();
    public Integer score = 0;//variable to track score
    public static int iconStart = 6;//render first life icon at 600 x
    public static float degrees;//variable to track degree of rotation for player image display
    public Vec2[] vertices = new Vec2[3];//list for vertices of triangle shape
    private Thread gameThread;//main game thead
    private Font f = new Font("SansSerif", Font.BOLD, 20);//creates font for score and game over display
    public final WorldManifold wm = new WorldManifold();//creates empty world manifold for use with contact points
    //handels key pressed and releases, adds key to list if pressed, removes if released
    public class PhysicsWorldKeyListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_RIGHT){
                keyR = (Integer)key;
                if(keys.contains(keyR)!=true){
                    keys.add(keyR);
                }
            }
            if(key == KeyEvent.VK_LEFT){
                keyL = (Integer)key;
                if(keys.contains(keyL)!=true){
                    keys.add(keyL);
                }
            }
            if(key == KeyEvent.VK_SPACE){
                keyS = (Integer)key;
                if(keys.contains(keyS)!=true){
                    keys.add(keyS);
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_RIGHT){
                keys.remove(keyR);
            }
            if(key == KeyEvent.VK_LEFT){
                keys.remove(keyL);
            }
            if(key == KeyEvent.VK_SPACE){
                keys.remove(keyS);
            }
        }
        @Override
        public void keyTyped(KeyEvent e){
        }
    }
    
    
    public Game(){
        JFrame container = new JFrame("Physics World");//initializes JFrame window
        container.setSize(screenW ,screenH);//sets size to screen height and width
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Exit all threads if window is closed
        container.addKeyListener(new PhysicsWorldKeyListener());//adds my key listener
        JPanel panel = (JPanel)container.getContentPane();//initializes JPanel
        panel.setPreferredSize(new Dimension(screenW,screenH));//sets size of panel
        panel.setLayout(null);//default layout
        setBounds(0,0,screenW,screenH);//set boundries of panel
        panel.add(this);//add canvas to panel
        setIgnoreRepaint(true);//I say when to paint
        container.pack();
        container.setResizable(false);//not resizeable
        container.setVisible(true);//makes window visible
        
        createBufferStrategy(2);//creates double buffer strategy
        buffer = getBufferStrategy();//assigns buffer strategy to variable
        
    }
    //loads and returns a buffered image
    public static BufferedImage loadImage(String ref) {  
        BufferedImage bimg = null;  
        try {  
  
            bimg = ImageIO.read(new File(ref));  
        } catch (Exception e) {  
             
        }  
        return bimg;  
    }
    //horizontally flips a buffered image
    public static BufferedImage horizontalflip(BufferedImage img) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);  
        g.dispose();  
        return dimg;  
    }
    
    //sets up starting components of game, ie..player, polygons
    public void gameSetup(){
            Vec2 a = new Vec2(0.5f,0.0f);//vertices for triangle shape
            Vec2 b = new Vec2(1.0f,-1.0f);
            Vec2 c = new Vec2(2.0f,-0.5f);
            polyBodyDef = new BodyDef();//body definition for DynamicPolygon
            polyBodyDef.type = BodyType.DYNAMIC;//sets DynamicPolygons to dynamic
            vertices[0]= a;vertices[1]=b;vertices[2]=c;//adds triangle vertices to a Vec2 array
            dynamicTriangle = new PolygonShape();//creates empty polygon
            dynamicTriangle.set(vertices, 3);//makes polygon a triangle
            dynamicPolygon = new PolygonShape();//creates polygon for use with blocks
        for(int i = 1; i < 8; i++){//creates 8 randomly sized and positioned blocks
            polygonPos.x = (float)(Math.random()*12.0f)+offset+1;//random x position
            polygonPos.y = 8.0f;//set y position
            polyBodyDef.position.set(polygonPos);//set start position of DynamicPolygons
            
            double width = (Math.random()*0.5)+0.1;//random width
            double height = (Math.random()*0.5)+0.1;//random height
            dynamicPolygon.setAsBox((float)width, (float)height);//sets polygon shape to random width and height
            DynamicPolygon p = new DynamicPolygon(polyBodyDef, dynamicPolygon);//creates new DynamicPolygon
            
            polygons.add(p);//adds polygon to render array
        }
        for(int i = 1; i < 8; i++){//creates 8 randomly positioned triangles
            polygonPos.x = (float)(Math.random()*12.0f)+offset+1;
            polygonPos.y = 9.0f;
            polyBodyDef.position.set(polygonPos);//sets start position
            DynamicPolygon t = new DynamicPolygon(polyBodyDef, dynamicTriangle);//creates new dynamicpolygon that is a triangle
            polygons.add(t);//adds to render array
        }
        //creates body definition for the ground and creates ground body
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0.0f, -0.9f);//sets position of ground
        groundBodyDef.type = BodyType.STATIC;//sets ground to static
        ground = Game.loadImage("images/grass_sprite_light.png");//loads ground image
        background = Game.loadImage("images/background_2.png");//loads background image
        groundBlock = new GroundBlock(groundBodyDef,PhysicsWorld.world);//adds ground to physics world
        
        spriteSheet = Game.loadImage("images/TransparentRoadRunner.png");//loads player images
        gameover = Game.loadImage("images/gameover.png");//loads game over screen
        BodyDef playerBodyDef = new BodyDef();//creates player body definition
        playerBodyDef.position.set(0.27f, 0.7f);//sets player start position
        playerBodyDef.type = BodyType.DYNAMIC;//sets player to dynamic
        player = new Player(playerBodyDef, PhysicsWorld.world);//adds player to physics world
        Player.currentAnim = Player.rightStopped;//starts player with stopped animation
        AnimationThread animRun = new AnimationThread();//creates runnable for thread to run animations
        Thread animThread = new Thread(animRun);//creates thread for animation
        
        animThread.start();//starts animation thread
        //adds life icons to a render array
        for(int i = 0; i < 3;i++){lifeIcons.add(spriteSheet.getSubimage(405, 265, 85, 120));}
    }
    
    
    public void gameLoop(){
        gameThread = Thread.currentThread();//sets gameThread to main thread
        while(gameRunning){
            
            
            long current = startTime;
            startTime = System.currentTimeMillis();//gets system time
            levelTimer += startTime - current;
            jumpTimer += startTime - current;
            long fps = 32;//my frames per second limit
            
            float timeStep = (float)1.0/60.0f;//variables for stepping physics world
            int velocityIterations = 8;
            int positionIterations = 3;
            Graphics2D g = (Graphics2D) buffer.getDrawGraphics();//sets my graphics object to variable g
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//sets antialliasing to on

            g.drawImage(background, null, 0, 0);//draws background image
            g.setFont(f);//sets font
            //keeps track of offset based on player position
            if(player.playerBody.getPosition().x>offset+offsetStart){
                offset += player.playerBody.getPosition().x-(offset+offsetStart);
            }
            //itterates through list of DynamicPolygons and removes if it has been passed by my offset
            //and adds 100 to score, paints remaining DynamicPolygons in list
            for(int i = 0; i < polygons.size(); i++){
                    DynamicPolygon poly =(DynamicPolygon) polygons.get(i);
                    if(poly.polyBody.getPosition().x<offset-2.0f){polygons.remove(poly);score += 100;}
                    poly.paint(g);
                    
            }
            
            
            for(int i = 0;i<bodyContacts.size();i++){//itterates over list of current contacts with player body
                Contact contact = (Contact)bodyContacts.get(i);//gets contact from list
                
                contact.getWorldManifold(wm);//gets the world for each contact
                float wx = wm.points[0].x;
                float wy = wm.points[0].y;
                worldP = new Vec2(wx,wy);//creates vector2 from first point of contact in world manifold
                Vec2 localP = player.playerBody.getLocalPoint(worldP);//gets player local point from contact world point
                float x = localP.x;//sets x and y to x and y of player local point
                float y = localP.y;
                float angle = player.playerBody.getAngle();//gets the angle of rotation of playe body
                //updates the acctual local point of contact based on the angle of rotation of player
                float px =(float)(Math.cos(angle) * (x-0.0f) - Math.sin(angle) * (y-0.0));
                float py =(float)(Math.sin(angle) * (x-0.0f) + Math.cos(angle) * (y-0.0));
                jumpForce.x = -px;
                jumpForce.y = jump;
                //finds the length of the hypotnues
                float hyp = (float)Math.sqrt((Math.pow((double)px, 2))+(Math.pow((double)py, 2)));
                //finds andgle to display player image based on angle of contact surface
                float ratio = px/hyp;
                //sets degree of image rotatin if contact is on bottom half of player
                if(py<-0.1){degrees = (float)(Math.asin((double)ratio));
                canJump = true;}
                if(contact.getFixtureA().getUserData() == player.playerFixture.getUserData()){
                Body hit = contact.getFixtureB().getBody();//if head sensor is fixture A
                Vec2 hitV = hit.getLinearVelocity();//get velocity of fixture B
                velocity = hitV.y;//assign velocity to fixture B y velocity
                }
                if(contact.getFixtureB().getUserData() == player.playerFixture.getUserData()){
                Body hit = contact.getFixtureA().getBody();//if head sensot is fixture B
                Vec2 hitV = hit.getLinearVelocity();//get velocity of fixture A
                velocity = hitV.y;//assign velocity to fixtue A y velocity

                }
                if(py>0.1&&Math.abs(velocity)>critHit){Player.dead = true;Player.justDied = true;}
            }
            //sets degree of image rotation to 0 if player is in the air
            if(bodyContacts.isEmpty()){degrees = 0.0f;canJump = false;}
            //updates player
            player.update();
            //paints ground images
            groundBlock.paint(g);
            //paints player
            player.paint(g);
            //iterates over life icons and paints
            for(int i = 0;i<lifeIcons.size();i++){
                
                BufferedImage icon = (BufferedImage)lifeIcons.get(i);
                g.drawImage(icon, null, (i+iconStart)*icon.getWidth(this)+5, 5);
            }
            
            if(levelTimer>levelTime){
                levelTimer = 0;
                levelTime -= 1000;
                polygonPos.x = (float)(Math.random()*8.0f)+offset;
                polyBodyDef.position.set(polygonPos);//sets posistion to random
                double width = (Math.random()*0.5)+0.1;
                double height = (Math.random()*0.5)+0.1;
                dynamicPolygon.setAsBox((float)width, (float)height);//sets size to random
                DynamicPolygon c = new DynamicPolygon(polyBodyDef,dynamicPolygon);//creates new polygon
                polygons.add(c);//adds to render list
                polygonPos.x = (float)(Math.random()*8.0f)+offset;
                polyBodyDef.position.set(polygonPos);//sets triangle to random positon
                DynamicPolygon t = new DynamicPolygon(polyBodyDef, dynamicTriangle);//creates new triangle
                polygons.add(t);//adds to render list 
                
            }
            //adds new dynamic polygon if render list is less than 8
            
            if(polygons.size()<8){
                polygonPos.x = (float)(Math.random()*24.0f)+offset;
                polyBodyDef.position.set(polygonPos);//sets posistion to random
                double width = (Math.random()*0.5)+0.1;
                double height = (Math.random()*0.5)+0.1;
                dynamicPolygon.setAsBox((float)width, (float)height);//sets size to random
                DynamicPolygon c = new DynamicPolygon(polyBodyDef,dynamicPolygon);//creates new polygon
                polygons.add(c);//adds to render list
                polygonPos.x = (float)(Math.random()*24.0f)+offset;
                polyBodyDef.position.set(polygonPos);//sets triangle to random positon
                DynamicPolygon t = new DynamicPolygon(polyBodyDef, dynamicTriangle);//creates new triangle
                polygons.add(t);//adds to render list
            }
            
                
            g.setColor(Color.yellow);//set color of score to be displayed
            String scoreString = "SCORE"+" "+score.toString();//creates string to display
            g.drawString(scoreString, 5, 20);//display score
            
            if(Game.lifeIcons.isEmpty()){//if life icons are gone
                String gameOver = "GAME"+" "+"OVER";//creates game over string
                g.drawImage(gameover, null, 0, 0);//draws game over background
                g.drawString(gameOver, 340, 290);//draws game over string
                g.drawString(scoreString, 340, 310);gameEnd = true;//draws score string in middle of screen
            }
            
            
            g.dispose();//flips buffer
            buffer.show();//shows buffer
            if(gameEnd){//if game is over sleep for 5 seconds and exit all threads
               try {gameThread.sleep(5000); } catch (Exception e) {};
               System.exit(0);
            }
            
            if(!Player.dead){//if player is not dead updat the physics world
               PhysicsWorld.world.step(timeStep, velocityIterations, positionIterations); 
            }
            
            
            //sets frames per second to my fps variable
            long fps_lim = (long)(1000/fps);
            long cur_t;
            long delta_t;
            do{
             cur_t = System.currentTimeMillis();//
             delta_t = (cur_t - startTime);
             
             
            }while(delta_t < fps_lim);
        }
        
    }
    
    }
