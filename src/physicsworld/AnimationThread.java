/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;

/**
 *
 * @author trblair
 */

public class AnimationThread implements Runnable {//overrides run function to add to my animation thread
    public long delta_t, cur_t, startTime;//variables for timer
    public AnimationThread(){
        
    }
    @Override
    public void run(){
        startTime = System.currentTimeMillis();//system time
        //updates my player animation 5 times per second in separate thread from game
        do{
             cur_t = System.currentTimeMillis();
             delta_t = (cur_t - startTime);
             startTime = System.currentTimeMillis();
             Player.currentAnim.update(delta_t);
             
             try {Thread.sleep(200);}catch(Exception e){}
        }while(true);     
    
    }
}

