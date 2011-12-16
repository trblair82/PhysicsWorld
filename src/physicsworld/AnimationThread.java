/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;

/**
 *
 * @author trblair
 */
import java.awt.*;
public class AnimationThread implements Runnable {
    public long delta_t, cur_t, startTime;
    public AnimationThread(){
        
    }
    @Override
    public void run(){
        startTime = System.currentTimeMillis();
        System.out.println("in");
        do{
             cur_t = System.currentTimeMillis();
             delta_t = (cur_t - startTime);
             startTime = System.currentTimeMillis();
             Player.currentAnim.update(delta_t);
             
             try {Thread.sleep(200);}catch(Exception e){}
        }while(true);     
    
    }
}

