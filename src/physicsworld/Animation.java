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
    private ArrayList frames;
    private int currentFrame;
    public long animTime;
    public long totalDuration;
    
    public Animation(){
        frames = new ArrayList();
        totalDuration = 0;
        start();
        
        
    }
    public synchronized void addFrame(Image image, long duration){
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
        
    }
    public void start(){
       animTime = 0;
       currentFrame = 0; 
    }
    public synchronized void update(long elapsedTime){
        if(frames.size()>1){
            animTime += elapsedTime;
            if(animTime >= totalDuration){
                animTime = animTime % totalDuration;
                currentFrame = 0;
            }
            if(animTime >= totalDuration){
                animTime = 0;
                currentFrame = frames.size();
            }
            while(animTime > getFrame(currentFrame).endTime){
                currentFrame++;
            }
        }
    }
    public synchronized Image getImage(){
        return getFrame(currentFrame).image;
    }
    
    
    private AnimFrame getFrame(int i){
        if(frames.size() == 0){
            return null;
        }else{
        return (AnimFrame)frames.get(i);
        }
    }
    
    
    private class AnimFrame{
        Image image;
        long endTime;
        public AnimFrame(Image image, long endTime){
            this.image = image;
            this.endTime = endTime;
        }
    }
}
