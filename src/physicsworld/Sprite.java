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

public class Sprite {
    private int x, y;
    
    
    public Sprite(){
        
    }
    public void update(int x, int y){
        this.x = x;
        this.y = y;
        
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    
    
}
