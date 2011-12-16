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
import org.jbox2d.callbacks.*;
import org.jbox2d.collision.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.*;
import java.io.File;
/**
 *
 * @author trblair
 */
public class Game extends Canvas {
    private BufferStrategy buffer;
    public static Integer keyS, keyR, keyL;
    public static Color colors[] = {Color.BLUE,Color.GREEN,Color.ORANGE};
    private boolean gameRunning = true;
    public static ArrayList blocks;
//    public static Vec2 playerP, playerF, playerC, playerV, playerLF, jump;
    public static Player player;
    public static ArrayList keys = new ArrayList();
    public static ArrayList contacts = new ArrayList();
    public static long startTime;
    public static BufferedImage spriteSheet, spriteSheetFlip;
    
    
    
    public class PhysicsWorldKeyListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e){
            
            int key = e.getKeyCode();
//            playerLF = new Vec2(-5.0f, 0.0f);
//            playerF = new Vec2(5.0f,0.0f);
//            
//            jump = new Vec2(0.0f, 5.0f);
//            playerV = player.playerBody.getLinearVelocity();
//            playerC = player.playerBody.getWorldCenter();
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
        JFrame container = new JFrame("Physics World");
        container.setSize(800 ,600);
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        container.addKeyListener(new PhysicsWorldKeyListener());
        JPanel panel = (JPanel)container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);
        
        setBounds(0,0,800,600);
        panel.add(this);
        setIgnoreRepaint(true);
        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        blocks = new ArrayList();
        
        
        

        
        
        
        
        createBufferStrategy(2);
        buffer = getBufferStrategy();
        
        
        
        
    }
    public static BufferedImage loadImage(String ref) {  
        BufferedImage bimg = null;  
        try {  
  
            bimg = ImageIO.read(new File(ref));  
        } catch (Exception e) {  
             
        }  
        return bimg;  
    }
    public static BufferedImage horizontalflip(BufferedImage img) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);  
        g.dispose();  
        return dimg;  
    }  
    public void gameSetup(){
        for(int i = 1; i < 4; i++){
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DYNAMIC;
            bodyDef.position.set((i*1.9f)+2.0f,6.0f);
            Block b = new Block(bodyDef, PhysicsWorld.world);
            
            blocks.add(b);
        }
        for(int p = 1;p<4;p++){
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DYNAMIC;
            bodyDef.position.set(p*1.5f,8.0f);
            Block a = new Block(bodyDef, PhysicsWorld.world);
            blocks.add(a);
        }
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0.0f, -1.0f);
        groundBodyDef.type = BodyType.STATIC;
        Body groundBody = PhysicsWorld.world.createBody(groundBodyDef);
        PolygonShape groundBlock = new PolygonShape();
        groundBlock.setAsBox(10.0f, 1.0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBlock;
        fixtureDef.friction = 1.0f;
        groundBody.createFixture(fixtureDef);
        spriteSheet = Game.loadImage("images/TransparentRoadRunner.png");
        
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.position.set(0.5f, 0.5f);
        playerBodyDef.type = BodyType.DYNAMIC;
        player = new Player(playerBodyDef, PhysicsWorld.world);
        Player.currentAnim = Player.rightStopped;
        AnimationThread animRun = new AnimationThread();
        Thread animThread = new Thread(animRun);
        System.out.println("start");
        animThread.start();
        
        
        
        
    }
    
    
    public void gameLoop(){
        while(gameRunning){
            startTime = System.currentTimeMillis();
            long fps = 32;
            float timeStep = (float)1.0/60.0f;
            int velocityIterations = 8;
            int positionIterations = 3;
            Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.black); 
            g.fillRect(0, 0, 800, 600);
            
            
//             if(keys.contains(keyS)){
//                
//                for(int i = 0;i<contacts.size();i++){
//                    Contact contact = (Contact)contacts.get(i);
//                    Manifold manifold = contact.getManifold();
//                    
//                    if(manifold.localPoint.y>-0.1f&&playerV.y<0.1){
//                        player.playerBody.applyLinearImpulse(jump, playerC);
//                        break;
//                    }
//                }    
//                
//            }
//            if(keys.contains(keyR)){
//                player.playerBody.applyForce(playerF, playerC);
//                
//            }
//            if(keys.contains(keyL)){
//                player.playerBody.applyForce(playerLF, playerC);
//                
//            }
            for(int i = 0; i < blocks.size(); i++){
                    Block block =(Block) blocks.get(i);
                    
                    block.paint(g);
                    
            }
            player.update();
//            player.checkBounds();
            player.paint(g);
            PhysicsWorld.world.step(timeStep, velocityIterations, positionIterations);
            
            
            
            
            
            
            
            g.dispose();
            buffer.show();
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
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
