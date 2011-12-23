/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;

/**
 *
 * @author trblair
 */
import java.awt.Image;
import java.util.ArrayList;

public class Animation {
    private ArrayList frames;//list to hold animation frames
    private int currentFrame;//variable to track current animation frame
    public long animTime;//variable to track where at in animation
    public long totalDuration;//total time of given animation
    
    public Animation(){
        frames = new ArrayList();//initializes frames array
        totalDuration = 0;//sets duration to zero
        start();//sets animation time to 0 and current frame to 0
        
        
    }
    public synchronized void addFrame(Image image, long duration){//adds frame to animation
        totalDuration += duration;//adds duration of frame to animation duration
        frames.add(new AnimFrame(image, totalDuration));//adds frame to list
        
    }
    public void start(){//resets animation
       animTime = 0;
       currentFrame = 0; 
    }
    public synchronized void update(long elapsedTime){//updates current animation frame for rendering
        if(frames.size()>1){
            animTime += elapsedTime;//adds passed time to animation time
            if(animTime >= totalDuration){//if animation time is greater than animation durration reset animation
                animTime = animTime % totalDuration;
                currentFrame = 0;
            }
            
            
            while(animTime > getFrame(currentFrame).endTime){//if animation time is greater than frame end time go to next frame
                currentFrame++;
            }
        }
    }
    public synchronized Image getImage(){//returns image from animFrame
        return getFrame(currentFrame).image;
    }
    
    
    private AnimFrame getFrame(int i){//returns animFrame object
        if(frames.isEmpty()){
            return null;
        }else{
        return (AnimFrame)frames.get(i);
        }
    }
    
    
    private class AnimFrame{//creates animation frame with image and end time
        Image image;
        long endTime;
        public AnimFrame(Image image, long endTime){
            this.image = image;
            this.endTime = endTime;
        }
    }
}
