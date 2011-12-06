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
/**
 *
 * @author trblair
 */
public class Game extends Canvas {
    private BufferStrategy buffer;
    public int b_speed;
    
    private boolean gameRunning = true;
    private ArrayList blocks;
    public Vec2 position;
    
    
    
    
    public class PhysicsWorldKeyListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_RIGHT){
                b_speed = -5;
            }
        }
        @Override
        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_RIGHT){
                b_speed = 0;
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
    public void gameSetup(){
        for(int i = 0; i < 4; i++){
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DYNAMIC;
            bodyDef.position.set((i*1.9f)+2.0f,6.0f);
            Block b = new Block(bodyDef, PhysicsWorld.world);
            
            blocks.add(b);
        }
        for(int p = 0;p<4;p++){
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
        groundBody.createFixture(fixtureDef);
        
//        BodyDef blockBodyDef = new BodyDef();
//        blockBodyDef.position.set(1.0f, 3.0f);
//        blockBodyDef.type = BodyType.DYNAMIC;
//        blockBody = PhysicsWorld.world.createBody(blockBodyDef);
//        PolygonShape dynamicBlock = new PolygonShape();
//        dynamicBlock.setAsBox(0.5f, 0.5f);
//        FixtureDef blockFixtureDef = new FixtureDef();
//        blockFixtureDef.shape = dynamicBlock;
//        blockFixtureDef.density = 1.0f;
//        blockFixtureDef.friction = 0.3f;
//        blockBody.createFixture(fixtureDef);
        
    }
    
    
    public void gameLoop(){
        while(gameRunning){
            long startTime = System.currentTimeMillis();
            long fps = 60;
            float timeStep = (float)1.0/60.0f;
            int velocityIterations = 8;
            int positionIterations = 3;
            Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
            g.setColor(Color.black); 
            g.fillRect(0, 0, 800, 600);
            
            
            
            for(int i = 0; i < blocks.size(); i++){
                    Block block =(Block) blocks.get(i);
                    Block.blockBody = (Body)Block.blockBodies.get(i);
                    block.paint(g);
                    
            }
            
            
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
